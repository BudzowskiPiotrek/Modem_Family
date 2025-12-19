package metroMalaga.Controller.menu;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.SwingUtilities;
import metroMalaga.Controller.Common;
import metroMalaga.View.PanelMenu;

/**
 * Listener for toggling the application's theme (light/dark mode).
 */
public class ThemeToggleListener implements ItemListener {
    private PanelMenu view;

    /**
     * Constructor for ThemeToggleListener.
     * 
     * @param view The main menu view to update when theme changes.
     */
    public ThemeToggleListener(PanelMenu view) {
        this.view = view;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        Common.isDarkMode = (e.getStateChange() == ItemEvent.SELECTED);

        SwingUtilities.updateComponentTreeUI(view);
    }
}