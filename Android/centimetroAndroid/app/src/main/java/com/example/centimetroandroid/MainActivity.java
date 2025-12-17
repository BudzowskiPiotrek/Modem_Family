package com.example.centimetroandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast; // Importante para mostrar mensajes

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private EditText etUser, etPassword;
    private Button btnLogin;
    private FirebaseFirestore db; // Declaramos la variable de la base de datos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // 1. IMPORTANTE: Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        initViews();
        setupFocusListeners();
        setupButtonListener();
    }

    private void initViews() {
        etUser = findViewById(R.id.etUser);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void setupFocusListeners() {
        View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    v.setBackgroundResource(R.drawable.input_box_focused);
                } else {
                    v.setBackgroundResource(R.drawable.input_box_default);
                }
            }
        };

        etUser.setOnFocusChangeListener(focusListener);
        etPassword.setOnFocusChangeListener(focusListener);
    }

    private void setupButtonListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userIngresado = etUser.getText().toString().trim(); // .trim() quita espacios accidentales
                String passIngresado = etPassword.getText().toString().trim();

                if (!userIngresado.isEmpty() && !passIngresado.isEmpty()) {
                    loginUsuario(userIngresado, passIngresado);
                } else {
                    Toast.makeText(MainActivity.this, "Por favor completa los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método separado para mantener el código limpio
    private void loginUsuario(String usuario, String password) {
        // Buscamos en la colección "usuarios" donde el campo "usuario" sea igual al que
        // escribió la persona
        db.collection("Usuarios")
                .whereEqualTo("user", usuario)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Verificamos si la consulta trajo algún resultado
                            if (task.getResult().isEmpty()) {
                                // El usuario NO existe en la base de datos
                                Toast.makeText(MainActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                            } else {
                                // El usuario existe, ahora verificamos la contraseña.
                                // Obtenemos el primer documento (asumimos que los nombres de usuario son
                                // únicos)
                                DocumentSnapshot document = task.getResult().getDocuments().get(0);

                                // Sacamos la contraseña guardada en la BD
                                String passGuardada = document.getString("password");

                                if (passGuardada != null && passGuardada.equals(password)) {
                                    // --- LOGIN EXITOSO ---
                                    // Navegamos al MenuActivity
                                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                                    startActivity(intent);
                                    etUser.setText("");
                                    etPassword.setText("");

                                } else {
                                    // La contraseña no coincide
                                    Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT)
                                            .show();
                                }
                            }
                        } else {
                            // Error de conexión o de Firebase
                            Toast.makeText(MainActivity.this, "Error al conectar: " + task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}