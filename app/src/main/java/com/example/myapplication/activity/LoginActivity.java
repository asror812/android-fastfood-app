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
    private Button btnLogin;
    private Button btnRegister;

    EditText etPhone, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        authApi = ApiClient.getClient(LoginActivity.this).create(AuthApi.class);


        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        etPassword = findViewById(R.id.etPassword);
        etPhone = findViewById(R.id.etPhone);

        btnRegister.setOnClickListener(v -> redirectToRegisterPage());
        btnLogin.setOnClickListener(v -> {

            SignInDto signInDto = validate();
            if (signInDto == null) return;

            authApi.signIn(signInDto).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<TokenResponseDto> call, Response<TokenResponseDto> response) {

                    if (!response.isSuccessful() || response.body() == null) {
                        Log.e("API", "Error: " + response.code());
                        Toast.makeText(LoginActivity.this, "Noto'g'ri login yoki parol", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    TokenResponseDto token = response.body();
                    TokenStorage.saveToken(LoginActivity.this, token.getToken());

                    Toast.makeText(LoginActivity.this, "Muvaffaqiyatli!", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(LoginActivity.this, Main1Activity.class));
                    finish();
                }

                @Override
                public void onFailure(Call<TokenResponseDto> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Internet xatosi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void redirectToRegisterPage() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private SignInDto validate() {
        String phone = etPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Telefon raqamni kiriting");
            etPhone.requestFocus();
            return null;
        }

        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Parolni kiriting");
            etPassword.requestFocus();
            return null;
        }

        return new SignInDto(phone, password);
    }
}

