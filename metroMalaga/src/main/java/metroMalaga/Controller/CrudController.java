package metroMalaga.Controller;

import metroMalaga.View.CrudFrontend;
import metroMalaga.Controller.menu.MenuSelect;
import metroMalaga.Model.Language;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CrudController {

    private static final String URL = "jdbc:mysql://192.168.1.35/centimetromalaga";
    private static final String USER = "remoto";
    private static final String PASS = "proyecto";

    private CrudFrontend vista;
    private Connection conn;
    private MenuSelect menuSelect;

    private String tablaActual;
    private List<String> nombresColumnas;
    private String idRegistroEdicion = null;

    public CrudController(CrudFrontend vista, MenuSelect menuSelect) {
        this.vista = vista;
        this.menuSelect = menuSelect;
        this.nombresColumnas = new ArrayList<>();

        conectarBD();
        inicializarEventos();
        cargarListaTablas();
    }

    private void conectarBD() {
        try {
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista, Language.get(171) + e.getMessage());
            System.exit(1);
        }
    }

    private void inicializarEventos() {
        vista.getListaTablas().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String seleccion = vista.getTablaSeleccionada();
                if (seleccion != null) {
                    tablaActual = seleccion;
                    idRegistroEdicion = null;
                    vista.limpiarCamposFormulario();
                    cargarDatosTabla();
                }
            }
        });

        vista.getBtnGuardar().addActionListener(e -> accionGuardar());

        vista.getBtnCancelarEdicion().addActionListener(e -> {
            idRegistroEdicion = null;
            vista.limpiarCamposFormulario();
        });

        vista.setAccionFilaListener(new CrudFrontend.AccionFilaListener() {
            @Override
            public void onEditar(int fila) {
                Object id = vista.getTableValueAt(fila, 0);
                idRegistroEdicion = id.toString();

                int numColumnas = nombresColumnas.size();
                Object[] datosFila = new Object[numColumnas];
                for (int i = 0; i < numColumnas; i++) {
                    datosFila[i] = vista.getTableValueAt(fila, i);
                }
                vista.llenarFormularioParaEditar(datosFila);
            }

            @Override
            public void onEliminar(int fila) {
                accionEliminar(fila);
            }
        });

        vista.getBtnVolver().addActionListener(e -> {
            if (menuSelect != null) {
                menuSelect.switchToTab(0);
            }
        });
    }

    private void cargarListaTablas() {
        try {
            String catalogo = conn.getCatalog();

            if (catalogo == null) {
                JOptionPane.showMessageDialog(vista,
                        Language.get(172),
                        Language.get(173),
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getTables(catalogo, null, "%", new String[] { "TABLE" });

            List<String> tablas = new ArrayList<>();
            while (rs.next()) {
                String tableName = rs.getString("TABLE_NAME");
                if (canViewTable(tableName)) {
                    tablas.add(tableName);
                }
            }

            vista.setListaTablas(tablas);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista,
                    Language.get(174) + e.getMessage(),
                    Language.get(175),
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosTabla() {
        if (tablaActual == null)
            return;
        nombresColumnas.clear();

        vista.setTablaActual(tablaActual);

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + tablaActual);
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();

            String[] arrayColumnas = new String[colCount];
            for (int i = 1; i <= colCount; i++) {
                String nombreCol = meta.getColumnName(i);
                nombresColumnas.add(nombreCol);
                arrayColumnas[i - 1] = nombreCol;
            }

            List<Object[]> datos = new ArrayList<>();
            while (rs.next()) {
                Object[] fila = new Object[colCount];
                for (int i = 1; i <= colCount; i++) {
                    // Hide passwords in the usuarios table
                    if (tablaActual.equalsIgnoreCase("usuarios") &&
                            nombresColumnas.get(i - 1).equalsIgnoreCase("password")) {
                        fila[i - 1] = "********";
                    } else {
                        fila[i - 1] = rs.getObject(i);
                    }
                }
                datos.add(fila);
            }

            Object[][] matrizDatos = datos.toArray(new Object[0][]);
            vista.actualizarTablaDatos(arrayColumnas, matrizDatos);
            vista.generarFormulario(arrayColumnas);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista, Language.get(176) + e.getMessage());
        }
    }

    private void accionGuardar() {
        if (tablaActual == null)
            return;

        if (!canModifyTable(tablaActual)) {
            JOptionPane.showMessageDialog(vista,
                    Language.get(177),
                    Language.get(178),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<String> valores = vista.getDatosFormulario();

        // Hash passwords before saving if this is the usuarios table
        if (tablaActual.equalsIgnoreCase("usuarios")) {
            int passwordIndex = nombresColumnas.indexOf("password");
            if (passwordIndex != -1 && passwordIndex < valores.size()) {
                String plainPassword = valores.get(passwordIndex);
                // Only hash if it's not already a BCrypt hash and not the placeholder
                if (!plainPassword.equals("********") && !PasswordUtil.isBCryptHash(plainPassword)) {
                    String hashedPassword = PasswordUtil.hashPassword(plainPassword);
                    valores.set(passwordIndex, hashedPassword);
                }
            }
        }

        try {
            PreparedStatement pst;
            if (idRegistroEdicion == null) {
                StringBuilder sql = new StringBuilder("INSERT INTO " + tablaActual + " VALUES (");
                for (int i = 0; i < valores.size(); i++) {
                    sql.append("?");
                    if (i < valores.size() - 1)
                        sql.append(",");
                }
                sql.append(")");

                pst = conn.prepareStatement(sql.toString());
                for (int i = 0; i < valores.size(); i++) {
                    pst.setString(i + 1, valores.get(i));
                }
            } else {
                String nombreColID = nombresColumnas.get(0);

                StringBuilder sql = new StringBuilder("UPDATE " + tablaActual + " SET ");
                for (int i = 1; i < nombresColumnas.size(); i++) {
                    sql.append(nombresColumnas.get(i)).append("=?");
                    if (i < nombresColumnas.size() - 1)
                        sql.append(", ");
                }
                sql.append(" WHERE ").append(nombreColID).append("=?");

                pst = conn.prepareStatement(sql.toString());

                int paramIndex = 1;
                for (int i = 1; i < valores.size(); i++) {
                    pst.setString(paramIndex++, valores.get(i));
                }
                pst.setString(paramIndex, idRegistroEdicion);
            }

            pst.executeUpdate();

            idRegistroEdicion = null;
            vista.limpiarCamposFormulario();
            cargarDatosTabla();
            JOptionPane.showMessageDialog(vista, Language.get(179));

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista, Language.get(180) + e.getMessage());
        }
    }

    private void accionEliminar(int fila) {
        if (!canModifyTable(tablaActual)) {
            JOptionPane.showMessageDialog(vista,
                    Language.get(181),
                    Language.get(178),
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object id = vista.getTableValueAt(fila, 0);
        String nombreColID = nombresColumnas.get(0);

        int confirm = JOptionPane.showConfirmDialog(vista,
                Language.get(182) + id + Language.get(183));

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM " + tablaActual + " WHERE " + nombreColID + " = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, id.toString());
                pst.executeUpdate();
                cargarDatosTabla();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(vista, Language.get(176) + e.getMessage());
            }
        }
    }

    private boolean canViewTable(String tableName) {
        if (vista.getUser() == null || vista.getUser().getRol() == null) {
            return false;
        }

        String permiso = vista.getUser().getRol().getPermiso();

        if ("admin".equalsIgnoreCase(permiso)) {
            return true;
        } else if ("usuario".equalsIgnoreCase(permiso)) {
            return tableName.equalsIgnoreCase("cocheras") ||
                    tableName.equalsIgnoreCase("estaciones") ||
                    tableName.equalsIgnoreCase("lineas") ||
                    tableName.equalsIgnoreCase("trenes") ||
                    tableName.equalsIgnoreCase("viajes");
        }

        return false;
    }

    private boolean canModifyTable(String tableName) {
        if (vista.getUser() == null || vista.getUser().getRol() == null) {
            return false;
        }

        String permiso = vista.getUser().getRol().getPermiso();

        if ("usuario".equalsIgnoreCase(permiso)) {
            return false;
        } else if ("admin".equalsIgnoreCase(permiso)) {
            return !tableName.equalsIgnoreCase("logs");
        }

        return false;
    }

    public String getUserPermission() {
        if (vista.getUser() == null || vista.getUser().getRol() == null) {
            return "";
        }
        return vista.getUser().getRol().getPermiso();
    }
}
