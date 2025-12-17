package com.example.centimetroandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "MenuActivity";
    private FirebaseFirestore db;

    private TextView tvTitle;
    private TextView tvSubtitle;
    private Button btnIntroduccion;
    private Button btnLogin;
    private Button btnMenu;
    private Button btnCorreos;
    private Button btnCrud;
    private Button btnFtp;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        db = FirebaseFirestore.getInstance();

        // Referencias a los elementos UI
        tvTitle = findViewById(R.id.tvTitle);
        tvSubtitle = findViewById(R.id.tvSubtitle);
        btnIntroduccion = findViewById(R.id.btnIntroduccion);
        btnLogin = findViewById(R.id.btnLogin);
        btnMenu = findViewById(R.id.btnMenu);
        btnCorreos = findViewById(R.id.btnCorreos);
        btnCrud = findViewById(R.id.btnCrud);
        btnFtp = findViewById(R.id.btnFtp);

        // Cargar strings desde Firebase
        loadUIStrings();

        // Configurar listeners
        btnIntroduccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, IntroduccionActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, LoginManualActivity.class);
                startActivity(intent);
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MenuManualActivity.class);
                startActivity(intent);
            }
        });

        btnCorreos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CorreosActivity.class);
                startActivity(intent);
            }
        });

        btnCrud.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, CrudActivity.class);
                startActivity(intent);
            }
        });

        btnFtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, FtpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loadUIStrings() {
        db.collection("ui_strings").document("menu")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                String title = document.getString("title");
                                String subtitle = document.getString("subtitle");
                                String btnIntroText = document.getString("btn_introduction");
                                String btnLoginText = document.getString("btn_login");
                                String btnMenuText = document.getString("btn_menu");
                                String btnEmailsText = document.getString("btn_emails");
                                String btnCrudText = document.getString("btn_crud");
                                String btnFtpText = document.getString("btn_ftp");

                                if (title != null)
                                    tvTitle.setText(title);
                                if (subtitle != null)
                                    tvSubtitle.setText(subtitle);
                                if (btnIntroText != null)
                                    btnIntroduccion.setText(btnIntroText);
                                if (btnLoginText != null)
                                    btnLogin.setText(btnLoginText);
                                if (btnMenuText != null)
                                    btnMenu.setText(btnMenuText);
                                if (btnEmailsText != null)
                                    btnCorreos.setText(btnEmailsText);
                                if (btnCrudText != null)
                                    btnCrud.setText(btnCrudText);
                                if (btnFtpText != null)
                                    btnFtp.setText(btnFtpText);
                            } else {
                                Log.w(TAG, "UI strings not found, using defaults");
                            }
                        } else {
                            Log.e(TAG, "Error loading UI strings", task.getException());
                        }
                    }
                });
    }
}
