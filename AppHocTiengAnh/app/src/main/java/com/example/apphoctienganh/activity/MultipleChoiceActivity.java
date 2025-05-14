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
import com.example.apphoctienganh.database.GramarEnglishApi;
import com.example.apphoctienganh.database.PointUserApi;
import com.example.apphoctienganh.model.ApiResponse;
import com.example.apphoctienganh.model.Question;
import com.example.apphoctienganh.model.QuestionListResponse;
import com.example.apphoctienganh.model.UserPoint;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MultipleChoiceActivity extends AppCompatActivity {
    private TextView txtScore, txtQuestionCount, txtTime, txtQuestion;
    private Button[] answerButtons;
    private Button btnQuit, btnConfirm;
    private GramarEnglishApi database;
    private PointUserApi pointUserApi;
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
    private static final String KEY_TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trac_nghiem);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        username = sharedPreferences.getString(KEY_USERNAME, null);
        String token = sharedPreferences.getString(KEY_TOKEN, null);
        if (username == null || token == null) {
            Toast.makeText(this, "Không tìm thấy thông tin người dùng. Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            navigateToLogin();
            return;
        }

        initializeViews();
        initializeDatabase();
        setQuestions();
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
        database = new GramarEnglishApi(this);
        pointUserApi = new PointUserApi(this);
    }

    private void setQuestions() {
        questionList = new ArrayList<>();
        database.getAllQuestions(new Callback<QuestionListResponse>() {
            @Override
            public void onResponse(Call<QuestionListResponse> call, Response<QuestionListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isResult()) {
                    questionList = response.body().getData().getContent();
                    if (questionList != null && !questionList.isEmpty()) {
                        setQuestionCount();
                        displayQuestion();
                    } else {
                        Toast.makeText(MultipleChoiceActivity.this, "Không có câu hỏi nào.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MultipleChoiceActivity.this, "Không thể tải câu hỏi.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<QuestionListResponse> call, Throwable t) {
                Toast.makeText(MultipleChoiceActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setQuestionCount() {
        total = questionList.size();
        currentQuestionIndexSetText = currentQuestionIndex + 1;
        txtQuestionCount.setText("Question: " + currentQuestionIndexSetText + "/" + total);
        txtQuestionCount.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_question, 0);
    }

    private void displayQuestion() {
        if (questionList == null || questionList.isEmpty()) return;
        Question currentQuestion = questionList.get(currentQuestionIndex);
        String[] choices = currentQuestion.getAllchoice().split(" ");
        for (int i = 0; i < choices.length && i < answerButtons.length; i++) {
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
                Toast.makeText(MultipleChoiceActivity.this, "Hết thời gian!", Toast.LENGTH_SHORT).show();
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
                UserPoint.Account account = new UserPoint.Account();
                account.setUsername(username);

                UserPoint.User user = new UserPoint.User();
                user.setAccount(account);

                UserPoint userPoint = new UserPoint();
                userPoint.setUser(user);
                userPoint.setPoint(score);
                userPoint.setTime(time);
                pointUserApi.addPoints(userPoint, new Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        if (response.isSuccessful() && response.body().isResult()) {
                            navigateToLayout();
                        } else {
                            Toast.makeText(MultipleChoiceActivity.this, "Không thể lưu điểm.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        Toast.makeText(MultipleChoiceActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnQuit.setOnClickListener(v -> {
            countdownTimer.cancel();
            navigateToLayout();
        });
    }

    private void navigateToLayout() {
        Intent intent = new Intent(MultipleChoiceActivity.this, LayoutActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(MultipleChoiceActivity.this, LoginActivity.class);
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