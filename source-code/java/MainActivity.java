package com.example.uas_webservice;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.example.uas_webservice.adapter.MahasiswaAdapter;
import com.example.uas_webservice.model.Mahasiswa;
import com.example.uas_webservice.network.RetrofitClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MahasiswaAdapter adapter;
    private SwipeRefreshLayout swipeRefresh;
    private EditText etSearch;
    private FloatingActionButton fabTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        etSearch     = findViewById(R.id.etSearch);
        fabTambah    = findViewById(R.id.fabTambah);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MahasiswaAdapter(this, new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Search box listener
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}
            @Override public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                adapter.filter(s.toString());
            }
            @Override public void afterTextChanged(Editable e) {}
        });

        // Swipe refresh
        swipeRefresh.setColorSchemeResources(android.R.color.holo_red_dark);
        swipeRefresh.setOnRefreshListener(this::loadData);

        // FAB
        fabTambah.setOnClickListener(v ->
                startActivity(new Intent(this, TambahActivity.class)));

        loadData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh list setiap balik dari TambahActivity / EditActivity
        loadData();
    }

    private int retryCount = 0;

    private void loadData() {
        swipeRefresh.setRefreshing(true);

        RetrofitClient.getInstance().getApiService()
                .getAllMahasiswa()
                .enqueue(new Callback<List<Mahasiswa>>() {
                    @Override
                    public void onResponse(Call<List<Mahasiswa>> call,
                                           Response<List<Mahasiswa>> response) {
                        swipeRefresh.setRefreshing(false);
                        if (response.isSuccessful() && response.body() != null) {
                            retryCount = 0; // reset retry counter on success
                            adapter.updateData(response.body());
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "Gagal memuat data: " + response.code(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Mahasiswa>> call, Throwable t) {
                        swipeRefresh.setRefreshing(false);
                        
                        // Check if it's a parsing error caused by HTML instead of JSON
                        String msg = t.getMessage() != null ? t.getMessage() : "";
                        if (msg.contains("MalformedJsonException") || t instanceof com.google.gson.JsonSyntaxException) {
                            if (retryCount < 3) {
                                retryCount++;
                                // Clear and refresh bypass cookie
                                com.example.uas_webservice.network.BotBypassHelper.clearCookie(MainActivity.this);
                                com.example.uas_webservice.network.BotBypassHelper.prepareBypass(
                                        MainActivity.this, "https://webserviceumc.byethost11.com/", 
                                        new com.example.uas_webservice.network.BotBypassHelper.BypassCallback() {
                                            @Override
                                            public void onCompleted(String cookie) {
                                                loadData(); // retry loading
                                            }

                                            @Override
                                            public void onError(String error) {
                                                Toast.makeText(MainActivity.this, 
                                                        "Sambungan aman gagal: " + error, Toast.LENGTH_LONG).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(MainActivity.this,
                                        "Gagal memuat data. Server mengembalikan respon tidak valid (kemungkinan database error atau tabel belum diimport).",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "Koneksi gagal: " + t.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}