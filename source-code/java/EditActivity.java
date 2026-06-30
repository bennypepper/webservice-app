package com.example.uas_webservice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.uas_webservice.network.RetrofitClient;
import org.json.JSONObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditActivity extends AppCompatActivity {

    private EditText etNIM, etNama, etJurusan, etAlamat;
    private Button btnUpdate, btnDelete, btnKembali;
    private TextView btnBack, tvAvatarStrip, tvNamaStrip, tvNIMStrip;
    private ProgressBar progressBar;
    private String originalNIM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        etNIM          = findViewById(R.id.etNIM);
        etNama         = findViewById(R.id.etNama);
        etJurusan      = findViewById(R.id.etJurusan);
        etAlamat       = findViewById(R.id.etAlamat);
        btnUpdate      = findViewById(R.id.btnUpdate);
        btnDelete      = findViewById(R.id.btnDelete);
        btnKembali     = findViewById(R.id.btnKembali);
        btnBack        = findViewById(R.id.btnBack);
        tvAvatarStrip  = findViewById(R.id.tvAvatarStrip);
        tvNamaStrip    = findViewById(R.id.tvNamaStrip);
        tvNIMStrip     = findViewById(R.id.tvNIMStrip);
        progressBar    = findViewById(R.id.progressBar);

        // Populate from intent
        originalNIM   = getIntent().getStringExtra("NIM");
        String nama   = getIntent().getStringExtra("Nama");
        String jurusan= getIntent().getStringExtra("Jurusan");
        String alamat = getIntent().getStringExtra("Alamat");

        etNIM.setText(originalNIM);
        etNama.setText(nama);
        etJurusan.setText(jurusan);
        etAlamat.setText(alamat);

        // Populate id-strip
        tvAvatarStrip.setText(getInitials(nama));
        tvNamaStrip.setText(nama != null ? nama : "-");
        tvNIMStrip.setText("NIM " + (originalNIM != null ? originalNIM : "-"));

        btnBack.setOnClickListener(v -> finish());
        btnUpdate.setOnClickListener(v -> doUpdate());
        btnDelete.setOnClickListener(v -> confirmDelete());
        btnKembali.setOnClickListener(v -> finish());
    }

    private String getInitials(String name) {
        if (name == null || name.trim().isEmpty()) return "?";
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2) {
            return (String.valueOf(parts[0].charAt(0)) +
                    String.valueOf(parts[1].charAt(0))).toUpperCase();
        }
        return String.valueOf(parts[0].charAt(0)).toUpperCase();
    }

    private void doUpdate() {
        String nama    = etNama.getText().toString().trim();
        String jurusan = etJurusan.getText().toString().trim();
        String alamat  = etAlamat.getText().toString().trim();

        if (nama.isEmpty()) { etNama.setError("Nama wajib diisi"); etNama.requestFocus(); return; }
        if (jurusan.isEmpty()) { etJurusan.setError("Jurusan wajib diisi"); etJurusan.requestFocus(); return; }
        if (alamat.isEmpty()) { etAlamat.setError("Alamat wajib diisi"); etAlamat.requestFocus(); return; }

        setLoading(true);
        RetrofitClient.getInstance().getApiService()
                .updateMahasiswa(originalNIM, nama, jurusan, alamat)
                .enqueue(new Callback<ResponseBody>() {
                    @Override public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        setLoading(false);
                        handleResponse(response, "Data berhasil diupdate!", "Gagal update");
                    }
                    @Override public void onFailure(Call<ResponseBody> call, Throwable t) {
                        setLoading(false);
                        Toast.makeText(EditActivity.this, "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void confirmDelete() {
        String nama = etNama.getText().toString().trim();
        new AlertDialog.Builder(this)
                .setTitle("Konfirmasi Hapus")
                .setMessage("Yakin ingin menghapus data " + nama + "?")
                .setPositiveButton("Hapus", (d, w) -> doDelete())
                .setNegativeButton("Batal", null)
                .show();
    }

    private void doDelete() {
        setLoading(true);
        RetrofitClient.getInstance().getApiService()
                .deleteMahasiswa(originalNIM)
                .enqueue(new Callback<ResponseBody>() {
                    @Override public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        setLoading(false);
                        handleResponse(response, "Data berhasil dihapus!", "Gagal hapus");
                    }
                    @Override public void onFailure(Call<ResponseBody> call, Throwable t) {
                        setLoading(false);
                        Toast.makeText(EditActivity.this, "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void handleResponse(Response<ResponseBody> response, String successMsg, String failMsg) {
        try {
            if (response.isSuccessful() && response.body() != null) {
                String raw = response.body().string();
                JSONObject obj = new JSONObject(raw);
                String status = obj.optString("status", "");
                if (status.equals("sukses") || status.equals("success") || status.equals("berhasil")) {
                    Toast.makeText(this, successMsg, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, obj.optString("pesan", failMsg), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnUpdate.setEnabled(!loading);
        btnDelete.setEnabled(!loading);
    }
}
