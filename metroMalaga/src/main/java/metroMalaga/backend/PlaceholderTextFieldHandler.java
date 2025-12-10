package metroMalaga.backend;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class PlaceholderTextFieldHandler implements FocusListener{

	private final JTextField field;
	private final String placeholder;
	
	
	
	public PlaceholderTextFieldHandler(JTextField field, String placeholder) {
		super();
		this.field = field;
		this.placeholder = placeholder;
	}

	@Override
	public void focusGained(FocusEvent e) {
		if(field.getText().equals(placeholder)) {
			field.setText("");
			field.setForeground(Color.BLACK);
		}
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (field.getText().isEmpty()) {
            field.setText(placeholder);
            field.setForeground(Color.GRAY);
        }
		
	}
	
}
