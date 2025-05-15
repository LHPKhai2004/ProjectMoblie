package com.example.apphoctienganh.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.apphoctienganh.R;
import com.example.apphoctienganh.adapter.VocabularyAdapter;
import com.example.apphoctienganh.api.ApiService;
import com.example.apphoctienganh.api.RetrofitClient;
import com.example.apphoctienganh.model.ApiResponse;
import com.example.apphoctienganh.model.Vocabulary;
import com.example.apphoctienganh.model.VocabularyListResponse;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VocabularyActivity extends AppCompatActivity {
    private static final String PREF_NAME = "UserPrefs", KEY_TOKEN = "token";
    private ListView lvVocab;
    private List<Vocabulary> vocabList = new ArrayList<>();
    private VocabularyAdapter adapter;
    private ApiService apiService = RetrofitClient.getApiService();
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        String token = prefs.getString(KEY_TOKEN, null);
        if (token == null) {
            Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
            navigateTo(LoginActivity.class);
            return;
        }
        lvVocab = findViewById(R.id.listView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String topicId = intent.getStringExtra("topicId");
        String topicName = intent.getStringExtra("topicName");
        toolbar.setTitle("Chủ đề: " + topicName);
        if (topicId != null && !topicId.trim().isEmpty()) {
            loadVocabularies(token, topicId);
        } else {
            Toast.makeText(this, "Không tìm thấy chủ đề", Toast.LENGTH_SHORT).show();
            navigateTo(TopicActivity.class);
        }
    }

    private void loadVocabularies(String token, String topicId) {
        apiService.getVocabularyList("Bearer " + token, topicId).enqueue(new Callback<VocabularyListResponse>() {
            @Override
            public void onResponse(Call<VocabularyListResponse> call, Response<VocabularyListResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().isResult()) {
                    List<Vocabulary> data = response.body().getData().getContent();
                    if (data != null && !data.isEmpty()) {
                        vocabList = data;
                        adapter = new VocabularyAdapter(VocabularyActivity.this, vocabList);
                        lvVocab.setAdapter(adapter);
                    } else {
                        Toast.makeText(VocabularyActivity.this, "Không có từ vựng nào", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VocabularyActivity.this, "Tải từ vựng thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VocabularyListResponse> call, Throwable t) {
                Toast.makeText(VocabularyActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", "Lỗi: " + t.getMessage(), t);
            }
        });
    }

    public void addVocabulary(Vocabulary vocabulary, Callback<ApiResponse> callback) {
        apiService.createVocabulary(vocabulary).enqueue(callback);
    }

    public void deleteVocabulary(String id, Callback<ApiResponse> callback) {
        apiService.deleteVocabulary(id).enqueue(callback);
    }

    private void navigateTo(Class<?> targetActivity) {
        startActivity(new Intent(this, targetActivity));
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            navigateTo(TopicActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}