package metroMalaga.Controller;

import metroMalaga.View.CrudFrontend;
import metroMalaga.Controller.menu.MenuSelect;
import metroMalaga.Model.Language;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller class for the CRUD interface.
 * Handles database interactions, table loading, and form actions.
 */
public class CrudController {

    private CrudFrontend vista;
    private Connection conn;
    private MenuSelect menuSelect;
    private ConnecionSQL conexionSQL;

    private String tablaActual;
    private List<String> nombresColumnas;
    private String idRegistroEdicion = null;

    /**
     * Constructor for CrudController.
     * 
     * @param vista      The frontend view.
     * @param menuSelect The menu controller for navigation.
     */
    public CrudController(CrudFrontend vista, MenuSelect menuSelect) {
        this.vista = vista;
        this.menuSelect = menuSelect;
        this.nombresColumnas = new ArrayList<>();
        this.conexionSQL = new ConnecionSQL();

        conectarBD();
        inicializarEventos();
        cargarListaTablas();
    }

    /**
     * Establishes connection to the database.
     */
    private void conectarBD() {
        conn = conexionSQL.connect();
        if (conn == null) {
            System.exit(1);
        }
    }

    /**
     * Initializes event listeners for the view components.
     */
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

    /**
     * Loads the list of available tables from the database.
     */
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

    /**
     * Loads data from the currently selected table into the view.
     */
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
                        Object value = rs.getObject(i);
                        // Convert Boolean to int for MySQL compatibility
                        if (value instanceof Boolean) {
                            fila[i - 1] = ((Boolean) value) ? 1 : 0;
                        } else {
                            fila[i - 1] = value;
                        }
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

    /**
     * Handles the save action (create or update record).
     */
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
                    // Validate password: at least 8 characters, only letters and numbers
                    if (plainPassword.length() < 8 || !plainPassword.matches("[a-zA-Z0-9]+")) {
                        JOptionPane.showMessageDialog(vista,
                                "The password must be at least 8 characters long and contain only letters and numbers.",
                                Language.get(178),
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
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

                // Build UPDATE statement, excluding password if it's the placeholder
                StringBuilder sql = new StringBuilder("UPDATE " + tablaActual + " SET ");
                List<String> valoresParaUpdate = new ArrayList<>();

                boolean first = true;
                for (int i = 1; i < nombresColumnas.size(); i++) {
                    String nombreCol = nombresColumnas.get(i);
                    String valor = valores.get(i);

                    // Skip password field if it's the placeholder (not changed)
                    boolean isPasswordPlaceholder = tablaActual.equalsIgnoreCase("usuarios") &&
                            nombreCol.equalsIgnoreCase("password") &&
                            valor.equals("********");

                    if (!isPasswordPlaceholder) {
                        if (!first) {
                            sql.append(", ");
                        }
                        sql.append(nombreCol).append("=?");
                        valoresParaUpdate.add(valor);
                        first = false;
                    }
                }
                sql.append(" WHERE ").append(nombreColID).append("=?");

                pst = conn.prepareStatement(sql.toString());

                int paramIndex = 1;
                for (String valor : valoresParaUpdate) {
                    pst.setString(paramIndex++, valor);
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

    /**
     * Handles the delete action for a record.
     * 
     * @param fila The row index of the record to delete.
     */
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

    /**
     * Checks if the current user has permission to view the specified table.
     * 
     * @param tableName The name of the table.
     * @return true if allowed, false otherwise.
     */
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

    /**
     * Checks if the current user has permission to modify the specified table.
     * 
     * @param tableName The name of the table.
     * @return true if allowed, false otherwise.
     */
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

    /**
     * Gets the permission string of the current user.
     * 
     * @return The permission string, or empty string if user/role is null.
     */
    public String getUserPermission() {
        if (vista.getUser() == null || vista.getUser().getRol() == null) {
            return "";
        }
        return vista.getUser().getRol().getPermiso();
    }
}
