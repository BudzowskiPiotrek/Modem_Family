package metroMalaga.Controller.smtp;

import java.awt.Color;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import javax.swing.JComponent;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

/**
 * Focus adapter to change the border color of form fields when they gain or
 * lose focus.
 */
public class FieldFocusHandle extends FocusAdapter {

    private final JComponent component;
    private final Color normalColor;
    private final Color focusColor;

    /**
     * Constructor for FieldFocusHandle.
     * 
     * @param component   The component to handle focus for.
     * @param normalColor The border color when not focused.
     * @param focusColor  The border color when focused.
     */
    public FieldFocusHandle(JComponent component, Color normalColor, Color focusColor) {
        this.component = component;
        this.normalColor = normalColor;
        this.focusColor = focusColor;
    }

    @Override
    public void focusGained(FocusEvent e) {
        component.setBorder(new CompoundBorder(
                new LineBorder(focusColor, 1),
                new EmptyBorder(2, 5, 2, 5)));
    }

    @Override
    public void focusLost(FocusEvent e) {
        component.setBorder(new CompoundBorder(
                new LineBorder(normalColor, 1),
                new EmptyBorder(2, 5, 2, 5)));
    }
}