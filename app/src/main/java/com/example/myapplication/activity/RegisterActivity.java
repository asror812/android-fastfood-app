package com.example.myapplication.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.AuthApi;
import com.example.myapplication.api.dto.request.ValidatePhoneNumberDto;
import com.example.myapplication.api.dto.response.ApiMessageResponse;
import com.example.myapplication.api.dto.response.ErrorResponse;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etPhone, etName, etBirthDate, etPassword;
    private Spinner spinnerRegion;
    private CheckBox cbAgree;
    private Button btnConfirm;
    private Button btnlogin;

    private AuthApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        setUp();
        setupRegionSpinner();
        api = ApiClient.getClient(this).create(AuthApi.class);

        etBirthDate.setOnClickListener(v -> showDatePickerDialog(etBirthDate));

        btnConfirm.setOnClickListener(v -> submitRegistration());
        btnlogin.setOnClickListener(v -> redirectToLoginPage());
    }

    private void redirectToLoginPage() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void setUp() {
        etPhone = findViewById(R.id.etPhone);
        etName = findViewById(R.id.etName);
        etBirthDate = findViewById(R.id.etBirthDate);
        etPassword = findViewById(R.id.etPassword);
        spinnerRegion = findViewById(R.id.spinnerRegion);
        cbAgree = findViewById(R.id.cbAgree);

        btnConfirm = findViewById(R.id.btnConfirm);
        btnlogin = findViewById(R.id.btnLogin);

        etBirthDate.setFocusable(false);
        etBirthDate.setClickable(true);
    }

    private void submitRegistration() {

        String phone = etPhone.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();
        String region = spinnerRegion.getSelectedItem().toString();
        String password = etPassword.getText().toString().trim();

        // Regex: +99 461-41-88
        Pattern phonePattern = Pattern.compile("^\\+\\d{2}\\s?\\d{3}-\\d{2}-\\d{2}$");

        // Validation
        if (TextUtils.isEmpty(phone)) {
            etPhone.setError("Telefon raqamni kiriting");
            etPhone.requestFocus();
            return;
        }
        if (!phonePattern.matcher(phone).matches()) {
            etPhone.setError("Telefon raqam formati xato: +99 461-41-88");
            etPhone.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            etName.setError("Ismingizni kiriting");
            etName.requestFocus();
            return;
        }
        if (password.length() < 8) {
            etPassword.setError("Parol kamida 8 ta belgidan iborat bo‘lishi kerak");
            etPassword.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(birthDate)) {
            etBirthDate.setError("Tug‘ilgan sanani kiriting");
            etBirthDate.requestFocus();
            return;
        }
        if (region.equals("Viloyatni tanlang")) {
            Toast.makeText(this, "Iltimos, viloyatni tanlang", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!cbAgree.isChecked()) {
            Toast.makeText(this, "Shartlarga rozilik belgilang", Toast.LENGTH_SHORT).show();
            return;
        }

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
                            Toast.makeText(RegisterActivity.this, "Server xatosi", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        ApiMessageResponse res = response.body();
                        String msg = res.getMessage();

                        if (msg.equals("Sms was sent successfully")) {
                            Toast.makeText(RegisterActivity.this, "Kod yuborildi", Toast.LENGTH_SHORT).show();
                            openVerifyScreen(phone);

                        } else if (msg.equals("Sms was re-send successfully")) {
                            Toast.makeText(RegisterActivity.this, "Kod qayta yuborildi", Toast.LENGTH_SHORT).show();
                            openVerifyScreen(phone);

                        } else {
                            Toast.makeText(RegisterActivity.this, "Xatolik: " + msg, Toast.LENGTH_SHORT).show();
                            Log.e("API", "Unexpected: " + msg);
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiMessageResponse> call, Throwable t) {
                        Log.e("API", "Error: " + t.getMessage());
                        Toast.makeText(RegisterActivity.this, "Internet xatosi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void openVerifyScreen(String phone) {
        Intent i = new Intent(this, VerifyCodeActivity.class);
        i.putExtra("user_phone", phone);
        startActivity(i);
    }

    private void showDatePickerDialog(EditText editText) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePicker = new DatePickerDialog(
                this,
                (DatePicker view, int year, int month, int day) -> {
                    String formatted = String.format("%02d.%02d.%04d", day, month + 1, year);
                    editText.setText(formatted);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePicker.show();
    }

    private void setupRegionSpinner() {
        List<String> regions = new ArrayList<>(Arrays.asList(
                "Viloyatni tanlang",
                "Toshkent", "Samarqand", "Farg‘ona", "Andijon", "Namangan",
                "Buxoro", "Xorazm", "Qashqadaryo", "Surxondaryo",
                "Jizzax", "Sirdaryo", "Navoiy", "Qoraqalpog‘iston"
        ));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, regions
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(adapter);
    }
}
