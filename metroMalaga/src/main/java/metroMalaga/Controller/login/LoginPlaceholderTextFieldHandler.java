package metroMalaga.Controller.login;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * Handles focus events for JTextField to implement placeholder behavior and
 * styling.
 */
public class LoginPlaceholderTextFieldHandler implements FocusListener {

    private final JTextField field;
    private final String placeholder;
    private final Color focusedColor;
    private final Color unfocusedColor;
    private final Color placeholderColor;
    private final MatteBorder focusedBorder;
    private final MatteBorder unfocusedBorder;
    private final EmptyBorder padding;

    /**
     * Full constructor with all styling options.
     * 
     * @param field            The JTextField to handle.
     * @param placeholder      The placeholder text.
     * @param focusedColor     Color when focused.
     * @param unfocusedColor   Color when unfocused.
     * @param placeholderColor Color of the placeholder text.
     * @param focusedBorder    Border when focused.
     * @param unfocusedBorder  Border when unfocused.
     * @param padding          Padding inside the field.
     */
    public LoginPlaceholderTextFieldHandler(JTextField field, String placeholder,
            Color focusedColor, Color unfocusedColor, Color placeholderColor,
            MatteBorder focusedBorder, MatteBorder unfocusedBorder,
            EmptyBorder padding) {
        this.field = field;
        this.placeholder = placeholder;
        this.focusedColor = focusedColor;
        this.unfocusedColor = unfocusedColor;
        this.placeholderColor = placeholderColor;
        this.focusedBorder = focusedBorder;
        this.unfocusedBorder = unfocusedBorder;
        this.padding = padding;
    }

    /**
     * Constructor without padding.
     * 
     * @param field            The JTextField to handle.
     * @param placeholder      The placeholder text.
     * @param focusedColor     Color when focused.
     * @param unfocusedColor   Color when unfocused.
     * @param placeholderColor Color of the placeholder text.
     * @param focusedBorder    Border when focused.
     * @param unfocusedBorder  Border when unfocused.
     */
    public LoginPlaceholderTextFieldHandler(JTextField field, String placeholder,
            Color focusedColor, Color unfocusedColor, Color placeholderColor,
            MatteBorder focusedBorder, MatteBorder unfocusedBorder) {
        this(field, placeholder, focusedColor, unfocusedColor, placeholderColor,
                focusedBorder, unfocusedBorder, null);
    }

    /**
     * Constructor with fewer styling options.
     * 
     * @param field            The JTextField to handle.
     * @param placeholder      The placeholder text.
     * @param focusedColor     Color when focused.
     * @param placeholderColor Color of the placeholder text.
     */
    public LoginPlaceholderTextFieldHandler(JTextField field, String placeholder,
            Color focusedColor, Color placeholderColor) {
        this(field, placeholder, focusedColor, Color.BLACK, placeholderColor, null, null, null);
    }

    /**
     * Minimal constructor.
     * 
     * @param field       The JTextField to handle.
     * @param placeholder The placeholder text.
     */
    public LoginPlaceholderTextFieldHandler(JTextField field, String placeholder) {
        this(field, placeholder, Color.BLACK, Color.GRAY);
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (field.getText().equals(placeholder) || field.getText().isEmpty()) {
            field.setText("");
            field.setForeground(focusedColor);
        }

        if (focusedBorder != null) {
            if (padding != null) {
                field.setBorder(new CompoundBorder(focusedBorder, padding));
            } else {
                field.setBorder(focusedBorder);
            }
        }
    }

    @Override
    public void focusLost(FocusEvent e) {
        if (field.getText().isEmpty()) {
            field.setText(placeholder);
            field.setForeground(placeholderColor);
        } else {
            field.setForeground(unfocusedColor);
        }

        if (unfocusedBorder != null) {
            if (padding != null) {
                field.setBorder(new CompoundBorder(unfocusedBorder, padding));
            } else {
                field.setBorder(unfocusedBorder);
            }
        }
    }
}
