package com.example.uas_webservice;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.uas_webservice.network.RetrofitClient;
import org.json.JSONObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TambahActivity extends AppCompatActivity {

    private EditText etNIM, etNama, etJurusan, etAlamat;
    private Button btnSimpan, btnBatal;
    private TextView btnBack;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);

        etNIM       = findViewById(R.id.etNIM);
        etNama      = findViewById(R.id.etNama);
        etJurusan   = findViewById(R.id.etJurusan);
        etAlamat    = findViewById(R.id.etAlamat);
        btnSimpan   = findViewById(R.id.btnSimpan);
        btnBatal    = findViewById(R.id.btnBatal);
        btnBack     = findViewById(R.id.btnBack);
        progressBar = findViewById(R.id.progressBar);

        btnBack.setOnClickListener(v -> finish());
        btnBatal.setOnClickListener(v -> finish());
        btnSimpan.setOnClickListener(v -> doInsert());
    }

    private void doInsert() {
        String nim     = etNIM.getText().toString().trim();
        String nama    = etNama.getText().toString().trim();
        String jurusan = etJurusan.getText().toString().trim();
        String alamat  = etAlamat.getText().toString().trim();

        if (nim.isEmpty())     { etNIM.setError("NIM wajib diisi");         etNIM.requestFocus();     return; }
        if (nama.isEmpty())    { etNama.setError("Nama wajib diisi");       etNama.requestFocus();    return; }
        if (jurusan.isEmpty()) { etJurusan.setError("Jurusan wajib diisi"); etJurusan.requestFocus(); return; }
        if (alamat.isEmpty())  { etAlamat.setError("Alamat wajib diisi");   etAlamat.requestFocus();  return; }

        setLoading(true);

        RetrofitClient.getInstance().getApiService()
                .insertMahasiswa(nim, nama, jurusan, alamat)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        setLoading(false);
                        try {
                            if (response.isSuccessful() && response.body() != null) {
                                String raw = response.body().string();
                                JSONObject obj = new JSONObject(raw);
                                String status = obj.optString("status", "");
                                if (status.equals("sukses") || status.equals("success") || status.equals("berhasil")) {
                                    Toast.makeText(TambahActivity.this,
                                            "Data berhasil ditambahkan!", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(TambahActivity.this,
                                            obj.optString("pesan", "Gagal menambahkan data"),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(TambahActivity.this,
                                    "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        setLoading(false);
                        Toast.makeText(TambahActivity.this,
                                "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnSimpan.setEnabled(!loading);
    }
}
