package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.AuthApi;
import com.example.myapplication.api.dto.request.ValidatePhoneNumberDto;
import com.example.myapplication.api.dto.response.ApiMessageResponse;
import com.example.myapplication.api.dto.response.ErrorResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyCodeActivity extends AppCompatActivity {
    private TextView tvSubtitle, tvTimer, tvResend;
    private int resendCount = 0;
    private CountDownTimer countDownTimer;
    private static final int MAX_RESEND = 3;
    private static final long TIMER_DURATION = 60_000;

    private AuthApi api;
    private EditText etCode;

    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        tvSubtitle = findViewById(R.id.tvSubtitle);
        tvTimer = findViewById(R.id.tvTimer);
        tvResend = findViewById(R.id.resend);
        etCode = findViewById(R.id.etCode);

        phone = getIntent().getStringExtra("user_phone");

        //Mask phone
        if (phone != null && phone.length() >= 5) {
            String masked = maskPhone(phone);
            tvSubtitle.setText("Bir martalik kod " + masked + " raqaminga yuborildi");
        }

        api = ApiClient.getClient(VerifyCodeActivity.this).create(AuthApi.class);
        startTimer();

        tvResend.setOnClickListener(v -> {
            if (resendCount < MAX_RESEND) {
                resendCount++;
                startTimer();

                // SEND OTP REQUEST
                api.validatePhoneNumber(new ValidatePhoneNumberDto(phone, null))
                        .enqueue(new Callback<>() {
                            @Override
                            public void onResponse(Call<ApiMessageResponse> call, Response<ApiMessageResponse> response) {
                                // ---- HANDLE 429 LIMIT ERROR ----
                                if (response.code() == 429) {
                                    handleOtpLimit(response);
                                    return;
                                }

                                // ---- GENERAL ERROR ----
                                if (!response.isSuccessful() || response.body() == null) {
                                    Log.e("API", "Error: " + response.code());
                                    Toast.makeText(VerifyCodeActivity.this, "Server xatosi", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                ApiMessageResponse res = response.body();
                                String msg = res.getMessage();

                                if (msg.equals("Sms was sent successfully")) {
                                    Toast.makeText(VerifyCodeActivity.this, "Kod yuborildi", Toast.LENGTH_SHORT).show();

                                } else if (msg.equals("Sms was re-send successfully")) {
                                    Toast.makeText(VerifyCodeActivity.this, "Kod qayta yuborildi", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(VerifyCodeActivity.this, "Xatolik: " + msg, Toast.LENGTH_SHORT).show();
                                    Log.e("API", "Unexpected: " + msg);
                                }
                            }

                            @Override
                            public void onFailure(Call<ApiMessageResponse> call, Throwable t) {
                                Log.e("API", "Error: " + t.getMessage());
                                Toast.makeText(VerifyCodeActivity.this, "Internet xatosi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(this, "Siz faqat 2 marta qayta yuborishingiz mumkin", Toast.LENGTH_SHORT).show();
            }
        });


        // Detect 5 digits automatically
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 5) {
                    verifyOtp(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void handleOtpLimit(Response<ApiMessageResponse> response) {
        try {
            Gson gson = new Gson();
            ErrorResponse err = gson.fromJson(response.errorBody().charStream(), ErrorResponse.class);

            if (err != null && "OTP_LIMIT".equals(err.getCode())) {
                Toast.makeText(this, "OTP limitiga yetdingiz. Keyinroq urinib ko‘ring.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "OTP xatosi", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Xatolik yuz berdi", Toast.LENGTH_SHORT).show();
        }
    }

    private void verifyOtp(String code) {
        ValidatePhoneNumberDto req = new ValidatePhoneNumberDto(phone, Integer.valueOf(code));

        api.validatePhoneNumber(req).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ApiMessageResponse> call, Response<ApiMessageResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    Toast.makeText(VerifyCodeActivity.this, "Internal Server Error", Toast.LENGTH_SHORT).show();
                    return;
                }

                ApiMessageResponse res = response.body();

                if (res.getMessage().equals("Otp was successfully verified")) {
                    Toast.makeText(VerifyCodeActivity.this, "Welcome!", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(VerifyCodeActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(VerifyCodeActivity.this, "Incorrect code", Toast.LENGTH_SHORT).show();
                    etCode.setText("");
                }
            }

            @Override
            public void onFailure(Call<ApiMessageResponse> call, Throwable t) {
                Toast.makeText(VerifyCodeActivity.this,
                        "Internet Error: " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String maskPhone(String phone) {
        if (phone.length() <= 5) return phone;
        String firstFive = phone.substring(0, 7);
        return firstFive + "-**-**";
    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        tvTimer.setText("01:00");

        countDownTimer = new CountDownTimer(TIMER_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int minutes = seconds / 60;
                int secs = seconds % 60;
                tvTimer.setText(String.format("%02d:%02d", minutes, secs));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
