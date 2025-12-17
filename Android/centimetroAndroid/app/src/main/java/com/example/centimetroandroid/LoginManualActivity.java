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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginManualActivity extends AppCompatActivity {

    private static final String TAG = "LoginManualActivity";
    private FirebaseFirestore db;

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

        db = FirebaseFirestore.getInstance();

        tvTitle = findViewById(R.id.tvSectionTitle);
        progressBar = findViewById(R.id.progressBar);
        scrollView = findViewById(R.id.scrollView);
        btnBack = findViewById(R.id.btnBack);
        contentLayout = findViewById(R.id.contentLayout);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Load UI strings
        UIStringsHelper.loadCommonStrings(db, tvTitle, btnBack, null);

        loadData();
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);

        db.collection("manual").document("login")
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
                                String fieldDescription = document.getString("field_description");
                                String buttonAction = document.getString("button_action");
                                String successMessage = document.getString("success_message");
                                String sessionInfo = document.getString("session_info");

                                tvTitle.setText(title != null ? title : "Login");

                                // Limpiar contenido previo
                                contentLayout.removeAllViews();

                                // Imagen de login AL INICIO (como en el HTML)
                                addImageToLayout(R.drawable.image3);

                                // Texto después de la imagen
                                StringBuilder content = new StringBuilder();
                                if (description != null) {
                                    content.append(description).append("\n\n");
                                }
                                if (fieldDescription != null) {
                                    content.append(fieldDescription).append("\n\n");
                                }
                                if (buttonAction != null) {
                                    content.append(buttonAction).append("\n\n");
                                }
                                if (successMessage != null) {
                                    content.append(successMessage).append("\n\n");
                                }
                                if (sessionInfo != null) {
                                    content.append(sessionInfo);
                                }

                                addTextToLayout(content.toString());

                            } else {
                                Toast.makeText(LoginManualActivity.this,
                                        "No se encontraron datos", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.e(TAG, "Error al cargar datos", task.getException());
                            Toast.makeText(LoginManualActivity.this,
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
