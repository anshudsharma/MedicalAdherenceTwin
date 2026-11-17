package com.example.medicineadherencetwin;

public class Medicine {
    private int id;
    private String name;
    private String dosage;
    private String startDate;
    private String time;
    private int frequency;
    private String notes;

    public Medicine() {
    }

    public Medicine(String name, String dosage, String startDate, String time, int frequency, String notes) {
        this.name = name;
        this.dosage = dosage;
        this.startDate = startDate;
        this.time = time;
        this.frequency = frequency;
        this.notes = notes;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDosage() { return dosage; }
    public void setDosage(String dosage) { this.dosage = dosage; }

    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public int getFrequency() { return frequency; }
    public void setFrequency(int frequency) { this.frequency = frequency; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
