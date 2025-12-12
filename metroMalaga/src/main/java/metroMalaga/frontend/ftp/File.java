package metroMalaga.frontend.ftp;

import java.util.ArrayList;

import javax.swing.JButton;

public class File {
	private String date;
	private String name;
	private ArrayList<JButton> buttons;
	
	public File(String date, String name, ArrayList<JButton> buttons) {
		super();
		this.date = date;
		this.name = name;
		this.buttons = buttons;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<JButton> getButtons() {
		return buttons;
	}

	public void setButtons(ArrayList<JButton> buttons) {
		this.buttons = buttons;
	}
	
}
