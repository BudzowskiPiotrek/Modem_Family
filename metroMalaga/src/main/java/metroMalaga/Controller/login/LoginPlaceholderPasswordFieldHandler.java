package metroMalaga.Controller.login;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

/**
 * Handles focus events for JPasswordField to implement placeholder behavior and
 * styling.
 */
public class LoginPlaceholderPasswordFieldHandler implements FocusListener {

    private final JPasswordField field;
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
     * @param field            The JPasswordField to handle.
     * @param placeholder      The placeholder text.
     * @param focusedColor     Color when focused.
     * @param unfocusedColor   Color when unfocused.
     * @param placeholderColor Color of the placeholder text.
     * @param focusedBorder    Border when focused.
     * @param unfocusedBorder  Border when unfocused.
     * @param padding          Padding inside the field.
     */
    public LoginPlaceholderPasswordFieldHandler(JPasswordField field, String placeholder,
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
     * @param field            The JPasswordField to handle.
     * @param placeholder      The placeholder text.
     * @param focusedColor     Color when focused.
     * @param unfocusedColor   Color when unfocused.
     * @param placeholderColor Color of the placeholder text.
     * @param focusedBorder    Border when focused.
     * @param unfocusedBorder  Border when unfocused.
     */
    public LoginPlaceholderPasswordFieldHandler(JPasswordField field, String placeholder,
            Color focusedColor, Color unfocusedColor, Color placeholderColor,
            MatteBorder focusedBorder, MatteBorder unfocusedBorder) {
        this(field, placeholder, focusedColor, unfocusedColor, placeholderColor,
                focusedBorder, unfocusedBorder, null);
    }

    /**
     * Constructor with fewer styling options.
     * 
     * @param field            The JPasswordField to handle.
     * @param placeholder      The placeholder text.
     * @param focusedColor     Color when focused.
     * @param placeholderColor Color of the placeholder text.
     */
    public LoginPlaceholderPasswordFieldHandler(JPasswordField field, String placeholder,
            Color focusedColor, Color placeholderColor) {
        this(field, placeholder, focusedColor, Color.BLACK, placeholderColor, null, null, null);
    }

    /**
     * Minimal constructor.
     * 
     * @param field       The JPasswordField to handle.
     * @param placeholder The placeholder text.
     */
    public LoginPlaceholderPasswordFieldHandler(JPasswordField field, String placeholder) {
        this(field, placeholder, Color.BLACK, Color.GRAY);
    }

    @Override
    public void focusGained(FocusEvent e) {
        String currentText = String.valueOf(field.getPassword());

        if (currentText.equals(placeholder) || currentText.isEmpty()) {
            field.setText("");
            field.setEchoChar('.');
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
        if (field.getPassword().length == 0) {
            field.setEchoChar((char) 0);
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
