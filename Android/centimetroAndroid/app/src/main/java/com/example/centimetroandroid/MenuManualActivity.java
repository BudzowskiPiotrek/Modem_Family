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

public class MenuManualActivity extends AppCompatActivity {

    private static final String TAG = "MenuManualActivity";
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

        db = FirebaseFirestore.getInstance();

        tvTitle = findViewById(R.id.tvSectionTitle);
        tvContent = findViewById(R.id.tvSectionContent);
        progressBar = findViewById(R.id.progressBar);
        scrollView = findViewById(R.id.scrollView);
        btnBack = findViewById(R.id.btnBack);

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

        db.collection("manual").document("menu")
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

                                tvTitle.setText(title != null ? title : "Menú");
                                tvContent.setText(description != null ? description : "");

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
}
