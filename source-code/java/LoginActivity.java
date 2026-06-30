package com.example.uas_webservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.uas_webservice.network.RetrofitClient;
import org.json.JSONObject;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername  = findViewById(R.id.etUsername);
        etPassword  = findViewById(R.id.etPassword);
        btnLogin    = findViewById(R.id.btnLogin);
        progressBar = findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(v -> doLogin());

        // Proactively initialize bypass challenge in background
        com.example.uas_webservice.network.BotBypassHelper.prepareBypass(
                this, "https://webserviceumc.byethost11.com/", null);
    }

    private void doLogin() {
        String user = etUsername.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Username dan password wajib diisi!", Toast.LENGTH_SHORT).show();
            return;
        }

        setLoading(true);

        // Ensure bot bypass cookie is loaded first
        String cookie = com.example.uas_webservice.network.BotBypassHelper.getCookie(this);
        if (cookie == null) {
            Toast.makeText(this, "Menghubungkan ke server aman...", Toast.LENGTH_SHORT).show();
            com.example.uas_webservice.network.BotBypassHelper.prepareBypass(
                    this, "https://webserviceumc.byethost11.com/", new com.example.uas_webservice.network.BotBypassHelper.BypassCallback() {
                @Override
                public void onCompleted(String cookie) {
                    executeLoginRequest(user, pass);
                }

                @Override
                public void onError(String error) {
                    setLoading(false);
                    Toast.makeText(LoginActivity.this, "Koneksi aman gagal: " + error, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            executeLoginRequest(user, pass);
        }
    }

    private void executeLoginRequest(String user, String pass) {
        RetrofitClient.getInstance().getApiService()
                .login(user, pass)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        setLoading(false);
                        try {
                            if (response.isSuccessful() && response.body() != null) {
                                String raw = response.body().string();

                                // If still HTML, clear cache and retry once
                                if (raw.trim().startsWith("<")) {
                                    com.example.uas_webservice.network.BotBypassHelper.clearCookie(LoginActivity.this);
                                    Toast.makeText(LoginActivity.this,
                                            "Koneksi kadaluarsa. Mengulang sambungan aman...",
                                            Toast.LENGTH_SHORT).show();
                                    doLogin();
                                    return;
                                }

                                JSONObject obj = new JSONObject(raw);
                                String status = obj.optString("status", "");

                                if ("sukses".equals(status) || "success".equals(status)) {
                                    Toast.makeText(LoginActivity.this,
                                            "Login berhasil!", Toast.LENGTH_SHORT).show();
                                    skipToMain();
                                } else {
                                    String pesan = obj.optString("pesan",
                                            obj.optString("message", "Username atau password salah"));
                                    Toast.makeText(LoginActivity.this, pesan, Toast.LENGTH_LONG).show();
                                }
                            } else {
                                String errBody = response.errorBody() != null ? response.errorBody().string() : "";
                                android.util.Log.e("LoginActivity", "HTTP Error: " + response.code() + " " + errBody);
                                
                                if (response.code() == 400 || response.code() == 403) {
                                    com.example.uas_webservice.network.BotBypassHelper.clearCookie(LoginActivity.this);
                                }
                                
                                Toast.makeText(LoginActivity.this,
                                        "Login gagal: HTTP " + response.code() + (errBody.isEmpty() ? "" : " (" + errBody + ")"), 
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this,
                                    "Error parsing response: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        setLoading(false);
                        Toast.makeText(LoginActivity.this,
                                "Koneksi gagal: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void skipToMain() {
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    private void setLoading(boolean loading) {
        progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        btnLogin.setEnabled(!loading);
    }
}
