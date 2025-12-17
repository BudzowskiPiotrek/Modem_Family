package metroMalaga.View;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import metroMalaga.Model.Usuario;

public class CrudFrontend extends JPanel {

    private Usuario user;

    private final Color COLOR_FONDO = Color.WHITE;
    private final Color COLOR_BORDE = new Color(220, 220, 220); // Gris suave
    private final Color COLOR_PRIMARIO = new Color(66, 139, 202); // Azul
    private final Color COLOR_PELIGRO = new Color(220, 53, 69); // Rojo
    private final Color COLOR_TEXTO = new Color(50, 50, 50);
    private final Font FUENTE_PRINCIPAL = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 14);

    // Componentes de la interfaz
    private JList<String> listaTablas;
    private DefaultListModel<String> modeloListaTablas;

    private JTable tablaDatos;
    private DefaultTableModel modeloTabla;

    private JPanel panelFormulario;
    private ArrayList<JTextField> camposFormulario;

    private JButton btnGuardar;
    private JButton btnVolver;
    private JButton btnCancelarEdicion;
    private JLabel lblEstadoFormulario;

    // Listener para acciones de fila
    private AccionFilaListener accionFilaListener;

    public CrudFrontend(Usuario user) {
        this.user = user;
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_FONDO);

        camposFormulario = new ArrayList<>();

        setBorder(new EmptyBorder(10, 10, 10, 10));

        crearMenuLateral();
        crearPanelCentral();
        crearPanelInferior();
    }

    // --- MENÚ LATERAL ---
    private void crearMenuLateral() {
        modeloListaTablas = new DefaultListModel<>();

        listaTablas = new JList<>(modeloListaTablas);
        listaTablas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTablas.setFont(FUENTE_PRINCIPAL);
        listaTablas.setFixedCellHeight(30);
        listaTablas.setBackground(COLOR_FONDO);
        listaTablas.setSelectionBackground(new Color(230, 240, 255));
        listaTablas.setSelectionForeground(Color.BLACK);

        JScrollPane scroll = new JScrollPane(listaTablas);
        scroll.setPreferredSize(new Dimension(200, 0));
        // Borde estilo imagen (Gris fino) con Título
        scroll.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_BORDE), "Tablas",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP, FUENTE_TITULO, COLOR_TEXTO));
        scroll.getViewport().setBackground(COLOR_FONDO);

        add(scroll, BorderLayout.WEST);
    }

    // --- TABLA DE DATOS ---
    private void crearPanelCentral() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == getColumnCount() - 1;
            }
        };

        // Configuración visual de la tabla
        tablaDatos = new JTable(modeloTabla);
        tablaDatos.setRowHeight(35);
        tablaDatos.setFont(FUENTE_PRINCIPAL);
        tablaDatos.setShowVerticalLines(false);
        tablaDatos.setGridColor(COLOR_BORDE);
        tablaDatos.setSelectionBackground(new Color(230, 240, 255));
        tablaDatos.setSelectionForeground(Color.BLACK);

        JTableHeader header = tablaDatos.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                    boolean hasFocus, int row, int column) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                        column);
                l.setBackground(new Color(245, 245, 245));
                l.setFont(FUENTE_TITULO);
                l.setForeground(Color.DARK_GRAY);
                l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, COLOR_BORDE));
                l.setHorizontalAlignment(SwingConstants.CENTER);
                return l;
            }
        });
        header.setPreferredSize(new Dimension(0, 35));

        JScrollPane scrollTabla = new JScrollPane(tablaDatos);
        scrollTabla.getViewport().setBackground(COLOR_FONDO);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_BORDE), "Registros",
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP, FUENTE_TITULO, COLOR_TEXTO));

        add(scrollTabla, BorderLayout.CENTER);
    }

    // --- FORMULARIO ---
    private void crearPanelInferior() {
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBackground(COLOR_FONDO);
        panelSur.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDE),
                new EmptyBorder(15, 15, 15, 15)));

        // Cabecera del formulario
        lblEstadoFormulario = new JLabel("Editar / Crear Registro");
        lblEstadoFormulario.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblEstadoFormulario.setForeground(COLOR_TEXTO);
        lblEstadoFormulario.setBorder(new EmptyBorder(0, 0, 15, 0));
        panelSur.add(lblEstadoFormulario, BorderLayout.NORTH);

        // Panel de campos dinámicos
        panelFormulario = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFormulario.setBackground(COLOR_FONDO);
        // Sin borde o borde muy sutil para el área de inputs
        panelFormulario.setBorder(null);

        // Panel de Botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setBorder(new EmptyBorder(15, 0, 0, 0));

        // --- Estilos de Botones ---

        btnCancelarEdicion = new JButton("Cancelar");
        estilarBotonSolido(btnCancelarEdicion, COLOR_PELIGRO, Color.WHITE);
        btnCancelarEdicion.setVisible(true);

        btnGuardar = new JButton("Guardar Nuevo");
        estilarBotonOutline(btnGuardar, COLOR_PRIMARIO);

        btnVolver = new JButton("Volver");
        estilarBotonOutline(btnVolver, Color.GRAY);

        panelBotones.add(btnCancelarEdicion);
        panelBotones.add(btnVolver);
        panelBotones.add(btnGuardar);
        panelSur.add(panelFormulario, BorderLayout.CENTER);
        panelSur.add(panelBotones, BorderLayout.SOUTH);

        panelSur.setPreferredSize(new Dimension(0, 250));

        add(panelSur, BorderLayout.SOUTH);
    }

    // --- UTILIDADES DE ESTILO ---

    private void estilarBotonSolido(JButton btn, Color bg, Color fg) {
        btn.setFont(FUENTE_TITULO);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(
                new LineBorder(bg, 1),
                new EmptyBorder(8, 20, 8, 20)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void estilarBotonOutline(JButton btn, Color color) {
        btn.setFont(FUENTE_TITULO);
        btn.setBackground(Color.WHITE);
        btn.setForeground(color);
        btn.setFocusPainted(false);
        btn.setBorder(new CompoundBorder(
                new LineBorder(color, 1),
                new EmptyBorder(8, 20, 8, 20)));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // --- MÉTODOS PÚBLICOS PARA EL CONTROLADOR ---

    // Getters para componentes UI (para que el controlador registre listeners
    // directamente)
    public JList<String> getListaTablas() {
        return listaTablas;
    }

    public JButton getBtnGuardar() {
        return btnGuardar;
    }

    public JButton getBtnCancelarEdicion() {
        return btnCancelarEdicion;
    }

    public JButton getBtnVolver() {
        return btnVolver;
    }

    // Table Selection
    public String getTablaSeleccionada() {
        return listaTablas.getSelectedValue();
    }

    public void setListaTablas(List<String> tablas) {
        modeloListaTablas.clear();
        for (String tabla : tablas) {
            modeloListaTablas.addElement(tabla);
        }
    }

    // Table Data Display
    public void actualizarTablaDatos(String[] columnas, Object[][] datos) {
        modeloTabla.setDataVector(datos, columnas);

        // Agregar columna de acciones si no existe
        if (modeloTabla.getColumnCount() > 0) {
            // Añadir renderizador de botones para la última columna (acciones)
            agregarColumnaAcciones();
        }
    }

    private void agregarColumnaAcciones() {
        // Agregar columna de acciones
        modeloTabla.addColumn("Acciones");

        int accionesCol = modeloTabla.getColumnCount() - 1;
        tablaDatos.getColumnModel().getColumn(accionesCol).setCellRenderer(new ButtonRenderer());
        tablaDatos.getColumnModel().getColumn(accionesCol).setCellEditor(new ButtonEditor(new JCheckBox()));
        tablaDatos.getColumnModel().getColumn(accionesCol).setPreferredWidth(150);
    }

    public Object getTableValueAt(int fila, int columna) {
        return modeloTabla.getValueAt(fila, columna);
    }

    // Form Management
    public void generarFormulario(String[] nombresColumnas) {
        panelFormulario.removeAll();
        camposFormulario.clear();

        for (String nombreCol : nombresColumnas) {
            JPanel campoPanel = new JPanel(new BorderLayout(5, 5));
            campoPanel.setBackground(COLOR_FONDO);

            JLabel label = new JLabel(nombreCol + ":");
            label.setFont(FUENTE_PRINCIPAL);
            label.setForeground(COLOR_TEXTO);
            label.setPreferredSize(new Dimension(120, 25));

            JTextField textField = new JTextField(15);
            textField.setFont(FUENTE_PRINCIPAL);
            textField.setBorder(new CompoundBorder(
                    new LineBorder(COLOR_BORDE),
                    new EmptyBorder(5, 8, 5, 8)));

            campoPanel.add(label, BorderLayout.WEST);
            campoPanel.add(textField, BorderLayout.CENTER);

            panelFormulario.add(campoPanel);
            camposFormulario.add(textField);
        }

        panelFormulario.revalidate();
        panelFormulario.repaint();
    }

    public void limpiarCamposFormulario() {
        for (JTextField campo : camposFormulario) {
            campo.setText("");
        }
        btnGuardar.setText("Guardar Nuevo");
        lblEstadoFormulario.setText("Crear Nuevo Registro");
    }

    public void llenarFormularioParaEditar(Object[] datosFila) {
        for (int i = 0; i < datosFila.length && i < camposFormulario.size(); i++) {
            camposFormulario.get(i).setText(datosFila[i] != null ? datosFila[i].toString() : "");
        }
        btnGuardar.setText("Actualizar");
        lblEstadoFormulario.setText("Editando Registro");
    }

    public List<String> getDatosFormulario() {
        List<String> datos = new ArrayList<>();
        for (JTextField campo : camposFormulario) {
            datos.add(campo.getText());
        }
        return datos;
    }

    // Método para establecer el listener de acciones de fila
    public void setAccionFilaListener(AccionFilaListener listener) {
        this.accionFilaListener = listener;
    }

    // Inner interface for row actions
    public interface AccionFilaListener {
        void onEditar(int fila);

        void onEliminar(int fila);
    }

    // --- RENDERIZADORES Y EDITORES DE BOTONES ---

    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton btnEditar;
        private JButton btnEliminar;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
            setBackground(Color.WHITE);

            btnEditar = new JButton("Editar");
            btnEditar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            btnEditar.setBackground(COLOR_PRIMARIO);
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setFocusPainted(false);
            btnEditar.setBorder(new EmptyBorder(3, 10, 3, 10));

            btnEliminar = new JButton("Eliminar");
            btnEliminar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            btnEliminar.setBackground(COLOR_PELIGRO);
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setFocusPainted(false);
            btnEliminar.setBorder(new EmptyBorder(3, 10, 3, 10));

            add(btnEditar);
            add(btnEliminar);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class ButtonEditor extends DefaultCellEditor {
        private JPanel panel;
        private JButton btnEditar;
        private JButton btnEliminar;
        private int filaActual;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);

            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 2));
            panel.setBackground(Color.WHITE);

            btnEditar = new JButton("Editar");
            btnEditar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            btnEditar.setBackground(COLOR_PRIMARIO);
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setFocusPainted(false);
            btnEditar.setBorder(new EmptyBorder(3, 10, 3, 10));
            btnEditar.addActionListener(e -> {
                fireEditingStopped();
                if (accionFilaListener != null) {
                    accionFilaListener.onEditar(filaActual);
                }
            });

            btnEliminar = new JButton("Eliminar");
            btnEliminar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            btnEliminar.setBackground(COLOR_PELIGRO);
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setFocusPainted(false);
            btnEliminar.setBorder(new EmptyBorder(3, 10, 3, 10));
            btnEliminar.addActionListener(e -> {
                fireEditingStopped();
                if (accionFilaListener != null) {
                    accionFilaListener.onEliminar(filaActual);
                }
            });

            panel.add(btnEditar);
            panel.add(btnEliminar);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            filaActual = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }
    }

    public Usuario getUser() {
        return user;
    }
}
