package com.example.medicineadherencetwin;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdherenceReportActivity extends AppCompatActivity {

    private TextView tvTotalDoses, tvTakenDoses, tvMissedDoses, tvAdherencePercentage;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adherence_report);

        dbHelper = new DatabaseHelper(this);

        tvTotalDoses = findViewById(R.id.tvTotalDoses);
        tvTakenDoses = findViewById(R.id.tvTakenDoses);
        tvMissedDoses = findViewById(R.id.tvMissedDoses);
        tvAdherencePercentage = findViewById(R.id.tvAdherencePercentage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        calculateAdherence();
    }

    private void calculateAdherence() {
        int takenDoses = dbHelper.getTakenDosesCount();
        int missedDoses = dbHelper.getMissedDosesCount();
        int totalDoses = takenDoses + missedDoses;

        tvTotalDoses.setText(getString(R.string.total_doses, totalDoses));
        tvTakenDoses.setText(getString(R.string.taken_doses, takenDoses));
        tvMissedDoses.setText(getString(R.string.missed_doses, missedDoses));

        int adherencePercentage = 0;
        if (totalDoses > 0) {
            adherencePercentage = (int) (((float) takenDoses / totalDoses) * 100);
        }

        tvAdherencePercentage.setText(getString(R.string.adherence_percentage_format, adherencePercentage));
    }
}
