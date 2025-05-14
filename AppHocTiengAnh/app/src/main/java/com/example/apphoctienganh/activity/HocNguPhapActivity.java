package com.example.apphoctienganh.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.apphoctienganh.R;
import com.example.apphoctienganh.database.DataBasePointUser;
import com.example.apphoctienganh.database.DataTenseSqlite;
import com.example.apphoctienganh.model.Question;

import java.util.ArrayList;
import java.util.List;

public class HocNguPhapActivity extends AppCompatActivity {

    private TextView txtScore, txtQuestionCount, txtTime, txtQuestion;
    private Button[] answerButtons;
    private Button btnQuit, btnConfirm;
    private DataTenseSqlite database;
    private DataBasePointUser dataBasePointUser;
    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private CountDownTimer countdownTimer;
    private int score = 0;
    private int total;
    private int currentQuestionIndexSetText;
    private String username;
    private long remainingTimeInMillis;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_USERNAME = "username";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trac_nghiem);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        username = sharedPreferences.getString(KEY_USERNAME, null);
        if (username == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            navigateToLogin();
            return;
        }

        initializeViews();
        initializeDatabase();
        setQuestions();
        displayQuestion();
        setupAnswerButtons();
        startCountdownTimer();
        setupButtons();
    }

    private void initializeViews() {
        txtScore = findViewById(R.id.txtscoreDK);
        txtQuestionCount = findViewById(R.id.txtquestcountDK);
        txtTime = findViewById(R.id.txttimeDK);
        txtQuestion = findViewById(R.id.txtquestionDK);
        answerButtons = new Button[] {
                findViewById(R.id.txtanswer1),
                findViewById(R.id.txtanswer2),
                findViewById(R.id.txtanswer3),
                findViewById(R.id.txtanswer4)
        };
        btnQuit = findViewById(R.id.btnQuitDK);
        btnConfirm = findViewById(R.id.btnconfirmDK);
    }

    private void initializeDatabase() {
        database = new DataTenseSqlite(this);
        dataBasePointUser = new DataBasePointUser(this);
    }

    private void setQuestions() {
        questionList = new ArrayList<>();
        questionList.add(new Question("She __________ (play) tennis every Sunday", "plays", "plays play played playies"));
        questionList.add(new Question("They __________ (visit) their grandparents last week.", "visited", "visit visits visited vista"));
        questionList.add(new Question("I __________ (watch) a movie next weekend.", "watch", "watching watch watches watched"));
        questionList.add(new Question("He __________ (study) Spanish at the moment.", "studying", "studying study studies studied"));
        questionList.add(new Question("We __________ (have) lunch when they arrived.", "had", "has have had having"));
        questionList.add(new Question("I __________ (not finish) my homework yet.", "finish", "finish finished finishes fun"));

        for (Question question : questionList) {
            database.addQuestion(question);
        }
        setQuestionCount();
    }

    private void setQuestionCount() {
        total = questionList.size();
        currentQuestionIndexSetText = currentQuestionIndex + 1;
        txtQuestionCount.setText("Question: " + currentQuestionIndexSetText + "/" + total);
        txtQuestionCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_question, 0);
    }

    private void displayQuestion() {
        Question currentQuestion = questionList.get(currentQuestionIndex);
        String[] choices = currentQuestion.getAllchoice().split(" ");
        for (int i = 0; i < choices.length; i++) {
            answerButtons[i].setText(choices[i]);
        }
        txtQuestion.setText(currentQuestion.getQuestion());
        setQuestionCount();
    }

    private void setupAnswerButtons() {
        for (final Button button : answerButtons) {
            button.setOnClickListener(v -> {
                String answer = button.getText().toString();
                checkAnswer(answer);
                moveToNextQuestion();
            });
        }
    }

    private void checkAnswer(String selectedAnswer) {
        String correctAnswer = questionList.get(currentQuestionIndex).getAnswer();
        String feedback;
        if (selectedAnswer.equals(correctAnswer)) {
            feedback = "Đáp án đúng";
            score += 10;
            txtScore.setText("Score: " + score);
        } else {
            feedback = "Đáp án sai";
        }
        Toast.makeText(this, feedback, Toast.LENGTH_SHORT).show();
    }

    private void moveToNextQuestion() {
        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            displayQuestion();
        } else {
            Toast.makeText(this, "Bạn đã hoàn thành tất cả câu hỏi", Toast.LENGTH_SHORT).show();
            for (Button button : answerButtons) {
                button.setEnabled(false);
            }
        }
    }

    private void startCountdownTimer() {
        countdownTimer = new CountDownTimer(600000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 60000;
                long seconds = (millisUntilFinished % 60000) / 1000;
                txtTime.setText(String.format("%02d:%02d", minutes, seconds));
                remainingTimeInMillis = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                Toast.makeText(HocNguPhapActivity.this, "Hết thời gian!", Toast.LENGTH_SHORT).show();
                for (Button button : answerButtons) {
                    button.setEnabled(false);
                }
            }
        };
        countdownTimer.start();
    }

    private void setupButtons() {
        btnConfirm.setOnClickListener(v -> {
            if (currentQuestionIndex < questionList.size()) {
                Toast.makeText(this, "Bạn chưa hoàn thành tất cả câu hỏi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Điểm của bạn: " + score, Toast.LENGTH_SHORT).show();
                long totalSeconds = remainingTimeInMillis / 1000;
                long minutes = totalSeconds / 60;
                long seconds = totalSeconds % 60;
                String time = String.format("%02d:%02d", minutes, seconds);
                dataBasePointUser.addPoints(username, score, time);
                navigateToLayout();
            }
        });

        btnQuit.setOnClickListener(v -> {
            countdownTimer.cancel();
            navigateToLayout();
        });
    }

    private void navigateToLayout() {
        Intent intent = new Intent(HocNguPhapActivity.this, LayoutActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(HocNguPhapActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countdownTimer != null) {
            countdownTimer.cancel();
        }
    }
}