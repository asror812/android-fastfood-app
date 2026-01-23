package com.example.myapplication.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;
import com.example.myapplication.api.ApiClient;
import com.example.myapplication.api.AuthApi;
import com.example.myapplication.api.dto.request.ValidatePhoneNumberDto;
import com.example.myapplication.api.dto.response.ApiMessageResponse;
import com.example.myapplication.api.dto.response.ErrorResponse;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etPhone, etName, etBirthDate, etPassword;
    private Spinner spinnerRegion;
    private CheckBox cbAgree;
    private Button btnConfirm, btnLogin;

    private AuthApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        api = ApiClient.getClient(this).create(AuthApi.class);

        initViews();
        initRegionSpinner();
        initListeners();
    }

    private void initViews() {
        etPhone = findViewById(R.id.etPhone);
        etName = findViewById(R.id.etName);
        etBirthDate = findViewById(R.id.etBirthDate);
        etPassword = findViewById(R.id.etPassword);

        spinnerRegion = findViewById(R.id.spinnerRegion);
        cbAgree = findViewById(R.id.cbAgree);

        btnConfirm = findViewById(R.id.btnConfirm);
        btnLogin = findViewById(R.id.btnLogin);

        etBirthDate.setFocusable(false);
        etBirthDate.setClickable(true);
    }

    private void initListeners() {
        etBirthDate.setOnClickListener(v -> showDatePickerDialog(etBirthDate));

        btnConfirm.setOnClickListener(v -> onConfirmClick());

        btnLogin.setOnClickListener(v -> startActivity(new Intent(this, LoginActivity.class)));
    }

    private void onConfirmClick() {
        String phone = etPhone.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String birthDate = etBirthDate.getText().toString().trim();
        String region = String.valueOf(spinnerRegion.getSelectedItem());
        String password = etPassword.getText().toString().trim();

        if (!validateInput(phone, name, birthDate, region, password)) return;

        requestOtp(phone, name, birthDate, region, password);
    }

    private boolean validateInput(String phone, String name, String birthDate, String region, String password) {
        etPhone.setError(null);
        etName.setError(null);
        etBirthDate.setError(null);
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

        if (TextUtils.isEmpty(name)) {
            etName.setError("Ismingizni kiriting");
            etName.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(birthDate)) {
            etBirthDate.setError("Tug‘ilgan sanani kiriting");
            etBirthDate.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(password) || password.length() < 8) {
            etPassword.setError("Parol kamida 8 ta belgidan iborat bo‘lishi kerak");
            etPassword.requestFocus();
            return false;
        }

        if ("Viloyatni tanlang".equals(region)) {
            toast("Iltimos, viloyatni tanlang");
            return false;
        }

        if (!cbAgree.isChecked()) {
            toast("Shartlarga rozilik belgilang");
            return false;
        }

        return true;
    }

    private void requestOtp(String phone, String name, String birthDate, String region, String password) {
        api.validatePhoneNumber(new ValidatePhoneNumberDto(phone, null)).enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ApiMessageResponse> call, @NonNull Response<ApiMessageResponse> response) {
                if (response.code() == 429) {
                    handleOtpLimit(response);
                    return;
                }

                if (!response.isSuccessful()) {
                    toast("Server xatosi: " + response.code());
                    Log.e("API", "HTTP error: " + response.code());
                    return;
                }

                ApiMessageResponse body = response.body();
                if (body == null) {
                    toast("Server xatosi: body null");
                    return;
                }

                toast("Kod yuborildi");
                openVerifyScreen(phone, name, birthDate, region, password);
            }

            @Override
            public void onFailure(@NonNull Call<ApiMessageResponse> call, @NonNull Throwable t) {
                toast("Internet xatosi: " + t.getMessage());
                Log.e("API", "Network error: " + t.getMessage());
            }
        });
    }

    private void openVerifyScreen(String phone, String name, String birthDate, String region, String password) {
        Intent i = new Intent(this, VerifyCodeActivity.class);
        i.putExtra("user_phone", phone);
        i.putExtra("user_name", name);
        i.putExtra("user_birthDate", birthDate);
        i.putExtra("user_region", region);
        i.putExtra("user_password", password);
        startActivity(i);
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void handleOtpLimit(Response<ApiMessageResponse> response) {
        try {
            Gson gson = new Gson();
            assert response.errorBody() != null;
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

    private void showDatePickerDialog(EditText editText) {
        final Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePicker = new DatePickerDialog(this, (view, year, month, day) -> editText.setText(String.format("%02d.%02d.%04d", day, month + 1, year)), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePicker.show();
    }

    private void initRegionSpinner() {
        List<String> regions = Arrays.asList("Viloyatni tanlang", "Toshkent shahri", "Toshkent viloyati", "Samarqand", "Farg‘ona", "Andijon", "Namangan", "Buxoro", "Xorazm", "Qashqadaryo", "Surxondaryo", "Jizzax", "Sirdaryo", "Navoiy", "Qoraqalpog‘iston");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, regions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRegion.setAdapter(adapter);
    }
}
