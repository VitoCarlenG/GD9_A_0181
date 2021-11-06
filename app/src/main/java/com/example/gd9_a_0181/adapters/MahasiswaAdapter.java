package com.example.gd9_a_0181.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gd9_a_0181.AddEditActivity;
import com.example.gd9_a_0181.MainActivity;
import com.example.gd9_a_0181.R;
import com.example.gd9_a_0181.models.Mahasiswa;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ViewHolder>
        implements Filterable {

    private List<Mahasiswa> mahasiswaList, filteredMahasiswaList;
    private Context context;

    public MahasiswaAdapter(List<Mahasiswa> mahasiswaList, Context context) {
        this.mahasiswaList = mahasiswaList;
        filteredMahasiswaList = new ArrayList<>(mahasiswaList);
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_mahasiswa, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mahasiswa mahasiswa = filteredMahasiswaList.get(position);

        holder.tvNama.setText(mahasiswa.getNama());
        holder.tvNpm.setText(mahasiswa.getNpm());
        holder.tvInfo.setText(mahasiswa.getFakultas() + " - " + mahasiswa.getProdi());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(context);
                materialAlertDialogBuilder.setTitle("Konfirmasi")
                        .setMessage("Apakah anda yakin ingin menghapus data mahasiswa ini?")
                        .setNegativeButton("Batal", null)
                        .setPositiveButton("Hapus", new
                                DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if (context instanceof MainActivity)
                                            ((MainActivity)
                                                    context).deleteMahasiswa(mahasiswa.getId());

                                    }
                                })
                        .show();
            }
        });

        holder.cvMahasiswa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, AddEditActivity.class);
                i.putExtra("id", mahasiswa.getId());

                if (context instanceof MainActivity)
                    ((MainActivity) context).startActivityForResult(i, MainActivity.LAUNCH_ADD_ACTIVITY);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredMahasiswaList.size();
    }

    public void setMahasiswaList(List<Mahasiswa> mahasiswaList) {
        this.mahasiswaList =  mahasiswaList;
        filteredMahasiswaList = new ArrayList<>(mahasiswaList);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charSequenceString = charSequence.toString();
                List<Mahasiswa> filtered = new ArrayList<>();

                if(charSequenceString.isEmpty()) {
                    filtered.addAll(mahasiswaList);
                }else{
                    for (Mahasiswa mahasiswa : mahasiswaList) {
                        if (mahasiswa.getNama().toLowerCase()
                                .contains(charSequenceString.toLowerCase()))
                            filtered.add(mahasiswa);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filtered;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredMahasiswaList.clear();
                filteredMahasiswaList.addAll((List<Mahasiswa>) filterResults.values);
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNama, tvNpm, tvInfo;
        ImageButton btnDelete;
        CardView cvMahasiswa;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNpm = itemView.findViewById(R.id.tv_npm);
            tvNama = itemView.findViewById(R.id.tv_title);
            tvInfo = itemView.findViewById(R.id.tv_info);
            btnDelete = itemView.findViewById(R.id.btn_delete);
            cvMahasiswa = itemView.findViewById(R.id.cv_mahasiswa);
        }
    }
}