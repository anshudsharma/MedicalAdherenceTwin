package com.example.medicineadherencetwin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class EditMedicineActivity extends AppCompatActivity {

    private EditText etMedicineName, etDosage, etFrequency, etStartDate, etTime, etNotes;
    private Button btnUpdateMedicine, btnDeleteMedicine;
    private DatabaseHelper dbHelper;
    private Medicine currentMedicine;
    private int medicineId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine);

        dbHelper = new DatabaseHelper(this);
        
        medicineId = getIntent().getIntExtra("medicine_id", -1);

        etMedicineName = findViewById(R.id.etEditMedicineName);
        etDosage = findViewById(R.id.etEditDosage);
        etFrequency = findViewById(R.id.etEditFrequency);
        etStartDate = findViewById(R.id.etEditStartDate);
        etTime = findViewById(R.id.etEditTime);
        etNotes = findViewById(R.id.etEditNotes);
        btnUpdateMedicine = findViewById(R.id.btnUpdateMedicine);
        btnDeleteMedicine = findViewById(R.id.btnDeleteMedicine);

        if (medicineId != -1) {
            currentMedicine = dbHelper.getMedicine(medicineId);
            if (currentMedicine != null) {
                populateData();
            }
        }

        etStartDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());
        
        btnUpdateMedicine.setOnClickListener(v -> updateMedicine());
        btnDeleteMedicine.setOnClickListener(v -> deleteMedicine());
    }

    private void populateData() {
        etMedicineName.setText(currentMedicine.getName());
        etDosage.setText(currentMedicine.getDosage());
        etFrequency.setText(String.valueOf(currentMedicine.getFrequency()));
        etStartDate.setText(currentMedicine.getStartDate());
        etTime.setText(currentMedicine.getTime());
        etNotes.setText(currentMedicine.getNotes());
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    String date = String.format(Locale.getDefault(), "%02d/%02d/%04d", dayOfMonth, month1 + 1, year1);
                    etStartDate.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute1) -> {
                    String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute1);
                    etTime.setText(time);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private void updateMedicine() {
        String name = etMedicineName.getText().toString().trim();
        String dosage = etDosage.getText().toString().trim();
        String freqStr = etFrequency.getText().toString().trim();
        String startDate = etStartDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String notes = etNotes.getText().toString().trim();

        if (name.isEmpty() || dosage.isEmpty() || freqStr.isEmpty() || startDate.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        currentMedicine.setName(name);
        currentMedicine.setDosage(dosage);
        currentMedicine.setFrequency(Integer.parseInt(freqStr));
        currentMedicine.setStartDate(startDate);
        currentMedicine.setTime(time);
        currentMedicine.setNotes(notes);

        int result = dbHelper.updateMedicine(currentMedicine);

        if (result > 0) {
            AlarmHelper.scheduleAlarm(this, currentMedicine);
            Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }
    
    private void deleteMedicine() {
        new AlertDialog.Builder(this)
                .setTitle("Delete Medicine")
                .setMessage("Are you sure you want to delete this medicine?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    AlarmHelper.cancelAlarm(this, medicineId);
                    dbHelper.deleteMedicine(medicineId);
                    Toast.makeText(EditMedicineActivity.this, "Deleted successfully", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .setNegativeButton("No", null)
                .show();
    }
}
