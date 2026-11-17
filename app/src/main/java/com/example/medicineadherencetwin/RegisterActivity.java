package com.example.medicineadherencetwin;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPassword, etConfirmPassword;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        sessionManager = new SessionManager(this);

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        
        Button btnRegister = findViewById(R.id.btnRegister);
        TextView tvLogin = findViewById(R.id.tvLogin);

        btnRegister.setOnClickListener(v -> registerUser());
        tvLogin.setOnClickListener(v -> finish());
    }

    private void registerUser() {
        String username = etUsername.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        if (sessionManager.isUserRegistered(username)) {
            Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        sessionManager.registerUser(username, email, password);
        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
        finish();
    }
}
