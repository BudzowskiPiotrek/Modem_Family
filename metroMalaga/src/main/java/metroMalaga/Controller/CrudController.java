package metroMalaga.Controller;

import metroMalaga.View.CrudFrontend;
import metroMalaga.Controller.menu.MenuSelect;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CrudController {

    // --- CONFIGURACIÓN BD ---
    private static final String URL = "jdbc:mysql://192.168.1.35/centimetromalaga";
    private static final String USER = "remoto";
    private static final String PASS = "proyecto";

    private CrudFrontend vista;
    private Connection conn;
    private MenuSelect menuSelect;

    // Estado actual
    private String tablaActual;
    private List<String> nombresColumnas; // Para saber nombres al hacer queries
    private String idRegistroEdicion = null; // Si es null, es INSERT. Si tiene valor, es UPDATE.

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
        // 1. Selección de Tabla en el menú lateral
        vista.getListaTablas().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String seleccion = vista.getTablaSeleccionada();
                if (seleccion != null) {
                    tablaActual = seleccion;
                    idRegistroEdicion = null; // Reseteamos modo edición
                    vista.limpiarCamposFormulario();
                    cargarDatosTabla();
                }
            }
        });

        // 2. Botón Guardar (Sirve para INSERT y UPDATE)
        vista.getBtnGuardar().addActionListener(e -> accionGuardar());

        // 3. Botón Cancelar Edición
        vista.getBtnCancelarEdicion().addActionListener(e -> {
            idRegistroEdicion = null;
            vista.limpiarCamposFormulario();
        });

        // 4. Acciones dentro de la tabla (Editar / Eliminar)
        vista.setAccionFilaListener(new CrudFrontend.AccionFilaListener() {
            @Override
            public void onEditar(int fila) {
                // Recuperar datos visuales de la tabla
                // Asumimos que la columna 0 es el ID
                Object id = vista.getTableValueAt(fila, 0);
                idRegistroEdicion = id.toString();

                // Obtener toda la fila para rellenar el formulario
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

    // --- LÓGICA DE NEGOCIO ---

    private void cargarListaTablas() {
        try {
            // 1. Obtener el nombre de la base de datos (Catálogo) de la conexión
            // Esto asume que la conexión está abierta y apunta a la BD correcta
            String catalogo = conn.getCatalog();

            if (catalogo == null) {
                // Si getCatalog() devuelve null (algunas implementaciones JDBC antiguas)
                JOptionPane.showMessageDialog(vista,
                        "Error: No se pudo obtener el nombre del catálogo (Base de Datos).", "Error de Configuración",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 2. Usar el Catálogo en getTables para filtrar por la BD específica
            DatabaseMetaData meta = conn.getMetaData();
            // Parámetros de getTables: (catalogo, esquema, nombreTablaPatrón, tipos)
            // Usamos el 'catalogo' que acabamos de obtener.
            ResultSet rs = meta.getTables(catalogo, null, "%", new String[] { "TABLE" });

            List<String> tablas = new ArrayList<>();
            while (rs.next()) {
                tablas.add(rs.getString("TABLE_NAME"));
            }

            // La vista solo se encarga de mostrar
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

            // Guardar nombres de columnas
            String[] arrayColumnas = new String[colCount];
            for (int i = 1; i <= colCount; i++) {
                String nombreCol = meta.getColumnName(i);
                nombresColumnas.add(nombreCol);
                arrayColumnas[i - 1] = nombreCol;
            }

            // Guardar datos
            List<Object[]> datos = new ArrayList<>();
            while (rs.next()) {
                Object[] fila = new Object[colCount];
                for (int i = 1; i <= colCount; i++) {
                    fila[i - 1] = rs.getObject(i);
                }
                datos.add(fila);
            }

            // Actualizar Vista
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
                // --- MODO INSERTAR ---
                // Generar SQL: INSERT INTO tabla VALUES (?,?,?)
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
                // --- MODO ACTUALIZAR ---
                // Generar SQL: UPDATE tabla SET col1=?, col2=? WHERE colID=?
                // IMPORTANTE: Asumimos que la columna 0 es la Primary Key (ID)
                String nombreColID = nombresColumnas.get(0);

                StringBuilder sql = new StringBuilder("UPDATE " + tablaActual + " SET ");
                // Empezamos desde 1 para no actualizar el ID (asumiendo que es fijo)
                for (int i = 1; i < nombresColumnas.size(); i++) {
                    sql.append(nombresColumnas.get(i)).append("=?");
                    if (i < nombresColumnas.size() - 1)
                        sql.append(", ");
                }
                sql.append(" WHERE ").append(nombreColID).append("=?");

                pst = conn.prepareStatement(sql.toString());

                // Llenar parámetros SET
                int paramIndex = 1;
                for (int i = 1; i < valores.size(); i++) {
                    pst.setString(paramIndex++, valores.get(i));
                }
                // Llenar parámetro WHERE
                pst.setString(paramIndex, idRegistroEdicion);
            }

            pst.executeUpdate();

            // Refrescar interfaz
            idRegistroEdicion = null;
            vista.limpiarCamposFormulario();
            cargarDatosTabla();
            JOptionPane.showMessageDialog(vista, "Operación realizada con éxito");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(vista, "Error SQL: " + e.getMessage());
        }
    }

    private void accionEliminar(int fila) {
        // Asumimos que la Columna 0 es la Primary Key
        Object id = vista.getTableValueAt(fila, 0);
        String nombreColID = nombresColumnas.get(0);

        int confirm = JOptionPane.showConfirmDialog(vista, "¿Eliminar registro con ID " + id + "?");
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                String sql = "DELETE FROM " + tablaActual + " WHERE " + nombreColID + " = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setString(1, id.toString());
                pst.executeUpdate();
                cargarDatosTabla(); // Recargar
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(vista, "Error eliminando: " + e.getMessage());
            }
        }
    }
}