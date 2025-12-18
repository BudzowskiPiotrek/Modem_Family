package metroMalaga.Model;

import metroMalaga.Controller.TranslateService;

public class Language {

    private static TranslateService translate;

    public static TranslateService getTranslate() {
        if (translate == null) {
            translate = new TranslateService();
        }
        return translate;
    }

    public static String get(int id) {
        return getTranslate().get(id);
    }

    public static void setSpanish() {
        getTranslate().setSpanish();
    }

    public static void setEnglish() {
        getTranslate().setEnglish();
    }

    public static String getCurrentLanguage() {
        return getTranslate().getCurrentLanguage();
    }
}