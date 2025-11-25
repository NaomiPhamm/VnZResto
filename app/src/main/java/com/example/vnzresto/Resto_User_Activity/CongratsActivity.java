package com.example.vnzresto.Resto_User_Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vnzresto.R;

public class CongratsActivity extends AppCompatActivity {

    private Button goHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrats);

        goHome = findViewById(R.id.goHome);

        goHome.setOnClickListener(v -> {
            Intent intent = new Intent(CongratsActivity.this, UserDashBoard.class);
            startActivity(intent);
            finish();
        });
    }
}
