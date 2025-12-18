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
import metroMalaga.Controller.Common;

public class CrudFrontend extends JPanel {

    private Usuario user;

    private final Font FUENTE_PRINCIPAL = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 14);

    private JList<String> listaTablas;
    private DefaultListModel<String> modeloListaTablas;

    private JTable tablaDatos;
    private DefaultTableModel modeloTabla;

    private JPanel panelFormulario;
    private JPanel panelBotonesInferior;
    private ArrayList<JTextField> camposFormulario;

    private JButton btnGuardar;
    private JButton btnVolver;
    private JButton btnCancelarEdicion;
    private JLabel lblEstadoFormulario;

    private JPanel panelSur;

    private JScrollPane scrollTablasPanel;
    private JScrollPane scrollTablaPanel;

    private AccionFilaListener accionFilaListener;

    private String tablaActual;

    public CrudFrontend(Usuario user) {
        this.user = user;
        setLayout(new BorderLayout(10, 10));

        camposFormulario = new ArrayList<>();
        setBorder(new EmptyBorder(10, 10, 10, 10));

        crearMenuLateral();
        crearPanelCentral();
        crearPanelInferior();
        
        applyTheme();
    }

    public void applyTheme() {
        Color bgMain = Common.getBackground();
        Color bgPanel = Common.getPanelBackground();
        Color txt = Common.getText();
        Color border = Common.getBorder();
        Color fieldBg = Common.getFieldBackground();
        Color accent = Common.getAccent();
        Color danger = Common.getDanger();

        setBackground(bgMain);

        // Lista de tablas lateral
        if (listaTablas != null) {
            listaTablas.setBackground(fieldBg);
            listaTablas.setForeground(txt);
            listaTablas.setSelectionBackground(Common.isDarkMode ? new Color(200, 0, 0) : new Color(230, 240, 255));
            listaTablas.setSelectionForeground(Common.isDarkMode ? Color.WHITE : Color.BLACK);
        }

        if (scrollTablasPanel != null) {
            scrollTablasPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(border), Language.get(71),
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP, FUENTE_TITULO, txt));
            scrollTablasPanel.getViewport().setBackground(fieldBg);
            scrollTablasPanel.setBackground(fieldBg);
        }

        // Tabla central - CELDAS NORMALES
        if (tablaDatos != null) {
            tablaDatos.setBackground(fieldBg);
            tablaDatos.setForeground(txt);
            tablaDatos.setGridColor(Common.isDarkMode ? new Color(60, 60, 60) : border);
            tablaDatos.setSelectionBackground(Common.isDarkMode ? new Color(200, 0, 0) : new Color(230, 240, 255));
            tablaDatos.setSelectionForeground(Common.isDarkMode ? Color.WHITE : Color.BLACK);

            // 👇 RENDERER PARA CELDAS NORMALES
            tablaDatos.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    
                    if (!isSelected) {
                        c.setBackground(Common.getFieldBackground());
                        c.setForeground(Common.getText());
                    } else {
                        c.setBackground(Common.isDarkMode ? new Color(200, 0, 0) : new Color(230, 240, 255));
                        c.setForeground(Common.isDarkMode ? Color.WHITE : Color.BLACK);
                    }
                    
                    ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                    return c;
                }
            });

            // Header de la tabla
            JTableHeader header = tablaDatos.getTableHeader();
            header.setBackground(Common.isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
            header.setForeground(txt);
            
            header.setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {
                    JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    l.setBackground(Common.isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
                    l.setFont(FUENTE_TITULO);
                    l.setForeground(Common.getText());
                    l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Common.getBorder()));
                    l.setHorizontalAlignment(SwingConstants.CENTER);
                    return l;
                }
            });
        }

        if (scrollTablaPanel != null) {
            scrollTablaPanel.getViewport().setBackground(fieldBg);
            scrollTablaPanel.setBackground(fieldBg);
            scrollTablaPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(border), Language.get(72),
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP, FUENTE_TITULO, txt));
        }

        // Panel inferior
        if (panelSur != null) {
            panelSur.setBackground(bgPanel);
            panelSur.setBorder(new CompoundBorder(
                new LineBorder(border),
                new EmptyBorder(15, 15, 15, 15)));
        }

        if (lblEstadoFormulario != null) {
            lblEstadoFormulario.setForeground(txt);
        }

        if (panelFormulario != null) {
            panelFormulario.setBackground(bgPanel);
            for (Component comp : panelFormulario.getComponents()) {
                if (comp instanceof JPanel) {
                    JPanel campoPanel = (JPanel) comp;
                    campoPanel.setBackground(bgPanel);
                    for (Component subComp : campoPanel.getComponents()) {
                        if (subComp instanceof JLabel) {
                            ((JLabel) subComp).setForeground(txt);
                        } else if (subComp instanceof JTextField) {
                            JTextField field = (JTextField) subComp;
                            field.setBackground(fieldBg);
                            field.setForeground(txt);
                            field.setCaretColor(txt);
                            field.setBorder(new CompoundBorder(
                                new LineBorder(border),
                                new EmptyBorder(5, 8, 5, 8)));
                        }
                    }
                }
            }
        }

        if (panelBotonesInferior != null) {
            panelBotonesInferior.setBackground(bgPanel);
        }

        if (btnGuardar != null) {
            btnGuardar.setBackground(bgPanel);
            btnGuardar.setForeground(accent);
            btnGuardar.setBorder(new CompoundBorder(
                new LineBorder(accent, 1),
                new EmptyBorder(8, 20, 8, 20)));
        }

        if (btnVolver != null) {
            btnVolver.setBackground(bgPanel);
            btnVolver.setForeground(txt);
            btnVolver.setBorder(new CompoundBorder(
                new LineBorder(border, 1),
                new EmptyBorder(8, 20, 8, 20)));
        }

        if (btnCancelarEdicion != null) {
            btnCancelarEdicion.setBackground(danger);
            btnCancelarEdicion.setForeground(Color.WHITE);
            btnCancelarEdicion.setBorder(new CompoundBorder(
                new LineBorder(danger, 1),
                new EmptyBorder(8, 20, 8, 20)));
        }

        revalidate();
        repaint();
    }

    public void updateAllTexts() {
        scrollTablasPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Common.getBorder()), Language.get(71),
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP, FUENTE_TITULO, Common.getText()));

        scrollTablaPanel.setBorder(BorderFactory.createTitledBorder(
                new LineBorder(Common.getBorder()), Language.get(72),
                javax.swing.border.TitledBorder.LEFT,
                javax.swing.border.TitledBorder.TOP, FUENTE_TITULO, Common.getText()));

        if (lblEstadoFormulario.getText().contains("Editar") || lblEstadoFormulario.getText().contains("Edit")) {
            lblEstadoFormulario.setText(Language.get(73));
        } else if (lblEstadoFormulario.getText().contains("Crear")
                || lblEstadoFormulario.getText().contains("Create")) {
            lblEstadoFormulario.setText(Language.get(78));
        } else if (lblEstadoFormulario.getText().contains("Editando")
                || lblEstadoFormulario.getText().contains("Editing")) {
            lblEstadoFormulario.setText(Language.get(79));
        }

        btnCancelarEdicion.setText(Language.get(74));

        if (btnGuardar.getText().contains("Actualizar") || btnGuardar.getText().contains("Update")) {
            btnGuardar.setText(Language.get(77));
        } else {
            btnGuardar.setText(Language.get(75));
        }

        btnVolver.setText(Language.get(76));

        if (modeloTabla.getColumnCount() > 0) {
            Object[] columnNames = getUpdatedColumnNames();

            int rowCount = modeloTabla.getRowCount();
            int colCount = modeloTabla.getColumnCount();
            Object[][] data = new Object[rowCount][colCount];
            for (int i = 0; i < rowCount; i++) {
                for (int j = 0; j < colCount; j++) {
                    data[i][j] = modeloTabla.getValueAt(i, j);
                }
            }

            modeloTabla.setDataVector(data, columnNames);

            int accionesCol = modeloTabla.getColumnCount() - 1;
            tablaDatos.getColumnModel().getColumn(accionesCol).setCellRenderer(new ButtonRenderer());
            tablaDatos.getColumnModel().getColumn(accionesCol).setCellEditor(new ButtonEditor(new JCheckBox()));
            tablaDatos.getColumnModel().getColumn(accionesCol).setPreferredWidth(150);
        }

        applyTheme();
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

        scrollTablasPanel = new JScrollPane(listaTablas);
        scrollTablasPanel.setPreferredSize(new Dimension(200, 0));

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

        scrollTablaPanel = new JScrollPane(tablaDatos);

        add(scrollTablaPanel, BorderLayout.CENTER);
    }

    private void crearPanelInferior() {
        panelSur = new JPanel(new BorderLayout());

        lblEstadoFormulario = new JLabel(Language.get(73));
        lblEstadoFormulario.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblEstadoFormulario.setBorder(new EmptyBorder(0, 0, 15, 0));
        panelSur.add(lblEstadoFormulario, BorderLayout.NORTH);

        panelFormulario = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelFormulario.setBorder(null);

        panelBotonesInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotonesInferior.setBorder(new EmptyBorder(15, 0, 0, 0));

        btnCancelarEdicion = new JButton(Language.get(74));
        btnCancelarEdicion.setFont(FUENTE_TITULO);
        btnCancelarEdicion.setFocusPainted(false);
        btnCancelarEdicion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelarEdicion.setVisible(true);

        btnGuardar = new JButton(Language.get(75));
        btnGuardar.setFont(FUENTE_TITULO);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnVolver = new JButton(Language.get(76));
        btnVolver.setFont(FUENTE_TITULO);
        btnVolver.setFocusPainted(false);
        btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelBotonesInferior.add(btnCancelarEdicion);
        panelBotonesInferior.add(btnVolver);
        panelBotonesInferior.add(btnGuardar);
        
        panelSur.add(panelFormulario, BorderLayout.CENTER);
        panelSur.add(panelBotonesInferior, BorderLayout.SOUTH);

        panelSur.setPreferredSize(new Dimension(0, 250));

        add(panelSur, BorderLayout.SOUTH);

        updatePanelInferiorVisibility();
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

        if (modeloTabla.getColumnCount() > 0 && canModifyCurrentTable()) {
            agregarColumnaAcciones();
        }
        
        // Re-aplicar tema después de actualizar datos
        applyTheme();
    }

    public void setTablaActual(String tabla) {
        this.tablaActual = tabla;
        updatePanelInferiorVisibility();
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
            campoPanel.setBackground(Common.getPanelBackground());

            JLabel label = new JLabel(nombreCol + ":");
            label.setFont(FUENTE_PRINCIPAL);
            label.setForeground(Common.getText());
            label.setPreferredSize(new Dimension(120, 25));

            JTextField textField = new JTextField(15);
            textField.setFont(FUENTE_PRINCIPAL);
            textField.setBackground(Common.getFieldBackground());
            textField.setForeground(Common.getText());
            textField.setCaretColor(Common.getText());
            textField.setBorder(new CompoundBorder(
                    new LineBorder(Common.getBorder()),
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

            btnEditar = new JButton(Language.get(80));
            btnEditar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            btnEditar.setFocusPainted(false);
            btnEditar.setBorder(new EmptyBorder(3, 10, 3, 10));
            btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));

            btnEliminar = new JButton(Language.get(81));
            btnEliminar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            btnEliminar.setFocusPainted(false);
            btnEliminar.setBorder(new EmptyBorder(3, 10, 3, 10));
            btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));

            add(btnEditar);
            add(btnEliminar);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            setBackground(Common.getFieldBackground());
            
            btnEditar.setBackground(Common.getAccent());
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setText(Language.get(80));

            btnEliminar.setBackground(Common.getDanger());
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setText(Language.get(81));

            if (canModifyCurrentTable()) {
                btnEditar.setVisible(true);
                btnEliminar.setVisible(true);
            } else {
                btnEditar.setVisible(false);
                btnEliminar.setVisible(false);
            }
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

            btnEditar = new JButton(Language.get(80));
            btnEditar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            btnEditar.setFocusPainted(false);
            btnEditar.setBorder(new EmptyBorder(3, 10, 3, 10));
            btnEditar.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnEditar.addActionListener(e -> {
                fireEditingStopped();
                if (accionFilaListener != null) {
                    accionFilaListener.onEditar(filaActual);
                }
            });

            btnEliminar = new JButton(Language.get(81));
            btnEliminar.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            btnEliminar.setFocusPainted(false);
            btnEliminar.setBorder(new EmptyBorder(3, 10, 3, 10));
            btnEliminar.setCursor(new Cursor(Cursor.HAND_CURSOR));
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

            panel.setBackground(Common.getFieldBackground());
            
            btnEditar.setBackground(Common.getAccent());
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setText(Language.get(80));

            btnEliminar.setBackground(Common.getDanger());
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setText(Language.get(81));

            if (canModifyCurrentTable()) {
                btnEditar.setVisible(true);
                btnEliminar.setVisible(true);
            } else {
                btnEditar.setVisible(false);
                btnEliminar.setVisible(false);
            }
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

    private boolean canModifyCurrentTable() {
        if (user == null || user.getRol() == null || tablaActual == null) {
            return false;
        }

        String permiso = user.getRol().getPermiso();

        if ("usuario".equalsIgnoreCase(permiso)) {
            return false;
        } else if ("admin".equalsIgnoreCase(permiso)) {
            return !tablaActual.equalsIgnoreCase("logs");
        }

        return false;
    }

    private void updatePanelInferiorVisibility() {
        if (panelSur == null) {
            return;
        }

        if (user == null || user.getRol() == null) {
            panelSur.setVisible(false);
            return;
        }

        String permiso = user.getRol().getPermiso();

        if ("usuario".equalsIgnoreCase(permiso)) {
            panelSur.setVisible(false);
        } else {
            panelSur.setVisible(true);
        }

        revalidate();
        repaint();
    }
}
