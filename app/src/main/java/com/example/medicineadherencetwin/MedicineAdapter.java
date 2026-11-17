package com.example.medicineadherencetwin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {

    private Context context;
    private List<Medicine> medicineList;

    public MedicineAdapter(Context context, List<Medicine> medicineList) {
        this.context = context;
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medicine, parent, false);
        return new MedicineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        Medicine medicine = medicineList.get(position);
        holder.tvMedicineName.setText(medicine.getName());
        holder.tvMedicineDosage.setText(context.getString(R.string.dosage_format, medicine.getDosage()));
        holder.tvMedicineFrequency.setText(context.getString(R.string.frequency_format, medicine.getFrequency()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditMedicineActivity.class);
            intent.putExtra("medicine_id", medicine.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public static class MedicineViewHolder extends RecyclerView.ViewHolder {
        TextView tvMedicineName, tvMedicineDosage, tvMedicineFrequency;

        public MedicineViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMedicineName = itemView.findViewById(R.id.tvMedicineName);
            tvMedicineDosage = itemView.findViewById(R.id.tvMedicineDosage);
            tvMedicineFrequency = itemView.findViewById(R.id.tvMedicineFrequency);
        }
    }
}
