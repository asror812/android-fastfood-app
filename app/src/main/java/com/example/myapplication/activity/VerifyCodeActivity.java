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
    private EditText etCode;

    private AuthApi api;

    private String phone;

    private int resendCount = 0;

    private static final int MAX_RESEND = 2;
    private static final long TIMER_DURATION = 60_000; // 60 секунд
    private CountDownTimer countDownTimer;

    private static final String TAG = "VERIFY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_code);

        api = ApiClient.getClient(this).create(AuthApi.class);

        phone = getIntent().getStringExtra("user_phone");
        if (phone == null || phone.trim().isEmpty()) {
            toast("Telefon raqam topilmadi");
            finish();
            return;
        }

        initViews();
        initUi();
        initListeners();

        startTimer();
    }

    private void initViews() {
        tvSubtitle = findViewById(R.id.tvSubtitle);
        tvTimer = findViewById(R.id.tvTimer);
        tvResend = findViewById(R.id.resend);
        etCode = findViewById(R.id.etCode);
    }

    private void initUi() {
        tvSubtitle.setText("Bir martalik kod " + maskPhone(phone) + " raqaminga yuborildi");
        setResendEnabled(false);
    }

    private void initListeners() {
        tvResend.setOnClickListener(v -> requestResendOtp());

        // Авто-проверка при вводе 5 цифр
        etCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != null && s.length() == 5) {
                    verifyOtp(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void requestResendOtp() {
        if (resendCount >= MAX_RESEND) {
            toast("Siz faqat " + MAX_RESEND + " marta qayta yuborishingiz mumkin");
            setResendEnabled(false);
            return;
        }

        // пока идет запрос — блокируем resend
        setResendEnabled(false);

        api.validatePhoneNumber(new ValidatePhoneNumberDto(phone, null))
                .enqueue(new Callback<ApiMessageResponse>() {
                    @Override
                    public void onResponse(Call<ApiMessageResponse> call, Response<ApiMessageResponse> response) {

                        // лимит по серверу (429)
                        if (response.code() == 429) {
                            handleOtpLimit(response);
                            // если лимит — не включаем resend обратно
                            return;
                        }

                        if (!response.isSuccessful()) {
                            Log.e(TAG, "Resend HTTP error: " + response.code());
                            toast("Server xatosi: " + response.code());
                            // разрешим попробовать снова (если ещё можно)
                            setResendEnabled(resendCount < MAX_RESEND);
                            return;
                        }

                        ApiMessageResponse body = response.body();
                        if (body == null) {
                            toast("Server xatosi: body null");
                            setResendEnabled(resendCount < MAX_RESEND);
                            return;
                        }

                        resendCount++;
                        toast("Kod yuborildi");
                        etCode.setText("");
                        startTimer();
                    }

                    @Override
                    public void onFailure(Call<ApiMessageResponse> call, Throwable t) {
                        Log.e(TAG, "Resend failure: " + t.getMessage());
                        toast("Internet xatosi: " + t.getMessage());
                        // разрешим снова нажать (если лимит не достигнут)
                        setResendEnabled(resendCount < MAX_RESEND);
                    }
                });
    }

    private void verifyOtp(String code) {
        // защита от краша и мусора
        if (code == null || !code.matches("\\d{5}")) {
            toast("Kod 5 ta raqam bo‘lishi kerak");
            etCode.setText("");
            return;
        }

        int otp = Integer.parseInt(code);

        // блокируем ввод, чтобы не отправляли много раз
        setCodeEnabled(false);

        api.validatePhoneNumber(new ValidatePhoneNumberDto(phone, otp))
                .enqueue(new Callback<ApiMessageResponse>() {
                    @Override
                    public void onResponse(Call<ApiMessageResponse> call, Response<ApiMessageResponse> response) {
                        setCodeEnabled(true);

                        if (response.code() == 429) {
                            handleOtpLimit(response);
                            return;
                        }

                        if (!response.isSuccessful() || response.body() == null) {
                            toast("Kod noto‘g‘ri yoki xatolik: " + response.code());
                            etCode.setText("");
                            return;
                        }

                        ApiMessageResponse res = response.body();

                        if ("Otp was successfully verified".equals(res.getMessage())) {
                            toast("Welcome!");

                            startActivity(new Intent(VerifyCodeActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            toast("Incorrect code");
                            etCode.setText("");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiMessageResponse> call, Throwable t) {
                        setCodeEnabled(true);
                        toast("Internet Error: " + t.getMessage());
                    }
                });
    }

    private void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();

        setResendEnabled(false);
        tvTimer.setText("01:00");

        countDownTimer = new CountDownTimer(TIMER_DURATION, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int totalSeconds = (int) (millisUntilFinished / 1000);
                int minutes = totalSeconds / 60;
                int seconds = totalSeconds % 60;
                tvTimer.setText(String.format("%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                // включаем resend только если не достигли лимита
                setResendEnabled(resendCount < MAX_RESEND);
            }
        }.start();
    }

    private void setResendEnabled(boolean enabled) {
        tvResend.setEnabled(enabled);
        tvResend.setAlpha(enabled ? 1f : 0.4f);
    }

    private void setCodeEnabled(boolean enabled) {
        etCode.setEnabled(enabled);
        etCode.setAlpha(enabled ? 1f : 0.7f);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private String maskPhone(String phone) {
        if (phone == null) return "";

        if (phone.length() < 7) return phone;

        String start = phone.substring(0, Math.min(7, phone.length()));
        String end = phone.substring(Math.max(phone.length() - 2, 0));
        return start + "***" + end;
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

        // При лимите лучше полностью отключить resend
        setResendEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) countDownTimer.cancel();
    }
}
