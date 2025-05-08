package com.example.apphoctienganh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.apphoctienganh.R;
import com.example.apphoctienganh.adapter.TopicAdapter;
import com.example.apphoctienganh.model.TopicModel;
import com.example.apphoctienganh.model.Vocabulary;
import com.example.apphoctienganh.database.VocabularySqlite;

import java.util.ArrayList;
import java.util.List;

public class TopicActivity extends AppCompatActivity {
    private ListView listView;
    private TopicAdapter adapter;
    private List<TopicModel> list;
    private VocabularySqlite vocabularySqlite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        listView = findViewById(R.id.listview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        list = new ArrayList<>();
        vocabularySqlite = new VocabularySqlite(TopicActivity.this);
        vocabularySqlite.addVocabulary(new Vocabulary("Environment","https://png.pngtree.com/element_our/20190531/ourmid/pngtree-free-cartoon-green-image_1321755.jpg","Môi trường","Môi trường"));
        vocabularySqlite.addVocabulary(new Vocabulary("Pollution","https://cdn-icons-png.flaticon.com/512/2640/2640454.png","Ô nhiễm","Môi trường"));
        vocabularySqlite.addVocabulary(new Vocabulary("Recycle","https://cdn-icons-png.flaticon.com/512/7296/7296308.png","Tái chế","Môi trường"));
        vocabularySqlite.addVocabulary(new Vocabulary("Renewable Energy","https://cdn-icons-png.flaticon.com/512/4515/4515708.png","Năng lượng tái tạo","Môi trường"));
        vocabularySqlite.addVocabulary(new Vocabulary("Sustainability","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR8sZyxRVHjOrJt6iEFqhRTxwRS4E5wxshVwefE6kWtYQ&s","Bền vững","Môi trường"));

        vocabularySqlite.addVocabulary(new Vocabulary("School", "https://cdn-icons-png.freepik.com/256/8074/8074788.png?semt=ais_hybrid", "Trường học", "Giáo dục"));
        vocabularySqlite.addVocabulary(new Vocabulary("Teacher", "https://cdn-icons-png.flaticon.com/512/8065/8065183.png", "Giáo viên", "Giáo dục"));
        vocabularySqlite.addVocabulary(new Vocabulary("Student", "https://cdn-icons-png.flaticon.com/512/5850/5850276.png", "Học sinh", "Giáo dục"));
        vocabularySqlite.addVocabulary(new Vocabulary("Book", "https://cdn-icons-png.flaticon.com/512/2232/2232688.png", "Sách", "Giáo dục"));
        vocabularySqlite.addVocabulary(new Vocabulary("Study", "https://cdn-icons-png.freepik.com/256/8263/8263115.png?semt=ais_hybrid", "Học", "Giáo dục"));



        vocabularySqlite.addVocabulary(new Vocabulary("Football", "https://cdn-icons-png.flaticon.com/512/1099/1099672.png", "Bóng đá", "Thể thao"));
        vocabularySqlite.addVocabulary(new Vocabulary("Basketball", "https://cdn-icons-png.flaticon.com/512/889/889455.png", "Bóng rổ", "Thể thao"));
        vocabularySqlite.addVocabulary(new Vocabulary("Swimming", "https://cdn-icons-png.flaticon.com/512/186/186192.png", "Bơi lội", "Thể thao"));
        vocabularySqlite.addVocabulary(new Vocabulary("Tennis", "https://cdn-icons-png.flaticon.com/512/2633/2633933.png", "Quần vợt", "Thể thao"));
        vocabularySqlite.addVocabulary(new Vocabulary("Cycling", "https://cdn-icons-png.freepik.com/512/6669/6669767.png", "Đua xe đạp", "Thể thao"));
        list.add(new TopicModel(R.drawable.sport,"Thể thao"));
        list.add(new TopicModel(R.drawable.education,"Giáo dục"));
        list.add(new TopicModel(R.drawable.enviroment,"Môi trường"));
        adapter = new TopicAdapter(TopicActivity.this,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    Intent intent = new Intent(TopicActivity.this, VocabularyActivity.class);
                    intent.putExtra("topic", "Thể thao");
                    startActivity(intent);
                } else if (i == 1) {
                    Intent intent = new Intent(TopicActivity.this, VocabularyActivity.class);
                    intent.putExtra("topic", "Giáo dục");
                    startActivity(intent);
                } else if (i == 2) {
                    Intent intent = new Intent(TopicActivity.this, VocabularyActivity.class);
                    intent.putExtra("topic", "Môi trường");
                    startActivity(intent);
                }
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(TopicActivity.this, LayoutActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}