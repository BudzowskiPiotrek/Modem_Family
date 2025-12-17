package com.example.centimetroandroid;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ManualActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manual_view); // Vinculamos el XML de arriba

        // Ocultar la barra superior por estética
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Al pulsar volver, simplemente cerramos esta actividad
                // para regresar a la anterior (el Login o el Home)
                finish();
            }
        });
    }
}