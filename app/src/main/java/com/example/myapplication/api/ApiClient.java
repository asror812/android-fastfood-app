package com.example.myapplication.api;

import android.content.Context;

import com.example.myapplication.security.TokenInterceptor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import java.time.LocalDate;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "http://10.0.2.2:8080";
    private static Retrofit retrofit;

    public static Retrofit getClient(Context ctx) {
        if (retrofit == null) {

            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>)
                            (json, type, context) -> LocalDate.parse(json.getAsString()))
                    .create();

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new TokenInterceptor(ctx))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
