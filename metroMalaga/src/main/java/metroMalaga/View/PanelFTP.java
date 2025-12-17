package metroMalaga.View;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

import org.apache.commons.net.ftp.FTPFile;

import metroMalaga.Controller.ServiceFTP;
import metroMalaga.Controller.ftp.FTPButtonsEditor;
import metroMalaga.Controller.ftp.FTPButtonsRenderer;
import metroMalaga.Controller.ftp.FTPLanguageController;
import metroMalaga.Model.FTPTableModel;
import metroMalaga.Model.Usuario;
import metroMalaga.Model.Language;

public class PanelFTP extends JFrame {
    private Usuario user;
    private FTPTableModel ftpModel;
    private JTable fileTable;
    private JTextField searchField;
    private JButton uploadButton, upButton, returnButton, folderButton, btnLanguage;
    private JLabel lblFilter;
    private ServiceFTP service;

    private static final Color ACCENT_RED = new Color(220, 53, 69);
    private static final Color ACCENT_BLUE = new Color(70, 130, 180);
    private static final Color BACKGROUND_LIGHT = Color.WHITE;
    private static final Color HEADER_GRAY = new Color(248, 249, 250);

    public PanelFTP(Usuario user, ServiceFTP service, List<FTPFile> initialFiles, FTPTableModel ftpModel) {
        this.service = service;
        this.ftpModel = ftpModel;
        this.user = user;
        initializeComponents();
        applyStyle();
        setupFrameConfiguration();
        setupLayout();
        createLanguageButton();
        
        new FTPLanguageController(this);
    }

    private void createLanguageButton() {
        String langText = Language.getCurrentLanguage().equals("espanol") ? "🇪🇸 ES" : "🇬🇧 EN";
        btnLanguage = new JButton(langText);
        btnLanguage.setBounds(680, 10, 100, 35);
        btnLanguage.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLanguage.setBackground(ACCENT_BLUE);
        btnLanguage.setForeground(Color.WHITE);
        btnLanguage.setBorder(new CompoundBorder(
            new LineBorder(ACCENT_BLUE, 1, true),
            new EmptyBorder(5, 10, 5, 10)
        ));
        btnLanguage.setFocusPainted(false);
        btnLanguage.setCursor(new Cursor(Cursor.HAND_CURSOR));

        getLayeredPane().add(btnLanguage, JLayeredPane.PALETTE_LAYER);
    }

    public void updateAllTexts() {
        setTitle(Language.get(83) + " - " + user.getUsernameApp().toUpperCase());

        returnButton.setText(Language.get(84));
        folderButton.setText("📁 " + Language.get(85));
        lblFilter.setText(Language.get(86));
        
        ftpModel.fireTableStructureChanged();
        
        FTPButtonsEditor buttonsEditor = new FTPButtonsEditor(this.service, this.ftpModel, user);
        fileTable.getColumnModel().getColumn(3).setCellRenderer(new FTPButtonsRenderer());
        fileTable.getColumnModel().getColumn(3).setCellEditor(buttonsEditor);
        fileTable.setRowHeight(30);

        revalidate();
        repaint();
    }

    private void initializeComponents() {
        this.fileTable = new JTable(this.ftpModel);
        this.searchField = new JTextField(20);
        this.uploadButton = new JButton("⤒");
        this.upButton = new JButton("🔙");
        this.returnButton = new JButton(Language.get(84));
        this.folderButton = new JButton("📁 " + Language.get(85));
        this.lblFilter = new JLabel(Language.get(86));

        FTPButtonsEditor buttonsEditor = new FTPButtonsEditor(this.service, this.ftpModel, user);
        fileTable.getColumnModel().getColumn(3).setCellRenderer(new FTPButtonsRenderer());
        fileTable.getColumnModel().getColumn(3).setCellEditor(buttonsEditor);
        fileTable.setRowHeight(30);
    }

    private void applyStyle() {
        Font modernFont = new Font("Dialog", Font.PLAIN, 14);
        this.getContentPane().setBackground(BACKGROUND_LIGHT);
        searchField.setFont(modernFont);
        searchField.setBorder(new EmptyBorder(5, 10, 5, 10));

        fileTable.setBackground(BACKGROUND_LIGHT);
        fileTable.setFont(modernFont);
        fileTable.setGridColor(HEADER_GRAY);
        fileTable.setSelectionBackground(new Color(230, 245, 255));

        JTableHeader header = fileTable.getTableHeader();
        header.setFont(modernFont.deriveFont(Font.BOLD, 14));
        header.setBackground(HEADER_GRAY);
        header.setForeground(Color.BLACK);
        header.setBorder(null);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < fileTable.getColumnCount() - 1; i++) {
            fileTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        styleButton(uploadButton, ACCENT_RED, Color.WHITE);
        styleButton(upButton, Color.GRAY, Color.WHITE);
        styleButton(returnButton, ACCENT_RED, Color.WHITE);
        styleButton(folderButton, new Color(40, 167, 69), Color.WHITE);

        folderButton.setFont(modernFont);
        uploadButton.setFont(modernFont);
        upButton.setFont(modernFont);
        returnButton.setFont(modernFont);
    }

    private void styleButton(JButton button, Color background, Color foreground) {
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void setupFrameConfiguration() {
        this.setTitle(Language.get(83) + " - " + user.getUsernameApp().toUpperCase());
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
    }

    private void setupLayout() {
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        actionPanel.setBackground(HEADER_GRAY);
        actionPanel.add(this.uploadButton);
        actionPanel.add(this.upButton);
        actionPanel.add(this.folderButton);
        actionPanel.add(this.returnButton);

        JPanel filterPanel = new JPanel();
        filterPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        filterPanel.add(lblFilter);
        filterPanel.add(this.searchField);
        filterPanel.setBackground(HEADER_GRAY);
        filterPanel.setBorder(new EmptyBorder(0, 10, 0, 10));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(HEADER_GRAY);
        topPanel.add(actionPanel, BorderLayout.WEST);
        topPanel.add(filterPanel, BorderLayout.EAST);

        this.add(topPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(fileTable);
        scrollPane.setBorder(null);
        this.add(scrollPane, BorderLayout.CENTER);

        this.setVisible(true);
    }

    public JButton getFolderButton() {
        return folderButton;
    }

    public JTable getFileTable() {
        return fileTable;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JButton getUploadButton() {
        return uploadButton;
    }

    public JButton getUpButton() {
        return upButton;
    }

    public JButton getReturnButton() {
        return returnButton;
    }

    public JButton getBtnLanguage() {
        return btnLanguage;
    }
}
