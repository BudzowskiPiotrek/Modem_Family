package metroMalaga.backend.smtp;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JComponent;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class FieldFocusHandle extends FocusAdapter {

    private final JComponent component; // Puede ser JTextField o JTextArea
    private final Color normalColor;
    private final Color focusColor;

    public FieldFocusHandle(JComponent component, Color normalColor, Color focusColor) {
        this.component = component;
        this.normalColor = normalColor;
        this.focusColor = focusColor;
    }

    @Override
    public void focusGained(FocusEvent e) {
        // Al hacer clic dentro: Borde Color Acento
        component.setBorder(new CompoundBorder(
            new LineBorder(focusColor, 1), 
            new EmptyBorder(2, 5, 2, 5)
        ));
    }

    @Override
    public void focusLost(FocusEvent e) {
        // Al salir fuera: Borde Color Normal
        component.setBorder(new CompoundBorder(
            new LineBorder(normalColor, 1), 
            new EmptyBorder(2, 5, 2, 5)
        ));
    }
}