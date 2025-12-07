package com.example.myapplication.security;

import android.content.Context;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    private Context context;

    public TokenInterceptor(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String url = request.url().encodedPath();

        boolean isAuthUrl = url.contains("/auth");
        if (isAuthUrl) return chain.proceed(request);

        String token = TokenStorage.getToken(context);

        if (token != null) {
            request = request
                    .newBuilder()
                    .addHeader("Authorization", "Bearer " + token).build();
        }

        return chain.proceed(request);
    }
}
