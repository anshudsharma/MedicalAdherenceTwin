package com.example.medicineadherencetwin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);

        btnLogin.setOnClickListener(v -> loginUser());

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void loginUser() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        if (sessionManager.validateCredentials(username, password)) {
            sessionManager.createLoginSession(username);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid credentials or user not registered", Toast.LENGTH_SHORT).show();
        }
    }
}
