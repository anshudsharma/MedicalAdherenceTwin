package com.example.medicineadherencetwin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DoseTrackingAdapter extends RecyclerView.Adapter<DoseTrackingAdapter.DoseViewHolder> {

    private Context context;
    private List<Medicine> medicineList;
    private DatabaseHelper dbHelper;

    public DoseTrackingAdapter(Context context, List<Medicine> medicineList, DatabaseHelper dbHelper) {
        this.context = context;
        this.medicineList = medicineList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public DoseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dose_track, parent, false);
        return new DoseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoseViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.tvMedicineName.setText(medicine.getName());
        holder.tvDosage.setText(context.getString(R.string.dosage_format, medicine.getDosage()));
        holder.tvTime.setText(context.getString(R.string.scheduled_format, medicine.getTime()));

        holder.btnMarkTaken.setOnClickListener(v -> markDose(medicine.getId(), "Taken"));
        holder.btnMarkMissed.setOnClickListener(v -> markDose(medicine.getId(), "Missed"));
    }

    private void markDose(int medicineId, String status) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        boolean success = dbHelper.addOrUpdateDose(medicineId, currentDate, status);

        if (success) {
            Toast.makeText(context, "Dose marked as " + status, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Failed to record dose", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public static class DoseViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedicineName, tvDosage, tvTime;
        Button btnMarkTaken, btnMarkMissed;

        public DoseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMedicineName = itemView.findViewById(R.id.tvTrackMedicineName);
            tvDosage = itemView.findViewById(R.id.tvTrackDosage);
            tvTime = itemView.findViewById(R.id.tvTrackTime);
            btnMarkTaken = itemView.findViewById(R.id.btnMarkTaken);
            btnMarkMissed = itemView.findViewById(R.id.btnMarkMissed);
        }
    }
}
