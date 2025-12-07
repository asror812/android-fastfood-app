package com.example.myapplication.security;

import android.content.Context;
import android.util.Log;

public class TokenStorage {
    private static final String PREF = "auth_pref";
    private static final String KEY_TOKEN = "token";

    public static void saveToken(Context ctx, String token) {

        Log.d("API", "Save(token): " + token);
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_TOKEN, token)
                .apply();
    }

    public static String getToken(Context ctx) {
        String token = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .getString(KEY_TOKEN, null);
        Log.d("API", "Get(token): " + token);

        return token;
    }

    public static void clear(Context ctx) {
        ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE)
                .edit()
                .clear()
                .apply();
    }
}

