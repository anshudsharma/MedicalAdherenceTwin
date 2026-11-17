package com.example.medicineadherencetwin;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;
import java.util.Locale;

public class AddMedicineActivity extends AppCompatActivity {

    private EditText etMedicineName, etDosage, etFrequency, etStartDate, etTime, etNotes;
    private Button btnSaveMedicine;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        dbHelper = new DatabaseHelper(this);

        etMedicineName = findViewById(R.id.etMedicineName);
        etDosage = findViewById(R.id.etDosage);
        etFrequency = findViewById(R.id.etFrequency);
        etStartDate = findViewById(R.id.etStartDate);
        etTime = findViewById(R.id.etTime);
        etNotes = findViewById(R.id.etNotes);
        btnSaveMedicine = findViewById(R.id.btnSaveMedicine);

        etStartDate.setOnClickListener(v -> showDatePicker());
        etTime.setOnClickListener(v -> showTimePicker());
        btnSaveMedicine.setOnClickListener(v -> saveMedicine());
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

    private void saveMedicine() {
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

        int frequency = Integer.parseInt(freqStr);

        Medicine medicine = new Medicine(name, dosage, startDate, time, frequency, notes);
        long id = dbHelper.addMedicine(medicine);

        if (id != -1) {
            medicine.setId((int) id);
            AlarmHelper.scheduleAlarm(this, medicine);
            Toast.makeText(this, getString(R.string.success_save), Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save medicine", Toast.LENGTH_SHORT).show();
        }
    }
}
