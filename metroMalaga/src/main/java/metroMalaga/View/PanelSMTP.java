package metroMalaga.frontend.smtp;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import metroMalaga.Clases.Usuario;
import metroMalaga.backend.smtp.ButtonHandleSMTP;
import metroMalaga.backend.smtp.ButtonHoverHandle;
import metroMalaga.backend.smtp.HandleSMTP;

public class PanelSMTP extends JFrame {

    private HandleSMTP backend;
    private Usuario loggedUser;

    // --- UI COMPONENTS ---
    private JTextField txtTo, txtSubject;
    private JTextArea txtBody, txtViewer;
    private JTable emailTable;
    private DefaultTableModel tableModel;

    private JButton btnSend, btnAttach, btnClearAttach;
    private JLabel lblAttachedFile;
    private JButton btnRefresh, btnDelete, btnToggleRead, btnDownloadEmail;

    // --- STYLE CONSTANTS ---
    private final Color P5_RED = new Color(220, 20, 60);
    private final Color P5_BRIGHT_RED = new Color(255, 0, 0);
    private final Color P5_BLACK = new Color(26, 26, 26); // #1A1A1A
    private final Color P5_WHITE = new Color(240, 240, 240);
    private final Color P5_GRAY_PLACEHOLDER = new Color(150, 150, 150);

    private final Font P5_HEADER_FONT = new Font("SansSerif", Font.BOLD | Font.ITALIC, 20);
    private final Font P5_TEXT_FONT = new Font("SansSerif", Font.BOLD, 14);
    private final Font P5_BUTTON_FONT = new Font("Dialog", Font.BOLD | Font.ITALIC, 16);

    public PanelSMTP(Usuario usuario) {
        this.loggedUser = usuario;
        this.backend = new HandleSMTP();

        backend.login(usuario.getEmailReal(), usuario.getPasswordApp());

        settingsFrame();

        JPanel backgroundPanel = new JPanel(new BorderLayout(15, 15));
        backgroundPanel.setBackground(P5_RED);
        backgroundPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        setContentPane(backgroundPanel);

        initComponents(backgroundPanel);
        
        // --- ATTACH LOGIC AND VISUAL LISTENERS ---
        registerListeners();
    }

    private void settingsFrame() {
        setTitle("Phantom Thieves Mail - " + loggedUser.getUsernameApp());
        setSize(1000, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    private void initComponents(JPanel mainContainer) {
        // --- 1. COMPOSE PANEL ---
        JPanel composePanel = new JPanel(new BorderLayout(10, 10));
        stylePanel(composePanel, "COMPOSE MESSAGE");

        JPanel fieldsPanel = new JPanel(new GridBagLayout());
        fieldsPanel.setBackground(P5_BLACK);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.1;
        fieldsPanel.add(createLabel("To:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.9;
        txtTo = new JTextField();
        styleP5Field(txtTo);
        fieldsPanel.add(txtTo, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.1;
        fieldsPanel.add(createLabel("Subject:"), gbc);

        gbc.gridx = 1; gbc.weightx = 0.9;
        txtSubject = new JTextField();
        styleP5Field(txtSubject);
        fieldsPanel.add(txtSubject, gbc);

        txtBody = new JTextArea(5, 20);
        styleP5TextArea(txtBody);
        JScrollPane scrollBody = styleScrollPane(new JScrollPane(txtBody));

        JPanel bottomComposePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        bottomComposePanel.setBackground(P5_BLACK);

        btnAttach = createP5Button("Attach (+)", false);
        btnClearAttach = createP5Button("Clear", true); // True = dark button
        
        lblAttachedFile = new JLabel("NO FILES");
        lblAttachedFile.setForeground(P5_GRAY_PLACEHOLDER);
        lblAttachedFile.setFont(P5_TEXT_FONT);
        lblAttachedFile.setPreferredSize(new Dimension(250, 30));

        btnSend = createP5Button("SEND CARD", false);
        btnSend.setBackground(P5_WHITE);
        btnSend.setForeground(P5_BLACK);

        bottomComposePanel.add(btnAttach);
        bottomComposePanel.add(btnClearAttach);
        bottomComposePanel.add(lblAttachedFile);
        bottomComposePanel.add(Box.createHorizontalStrut(20));
        bottomComposePanel.add(btnSend);

        composePanel.add(fieldsPanel, BorderLayout.NORTH);
        composePanel.add(scrollBody, BorderLayout.CENTER);
        composePanel.add(bottomComposePanel, BorderLayout.SOUTH);

        // --- 2. INBOX PANEL ---
        JPanel inboxPanel = new JPanel(new BorderLayout(10, 10));
        stylePanel(inboxPanel, "INBOX / COGNITION");

        String[] columns = { "Status", "Sender", "Subject" };
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        emailTable = new JTable(tableModel);
        styleP5Table(emailTable);
        JScrollPane scrollTable = styleScrollPane(new JScrollPane(emailTable));

        txtViewer = new JTextArea("Select a target...");
        styleP5TextArea(txtViewer);
        txtViewer.setEditable(false);
        JScrollPane scrollViewer = styleScrollPane(new JScrollPane(txtViewer));

        JSplitPane splitInbox = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollTable, scrollViewer);
        splitInbox.setDividerLocation(500);
        splitInbox.setDividerSize(5);
        splitInbox.setBackground(P5_BLACK);
        splitInbox.setBorder(null);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(P5_BLACK);

        btnRefresh = createP5Button("REFRESH", false);
        btnToggleRead = createP5Button("READ/UNREAD", false);
        btnDownloadEmail = createP5Button("DOWNLOAD .EML", false);
        btnDownloadEmail.setEnabled(false);

        btnDelete = createP5Button("ELIMINATE", true); // Dark/Red button

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnToggleRead);
        buttonPanel.add(btnDownloadEmail);
        buttonPanel.add(btnDelete);

        inboxPanel.add(splitInbox, BorderLayout.CENTER);
        inboxPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- 3. MAIN SPLIT ---
        JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, composePanel, inboxPanel);
        mainSplit.setDividerLocation(380);
        mainSplit.setBackground(P5_RED);
        mainSplit.setBorder(null);
        mainSplit.setDividerSize(10);

        mainSplit.setUI(new javax.swing.plaf.basic.BasicSplitPaneUI() {
            public javax.swing.plaf.basic.BasicSplitPaneDivider createDefaultDivider() {
                return new javax.swing.plaf.basic.BasicSplitPaneDivider(this) {
                    public void setBorder(Border b) {}
                    @Override
                    public void paint(Graphics g) {
                        g.setColor(P5_RED);
                        g.fillRect(0, 0, getSize().width, getSize().height);
                        super.paint(g);
                    }
                };
            }
        });

        mainContainer.add(mainSplit, BorderLayout.CENTER);
    }

    // --- LISTENER REGISTRATION METHOD (Separated) ---
    private void registerListeners() {
        
        // 1. LOGIC CONTROLLER (Passing all components via Constructor)
        ButtonHandleSMTP logicController = new ButtonHandleSMTP(
            this, backend, txtTo, txtSubject, txtBody, lblAttachedFile, 
            btnSend, btnAttach, btnClearAttach, btnRefresh, btnToggleRead, btnDownloadEmail, btnDelete,
            emailTable, tableModel, txtViewer
        );

        // Logic Assignments
        btnAttach.addActionListener(logicController);
        btnClearAttach.addActionListener(logicController);
        btnSend.addActionListener(logicController);
        btnRefresh.addActionListener(logicController);
        btnToggleRead.addActionListener(logicController);
        btnDownloadEmail.addActionListener(logicController);
        btnDelete.addActionListener(logicController);
        
        // Mouse listener for table click
        emailTable.addMouseListener(logicController);

        // 2. VISUAL HOVER CONTROLLERS (Using ButtonHoverHandler)
        
        // Standard Red Buttons
        addHoverEffect(btnAttach, P5_BRIGHT_RED, P5_WHITE, P5_BLACK, P5_WHITE);
        addHoverEffect(btnRefresh, P5_BRIGHT_RED, P5_WHITE, P5_BLACK, P5_WHITE);
        addHoverEffect(btnToggleRead, P5_BRIGHT_RED, P5_WHITE, P5_BLACK, P5_WHITE);
        addHoverEffect(btnDownloadEmail, P5_BRIGHT_RED, P5_WHITE, P5_BLACK, P5_WHITE);
        
        // Send Button (White to Red)
        addHoverEffect(btnSend, P5_WHITE, P5_BLACK, P5_BRIGHT_RED, P5_WHITE);
        
        // Dark Buttons (Clear & Delete)
        addHoverEffect(btnClearAttach, P5_BLACK, P5_BRIGHT_RED, P5_BRIGHT_RED, P5_WHITE);
        addHoverEffect(btnDelete, P5_BLACK, P5_BRIGHT_RED, P5_BRIGHT_RED, P5_WHITE);
    }

    private void addHoverEffect(JButton btn, Color bg, Color fg, Color hoverBg, Color hoverFg) {
        MatteBorder defaultBorder = new MatteBorder(2, 2, 4, 2, hoverBg); 
        MatteBorder hoverBorder = new MatteBorder(2, 2, 4, 2, fg);
        
        btn.setBorder(defaultBorder);
        btn.addMouseListener(new ButtonHoverHandle(
            btn, bg, fg, hoverBg, hoverFg, defaultBorder, hoverBorder
        ));
    }

    // --- STYLE METHODS ---
    private void stylePanel(JPanel panel, String title) {
        panel.setBackground(P5_BLACK);
        TitledBorder border = BorderFactory.createTitledBorder(new MatteBorder(4, 4, 4, 4, P5_BLACK), title);
        border.setTitleColor(P5_WHITE);
        border.setTitleFont(P5_HEADER_FONT);
        border.setTitleJustification(TitledBorder.LEFT);
        panel.setBorder(new CompoundBorder(new MatteBorder(2, 2, 5, 2, P5_BLACK), 
                new CompoundBorder(new MatteBorder(2, 2, 2, 2, P5_WHITE), new EmptyBorder(10, 10, 10, 10))));
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(P5_BRIGHT_RED);
        label.setFont(P5_TEXT_FONT);
        return label;
    }

    private void styleP5Field(JTextField field) {
        field.setPreferredSize(new Dimension(200, 35));
        field.setBackground(P5_BLACK);
        field.setForeground(P5_WHITE);
        field.setCaretColor(P5_BRIGHT_RED);
        field.setFont(P5_TEXT_FONT);
        MatteBorder unfocusedBorder = new MatteBorder(2, 2, 4, 2, P5_WHITE);
        MatteBorder focusedBorder = new MatteBorder(2, 2, 4, 2, P5_BRIGHT_RED);
        field.setBorder(new CompoundBorder(unfocusedBorder, new EmptyBorder(2, 5, 2, 5)));
        field.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) { field.setBorder(new CompoundBorder(focusedBorder, new EmptyBorder(2, 5, 2, 5))); }
            public void focusLost(FocusEvent e) { field.setBorder(new CompoundBorder(unfocusedBorder, new EmptyBorder(2, 5, 2, 5))); }
        });
    }

    private void styleP5TextArea(JTextArea area) {
        area.setBackground(P5_BLACK);
        area.setForeground(P5_WHITE);
        area.setCaretColor(P5_BRIGHT_RED);
        area.setFont(P5_TEXT_FONT);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private JScrollPane styleScrollPane(JScrollPane scroll) {
        scroll.setBorder(new MatteBorder(2, 2, 2, 2, P5_WHITE));
        scroll.setBackground(P5_BLACK);
        scroll.getViewport().setBackground(P5_BLACK);
        scroll.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() { this.thumbColor = P5_BRIGHT_RED; this.trackColor = P5_BLACK; }
        });
        return scroll;
    }

    private JButton createP5Button(String text, boolean dark) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(140, 40));
        btn.setFont(P5_BUTTON_FONT);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (dark) {
            btn.setBackground(P5_BLACK);
            btn.setForeground(P5_BRIGHT_RED);
        } else {
            btn.setBackground(P5_BRIGHT_RED);
            btn.setForeground(P5_WHITE);
        }
        return btn;
    }

    private void styleP5Table(JTable table) {
        table.setBackground(P5_BLACK);
        table.setForeground(P5_WHITE);
        table.setFont(P5_TEXT_FONT);
        table.setRowHeight(30);
        table.setGridColor(P5_RED);
        table.setSelectionBackground(P5_BRIGHT_RED);
        table.setSelectionForeground(P5_BLACK);
        JTableHeader header = table.getTableHeader();
        header.setBackground(P5_RED);
        header.setForeground(P5_BLACK);
        header.setFont(P5_HEADER_FONT);
        header.setBorder(new MatteBorder(0, 0, 2, 0, P5_WHITE));
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
        renderer.setBackground(P5_BLACK);
        renderer.setForeground(P5_WHITE);
        table.setDefaultRenderer(Object.class, renderer);
    }
}