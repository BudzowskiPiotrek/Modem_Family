package metroMalaga.backend;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;

public class PlaceholderPasswordFieldHandler implements FocusListener {

	private final JPasswordField field;
	private final String placeholder;

	public PlaceholderPasswordFieldHandler(JPasswordField field, String placeholder) {
		this.field = field;
		this.placeholder = placeholder;
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (String.valueOf(field.getPassword()).equals(placeholder)) {
			field.setText("");
			field.setEchoChar('•');
			field.setForeground(Color.BLACK);
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (field.getPassword().length == 0) {
			field.setEchoChar((char) 0);
			field.setText(placeholder);
			field.setForeground(Color.GRAY);
		}
	}

}
