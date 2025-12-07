package com.example.myapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    Button russianBtn;
    Button uzbekBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);


        uzbekBtn = findViewById(R.id.btnUzbek);

        uzbekBtn.setOnClickListener(v -> {
            var intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        russianBtn = findViewById(R.id.btnRussian);

        russianBtn.setOnClickListener(v -> {
            var intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }


}