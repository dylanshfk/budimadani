package com.example.budimadani;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // 1. Initialize your Back Button using the ID from your XML
        Button backButton = findViewById(R.id.BackButton);

        // 2. Set the click listener to handle the navigation
        backButton.setOnClickListener(v -> {
            // finish() closes this activity and automatically safely reveals MainActivity
            finish();
        });
    }
}