package metroMalaga.View;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.util.List;
import java.util.Arrays;

import metroMalaga.Model.Usuario;
import metroMalaga.Model.Language;
import metroMalaga.Controller.Common;
import metroMalaga.Controller.smtp.ButtonHandleSMTP;
import metroMalaga.Controller.smtp.ButtonHoverHandle;
import metroMalaga.Controller.smtp.FieldFocusHandle;
import metroMalaga.Controller.smtp.HandleSMTP;

/**
 * Panel for SMTP email management.
 */
public class PanelSMTP extends JPanel {

    private final HandleSMTP backend;
    private final Usuario loggedUser;

    // Componentes UI
    private JTextField txtTo, txtSubject;
    private JTextArea txtBody, txtViewer;
    private JTable emailTable;
    private DefaultTableModel tableModel;
    private JButton btnSend, btnAttach, btnClearAttach, btnRefresh, btnDelete, btnToggleRead, btnDownloadEmail,
            btnReturn;
    private JLabel lblAttachedFile, lblTo, lblSubject, lblFolder;
    private JComboBox<String> cboFolders;

    // Paneles
    private JPanel pCompose, pInbox, pButtonsCompose, pButtonsInbox, pFields, pFolderSelector, pInboxTop;
    private JSplitPane mainSplit, inboxSplit;

    private ButtonHandleSMTP buttonHandler;

    // Fuentes
    private final Font F_HEADER = new Font("Segoe UI", Font.BOLD, 16);
    private final Font F_TEXT = new Font("Segoe UI", Font.PLAIN, 14);

    /**
     * Constructor for PanelSMTP.
     * 
     * @param usuario The logged-in user.
     */
    public PanelSMTP(Usuario usuario) {
        this.loggedUser = usuario;
        this.backend = new HandleSMTP();
        backend.login(usuario.getEmailReal(), usuario.getPasswordApp());

        setupPanel();
        initUI();
        registerListeners();
        applyTheme();
    }

    /**
     * Sets up the panel layout.
     */
    private void setupPanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(new EmptyBorder(15, 15, 15, 15));
    }

    /**
     * Applies the current application theme to the panel components.
     */
    public void applyTheme() {
        Color bgMain = Common.getBackground();
        Color bgPanel = Common.getPanelBackground();
        Color txt = Common.getText();
        Color border = Common.getBorder();
        Color fieldBg = Common.getFieldBackground();

        // 1. Fondos principales
        this.setBackground(bgMain);
        if (mainSplit != null) {
            mainSplit.setBackground(bgMain);
            mainSplit.setBorder(null);
        }
        if (inboxSplit != null) {
            inboxSplit.setBackground(bgMain);
            inboxSplit.setBorder(null);
        }

        // 2. Paneles
        updatePanelStyle(pCompose, Language.get(53));
        updatePanelStyle(pInbox, Language.get(54));

        if (pFields != null)
            pFields.setBackground(bgPanel);
        if (pButtonsCompose != null)
            pButtonsCompose.setBackground(bgPanel);
        if (pButtonsInbox != null)
            pButtonsInbox.setBackground(bgPanel);
        if (pFolderSelector != null)
            pFolderSelector.setBackground(bgPanel);
        if (pInboxTop != null)
            pInboxTop.setBackground(bgPanel);

        // 3. Etiquetas
        if (lblTo != null)
            lblTo.setForeground(txt);
        if (lblSubject != null)
            lblSubject.setForeground(txt);
        if (lblFolder != null)
            lblFolder.setForeground(txt);

        if (lblAttachedFile != null) {
            String lblText = lblAttachedFile.getText();
            if (lblText.contains(Language.get(148)) || lblText.contains("NO FILES") || lblText.contains("SIN")) {
                lblAttachedFile.setForeground(Color.GRAY);
            } else {
                lblAttachedFile.setForeground(txt);
            }
        }

        // 4. Campos de texto
        updateFieldStyle(txtTo);
        updateFieldStyle(txtSubject);
        updateTextAreaStyle(txtBody);
        updateTextAreaStyle(txtViewer);

        // 5. ComboBox
        if (cboFolders != null) {
            cboFolders.setBackground(fieldBg);
            cboFolders.setForeground(txt);
            cboFolders.setBorder(new LineBorder(border, 1));
        }

        // 6. Tabla - CON RENDERER PERSONALIZADO
        if (emailTable != null) {
            emailTable.setBackground(fieldBg);
            emailTable.setForeground(txt);
            emailTable.setGridColor(Common.isDarkMode ? new Color(60, 60, 60) : new Color(240, 240, 240));
            emailTable.setSelectionBackground(Common.isDarkMode ? new Color(200, 0, 0) : new Color(230, 240, 255));
            emailTable.setSelectionForeground(Common.isDarkMode ? Color.WHITE : Color.BLACK);

            // 👇 RENDERER PARA CELDAS NORMALES
            emailTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
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

            JTableHeader header = emailTable.getTableHeader();
            header.setBackground(Common.isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
            header.setForeground(txt);
            header.setBorder(new MatteBorder(0, 0, 1, 0, border));

            // 👇 RENDERER PARA EL HEADER
            header.setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {
                    JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                            column);
                    l.setBackground(Common.isDarkMode ? new Color(40, 40, 40) : new Color(245, 245, 245));
                    l.setFont(F_HEADER);
                    l.setForeground(Common.getText());
                    l.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 1, Common.getBorder()));
                    l.setHorizontalAlignment(SwingConstants.CENTER);
                    return l;
                }
            });

            // 👇 Actualizar ScrollPane de la tabla
            updateScrollPaneTheme(emailTable, fieldBg, border);
        }

        // 7. Botones
        updateButtonsTheme();

        this.repaint();
        this.revalidate();
    }

    /**
     * Updates the style of a panel.
     * 
     * @param p     The panel.
     * @param title The title for the panel border.
     */
    private void updatePanelStyle(JPanel p, String title) {
        if (p == null)
            return;
        p.setBackground(Common.getPanelBackground());
        p.setBorder(new CompoundBorder(
                new LineBorder(Common.getBorder(), 1),
                new TitledBorder(new EmptyBorder(10, 10, 10, 10), title,
                        TitledBorder.LEFT, TitledBorder.TOP, F_HEADER, Common.getText())));
    }

    /**
     * Updates the style of a text component.
     * 
     * @param c The text component.
     */
    private void updateFieldStyle(JTextComponent c) {
        if (c == null)
            return;
        c.setBackground(Common.getFieldBackground());
        c.setForeground(Common.getText());
        c.setCaretColor(Common.getText());
        if (c instanceof JTextField) {
            c.setBorder(new CompoundBorder(
                    new LineBorder(Common.getBorder(), 1),
                    new EmptyBorder(2, 5, 2, 5)));
        }
    }

    /**
     * Updates the style of a text area.
     * 
     * @param area The text area.
     */
    private void updateTextAreaStyle(JTextArea area) {
        if (area == null)
            return;
        area.setBackground(Common.getFieldBackground());
        area.setForeground(Common.getText());
        area.setCaretColor(Common.getText());
        area.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Actualizar ScrollPane
        updateScrollPaneTheme(area, Common.getFieldBackground(), Common.getBorder());
    }

    /**
     * Updates the theme of a scroll pane containing a component.
     * 
     * @param component The component inside the scroll pane.
     * @param bg        Background color.
     * @param border    Border color.
     */
    private void updateScrollPaneTheme(Component component, Color bg, Color border) {
        if (component.getParent() instanceof JViewport) {
            JViewport viewport = (JViewport) component.getParent();
            viewport.setBackground(bg);
            if (viewport.getParent() instanceof JScrollPane) {
                JScrollPane scroll = (JScrollPane) viewport.getParent();
                scroll.setBackground(bg);
                scroll.setBorder(new LineBorder(border, 1));
                scroll.getViewport().setBackground(bg);
            }
        }
    }

    /**
     * Updates the theme of the buttons.
     */
    private void updateButtonsTheme() {
        Color bg = Common.getPanelBackground();
        Color txt = Common.getText();
        Color danger = Common.getDanger();
        Color accent = Common.getAccent();

        JButton[] normalButtons = { btnAttach, btnRefresh, btnToggleRead, btnDownloadEmail };
        for (JButton b : normalButtons) {
            if (b != null) {
                b.setBackground(bg);
                b.setForeground(txt);
                b.setBorder(new LineBorder(Common.getBorder(), 1));
            }
        }

        if (btnSend != null) {
            btnSend.setBackground(bg);
            btnSend.setForeground(accent);
            btnSend.setBorder(new LineBorder(accent, 1, true));
        }

        JButton[] dangerButtons = { btnClearAttach, btnDelete, btnReturn };
        for (JButton b : dangerButtons) {
            if (b != null) {
                b.setBackground(danger);
                b.setForeground(Color.WHITE);
                b.setBorder(new LineBorder(danger, 1));
            }
        }
    }

    /**
     * Updates all text labels based on the current language.
     */
    public void updateAllTexts() {
        lblTo.setText(Language.get(55));
        lblSubject.setText(Language.get(56));

        if (lblFolder != null)
            lblFolder.setText(Language.get(197));

        String currentLabel = lblAttachedFile.getText();
        if (currentLabel.contains("No ") || currentLabel.contains("files") || currentLabel.contains("SIN")) {
            lblAttachedFile.setText(Language.get(148));
        }

        btnAttach.setText(Language.get(57));
        btnClearAttach.setText(Language.get(58));
        btnSend.setText(Language.get(59));

        tableModel.setColumnIdentifiers(new String[] {
                Language.get(61), Language.get(62), Language.get(63)
        });

        btnRefresh.setText(Language.get(64));
        btnToggleRead.setText(Language.get(65));
        btnDownloadEmail.setText(Language.get(66));
        btnDelete.setText(Language.get(67));
        btnReturn.setText(Language.get(69));

        String viewerText = txtViewer.getText();
        if (viewerText.contains("Select") || viewerText.contains("Selecciona")) {
            txtViewer.setText(Language.get(68));
        }

        // Actualizar títulos de paneles
        updatePanelStyle(pCompose, Language.get(53));
        updatePanelStyle(pInbox, Language.get(54));

        revalidate();
        repaint();
    }

    /**
     * Initializes the UI components.
     */
    private void initUI() {
        pCompose = new JPanel(new BorderLayout(10, 10));
        pFields = new JPanel(new GridBagLayout());

        txtTo = createField();
        txtSubject = createField();
        lblTo = new JLabel(Language.get(55));
        lblSubject = new JLabel(Language.get(56));

        addGBC(pFields, lblTo, 0, 0, 0.05);
        addGBC(pFields, txtTo, 1, 0, 0.95);
        addGBC(pFields, lblSubject, 0, 1, 0.05);
        addGBC(pFields, txtSubject, 1, 1, 0.95);

        txtBody = createTextArea();
        JScrollPane scrollBody = createScrollPane(txtBody);

        pButtonsCompose = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        btnAttach = createButton(Language.get(57), false);
        btnClearAttach = createButton(Language.get(58), true);
        btnSend = createButton(Language.get(59), false);

        lblAttachedFile = new JLabel(Language.get(148));
        lblAttachedFile.setForeground(Color.GRAY);
        lblAttachedFile.setFont(F_TEXT);
        lblAttachedFile.setPreferredSize(new Dimension(250, 30));

        pButtonsCompose.add(btnAttach);
        pButtonsCompose.add(btnClearAttach);
        pButtonsCompose.add(lblAttachedFile);
        pButtonsCompose.add(Box.createHorizontalStrut(20));
        pButtonsCompose.add(btnSend);

        pCompose.add(pFields, BorderLayout.NORTH);
        pCompose.add(scrollBody, BorderLayout.CENTER);
        pCompose.add(pButtonsCompose, BorderLayout.SOUTH);

        // --- Panel Inbox ---
        pInbox = new JPanel(new BorderLayout(10, 10));

        pFolderSelector = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        lblFolder = new JLabel(Language.get(197));
        lblFolder.setFont(F_TEXT);

        cboFolders = new JComboBox<>();
        cboFolders.setPreferredSize(new Dimension(200, 30));
        cboFolders.setFont(F_TEXT);

        // Cargar carpetas disponibles
        List<String> allFolders = backend.getAvailableFolders();
        List<String> allowedFolders = Arrays.asList("INBOX", "Spam", "Trash", "[Gmail]/Spam", "[Gmail]/Trash");

        for (String folder : allFolders) {
            if (allowedFolders.stream().anyMatch(allowed -> folder.equalsIgnoreCase(allowed) ||
                    folder.contains("Spam") ||
                    folder.contains("Trash") ||
                    folder.contains("INBOX"))) {
                cboFolders.addItem(folder);
            }
        }

        if (cboFolders.getItemCount() == 0) {
            cboFolders.addItem("INBOX");
        }

        cboFolders.setSelectedItem("INBOX");

        pFolderSelector.add(lblFolder);
        pFolderSelector.add(cboFolders);

        String[] cols = { Language.get(61), Language.get(62), Language.get(63) };
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        emailTable = createTable();

        txtViewer = createTextArea();
        txtViewer.setEditable(false);
        txtViewer.setText(Language.get(68));

        inboxSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createScrollPane(emailTable),
                createScrollPane(txtViewer));
        inboxSplit.setDividerLocation(500);
        inboxSplit.setDividerSize(5);
        inboxSplit.setBorder(null);

        pInboxTop = new JPanel(new BorderLayout());
        pInboxTop.add(pFolderSelector, BorderLayout.NORTH);
        pInboxTop.add(inboxSplit, BorderLayout.CENTER);

        pButtonsInbox = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));

        btnRefresh = createButton(Language.get(64), false);
        btnToggleRead = createButton(Language.get(65), false);
        btnDownloadEmail = createButton(Language.get(66), false);
        btnDownloadEmail.setEnabled(false);
        btnDelete = createButton(Language.get(67), true);
        btnReturn = createButton(Language.get(69), true);

        pButtonsInbox.add(btnRefresh);
        pButtonsInbox.add(btnToggleRead);
        pButtonsInbox.add(btnDownloadEmail);
        pButtonsInbox.add(btnDelete);
        pButtonsInbox.add(btnReturn);

        pInbox.add(pInboxTop, BorderLayout.CENTER);
        pInbox.add(pButtonsInbox, BorderLayout.SOUTH);

        mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, pCompose, pInbox);
        mainSplit.setDividerLocation(380);
        mainSplit.setBorder(null);
        mainSplit.setDividerSize(1);

        add(mainSplit, BorderLayout.CENTER);
    }

    /**
     * Registers action listeners for components.
     */
    private void registerListeners() {
        this.buttonHandler = new ButtonHandleSMTP(this, backend);

        cboFolders.addActionListener(e -> {
            String selectedFolder = (String) cboFolders.getSelectedItem();
            if (selectedFolder != null) {
                backend.setCurrentFolder(selectedFolder);
                buttonHandler.refreshInbox(false);
            }
        });

        applyHover(btnAttach, false);
        applyHover(btnRefresh, false);
        applyHover(btnToggleRead, false);
        applyHover(btnDownloadEmail, false);
        applyHover(btnSend, false);
        applyHover(btnClearAttach, true);
        applyHover(btnDelete, true);
        applyHover(btnReturn, true);
    }

    /**
     * Applies hover effect to a button.
     * 
     * @param btn      The button.
     * @param isDanger Whether it's a danger button.
     */
    private void applyHover(JButton btn, boolean isDanger) {
        Color bg = isDanger ? Common.getDanger() : Common.getPanelBackground();
        Color fg = isDanger ? Color.WHITE : Common.getText();
        Color borderColor = isDanger ? Common.getDanger() : (btn == btnSend ? Common.getAccent() : Common.getBorder());

        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setBorder(new CompoundBorder(
                new LineBorder(borderColor, 1),
                new EmptyBorder(5, 10, 5, 10)));

        for (java.awt.event.MouseListener ml : btn.getMouseListeners()) {
            if (ml instanceof ButtonHoverHandle) {
                btn.removeMouseListener(ml);
            }
        }
    }

    /**
     * Adds a component to a panel using GridBagLayout.
     * 
     * @param p      The panel.
     * @param c      The component.
     * @param x      Grid X position.
     * @param y      Grid Y position.
     * @param weight Horizontal weight.
     */
    private void addGBC(JPanel p, Component c, int x, int y, double weight) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;
        gbc.weightx = weight;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 5, 8, 5);
        if (c instanceof JLabel) {
            ((JLabel) c).setFont(F_TEXT);
        }
        p.add(c, gbc);
    }

    /**
     * Creates a text field.
     * 
     * @return The text field.
     */
    private JTextField createField() {
        JTextField f = new JTextField();
        f.setPreferredSize(new Dimension(200, 30));
        f.setFont(F_TEXT);
        f.addFocusListener(new FieldFocusHandle(f, Common.getBorder(), Common.getAccent()));
        return f;
    }

    /**
     * Creates a text area.
     * 
     * @return The text area.
     */
    private JTextArea createTextArea() {
        JTextArea a = new JTextArea();
        a.setFont(F_TEXT);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBorder(new EmptyBorder(5, 5, 5, 5));
        return a;
    }

    /**
     * Creates a scroll pane for a component.
     * 
     * @param c The component.
     * @return The scroll pane.
     */
    private JScrollPane createScrollPane(Component c) {
        return new JScrollPane(c);
    }

    /**
     * Creates a button.
     * 
     * @param text     Button text.
     * @param isDanger Whether it's a danger button.
     * @return The button.
     */
    private JButton createButton(String text, boolean isDanger) {
        JButton b = new JButton(text);
        b.setPreferredSize(new Dimension(140, 35));
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    /**
     * Creates the email table.
     * 
     * @return The table.
     */
    private JTable createTable() {
        JTable t = new JTable(tableModel);
        t.setFont(F_TEXT);
        t.setRowHeight(28);
        t.setShowVerticalLines(false);
        return t;
    }

    // Getters

    /**
     * Gets the "To" text field.
     * 
     * @return The "To" text field.
     */
    public JTextField getTxtTo() {
        return txtTo;
    }

    /**
     * Gets the "Subject" text field.
     * 
     * @return The "Subject" text field.
     */
    public JTextField getTxtSubject() {
        return txtSubject;
    }

    /**
     * Gets the body text area.
     * 
     * @return The body text area.
     */
    public JTextArea getTxtBody() {
        return txtBody;
    }

    /**
     * Gets the viewer text area.
     * 
     * @return The viewer text area.
     */
    public JTextArea getTxtViewer() {
        return txtViewer;
    }

    /**
     * Gets the email table.
     * 
     * @return The email table.
     */
    public JTable getEmailTable() {
        return emailTable;
    }

    /**
     * Gets the table model.
     * 
     * @return The table model.
     */
    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    /**
     * Gets the send button.
     * 
     * @return The send button.
     */
    public JButton getBtnSend() {
        return btnSend;
    }

    /**
     * Gets the attach button.
     * 
     * @return The attach button.
     */
    public JButton getBtnAttach() {
        return btnAttach;
    }

    /**
     * Gets the clear attachment button.
     * 
     * @return The clear attachment button.
     */
    public JButton getBtnClearAttach() {
        return btnClearAttach;
    }

    /**
     * Gets the refresh button.
     * 
     * @return The refresh button.
     */
    public JButton getBtnRefresh() {
        return btnRefresh;
    }

    /**
     * Gets the delete button.
     * 
     * @return The delete button.
     */
    public JButton getBtnDelete() {
        return btnDelete;
    }

    /**
     * Gets the toggle read button.
     * 
     * @return The toggle read button.
     */
    public JButton getBtnToggleRead() {
        return btnToggleRead;
    }

    /**
     * Gets the download email button.
     * 
     * @return The download email button.
     */
    public JButton getBtnDownloadEmail() {
        return btnDownloadEmail;
    }

    /**
     * Gets the attached file label.
     * 
     * @return The attached file label.
     */
    public JLabel getLblAttachedFile() {
        return lblAttachedFile;
    }

    /**
     * Gets the return button.
     * 
     * @return The return button.
     */
    public JButton getBtnReturn() {
        return btnReturn;
    }

    /**
     * Sets the return button.
     * 
     * @param btnReturn The new return button.
     */
    public void setBtnReturn(JButton btnReturn) {
        this.btnReturn = btnReturn;
    }

    /**
     * Sets the callback for the return action.
     * 
     * @param callback The callback to execute.
     */
    public void setOnReturnCallback(Runnable callback) {
        if (buttonHandler != null) {
            buttonHandler.setOnReturnCallback(callback);
        }
    }
}
