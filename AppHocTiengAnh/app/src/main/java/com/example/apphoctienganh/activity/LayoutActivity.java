package com.example.apphoctienganh.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.apphoctienganh.R;

public class LayoutActivity extends AppCompatActivity {
    private TextView tvUsername;
    private ImageView imgQuestionQuick, imgLeaderBoard, imgVocabulary, imgGrammar, imgListening;
    private SharedPreferences prefs;
    private static final String PREF_NAME = "UserPrefs", KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout);
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        initViews();
        tvUsername.setText(prefs.getString(KEY_USERNAME, "Khách"));
        setupClickListeners();
    }

    private void initViews() {
        tvUsername = findViewById(R.id.username);
        imgQuestionQuick = findViewById(R.id.questionQuick);
        imgLeaderBoard = findViewById(R.id.leaderBoard);
        imgVocabulary = findViewById(R.id.image_vocabulary);
        imgListening = findViewById(R.id.listening);
    }

    private void setupClickListeners() {
        imgQuestionQuick.setOnClickListener(v -> startActivity(new Intent(this, MultipleChoiceActivity.class)));
        imgLeaderBoard.setOnClickListener(v -> startActivity(new Intent(this, LeaderBoardActivity.class)));
        imgVocabulary.setOnClickListener(v -> startActivity(new Intent(this, TopicActivity.class)));
        imgListening.setOnClickListener(v -> startActivity(new Intent(this, ListeningActivity.class)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.logout) {
            prefs.edit().clear().apply();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}