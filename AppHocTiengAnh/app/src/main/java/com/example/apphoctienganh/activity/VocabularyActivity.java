package com.example.apphoctienganh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.apphoctienganh.R;
import com.example.apphoctienganh.model.Vocabulary;
import com.example.apphoctienganh.adapter.VocabularyAdapter;
import com.example.apphoctienganh.database.VocabularySqlite;

import java.util.List;

public class VocabularyActivity extends AppCompatActivity {
    private ListView listView;
    private List<Vocabulary> list;
    private VocabularyAdapter adapter;
    private VocabularySqlite vocabularySqlite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vocabulary);
        listView = findViewById(R.id.listView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        String topic = intent.getStringExtra("topic");
        toolbar.setTitle("Chủ đề: " + topic);
        vocabularySqlite = new VocabularySqlite(VocabularyActivity.this);
        if (topic != null) {
            list = vocabularySqlite.getVocabulariesByTopic(topic);
            adapter = new VocabularyAdapter(VocabularyActivity.this, list);
            listView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Topic is null", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(VocabularyActivity.this,TopicActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}