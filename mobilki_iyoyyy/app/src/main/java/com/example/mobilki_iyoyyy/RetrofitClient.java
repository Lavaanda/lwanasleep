package com.example.mobilki_iyoyyy;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static ApiService apiService;

    private static final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    public static ApiService getApiService() {
        if (apiService == null) {

            CookieJar cookieJar = new CookieJar() {
                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    synchronized (cookieStore) {
                        cookieStore.put(url.host(), new ArrayList<>(cookies));
                        Log.d("COOKIES", "Saved cookies for host: " + url.host() + " → " + cookies);
                    }
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    synchronized (cookieStore) {
                        List<Cookie> cookies = cookieStore.get(url.host());
                        if (cookies == null) {
                            Log.d("COOKIES", "No cookies for host: " + url.host());
                            return Collections.emptyList();
                        }
                        Log.d("COOKIES", "Loaded cookies for host: " + url.host() + " → " + cookies);
                        return new ArrayList<>(cookies);
                    }
                }
            };

            // ---- LOGGING ----
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            Interceptor cookieLogger = chain -> {
                Request req = chain.request();

                List<Cookie> cookiesForReq;
                synchronized (cookieStore) {
                    cookiesForReq = cookieStore.get(req.url().host());
                }

                if (cookiesForReq != null)
                    Log.d("COOKIES", "Sending cookies: " + cookiesForReq);

                Response resp = chain.proceed(req);

                List<String> setCookies = resp.headers("Set-Cookie");
                if (setCookies != null && !setCookies.isEmpty()) {
                    Log.d("COOKIES", "Received Set-Cookie: " + setCookies);
                }

                return resp;
            };

            OkHttpClient client = new OkHttpClient.Builder()
                    .cookieJar(cookieJar)
                    .addInterceptor(logging)
                    .addInterceptor(cookieLogger)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:5000/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }

        return apiService;
    }
}
