package com.example.apphoctienganh.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.apphoctienganh.R;
import com.example.apphoctienganh.api.ApiService;
import com.example.apphoctienganh.api.RetrofitClient;
import com.example.apphoctienganh.model.Question;
import com.example.apphoctienganh.model.QuestionListResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MultipleChoiceActivity extends AppCompatActivity {
    private TextView tvQuestion, tvTimer, tvScore, tvQuestionCount;
    private RadioGroup rgOptions;
    private RadioButton rbOption1, rbOption2, rbOption3, rbOption4;
    private Button btnSubmit, btnNext;
    private List<Question> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0, score = 0;
    private SharedPreferences prefs;
    private CountDownTimer countDownTimer;
    private static final String PREF_NAME = "UserPrefs", KEY_TOKEN = "token";
    private static final long TIME_PER_QUESTION_MS = 30_000; // 30 seconds per question

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        initViews();
        fetchQuestions();
        btnSubmit.setOnClickListener(v -> submitAnswer());
        btnNext.setOnClickListener(v -> showNextQuestion());
    }

    private void initViews() {
        tvQuestion = findViewById(R.id.tv_question);
        tvTimer = findViewById(R.id.tv_timer); // Add this ID to the timer TextView in XML
        tvScore = findViewById(R.id.tv_score); // Add this ID to the score TextView in XML
        tvQuestionCount = findViewById(R.id.tv_question_count); // Add this ID to the question count TextView in XML
        rgOptions = findViewById(R.id.rg_options);
        rbOption1 = findViewById(R.id.rb_option1);
        rbOption2 = findViewById(R.id.rb_option2);
        rbOption3 = findViewById(R.id.rb_option3);
        rbOption4 = findViewById(R.id.rb_option4);
        btnSubmit = findViewById(R.id.btn_submit);
        btnNext = findViewById(R.id.btn_next);
    }

    private void fetchQuestions() {
        String token = prefs.getString(KEY_TOKEN, "");
        if (token.isEmpty()) {
            Toast.makeText(this, "Vui lòng đăng nhập để tiếp tục", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }
        ApiService apiService = RetrofitClient.getApiService();
        apiService.getQuestionList("Bearer " + token).enqueue(new Callback<QuestionListResponse>() {
            @Override
            public void onResponse(Call<QuestionListResponse> call, Response<QuestionListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isResult()) {
                    questionList = response.body().getData().getContent();
                    if (!questionList.isEmpty()) {
                        startTimer(questionList.size() * TIME_PER_QUESTION_MS);
                        displayQuestion();
                    } else {
                        Toast.makeText(MultipleChoiceActivity.this, "Không có câu hỏi nào", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else {
                    Toast.makeText(MultipleChoiceActivity.this, "Tải câu hỏi thất bại", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onFailure(Call<QuestionListResponse> call, Throwable t) {
                Toast.makeText(MultipleChoiceActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void startTimer(long totalTimeMs) {
        countDownTimer = new CountDownTimer(totalTimeMs, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long minutes = millisUntilFinished / 1000 / 60;
                long seconds = (millisUntilFinished / 1000) % 60;
                tvTimer.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                Toast.makeText(MultipleChoiceActivity.this, "Hết thời gian!", Toast.LENGTH_SHORT).show();
                endQuiz();
            }
        }.start();
    }

    private void displayQuestion() {
        if (currentQuestionIndex < questionList.size()) {
            Question question = questionList.get(currentQuestionIndex);
            tvQuestion.setText(question.getQuestion());
            String[] options = question.getAllChoice().split(" ");
            if (options.length >= 4) {
                rbOption1.setText(options[0]);
                rbOption2.setText(options[1]);
                rbOption3.setText(options[2]);
                rbOption4.setText(options[3]);
            }
            rgOptions.clearCheck();
            btnSubmit.setEnabled(true);
            btnNext.setEnabled(false);
            tvScore.setText("Score: " + score);
            tvQuestionCount.setText("Question: " + (currentQuestionIndex + 1) + "/" + questionList.size());
        } else {
            endQuiz();
        }
    }

    private void submitAnswer() {
        int selectedId = rgOptions.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Vui lòng chọn đáp án", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton selectedOption = findViewById(selectedId);
        String selectedAnswer = selectedOption.getText().toString();
        Question currentQuestion = questionList.get(currentQuestionIndex);
        if (selectedAnswer.equals(currentQuestion.getAnswer())) {
            score++;
            Toast.makeText(this, "Đúng!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sai! Đáp án đúng: " + currentQuestion.getAnswer(), Toast.LENGTH_SHORT).show();
        }
        btnSubmit.setEnabled(false);
        btnNext.setEnabled(true);
    }

    private void showNextQuestion() {
        currentQuestionIndex++;
        displayQuestion();
    }

    private void endQuiz() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("TOTAL_QUESTIONS", questionList.size());
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        startActivity(new Intent(this, LayoutActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}