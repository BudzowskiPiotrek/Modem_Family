package metroMalaga.Controller.ftp;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import metroMalaga.Model.Language;
import metroMalaga.View.PanelFTP;

public class FTPLanguageController implements ActionListener {

    private PanelFTP view;

    public FTPLanguageController(PanelFTP view) {
        this.view = view;
        initListener();
    }

    private void initListener() {
        view.getBtnLanguage().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        toggleLanguage();
    }

    private void toggleLanguage() {
        if (Language.getCurrentLanguage().equals("espanol")) {
            Language.setEnglish();
            view.getBtnLanguage().setText("🇬🇧 EN");
        } else {
            Language.setSpanish();
            view.getBtnLanguage().setText("🇪🇸 ES");
        }
        view.updateAllTexts();
    }
}
