package com.example.medicineadherencetwin;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MedicineListActivity extends AppCompatActivity {

    private RecyclerView rvMedicines;
    private TextView tvNoMedicines;
    private DatabaseHelper dbHelper;
    private MedicineAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_list);

        dbHelper = new DatabaseHelper(this);

        rvMedicines = findViewById(R.id.rvMedicines);
        tvNoMedicines = findViewById(R.id.tvNoMedicines);

        rvMedicines.setLayoutManager(new LinearLayoutManager(this));
        
        showNextMedicineToast(dbHelper.getAllMedicines());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMedicines();
    }

    private void loadMedicines() {
        List<Medicine> medicineList = dbHelper.getAllMedicines();

        if (medicineList.isEmpty()) {
            rvMedicines.setVisibility(View.GONE);
            tvNoMedicines.setVisibility(View.VISIBLE);
        } else {
            rvMedicines.setVisibility(View.VISIBLE);
            tvNoMedicines.setVisibility(View.GONE);
            
            adapter = new MedicineAdapter(this, medicineList);
            rvMedicines.setAdapter(adapter);
        }
    }

    private void showNextMedicineToast(List<Medicine> medicineList) {
        if (medicineList.isEmpty()) {
            Toast.makeText(this, "No medicines added yet.", Toast.LENGTH_LONG).show();
            return;
        }

        SimpleDateFormat parseFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        SimpleDateFormat displayDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        SimpleDateFormat displayTimeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());

        Date now = new Date();
        Medicine nextMedicine = null;
        Date nextDate = null;

        for (Medicine m : medicineList) {
            try {
                String dtStr = m.getStartDate() + " " + m.getTime();
                Date mDate = parseFormat.parse(dtStr);
                if (mDate != null && mDate.after(now)) {
                    if (nextDate == null || mDate.before(nextDate)) {
                        nextDate = mDate;
                        nextMedicine = m;
                    }
                }
            } catch (Exception e) {
                // Safely ignore parse exceptions for invalid data
            }
        }

        if (nextMedicine != null && nextDate != null) {
            String formattedDate = displayDateFormat.format(nextDate);
            String formattedTime = displayTimeFormat.format(nextDate).toUpperCase(Locale.getDefault());
            String msg = "Next medicine: " + nextMedicine.getName() + ", " + nextMedicine.getDosage() + 
                         ", to be taken on " + formattedDate + " at " + formattedTime + ".";
            Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No upcoming medicines scheduled.", Toast.LENGTH_LONG).show();
        }
    }
}
