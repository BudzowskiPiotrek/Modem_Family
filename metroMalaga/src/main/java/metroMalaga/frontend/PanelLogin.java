package metroMalaga.frontend;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

// Asegúrate de que estas clases existan en tu paquete backend
import metroMalaga.backend.HandleLoginAttempt;
import metroMalaga.backend.PlaceholderPasswordFieldHandler;
import metroMalaga.backend.PlaceholderTextFieldHandler;

public class PanelLogin extends JFrame {

    private JTextField userField;
    private JPasswordField passwordField;
    private JButton buttonLogin;
    private JLabel labelLogin;

    // --- Constantes de Color Estilo Persona 5 ---
    private final Color P5_RED = new Color(220, 20, 60);
    private final Color P5_BRIGHT_RED = new Color(255, 0, 0);
    private final Color P5_BLACK = new Color(20, 20, 20);
    private final Color P5_WHITE = new Color(240, 240, 240);
    private final Color P5_GRAY_PLACEHOLDER = new Color(150, 150, 150);

    // --- NUEVA FUENTE ---
    // Usamos una fuente SansSerif estándar pero muy gruesa, inclinada y grande
    // para imitar el estilo visual de la imagen proporcionada lo mejor posible sin importar fuentes externas.
    private final Font P5_INPUT_FONT = new Font("SansSerif", Font.BOLD | Font.ITALIC, 24);
    private final Font P5_BUTTON_FONT = new Font("Dialog", Font.BOLD | Font.ITALIC, 24);

    public PanelLogin() {
        settingsFrame();
        JPanel backgroundPanel = new JPanel(new GridBagLayout());
        backgroundPanel.setBackground(P5_RED);
        setContentPane(backgroundPanel);

        settingsComponents();
        addComponentsFrame(backgroundPanel);

        // HandleLoginAttempt handler = new HandleLoginAttempt(userField,passwordField ,buttonLogin);
        
        this.getContentPane().setFocusable(true);
        this.getContentPane().requestFocusInWindow();
    }

    private void settingsComponents() {
        settingsLabel();
        settingsUser();
        settingsPass();
        settingsButton();
    }

    private void settingsLabel() {
        // Mantenemos el título estilo "nota de rescate"
        String p5StyledText = "<html>"
                + "<span style='background-color:black; color:white; font-size:32px; font-family:Sans-serif; font-style:italic;'>L</span>"
                + "<span style='background-color:red; color:black; font-size:40px; font-weight:bold; font-family:Serif;'>O</span>"
                + "<span style='color:black; font-size:35px; font-family:Sans-serif; font-weight:bold;'>G</span>"
                + "<span style='background-color:black; color:red; font-size:38px; font-style:italic;'>I</span>"
                + "<span style='background-color:white; color:black; font-size:30px; font-weight:bold;'>N</span>"
                + "</html>";

        labelLogin = new JLabel(p5StyledText, SwingConstants.CENTER);
        labelLogin.setBorder(new MatteBorder(0, 0, 5, 0, P5_BLACK));
    }

    private void settingsUser() {
        userField = new JTextField(15); // Reducido el número de columnas ligeramente por el tamaño de fuente
        styleP5Field(userField, "Username");
        userField.addFocusListener(new PlaceholderTextFieldHandler(userField, "Username") {
            @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                super.focusGained(e);
                if (userField.getText().equals("Username") || userField.getText().isEmpty()) {
                     userField.setForeground(P5_WHITE);
                }
                userField.setBorder(new MatteBorder(3, 3, 5, 5, P5_BRIGHT_RED));
            }
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                super.focusLost(e);
                if (userField.getText().equals("Username")) {
                     userField.setForeground(P5_GRAY_PLACEHOLDER);
                }
                 userField.setBorder(new MatteBorder(3, 3, 5, 3, P5_BLACK));
            }
        });
    }

    private void settingsPass() {
        passwordField = new JPasswordField(15);
        styleP5Field(passwordField, "Password");
        passwordField.setEchoChar((char) 0);
        
        passwordField.addFocusListener(new PlaceholderPasswordFieldHandler(passwordField, "Password") {
             @Override
            public void focusGained(java.awt.event.FocusEvent e) {
                super.focusGained(e);
                String pass = new String(passwordField.getPassword());
                if (pass.isEmpty() || pass.equals("Password")) {
                     passwordField.setForeground(P5_WHITE);
                }
                passwordField.setBorder(new MatteBorder(3, 3, 5, 5, P5_BRIGHT_RED));
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                super.focusLost(e);
                 String pass = new String(passwordField.getPassword());
                 if (pass.equals("Password")) {
                      passwordField.setForeground(P5_GRAY_PLACEHOLDER);
                      passwordField.setEchoChar((char) 0);
                 }
                passwordField.setBorder(new MatteBorder(3, 3, 5, 3, P5_BLACK));
            }
        });
    }

    // Método auxiliar actualizado con la nueva fuente y tamaño
    private void styleP5Field(JTextField field, String placeholder) {
        // Aumentamos la altura preferida para acomodar la fuente grande
        field.setPreferredSize(new Dimension(250, 50)); 
        field.setBackground(P5_BLACK);
        field.setForeground(P5_GRAY_PLACEHOLDER);
        field.setCaretColor(P5_BRIGHT_RED);
        field.setText(placeholder);
        
        // APLICAMOS LA NUEVA FUENTE GRANDE Y GRUESA
        field.setFont(P5_INPUT_FONT);
        
        field.setBorder(new MatteBorder(3, 3, 5, 3, P5_BLACK));
        // Padding interno ajustado para la fuente grande
        field.setBorder(new CompoundBorder(field.getBorder(), new EmptyBorder(5, 15, 5, 15)));
    }

    private void settingsButton() {
        buttonLogin = new JButton("LOGIN");
        buttonLogin.setPreferredSize(new Dimension(180, 60)); // Botón más grande
        buttonLogin.setBackground(P5_BRIGHT_RED);
        buttonLogin.setForeground(P5_WHITE);
        buttonLogin.setFont(P5_BUTTON_FONT);
        buttonLogin.setFocusPainted(false);
        buttonLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));

        MatteBorder defaultBorder = new MatteBorder(4, 4, 8, 6, P5_BLACK);
        buttonLogin.setBorder(defaultBorder);

        buttonLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                buttonLogin.setBackground(P5_BLACK);
                buttonLogin.setForeground(P5_BRIGHT_RED);
                buttonLogin.setBorder(new MatteBorder(4, 4, 8, 6, P5_BRIGHT_RED));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                buttonLogin.setBackground(P5_BRIGHT_RED);
                buttonLogin.setForeground(P5_WHITE);
                buttonLogin.setBorder(defaultBorder);
            }
        });
    }

    private void addComponentsFrame(Container container) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Título
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 20, 40, 20);
        container.add(labelLogin, gbc);

        // --- CAMBIOS DE ALINEACIÓN AQUÍ ---
        // Usamos los mismos márgenes laterales (ej. 40 a cada lado) para ambos campos
        
        // Usuario
        gbc.gridy = 1;
        // Margen superior, izquierdo, inferior, derecho
        gbc.insets = new Insets(10, 40, 15, 40); 
        container.add(userField, gbc);

        // Contraseña
        gbc.gridy = 2;
        // Exactamente los mismos márgenes que el usuario para alineación recta
        gbc.insets = new Insets(10, 40, 15, 40); 
        container.add(passwordField, gbc);

        // Botón
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(30, 20, 30, 20);
        container.add(buttonLogin, gbc);
    }

    private void settingsFrame() {
        setTitle("Phantom Thieves Login");
        // Aumentado el tamaño de la ventana para que los campos grandes respiren
        setSize(500, 450); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        // Activar anti-aliasing para que las fuentes grandes y en negrita se vean suaves
        System.setProperty("awt.useSystemAAFontSettings","on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            PanelLogin panelLogin = new PanelLogin();
            panelLogin.setVisible(true);
        });
    }
}