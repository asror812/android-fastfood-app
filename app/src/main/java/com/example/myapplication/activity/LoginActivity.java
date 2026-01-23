package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.AuthApi;
import com.example.myapplication.api.dto.request.SignInDto;
import com.example.myapplication.api.dto.response.TokenResponseDto;
import com.example.myapplication.security.TokenStorage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private AuthApi authApi;

    private Button btnLogin, btnRegister;
    private EditText etPhone, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        authApi = ApiClient.getClient(this).create(AuthApi.class);

        initViews();
        initListeners();
    }

    private void initViews() {
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);
    }

    private void initListeners() {
        btnRegister.setOnClickListener(v -> openRegister());
        btnLogin.setOnClickListener(v -> onLoginClick());
    }

    private void openRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void onLoginClick() {
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!validateInput(phone, password)) return;

        doLogin(phone, password);
    }

    private boolean validateInput(String phone, String password) {
        etPhone.setError(null);
        etPassword.setError(null);

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Telefon raqamni kiriting");
            etPhone.requestFocus();
            return false;
        }

        if (!phone.matches("^\\+\\d{9}$")) {
            etPhone.setError("Telefon formati: +901234567");
            etPhone.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Parolni kiriting");
            etPassword.requestFocus();
            return false;
        }

        if (password.length() < 8) {
            etPassword.setError("Parol kamida 8 ta belgidan iborat bo‘lishi kerak");
            etPassword.requestFocus();
            return false;
        }

        return true;
    }

    private void doLogin(String phone, String password) {
        setLoading(true);

        authApi.signIn(new SignInDto(phone, password)).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<TokenResponseDto> call, Response<TokenResponseDto> response) {
                setLoading(false);

                if (!response.isSuccessful()) {
                    showApiError(response.code());
                    return;
                }

                TokenResponseDto body = response.body();
                if (body == null || body.getToken() == null || body.getToken().isEmpty()) {
                    toast("Server xatosi: token kelmadi");
                    return;
                }

                TokenStorage.saveToken(LoginActivity.this, body.getToken());
                toast("Muvaffaqiyatli!");

                startActivity(new Intent(LoginActivity.this, BottomNavigation.class));
                finish();
            }

            @Override
            public void onFailure(Call<TokenResponseDto> call, Throwable t) {
                setLoading(false);
                toast("Internet xatosi: " + t.getMessage());
            }
        });
    }

    private void showApiError(int code) {
        if (code == 401) {
            toast("Telefon raqam yoki parol noto‘g‘ri");
        } else if (code >= 500) {
            toast("Serverda xatolik. Keyinroq urinib ko‘ring");
        } else {
            toast("Xatolik: " + code);
        }
        Log.e("API", "HTTP error: " + code);
    }

    private void setLoading(boolean isLoading) {
        btnLogin.setEnabled(!isLoading);
        btnRegister.setEnabled(!isLoading);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}


