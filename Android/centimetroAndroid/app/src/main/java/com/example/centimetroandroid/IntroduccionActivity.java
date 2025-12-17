package com.example.centimetroandroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class IntroduccionActivity extends AppCompatActivity {

    private static final String TAG = "IntroduccionActivity";
    private FirebaseFirestore db;

    private TextView tvTitle;
    private TextView tvContent;
    private ProgressBar progressBar;
    private ScrollView scrollView;
    private Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_section);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();

        // Inicializar vistas
        tvTitle = findViewById(R.id.tvSectionTitle);
        tvContent = findViewById(R.id.tvSectionContent);
        progressBar = findViewById(R.id.progressBar);
        scrollView = findViewById(R.id.scrollView);
        btnBack = findViewById(R.id.btnBack);

        // Configurar botón volver
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Cargar strings de UI
        UIStringsHelper.loadCommonStrings(db, tvTitle, btnBack, null);

        // Cargar datos de Firebase
        loadData();
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        db.collection("manual").document("introduccion")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                scrollView.setVisibility(View.VISIBLE);

                                // Obtener datos
                                String title = document.getString("title");
                                String paragraph1 = document.getString("paragraph1");
                                String paragraph2 = document.getString("paragraph2");
                                List<String> features = (List<String>) document.get("features");
                                String paragraph3 = document.getString("paragraph3");

                                // Mostrar título
                                tvTitle.setText(title != null ? title : "Introduction");

                                // Construir contenido
                                StringBuilder content = new StringBuilder();

                                if (paragraph1 != null) {
                                    content.append(paragraph1).append("\n\n");
                                }

                                if (paragraph2 != null) {
                                    content.append(paragraph2).append("\n\n");
                                }

                                if (features != null && !features.isEmpty()) {
                                    for (int i = 0; i < features.size(); i++) {
                                        content.append((i + 1)).append(". ").append(features.get(i)).append("\n\n");
                                    }
                                }

                                if (paragraph3 != null) {
                                    content.append(paragraph3);
                                }

                                tvContent.setText(content.toString());

                            } else {
                                Toast.makeText(IntroduccionActivity.this,
                                        "No se encontraron datos", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e(TAG, "Error al cargar datos", task.getException());
                            Toast.makeText(IntroduccionActivity.this,
                                    "Error al cargar datos: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
