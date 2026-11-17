package com.example.medicineadherencetwin;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DoseTrackingActivity extends AppCompatActivity {

    private RecyclerView rvDoseTracking;
    private TextView tvNoMedicinesToTrack;
    private DatabaseHelper dbHelper;
    private DoseTrackingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dose_tracking);

        dbHelper = new DatabaseHelper(this);

        rvDoseTracking = findViewById(R.id.rvDoseTracking);
        tvNoMedicinesToTrack = findViewById(R.id.tvNoMedicinesToTrack);

        rvDoseTracking.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMedicines();
    }

    private void loadMedicines() {
        List<Medicine> medicineList = dbHelper.getAllMedicines();

        if (medicineList.isEmpty()) {
            rvDoseTracking.setVisibility(View.GONE);
            tvNoMedicinesToTrack.setVisibility(View.VISIBLE);
        } else {
            rvDoseTracking.setVisibility(View.VISIBLE);
            tvNoMedicinesToTrack.setVisibility(View.GONE);

            adapter = new DoseTrackingAdapter(this, medicineList, dbHelper);
            rvDoseTracking.setAdapter(adapter);
        }
    }
}
