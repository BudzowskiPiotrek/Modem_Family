package metroMalaga.frontend.smtp;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

import metroMalaga.Clases.Usuario;
import metroMalaga.backend.smtp.HandleSMTP;
import metroMalaga.Clases.EmailModel;

public class PanelSMTP extends JFrame {

    private HandleSMTP backend;
    private Usuario loggedUser;
    
    // List to manage data in memory
    private List<EmailModel> currentEmailList; 

    // Components
    private JTextField txtTo;
    private JTextField txtSubject;
    private JTextArea txtBody;
    
    private JTable emailTable;
    private DefaultTableModel tableModel;
    private JTextArea txtViewer; // To view email content

    private JButton btnSend;
    private JButton btnRefresh;
    private JButton btnDelete;
    private JButton btnToggleRead;

    public PanelSMTP(Usuario usuario) {
        this.loggedUser = usuario;
        this.backend = new HandleSMTP();
        this.currentEmailList = new ArrayList<>();
        
        // Login to backend
        backend.login(usuario.getEmailReal(), usuario.getPasswordApp());

        setTitle("Email Manager - " + usuario.getUsernameApp());
        setSize(900, 700);
        setLocationRelativeTo(null); 
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- TOP PANEL: COMPOSE ---
        JPanel composePanel = new JPanel(new BorderLayout(5, 5));
        composePanel.setBorder(new TitledBorder("Compose from: " + loggedUser.getEmailReal()));
        
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2));
        fieldsPanel.add(new JLabel("To:")); 
        txtTo = new JTextField(); 
        fieldsPanel.add(txtTo);
        
        fieldsPanel.add(new JLabel("Subject:")); 
        txtSubject = new JTextField(); 
        fieldsPanel.add(txtSubject);
        
        txtBody = new JTextArea(3, 20);
        btnSend = new JButton("Send Email");
        
        composePanel.add(fieldsPanel, BorderLayout.NORTH);
        composePanel.add(new JScrollPane(txtBody), BorderLayout.CENTER);
        composePanel.add(btnSend, BorderLayout.SOUTH);

        // --- BOTTOM PANEL: INBOX ---
        JPanel inboxPanel = new JPanel(new BorderLayout(5, 5));
        inboxPanel.setBorder(new TitledBorder("Inbox (POP3)"));

        // Table Configuration
        String[] columns = {"Status", "Sender", "Subject"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        emailTable = new JTable(tableModel);
        emailTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Viewer (Right side)
        txtViewer = new JTextArea("Select an email to read...");
        txtViewer.setLineWrap(true);
        txtViewer.setEditable(false);
        txtViewer.setBackground(new Color(245, 245, 245));

        // Split Pane
        JSplitPane splitInbox = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
                new JScrollPane(emailTable), new JScrollPane(txtViewer));
        splitInbox.setDividerLocation(450);

        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnRefresh = new JButton("Refresh Inbox");
        btnToggleRead = new JButton("Mark Read/Unread");
        btnDelete = new JButton("Delete Selected");
        btnDelete.setForeground(Color.RED);

        buttonPanel.add(btnRefresh);
        buttonPanel.add(btnToggleRead);
        buttonPanel.add(btnDelete);

        inboxPanel.add(splitInbox, BorderLayout.CENTER);
        inboxPanel.add(buttonPanel, BorderLayout.SOUTH);

        // --- MAIN SPLIT ---
        JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, composePanel, inboxPanel);
        mainSplit.setDividerLocation(250);

        mainPanel.add(mainSplit, BorderLayout.CENTER);
        setContentPane(mainPanel);

        setupEvents();
    }

    private void setupEvents() {
        
        // 1. SEND EVENT
        btnSend.addActionListener(e -> {
            new Thread(() -> {
                try {
                    btnSend.setEnabled(false); btnSend.setText("Sending...");
                    backend.sendEmail(txtTo.getText(), txtSubject.getText(), txtBody.getText());
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "Email Sent Successfully.");
                        txtTo.setText(""); txtSubject.setText(""); txtBody.setText("");
                        btnSend.setText("Send Email"); btnSend.setEnabled(true);
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
                        btnSend.setText("Send Email"); btnSend.setEnabled(true);
                    });
                }
            }).start();
        });

        // 2. REFRESH EVENT
        btnRefresh.addActionListener(e -> {
            new Thread(() -> {
                SwingUtilities.invokeLater(() -> {
                    btnRefresh.setEnabled(false);
                    tableModel.setRowCount(0);
                    txtViewer.setText("Loading...");
                });

                try {
                    currentEmailList = backend.fetchEmails();

                    SwingUtilities.invokeLater(() -> {
                        for (EmailModel mail : currentEmailList) {
                            String status = mail.isRead() ? "Read" : "UNREAD";
                            tableModel.addRow(new Object[]{status, mail.getSender(), mail.getSubject()});
                        }
                        txtViewer.setText("Inbox updated. " + currentEmailList.size() + " messages.");
                        btnRefresh.setEnabled(true);
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> {
                        txtViewer.setText("Connection Error.");
                        JOptionPane.showMessageDialog(this, "Error fetching emails: " + ex.getMessage());
                        btnRefresh.setEnabled(true);
                    });
                }
            }).start();
        });

        // 3. TABLE CLICK EVENT
        emailTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = emailTable.getSelectedRow();
                if (row >= 0 && row < currentEmailList.size()) {
                    EmailModel mail = currentEmailList.get(row);
                    txtViewer.setText("FROM: " + mail.getSender() + "\n" +
                                      "SUBJECT: " + mail.getSubject() + "\n" +
                                      "--------------------------------\n" + 
                                      mail.getContent());
                }
            }
        });

     // 4. TOGGLE READ/UNREAD (Híbrido POP3 + IMAP)
        btnToggleRead.addActionListener(e -> {
            int row = emailTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona un correo primero.");
                return;
            }
            
            // 1. CAMBIO VISUAL INSTANTÁNEO (Para que el usuario no espere)
            EmailModel mail = currentEmailList.get(row);
            boolean nuevoEstado = !mail.isRead(); // Invertimos el estado actual
            
            mail.setRead(nuevoEstado);
            String statusTexto = nuevoEstado ? "Read" : "UNREAD";
            tableModel.setValueAt(statusTexto, row, 0); // Actualiza la tabla visualmente

            // 2. SINCRONIZACIÓN CON GMAIL (IMAP) EN SEGUNDO PLANO
            new Thread(() -> {
                try {
                    // Llamamos al método "puente" IMAP
                    backend.updateReadStatusIMAP(mail.getSubject(), mail.getSender(), nuevoEstado);
                } catch (Exception ex) {
                    // Si falla la sincro, al menos avisamos en consola, pero el usuario ya vio el cambio visual
                    System.out.println("Error sincronizando estado IMAP: " + ex.getMessage());
                }
            }).start();
        });

     // 5. DELETE EVENT
        btnDelete.addActionListener(e -> {
            int row = emailTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Please select an email to delete.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this email from the server?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) return;

            EmailModel mail = currentEmailList.get(row);

            new Thread(() -> {
                try {
                    // CAMBIO AQUÍ: Pasamos Asunto y Remitente en lugar del ID numérico
                    backend.deleteEmail(mail.getSubject(), mail.getSender());
                    
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(this, "Email deleted from server.");
                        currentEmailList.remove(row);
                        tableModel.removeRow(row);
                        txtViewer.setText("Email deleted.");
                    });
                } catch (Exception ex) {
                    SwingUtilities.invokeLater(() -> 
                        JOptionPane.showMessageDialog(this, "Error deleting: " + ex.getMessage())
                    );
                }
            }).start();
        });
    }
}