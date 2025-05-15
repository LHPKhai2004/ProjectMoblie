package com.example.apphoctienganh.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.apphoctienganh.R;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;

public class ListeningActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private Button btnStart, btnStop, btnNext, btnOut;
    private TextView tvStart, tvEnd;
    private SeekBar seekBar;
    private ImageView ivImage;
    private RadioButton rbA, rbB, rbC, rbD;
    private int count = 0, check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listening);
        initViews();
        initMediaPlayer();
        setupListeners();
    }

    private void initViews() {
        btnStart = findViewById(R.id.start);
        btnStop = findViewById(R.id.stop);
        tvStart = findViewById(R.id.textViewStart);
        tvEnd = findViewById(R.id.textViewEnd);
        seekBar = findViewById(R.id.seekBar);
        btnNext = findViewById(R.id.next);
        btnOut = findViewById(R.id.out);
        ivImage = findViewById(R.id.image);
        rbA = findViewById(R.id.aRadioButton);
        rbB = findViewById(R.id.bRadioButton);
        rbC = findViewById(R.id.cRadioButton);
        rbD = findViewById(R.id.dRadioButton);
        Picasso.with(this).load("https://study4.com/media/tez_media/img/ets_toeic_2018_test_3_ets_toeic_2018_test_3_1.png").into(ivImage);
    }

    private void initMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.ets_toeic_2018_test_3_1);
    }

    private void setupListeners() {
        btnStart.setOnClickListener(v -> {
            mediaPlayer.start();
            btnStart.setVisibility(View.GONE);
            btnStop.setVisibility(View.VISIBLE);
            setTimeTotal();
            updateTime();
        });

        btnStop.setOnClickListener(v -> {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnStart.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.GONE);
            }
        });

        btnNext.setOnClickListener(v -> {
            if (mediaPlayer != null) mediaPlayer.release();
            Picasso.with(this).load("https://study4.com/media/tez_media/img/ets_toeic_2018_test_3_ets_toeic_2018_test_3_3.png").into(ivImage);
            mediaPlayer = MediaPlayer.create(this, R.raw.ets_toeic_2018_test_3_3);
            setTimeTotal();
            count++;
            check++;
            if (count >= 2) {
                Toast.makeText(this, "Bộ câu hỏi đã hết, vui lòng đợi cập nhật", Toast.LENGTH_SHORT).show();
            }
            btnStart.setVisibility(View.VISIBLE);
            btnStop.setVisibility(View.GONE);
        });

        btnOut.setOnClickListener(v -> startActivity(new Intent(this, LayoutActivity.class)));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {}
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        RadioGroup rgAnswers = findViewById(R.id.answerRadioGroup);
        rgAnswers.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.aRadioButton && check == 0) {
                Toast.makeText(this, "Đáp án đúng", Toast.LENGTH_SHORT).show();
            } else if (checkedId == R.id.cRadioButton && check > 0) {
                Toast.makeText(this, "Đáp án đúng", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Đáp án sai", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setTimeTotal() {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        tvEnd.setText(sdf.format(mediaPlayer.getDuration()));
        seekBar.setMax(mediaPlayer.getDuration());
    }

    private void updateTime() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
                tvStart.setText(sdf.format(mediaPlayer.getCurrentPosition()));
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 500);
            }
        }, 100);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}