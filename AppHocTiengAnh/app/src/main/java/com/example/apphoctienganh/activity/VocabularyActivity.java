package com.example.apphoctienganh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.apphoctienganh.R;
import com.example.apphoctienganh.adapter.VocabularyAdapter;
import com.example.apphoctienganh.database.VocabularyApi;
import com.example.apphoctienganh.model.Vocabulary;
import com.example.apphoctienganh.model.VocabularyListResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VocabularyActivity extends AppCompatActivity {
    private ListView listView;
    private List<Vocabulary> list;
    private VocabularyAdapter adapter;
    private VocabularyApi vocabularyApi;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "UserPrefs";
    private static final String KEY_TOKEN = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = sharedPreferences.getString(KEY_TOKEN, null);
        if (token == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại.", Toast.LENGTH_LONG).show();
            navigateToLogin();
            return;
        }

        listView = findViewById(R.id.listView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String topicId = intent.getStringExtra("topicId"); // Nhận TopicId
        String topicName = intent.getStringExtra("topicName"); // Nhận tên chủ đề
        toolbar.setTitle("Chủ đề: " + topicName);

        vocabularyApi = new VocabularyApi();
        list = new ArrayList<>();
        if (topicId != null) {
            loadVocabularies(token, topicId); // Sử dụng TopicId để gọi API
        } else {
            Toast.makeText(this, "Không tìm thấy chủ đề.", Toast.LENGTH_SHORT).show();
            navigateToTopicActivity();
        }
    }

    private void loadVocabularies(String token, String topicId) {
        vocabularyApi.getVocabulariesByTopic(token, topicId, new Callback<VocabularyListResponse>() {
            @Override
            public void onResponse(Call<VocabularyListResponse> call, Response<VocabularyListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isResult()) {
                    list = response.body().getData();
                    if (list != null && !list.isEmpty()) {
                        adapter = new VocabularyAdapter(VocabularyActivity.this, list);
                        listView.setAdapter(adapter);
                    } else {
                        Toast.makeText(VocabularyActivity.this, "Không có từ vựng nào cho chủ đề này.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VocabularyActivity.this, "Không thể tải từ vựng.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VocabularyListResponse> call, Throwable t) {
                Toast.makeText(VocabularyActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(VocabularyActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void navigateToTopicActivity() {
        Intent intent = new Intent(VocabularyActivity.this, TopicActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(VocabularyActivity.this, TopicActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}