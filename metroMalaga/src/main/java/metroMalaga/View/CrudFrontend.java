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
import metroMalaga.Model.Language;

public class CrudFrontend extends JFrame {

    private Usuario user;

    private final Color COLOR_FONDO = Color.WHITE;
    private final Color COLOR_BORDE = new Color(220, 220, 220);
    private final Color COLOR_PRIMARIO = new Color(66, 139, 202);
    private final Color COLOR_PELIGRO = new Color(220, 53, 69);
    private final Color COLOR_TEXTO = new Color(50, 50, 50);
    private final Font FUENTE_PRINCIPAL = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 14);

    private JList<String> listaTablas;
    private DefaultListModel<String> modeloListaTablas;

    private JTable tablaDatos;
    private DefaultTableModel modeloTabla;

    private JPanel panelFormulario;
    private ArrayList<JTextField> camposFormulario;

    private JButton btnGuardar;
    private JButton btnVolver;
    private JButton btnCancelarEdicion;
    private JButton btnLanguage;
    private JLabel lblEstadoFormulario;

    private JScrollPane scrollTablasPanel;
    private JScrollPane scrollTablaPanel;

    private AccionFilaListener accionFilaListener;

    public CrudFrontend(Usuario user) {
        this.user = user;
        setTitle(Language.get(70));
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_FONDO);

        camposFormulario = new ArrayList<>();

        ((JPanel) getContentPane()).setBorder(new EmptyBorder(10, 10, 10, 10));

        crearMenuLateral();
        crearPanelCentral();
        crearPanelInferior();
        createLanguageButton();
    }

    private void createLanguageButton() {
        String langText = Language.getCurrentLanguage().equals("espanol") ? "🇪🇸 ES" : "🇬🇧 EN";
        btnLanguage = new JButton(langText);
        btnLanguage.setBounds(980, 10, 100, 35);
        btnLanguage.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLanguage.setBackground(COLOR_PRIMARIO);
        btnLanguage.setForeground(Color.WHITE);
        btnLanguage.setBorder(new CompoundBorder(
            new LineBorder(COLOR_PRIMARIO, 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        btnLanguage.setFocusPainted(false);
        btnLanguage.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        getLayeredPane().add(btnLanguage, JLayeredPane.PALETTE_LAYER);
    }

    public void updateAllTexts() {
        setTitle(Language.get(70));
        
        scrollTablasPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_BORDE), Language.get(71),
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP, FUENTE_TITULO, COLOR_TEXTO));
        
        scrollTablaPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_BORDE), Language.get(72),
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP, FUENTE_TITULO, COLOR_TEXTO));
        
        if (lblEstadoFormulario.getText().contains("Editar") || lblEstadoFormulario.getText().contains("Edit")) {
            lblEstadoFormulario.setText(Language.get(73));
        } else if (lblEstadoFormulario.getText().contains("Crear") || lblEstadoFormulario.getText().contains("Create")) {
            lblEstadoFormulario.setText(Language.get(78));
        } else if (lblEstadoFormulario.getText().contains("Editando") || lblEstadoFormulario.getText().contains("Editing")) {
            lblEstadoFormulario.setText(Language.get(79));
        }
        
        btnCancelarEdicion.setText(Language.get(74));
        
        if (btnGuardar.getText().contains("Actualizar") || btnGuardar.getText().contains("Update")) {
            btnGuardar.setText(Language.get(77));
        } else {
            btnGuardar.setText(Language.get(75));
        }
        
        btnVolver.setText(Language.get(76));
        
        // ACTUALIZAR LOS NOMBRES DE COLUMNAS Y FORZAR REDIBUJADO DE BOTONES
        if (modeloTabla.getColumnCount() > 0) {
            Object[] columnNames = getUpdatedColumnNames();
            
            // Guardar datos actuales
            int rowCount = modeloTabla.getRowCount();
            int colCount = modeloTabla.getColumnCount();
            Object[][] data = new Object[rowCount][colCount];
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < colCount; j++) {
                    data[i][j] = modeloTabla.getValueAt(i, j);
                }
            }
            
            // Recargar tabla con nuevos nombres
            modeloTabla.setDataVector(data, columnNames);
            
            // Reconfigurar columna de acciones
            int accionesCol = modeloTabla.getColumnCount() - 1;
            tablaDatos.getColumnModel().getColumn(accionesCol).setCellRenderer(new ButtonRenderer());
            tablaDatos.getColumnModel().getColumn(accionesCol).setCellEditor(new ButtonEditor(new JCheckBox()));
            tablaDatos.getColumnModel().getColumn(accionesCol).setPreferredWidth(150);
        }
        
        revalidate();
        repaint();
    }


    private Object[] getUpdatedColumnNames() {
        Object[] currentColumns = new Object[modeloTabla.getColumnCount()];
        for (int i = 0; i < modeloTabla.getColumnCount() - 1; i++) {
            currentColumns[i] = modeloTabla.getColumnName(i);
        }
        currentColumns[modeloTabla.getColumnCount() - 1] = Language.get(82);
        return currentColumns;
    }

    private void crearMenuLateral() {
        modeloListaTablas = new DefaultListModel<>();

        listaTablas = new JList<>(modeloListaTablas);
        listaTablas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTablas.setFont(FUENTE_PRINCIPAL);
        listaTablas.setFixedCellHeight(30);
        listaTablas.setBackground(COLOR_FONDO);
        listaTablas.setSelectionBackground(new Color(230, 240, 255));
        listaTablas.setSelectionForeground(Color.BLACK);

        scrollTablasPanel = new JScrollPane(listaTablas);
        scrollTablasPanel.setPreferredSize(new Dimension(200, 0));
        scrollTablasPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_BORDE), Language.get(71),
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP, FUENTE_TITULO, COLOR_TEXTO));
        scrollTablasPanel.getViewport().setBackground(COLOR_FONDO);

        add(scrollTablasPanel, BorderLayout.WEST);
    }

    private void crearPanelCentral() {
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == getColumnCount() - 1;
            }
        };

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

        scrollTablaPanel = new JScrollPane(tablaDatos);
        scrollTablaPanel.getViewport().setBackground(COLOR_FONDO);
        scrollTablaPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(COLOR_BORDE), Language.get(72),
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP, FUENTE_TITULO, COLOR_TEXTO));

        add(scrollTablaPanel, BorderLayout.CENTER);
    }

    private void crearPanelInferior() {
        JPanel panelSur = new JPanel(new BorderLayout());
        panelSur.setBackground(COLOR_FONDO);
        panelSur.setBorder(new CompoundBorder(
                new LineBorder(COLOR_BORDE),
                new EmptyBorder(15, 15, 15, 15)));

        lblEstadoFormulario = new JLabel(Language.get(73));
        lblEstadoFormulario.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblEstadoFormulario.setForeground(COLOR_TEXTO);
        lblEstadoFormulario.setBorder(new EmptyBorder(0, 0, 15, 0));
        panelSur.add(lblEstadoFormulario, BorderLayout.NORTH);

        panelFormulario = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFormulario.setBackground(COLOR_FONDO);
        panelFormulario.setBorder(null);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(COLOR_FONDO);
        panelBotones.setBorder(new EmptyBorder(15, 0, 0, 0));

        btnCancelarEdicion = new JButton(Language.get(74));
        estilarBotonSolido(btnCancelarEdicion, COLOR_PELIGRO, Color.WHITE);
        btnCancelarEdicion.setVisible(true);

        btnGuardar = new JButton(Language.get(75));
        estilarBotonOutline(btnGuardar, COLOR_PRIMARIO);

        btnVolver = new JButton(Language.get(76));
        estilarBotonOutline(btnVolver, Color.GRAY);

        panelBotones.add(btnCancelarEdicion);
        panelBotones.add(btnVolver);
        panelBotones.add(btnGuardar);
        panelSur.add(panelFormulario, BorderLayout.CENTER);
        panelSur.add(panelBotones, BorderLayout.SOUTH);

        panelSur.setPreferredSize(new Dimension(0, 250));

        add(panelSur, BorderLayout.SOUTH);
    }

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

    public JButton getBtnLanguage() {
        return btnLanguage;
    }

    public String getTablaSeleccionada() {
        return listaTablas.getSelectedValue();
    }

    public void setListaTablas(List<String> tablas) {
        modeloListaTablas.clear();
        for (String tabla : tablas) {
            modeloListaTablas.addElement(tabla);
        }
    }

    public void actualizarTablaDatos(String[] columnas, Object[][] datos) {
        modeloTabla.setDataVector(datos, columnas);

        if (modeloTabla.getColumnCount() > 0) {
            agregarColumnaAcciones();
        }
    }

    private void agregarColumnaAcciones() {
        modeloTabla.addColumn(Language.get(82));

        int accionesCol = modeloTabla.getColumnCount() - 1;
        tablaDatos.getColumnModel().getColumn(accionesCol).setCellRenderer(new ButtonRenderer());
        tablaDatos.getColumnModel().getColumn(accionesCol).setCellEditor(new ButtonEditor(new JCheckBox()));
        tablaDatos.getColumnModel().getColumn(accionesCol).setPreferredWidth(150);
    }

    public Object getTableValueAt(int fila, int columna) {
        return modeloTabla.getValueAt(fila, columna);
    }

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
        btnGuardar.setText(Language.get(75));
        lblEstadoFormulario.setText(Language.get(78));
    }

    public void llenarFormularioParaEditar(Object[] datosFila) {
        for (int i = 0; i < datosFila.length && i < camposFormulario.size(); i++) {
            camposFormulario.get(i).setText(datosFila[i] != null ? datosFila[i].toString() : "");
        }
        btnGuardar.setText(Language.get(77));
        lblEstadoFormulario.setText(Language.get(79));
    }

    public List<String> getDatosFormulario() {
        List<String> datos = new ArrayList<>();
        for (JTextField campo : camposFormulario) {
            datos.add(campo.getText());
        }
        return datos;
    }

    public void setAccionFilaListener(AccionFilaListener listener) {
        this.accionFilaListener = listener;
    }

    public interface AccionFilaListener {
        void onEditar(int fila);
        void onEliminar(int fila);
    }

    class ButtonRenderer extends JPanel implements TableCellRenderer {
        private JButton btnEditar;
        private JButton btnEliminar;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 2));
            setBackground(Color.WHITE);

            btnEditar = new JButton(Language.get(80));
            btnEditar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            btnEditar.setBackground(COLOR_PRIMARIO);
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setFocusPainted(false);
            btnEditar.setBorder(new EmptyBorder(3, 10, 3, 10));

            btnEliminar = new JButton(Language.get(81));
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

            btnEditar = new JButton(Language.get(80));
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

            btnEliminar = new JButton(Language.get(81));
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
