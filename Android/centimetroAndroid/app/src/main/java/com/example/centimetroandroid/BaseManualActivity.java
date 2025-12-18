package com.example.centimetroandroid;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

public abstract class BaseManualActivity extends AppCompatActivity {

    protected FirebaseFirestore db;
    protected LanguageManager languageManager;
    protected TextView btnLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        languageManager = new LanguageManager(this);
    }

    protected void setupLanguageButton(TextView btnLanguage) {
        this.btnLanguage = btnLanguage;

        if (btnLanguage != null) {
            btnLanguage.setOnClickListener(v -> showLanguageDialog());
        }
    }

    private void showLanguageDialog() {
        String[] languages = { "English", "Español" };
        int currentSelection = languageManager.isEnglish() ? 0 : 1;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Language / Seleccionar Idioma")
                .setSingleChoiceItems(languages, currentSelection, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newLang = (which == 0) ? "en" : "es";
                        String currentLang = languageManager.getCurrentLanguage();

                        if (!newLang.equals(currentLang)) {
                            languageManager.setLanguage(newLang);
                            dialog.dismiss();
                            onLanguageChanged();
                        } else {
                            dialog.dismiss();
                        }
                    }
                });

        builder.create().show();
    }

    /**
     * Override this method in child activities to reload content when language
     * changes
     */
    protected abstract void onLanguageChanged();
}
