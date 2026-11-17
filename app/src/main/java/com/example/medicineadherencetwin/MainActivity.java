package com.example.medicineadherencetwin;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private SessionManager sessionManager;
    private DatabaseHelper dbHelper;
    private TextView tvSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        dbHelper = new DatabaseHelper(this);
        
        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }
        
        createNotificationChannel();
        requestNotificationPermission();

        TextView tvWelcomeUser = findViewById(R.id.tvWelcomeUser);
        tvSummary = findViewById(R.id.tvSummary);
        Button btnLogout = findViewById(R.id.btnLogout);
        Button btnAddMedicine = findViewById(R.id.btnAddMedicine);
        Button btnViewMedicines = findViewById(R.id.btnViewMedicines);
        Button btnTrackDoses = findViewById(R.id.btnTrackDoses);
        Button btnAdherenceReport = findViewById(R.id.btnAdherenceReport);

        tvWelcomeUser.setText(getString(R.string.welcome_user, sessionManager.getUsername()));

        btnLogout.setOnClickListener(v -> {
            sessionManager.logoutUser();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        btnAddMedicine.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddMedicineActivity.class));
        });

        btnViewMedicines.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MedicineListActivity.class));
        });

        btnTrackDoses.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, DoseTrackingActivity.class));
        });

        btnAdherenceReport.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AdherenceReportActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager.isLoggedIn()) {
            int medCount = dbHelper.getAllMedicines().size();
            int taken = dbHelper.getTakenDosesCount();
            int missed = dbHelper.getMissedDosesCount();
            int total = taken + missed;
            int adherence = total > 0 ? (int) (((float) taken / total) * 100) : 0;
            
            tvSummary.setText(getString(R.string.summary_text, medCount, adherence));
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Medicine Reminders";
            String description = "Channel for medicine reminder alarms";
            int importance = android.app.NotificationManager.IMPORTANCE_HIGH;
            android.app.NotificationChannel channel = new android.app.NotificationChannel("medicine_channel", name, importance);
            channel.setDescription(description);
            android.app.NotificationManager notificationManager = getSystemService(android.app.NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }
}
