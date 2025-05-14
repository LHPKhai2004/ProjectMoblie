package com.example.apphoctienganh.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctienganh.R;

public class LayoutActivity extends AppCompatActivity {
    private TextView username;
    private ImageView questionQuick;
    private ImageView leaderBoard;
    private ImageView vocabulary;
    private ImageView grammar;
    private ImageView listening;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        mapping();
        displayUsername();
        setupImageClickListeners();
    }

    private void mapping() {
        username = findViewById(R.id.username);
        questionQuick = findViewById(R.id.questionQuick);
        leaderBoard = findViewById(R.id.leaderBoard);
        vocabulary = findViewById(R.id.image_vocabulary);
        grammar = findViewById(R.id.grammar);
        listening = findViewById(R.id.listening);
    }

    private void displayUsername() {
        // Retrieve username from SharedPreferences
        String savedUsername = sharedPreferences.getString(KEY_USERNAME, null);
        if (savedUsername != null) {
            username.setText(savedUsername);
        } else {
            // Fallback if no username is found (e.g., unexpected state)
            username.setText("Guest");
        }
    }

    private void setupImageClickListeners() {
        questionQuick.setOnClickListener(v -> {
            Intent intent = new Intent(LayoutActivity.this, MultipleChoiceActivity.class);
            startActivity(intent);
            finish();
        });

        leaderBoard.setOnClickListener(v -> {
            Intent intent = new Intent(LayoutActivity.this, LeaderBoardActivity.class);
            startActivity(intent);
            finish();
        });

        vocabulary.setOnClickListener(v -> {
            Intent intent = new Intent(LayoutActivity.this, TopicActivity.class);
            startActivity(intent);
            finish();
        });

        grammar.setOnClickListener(v -> {
            Intent intent = new Intent(LayoutActivity.this, HocNguPhapActivity.class);
            startActivity(intent);
            finish();
        });

        listening.setOnClickListener(v -> {
            Intent intent = new Intent(LayoutActivity.this, ListeningActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            // Clear user data from SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // Clears all data, including username and token
            editor.apply();

            // Navigate to LoginActivity
            Intent intent = new Intent(LayoutActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}