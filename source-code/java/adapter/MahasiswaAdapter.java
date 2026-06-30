package com.example.uas_webservice.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.uas_webservice.EditActivity;
import com.example.uas_webservice.R;
import com.example.uas_webservice.model.Mahasiswa;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaAdapter extends RecyclerView.Adapter<MahasiswaAdapter.ViewHolder> {

    private final Context context;
    private List<Mahasiswa> dataList;
    private List<Mahasiswa> dataListFull;

    public MahasiswaAdapter(Context context, List<Mahasiswa> list) {
        this.context      = context;
        this.dataList     = new ArrayList<>(list);
        this.dataListFull = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_mahasiswa, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder h, int pos) {
        Mahasiswa m = dataList.get(pos);

        h.tvNama.setText(m.getNama() != null ? m.getNama() : "-");
        h.tvNIM.setText(m.getNIM()  != null ? m.getNIM()  : "-");
        h.tvJurusan.setText(m.getJurusan() != null ? m.getJurusan() : "-");

        // Compute initials from name (max 2 chars)
        h.tvAvatar.setText(getInitials(m.getNama()));

        h.card.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditActivity.class);
            intent.putExtra("NIM",     m.getNIM());
            intent.putExtra("Nama",    m.getNama());
            intent.putExtra("Jurusan", m.getJurusan());
            intent.putExtra("Alamat",  m.getAlamat());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return dataList.size(); }

    /** Filter list by query — searches name, NIM, jurusan, alamat */
    public void filter(String query) {
        dataList.clear();
        if (query.isEmpty()) {
            dataList.addAll(dataListFull);
        } else {
            String q = query.toLowerCase().trim();
            for (Mahasiswa m : dataListFull) {
                if (matchesQuery(m, q)) dataList.add(m);
            }
        }
        notifyDataSetChanged();
    }

    public void updateData(List<Mahasiswa> newList) {
        dataList.clear();
        dataListFull.clear();
        dataList.addAll(newList);
        dataListFull.addAll(newList);
        notifyDataSetChanged();
    }

    // ── Helpers ──────────────────────────────────────────────────

    /** Returns up to 2 uppercase initials from the given name */
    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "?";
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2) {
            return (String.valueOf(parts[0].charAt(0)) +
                    String.valueOf(parts[1].charAt(0))).toUpperCase();
        }
        return String.valueOf(parts[0].charAt(0)).toUpperCase();
    }

    private boolean matchesQuery(Mahasiswa m, String q) {
        return (m.getNIM()     != null && m.getNIM().toLowerCase().contains(q))
            || (m.getNama()    != null && m.getNama().toLowerCase().contains(q))
            || (m.getJurusan() != null && m.getJurusan().toLowerCase().contains(q))
            || (m.getAlamat()  != null && m.getAlamat().toLowerCase().contains(q));
    }

    // ── ViewHolder ────────────────────────────────────────────────

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView tvAvatar, tvNama, tvJurusan, tvNIM;

        ViewHolder(@NonNull View v) {
            super(v);
            card      = v.findViewById(R.id.card);
            tvAvatar  = v.findViewById(R.id.tvAvatar);
            tvNama    = v.findViewById(R.id.tvNama);
            tvJurusan = v.findViewById(R.id.tvJurusan);
            tvNIM     = v.findViewById(R.id.tvNIM);
        }
    }
}
