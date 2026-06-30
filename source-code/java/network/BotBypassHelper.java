package com.example.uas_webservice.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class BotBypassHelper {
    private static final String TAG = "BotBypassHelper";
    private static final String PREF_NAME = "webservice_prefs";
    private static final String KEY_COOKIE = "bypass_cookie";
    
    private static String cachedCookie = null;
    private static boolean isInitializing = false;

    public interface BypassCallback {
        void onCompleted(String cookie);
        void onError(String error);
    }

    public static synchronized String getCachedCookie() {
        return cachedCookie;
    }

    public static synchronized void setCachedCookie(String cookie) {
        cachedCookie = cookie;
    }

    /**
     * Get the cached __test cookie. First checks RAM, then SharedPreferences.
     */
    public static synchronized String getCookie(Context context) {
        if (cachedCookie != null) {
            return cachedCookie;
        }
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        cachedCookie = prefs.getString(KEY_COOKIE, null);
        return cachedCookie;
    }

    /**
     * Clear cached cookies (forces re-evaluation of challenge)
     */
    public static synchronized void clearCookie(Context context) {
        cachedCookie = null;
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
                .edit()
                .remove(KEY_COOKIE)
                .apply();
        CookieManager.getInstance().removeAllCookies(null);
    }

    /**
     * Runs a headless WebView to load the base URL, solve the JS challenge, and extract the cookie.
     */
    public static void prepareBypass(Context context, String url, BypassCallback callback) {
        // Run on Main Thread
        new Handler(Looper.getMainLooper()).post(() -> {
            String current = getCookie(context);
            if (current != null) {
                Log.d(TAG, "Using cached cookie: " + current);
                if (callback != null) callback.onCompleted(current);
                return;
            }

            if (isInitializing) {
                Log.d(TAG, "Bypass already initializing. Waiting...");
                return;
            }

            isInitializing = true;
            Log.d(TAG, "Initializing bot-bypass WebView for: " + url);

            WebView webView = new WebView(context.getApplicationContext());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setUserAgentString(
                    "Mozilla/5.0 (Linux; Android 14; Pixel 8) " +
                    "AppleWebKit/537.36 (KHTML, like Gecko) " +
                    "Chrome/124.0.0.0 Mobile Safari/537.36"
            );

            webView.setWebViewClient(new WebViewClient() {
                private int loadCount = 0;

                @Override
                public void onPageFinished(WebView view, String loadedUrl) {
                    loadCount++;
                    Log.d(TAG, "Page loaded (" + loadCount + "): " + loadedUrl);
                    
                    String cookies = CookieManager.getInstance().getCookie(loadedUrl);
                    Log.d(TAG, "Cookies for URL: " + cookies);

                    if (cookies != null && cookies.contains("__test=")) {
                        // Extract __test cookie
                        String testCookie = null;
                        String[] cookieArray = cookies.split(";");
                        for (String c : cookieArray) {
                            if (c.trim().startsWith("__test=")) {
                                testCookie = c.trim();
                                break;
                            }
                        }

                        if (testCookie != null) {
                            Log.d(TAG, "Successfully extracted bypass cookie: " + testCookie);
                            synchronized (BotBypassHelper.class) {
                                cachedCookie = testCookie;
                                SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
                                prefs.edit().putString(KEY_COOKIE, testCookie).apply();
                            }
                            isInitializing = false;
                            if (callback != null) callback.onCompleted(testCookie);
                            webView.destroy();
                            return;
                        }
                    }

                    // If after 3 loads we still don't have it, trigger error
                    if (loadCount >= 3) {
                        isInitializing = false;
                        if (callback != null) callback.onError("Failed to get __test cookie after multiple reloads");
                        webView.destroy();
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Log.e(TAG, "WebView error: " + description);
                    isInitializing = false;
                    if (callback != null) callback.onError(description);
                    webView.destroy();
                }
            });

            webView.loadUrl(url);
        });
    }
}
