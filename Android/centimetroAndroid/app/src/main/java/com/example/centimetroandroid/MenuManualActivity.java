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

public class MenuManualActivity extends BaseManualActivity {

    private static final String TAG = "MenuManualActivity";

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

        db.collection(languageManager.getManualCollection()).document("menu")
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
                                String description = document.getString("description");
                                String features = document.getString("features");

                                tvTitle.setText(title != null ? title : "Menu");

                                // Limpiar contenido previo
                                contentLayout.removeAllViews();

                                if (description != null) {
                                    addTextToLayout(description);
                                }

                                if (features != null) {
                                    addTextToLayout(""); // Add some spacing
                                    addTextToLayout(features);
                                }

                            } else {
                                Toast.makeText(MenuManualActivity.this,
                                        "No se encontraron datos", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e(TAG, "Error al cargar datos", task.getException());
                            Toast.makeText(MenuManualActivity.this,
                                    "Error al cargar datos: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void addTextToLayout(String text) {
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

    @Override
    protected void onLanguageChanged() {
        loadData();
    }
}
