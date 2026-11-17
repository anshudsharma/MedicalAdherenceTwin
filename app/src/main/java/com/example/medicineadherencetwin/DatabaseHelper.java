package com.example.medicineadherencetwin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MedicineAdherence.db";
    private static final int DATABASE_VERSION = 2; // Incremented for schema change

    // Medicines Table
    private static final String TABLE_MEDICINES = "medicines";
    private static final String COLUMN_MED_ID = "id";
    private static final String COLUMN_MED_NAME = "name";
    private static final String COLUMN_MED_DOSAGE = "dosage";
    private static final String COLUMN_MED_START_DATE = "start_date";
    private static final String COLUMN_MED_TIME = "time";
    private static final String COLUMN_MED_FREQ = "frequency";
    private static final String COLUMN_MED_NOTES = "notes";

    // Doses Table
    private static final String TABLE_DOSES = "doses";
    private static final String COLUMN_DOSE_ID = "id";
    private static final String COLUMN_DOSE_MED_ID = "medicine_id";
    private static final String COLUMN_DOSE_DATE = "dose_date";
    private static final String COLUMN_DOSE_STATUS = "status";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createMedicinesTable = "CREATE TABLE " + TABLE_MEDICINES + "("
                + COLUMN_MED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MED_NAME + " TEXT,"
                + COLUMN_MED_DOSAGE + " TEXT,"
                + COLUMN_MED_START_DATE + " TEXT,"
                + COLUMN_MED_TIME + " TEXT,"
                + COLUMN_MED_FREQ + " INTEGER,"
                + COLUMN_MED_NOTES + " TEXT" + ")";
        db.execSQL(createMedicinesTable);

        String createDosesTable = "CREATE TABLE " + TABLE_DOSES + "("
                + COLUMN_DOSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DOSE_MED_ID + " INTEGER,"
                + COLUMN_DOSE_DATE + " TEXT,"
                + COLUMN_DOSE_STATUS + " TEXT,"
                + "FOREIGN KEY(" + COLUMN_DOSE_MED_ID + ") REFERENCES " + TABLE_MEDICINES + "(" + COLUMN_MED_ID + ") ON DELETE CASCADE)";
        db.execSQL(createDosesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MEDICINES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOSES);
        // Clean up old users table from version 1 just in case
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
    
    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // --- Medicine Operations ---
    public long addMedicine(Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MED_NAME, medicine.getName());
        values.put(COLUMN_MED_DOSAGE, medicine.getDosage());
        values.put(COLUMN_MED_START_DATE, medicine.getStartDate());
        values.put(COLUMN_MED_TIME, medicine.getTime());
        values.put(COLUMN_MED_FREQ, medicine.getFrequency());
        values.put(COLUMN_MED_NOTES, medicine.getNotes());

        long id = db.insert(TABLE_MEDICINES, null, values);
        db.close();
        return id;
    }

    public List<Medicine> getAllMedicines() {
        List<Medicine> medicineList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        try (Cursor cursor = db.query(TABLE_MEDICINES, null, null, null, null, null, COLUMN_MED_NAME + " ASC")) {
            if (cursor.moveToFirst()) {
                do {
                    Medicine medicine = new Medicine();
                    medicine.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MED_ID)));
                    medicine.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MED_NAME)));
                    medicine.setDosage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MED_DOSAGE)));
                    medicine.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MED_START_DATE)));
                    medicine.setTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MED_TIME)));
                    medicine.setFrequency(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MED_FREQ)));
                    medicine.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MED_NOTES)));
                    medicineList.add(medicine);
                } while (cursor.moveToNext());
            }
        }
        db.close();
        return medicineList;
    }

    public Medicine getMedicine(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_MED_ID + "=?";
        String[] selectionArgs = {String.valueOf(id)};
        
        Medicine medicine = null;
        try (Cursor cursor = db.query(TABLE_MEDICINES, null, selection, selectionArgs, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                medicine = new Medicine();
                medicine.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MED_ID)));
                medicine.setName(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MED_NAME)));
                medicine.setDosage(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MED_DOSAGE)));
                medicine.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MED_START_DATE)));
                medicine.setTime(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MED_TIME)));
                medicine.setFrequency(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MED_FREQ)));
                medicine.setNotes(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_MED_NOTES)));
            }
        }
        db.close();
        return medicine;
    }

    public int updateMedicine(Medicine medicine) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MED_NAME, medicine.getName());
        values.put(COLUMN_MED_DOSAGE, medicine.getDosage());
        values.put(COLUMN_MED_START_DATE, medicine.getStartDate());
        values.put(COLUMN_MED_TIME, medicine.getTime());
        values.put(COLUMN_MED_FREQ, medicine.getFrequency());
        values.put(COLUMN_MED_NOTES, medicine.getNotes());

        int result = db.update(TABLE_MEDICINES, values, COLUMN_MED_ID + " = ?",
                new String[]{String.valueOf(medicine.getId())});
        db.close();
        return result;
    }

    public void deleteMedicine(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MEDICINES, COLUMN_MED_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // --- Dose Operations ---
    public boolean addOrUpdateDose(int medicineId, String date, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        
        boolean exists;
        try (Cursor cursor = db.query(TABLE_DOSES, new String[]{COLUMN_DOSE_ID},
                COLUMN_DOSE_MED_ID + "=? AND " + COLUMN_DOSE_DATE + "=?",
                new String[]{String.valueOf(medicineId), date}, null, null, null)) {
            exists = (cursor != null && cursor.getCount() > 0);
        }
        
        ContentValues values = new ContentValues();
        values.put(COLUMN_DOSE_MED_ID, medicineId);
        values.put(COLUMN_DOSE_DATE, date);
        values.put(COLUMN_DOSE_STATUS, status);
        
        long result;
        if (exists) {
            result = db.update(TABLE_DOSES, values, 
                    COLUMN_DOSE_MED_ID + "=? AND " + COLUMN_DOSE_DATE + "=?",
                    new String[]{String.valueOf(medicineId), date});
        } else {
            result = db.insert(TABLE_DOSES, null, values);
        }
        db.close();
        return result != -1;
    }

    public int getTakenDosesCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_DOSES + " WHERE " + COLUMN_DOSE_STATUS + " = ?", new String[]{"Taken"})) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        }
        db.close();
        return count;
    }

    public int getMissedDosesCount() {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        try (Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_DOSES + " WHERE " + COLUMN_DOSE_STATUS + " = ?", new String[]{"Missed"})) {
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
        }
        db.close();
        return count;
    }
}
