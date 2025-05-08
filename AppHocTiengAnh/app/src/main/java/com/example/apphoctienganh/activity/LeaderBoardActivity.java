package com.example.apphoctienganh.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.apphoctienganh.adapter.LeaderBoardAdapter;
import com.example.apphoctienganh.R;
import com.example.apphoctienganh.model.UserPoint;
import com.example.apphoctienganh.database.DataBasePointUser;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity {
    private ListView listView;
    private List<UserPoint> list;
    private LeaderBoardAdapter adapter;
    private DataBasePointUser dataBasePointUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        dataBasePointUser = new DataBasePointUser(LeaderBoardActivity.this);
        mapping();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setLeaderBoard();
    }
    public  void mapping(){
        list = new ArrayList<>();
        listView = findViewById(R.id.listLeaderBoard);
    }
    public void setLeaderBoard(){
        list = dataBasePointUser.getAllUserPoints();
        adapter = new LeaderBoardAdapter(LeaderBoardActivity.this,list);
        listView.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(LeaderBoardActivity.this, LayoutActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}