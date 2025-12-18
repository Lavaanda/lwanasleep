package com.example.mobilki_iyoyyy;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FlaskClient {
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:5000/") //айпи эмулятора
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
