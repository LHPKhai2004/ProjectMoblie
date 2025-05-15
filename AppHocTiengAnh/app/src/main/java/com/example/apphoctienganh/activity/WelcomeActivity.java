package com.example.apphoctienganh.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.apphoctienganh.R;
import com.example.apphoctienganh.support.ProgressBarAnimation;

public class WelcomeActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView tvProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        progressBar = findViewById(R.id.progressBar);
        tvProgress = findViewById(R.id.txt_progress);
        startProgressAnimation();
    }

    private void startProgressAnimation() {
        ProgressBarAnimation anim = new ProgressBarAnimation(progressBar, tvProgress, 0f, 100f);
        anim.setDuration(3000);
        progressBar.setAnimation(anim);
        new LoadDataTask().execute();
    }

    private class LoadDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        }
    }
}