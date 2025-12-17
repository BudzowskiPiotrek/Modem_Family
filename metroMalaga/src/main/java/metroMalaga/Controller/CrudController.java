package metroMalaga.Controller;

import metroMalaga.View.CrudFrontend;

import metroMalaga.View.PanelMenu;
import metroMalaga.Model.Language;


import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CrudController {

    private static final String URL = "jdbc:mysql://192.168.1.32:3306/centimetromalaga";
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
            JOptionPane.showMessageDialog(vista, "Error conectando a BD: " + e.getMessage());
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
            // Switch back to first tab (or could be a specific tab)
            if (menuSelect != null) {
                menuSelect.switchToTab(0);
            }
        });
    }

        vista.getBtnLanguage().addActionListener(e -> toggleLanguage());
    }

    private void toggleLanguage() {
        if (Language.getCurrentLanguage().equals("espanol")) {
            Language.setEnglish();
            vista.getBtnLanguage().setText("🇬🇧 EN");
        } else {
            Language.setSpanish();
            vista.getBtnLanguage().setText("🇪🇸 ES");
        }
        vista.updateAllTexts();
    }

    private void cargarListaTablas() {
        try {
            String catalogo = conn.getCatalog();

            if (catalogo == null) {
                JOptionPane.showMessageDialog(vista,
                        "Error: No se pudo obtener el nombre del catálogo (Base de Datos).", "Error de Configuración",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            DatabaseMetaData meta = conn.getMetaData();
            ResultSet rs = meta.getTables(catalogo, null, "%", new String[] { "TABLE" });

            List<String> tablas = new ArrayList<>();
            while (rs.next()) {
                tablas.add(rs.getString("TABLE_NAME"));
            }

            vista.setListaTablas(tablas);

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(vista, "Error al cargar la lista de tablas: " + e.getMessage(),
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarDatosTabla() {
        if (tablaActual == null)
            return;
        nombresColumnas.clear();

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
                    fila[i - 1] = rs.getObject(i);
                }
                datos.add(fila);
            }

            Object[][] matrizDatos = datos.toArray(new Object[0][]);
            vista.actualizarTablaDatos(arrayColumnas, matrizDatos);
            vista.generarFormulario(arrayColumnas);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista, "Error cargando tabla: " + e.getMessage());
        }
    }

    private void accionGuardar() {
        if (tablaActual == null)
            return;
        List<String> valores = vista.getDatosFormulario();

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
            JOptionPane.showMessageDialog(vista, "Operación realizada con éxito");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista, "Error SQL: " + e.getMessage());
        }
    }

    private void accionEliminar(int fila) {
        Object id = vista.getTableValueAt(fila, 0);
        String nombreColID = nombresColumnas.get(0);

        int confirm = JOptionPane.showConfirmDialog(vista, "¿Eliminar registro con ID " + id + "?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM " + tablaActual + " WHERE " + nombreColID + " = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, id.toString());
                pst.executeUpdate();
                cargarDatosTabla();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(vista, "Error eliminando: " + e.getMessage());
            }
        }
    }
}
