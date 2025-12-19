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

public class FtpActivity extends BaseManualActivity {

    private static final String TAG = "FtpActivity";

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

        db.collection(languageManager.getManualCollection()).document("ftp")
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

                                String interfaceTitle = document.getString("interface_title");
                                String interfaceDescription = document.getString("interface_description");
                                List<String> interfaceColumns = (List<String>) document.get("interface_columns");

                                String actionsTitle = document.getString("actions_title");
                                String actionsIntro = document.getString("actions_intro");
                                List<String> actionsList = (List<String>) document.get("actions_list");

                                String navigationTitle = document.getString("navigation_title");
                                String navigationIntro = document.getString("navigation_intro");
                                List<String> navigationButtons = (List<String>) document.get("navigation_buttons");

                                String searchTitle = document.getString("search_title");
                                String searchDescription = document.getString("search_description");
                                List<String> searchSteps = (List<String>) document.get("search_steps");

                                String doubleclickTitle = document.getString("doubleclick_title");
                                String doubleclickDescription = document.getString("doubleclick_description");
                                String doubleclickAction = document.getString("doubleclick_action");

                                String notesTitle = document.getString("notes_title");
                                List<String> notes = (List<String>) document.get("notes");

                                tvTitle.setText(title != null ? title : "FTP");

                                // Limpiar contenido previo
                                contentLayout.removeAllViews();

                                StringBuilder content = new StringBuilder();

                                if (intro != null)
                                    content.append(intro).append("\n\n");

                                if (interfaceTitle != null)
                                    content.append("═══ ").append(interfaceTitle).append(" ═══\n");

                                addTextToLayout(content.toString());
                                content.setLength(0);

                                // Imagen 1: Interfaz Principal
                                addImageToLayout(R.drawable.image3); // FTP interface

                                if (interfaceDescription != null)
                                    content.append(interfaceDescription).append("\n");
                                if (interfaceColumns != null) {
                                    for (String column : interfaceColumns) {
                                        content.append("• ").append(column).append("\n");
                                    }
                                    content.append("\n");
                                }

                                addTextToLayout(content.toString());
                                content.setLength(0);

                                // Imagen 2: Tabla de archivos
                                addImageToLayout(R.drawable.image13); // FTP table columns

                                if (actionsTitle != null)
                                    content.append("═══ ").append(actionsTitle).append(" ═══\n");
                                if (actionsIntro != null)
                                    content.append(actionsIntro).append("\n");
                                if (actionsList != null) {
                                    for (String action : actionsList) {
                                        content.append("▸ ").append(action).append("\n");
                                    }
                                    content.append("\n");
                                }

                                addTextToLayout(content.toString());
                                content.setLength(0);

                                // Imagen 3: Botones de acción
                                addImageToLayout(R.drawable.image9); // FTP action buttons

                                if (navigationTitle != null)
                                    content.append("═══ ").append(navigationTitle).append(" ═══\n");
                                if (navigationIntro != null)
                                    content.append(navigationIntro).append("\n");
                                if (navigationButtons != null) {
                                    for (String button : navigationButtons) {
                                        content.append("▸ ").append(button).append("\n");
                                    }
                                    content.append("\n");
                                }

                                addTextToLayout(content.toString());
                                content.setLength(0);

                                // Imagen 4: Barra de herramientas
                                addImageToLayout(R.drawable.image5); // FTP toolbar

                                if (searchTitle != null)
                                    content.append("═══ ").append(searchTitle).append(" ═══\n");
                                if (searchDescription != null)
                                    content.append(searchDescription).append("\n");
                                if (searchSteps != null) {
                                    for (int i = 0; i < searchSteps.size(); i++) {
                                        content.append((i + 1)).append(". ").append(searchSteps.get(i)).append("\n");
                                    }
                                    content.append("\n");
                                }

                                addTextToLayout(content.toString());
                                content.setLength(0);

                                // Imagen 5: Filtrado
                                addImageToLayout(R.drawable.image8); // FTP filter

                                if (doubleclickTitle != null)
                                    content.append("═══ ").append(doubleclickTitle).append(" ═══\n");
                                if (doubleclickDescription != null)
                                    content.append(doubleclickDescription).append("\n");
                                if (doubleclickAction != null)
                                    content.append("• ").append(doubleclickAction).append("\n\n");

                                if (notesTitle != null)
                                    content.append("═══ ").append(notesTitle).append(" ═══\n");
                                if (notes != null) {
                                    for (String note : notes) {
                                        content.append("▸ ").append(note).append("\n");
                                    }
                                }

                                addTextToLayout(content.toString());

                            } else {
                                Toast.makeText(FtpActivity.this,
                                        "No se encontraron datos", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e(TAG, "Error al cargar datos", task.getException());
                            Toast.makeText(FtpActivity.this,
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
