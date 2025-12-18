package com.example.centimetroandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class CorreosActivity extends BaseManualActivity {

    private static final String TAG = "CorreosActivity";

    private TextView tvTitle;
    private ProgressBar progressBar;
    private ScrollView scrollView;
    private Button btnBack;
    private LinearLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_section);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        tvTitle = findViewById(R.id.tvSectionTitle);
        progressBar = findViewById(R.id.progressBar);
        scrollView = findViewById(R.id.scrollView);
        btnBack = findViewById(R.id.btnBack);
        btnLanguage = findViewById(R.id.btnLanguage);
        contentLayout = findViewById(R.id.contentLayout);

        setupLanguageButton(btnLanguage);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadData();
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        // Load UI strings
        UIStringsHelper.loadCommonStrings(db, tvTitle, btnBack, null, languageManager);

        db.collection(languageManager.getManualCollection()).document("gestion_correos")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                scrollView.setVisibility(View.VISIBLE);

                                String title = document.getString("title");
                                String intro = document.getString("intro");
                                String introListTitle = document.getString("intro_list_title");
                                List<String> sections = (List<String>) document.get("sections");

                                String envioTitle = document.getString("envio_title");
                                String envioIntro = document.getString("envio_intro");
                                List<String> envioFields = (List<String>) document.get("envio_fields");
                                String envioButtonAttach = document.getString("envio_button_attach");
                                String envioButtonClear = document.getString("envio_button_clear");
                                String envioButtonSend = document.getString("envio_button_send");

                                String recepcionTitle = document.getString("recepcion_title");
                                String recepcionIntro = document.getString("recepcion_intro");
                                String recepcionTable = document.getString("recepcion_table");
                                String recepcionDetail = document.getString("recepcion_detail");
                                String recepcionButtonsIntro = document.getString("recepcion_buttons_intro");
                                List<String> recepcionButtons = (List<String>) document.get("recepcion_buttons");

                                tvTitle.setText(title != null ? title : "Email Management");

                                // Limpiar contenido previo
                                contentLayout.removeAllViews();

                                StringBuilder content = new StringBuilder();

                                if (intro != null)
                                    content.append(intro).append("\n\n");
                                if (introListTitle != null)
                                    content.append(introListTitle).append("\n");
                                if (sections != null) {
                                    for (String section : sections) {
                                        content.append("• ").append(section).append("\n");
                                    }
                                    content.append("\n");
                                }

                                addTextToLayout(content.toString());
                                content.setLength(0);

                                // Imagen principal de gestión de correos
                                addImageToLayout(R.drawable.image6);

                                if (envioTitle != null)
                                    content.append("═══ ").append(envioTitle).append(" ═══\n\n");
                                if (envioIntro != null)
                                    content.append(envioIntro).append("\n");
                                if (envioFields != null) {
                                    for (String field : envioFields) {
                                        content.append("• ").append(field).append("\n");
                                    }
                                    content.append("\n");
                                }
                                if (envioButtonAttach != null)
                                    content.append("▸ ").append(envioButtonAttach).append("\n\n");
                                if (envioButtonClear != null)
                                    content.append("▸ ").append(envioButtonClear).append("\n\n");
                                if (envioButtonSend != null)
                                    content.append("▸ ").append(envioButtonSend).append("\n\n");

                                if (recepcionTitle != null)
                                    content.append("═══ ").append(recepcionTitle).append(" ═══\n\n");
                                if (recepcionIntro != null)
                                    content.append(recepcionIntro).append("\n");
                                if (recepcionTable != null)
                                    content.append("• ").append(recepcionTable).append("\n\n");
                                if (recepcionDetail != null)
                                    content.append("• ").append(recepcionDetail).append("\n\n");

                                addTextToLayout(content.toString());
                                content.setLength(0);

                                // Imagen de bandeja de entrada
                                addImageToLayout(R.drawable.image1);

                                if (recepcionButtonsIntro != null)
                                    content.append(recepcionButtonsIntro).append("\n");
                                if (recepcionButtons != null) {
                                    for (String button : recepcionButtons) {
                                        content.append("▸ ").append(button).append("\n");
                                    }
                                }

                                addTextToLayout(content.toString());

                            } else {
                                Toast.makeText(CorreosActivity.this,
                                        "No se encontraron datos", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e(TAG, "Error al cargar datos", task.getException());
                            Toast.makeText(CorreosActivity.this,
                                    "Error al cargar datos: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void addImageToLayout(int imageResId) {
        ImageView imageView = new ImageView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 16, 0, 16);
        imageView.setLayoutParams(params);
        imageView.setImageResource(imageResId);
        imageView.setAdjustViewBounds(true);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        contentLayout.addView(imageView);
    }

    private void addTextToLayout(String text) {
        if (text != null && !text.trim().isEmpty()) {
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 8, 0, 8);
            textView.setLayoutParams(params);
            textView.setText(text);
            textView.setTextSize(14);
            textView.setTextColor(getResources().getColor(android.R.color.white));
            contentLayout.addView(textView);
        }
    }

    @Override
    protected void onLanguageChanged() {
        loadData();
    }
}
