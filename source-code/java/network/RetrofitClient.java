package com.example.uas_webservice.network;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    // ✅ Updated to byethost — no bot-challenge, returns clean JSON
    private static final String BASE_URL = "https://webserviceumc.byethost11.com/";

    private static RetrofitClient instance = null;
    private final ApiService apiService;

    private RetrofitClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    
                    // Fetch __test cookie dynamically from CookieManager or BotBypassHelper
                    String cookieHeader = "";
                    String cached = BotBypassHelper.getCachedCookie();
                    if (cached != null) {
                        cookieHeader = cached;
                    } else {
                        try {
                            String webViewCookies = android.webkit.CookieManager.getInstance()
                                    .getCookie("https://webserviceumc.byethost11.com/");
                            if (webViewCookies != null && webViewCookies.contains("__test=")) {
                                String[] parts = webViewCookies.split(";");
                                for (String c : parts) {
                                    if (c.trim().startsWith("__test=")) {
                                        cookieHeader = c.trim();
                                        BotBypassHelper.setCachedCookie(cookieHeader);
                                        break;
                                    }
                                }
                            }
                        } catch (Exception e) {
                            // CookieManager might not be initialized yet on some threads/devices
                        }
                    }

                    Request.Builder builder = original.newBuilder()
                            .header("User-Agent",
                                    "Mozilla/5.0 (Linux; Android 14; Pixel 8) " +
                                    "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                    "Chrome/124.0.0.0 Mobile Safari/537.36")
                            .header("Accept", "application/json, */*")
                            .header("Connection", "keep-alive");

                    if (!cookieHeader.isEmpty()) {
                        builder.header("Cookie", cookieHeader);
                    }

                    return chain.proceed(builder.build());
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public ApiService getApiService() {
        return apiService;
    }
}
