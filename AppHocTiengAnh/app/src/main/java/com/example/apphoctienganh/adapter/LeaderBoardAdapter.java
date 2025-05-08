package com.example.apphoctienganh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apphoctienganh.R;
import com.example.apphoctienganh.model.UserPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderBoardAdapter extends BaseAdapter {
    private List<UserPoint> list = new ArrayList<>();
    private Context context;

    public LeaderBoardAdapter(Context context, List<UserPoint> list) {
        this.context = context;
        this.list = list;
        Collections.sort(list, new Comparator<UserPoint>() {
            @Override
            public int compare(UserPoint user1, UserPoint user2) {
                int compareResult = Integer.compare(user2.getPoints(), user1.getPoints());
                if (compareResult == 0) {
                    int time1 = convertTimeStringToInteger(user1.getTime());
                    int time2 = convertTimeStringToInteger(user2.getTime());
                    return Integer.compare(time2, time1);
                }
                return compareResult;
            }
        });
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.leaderboardlayout, parent, false);
        TextView txtUsername = itemView.findViewById(R.id.text_username);
        TextView txtScore = itemView.findViewById(R.id.text_score);
        TextView txtTime = itemView.findViewById(R.id.text_time);
        ImageView rank = itemView.findViewById(R.id.image_leaderboard);

        switch (position) {
            case 0:
                rank.setImageResource(R.drawable.top1);
                break;
            case 1:
                rank.setImageResource(R.drawable.top2);
                break;
            case 2:
                rank.setImageResource(R.drawable.top3);
                break;
            case 3:
                rank.setImageResource(R.drawable.top4);
                break;
            case 4:
                rank.setImageResource(R.drawable.top5);
                break;
            default:
                rank.setImageResource(R.drawable.default1);
                break;
        }

        UserPoint userPoint = list.get(position);
        txtUsername.setText(userPoint.getUser());
        txtScore.setText("Điểm số: " + userPoint.getPoints());
        String[] timeParts = userPoint.getTime().split(":");
        String formattedTime = String.format("%02d:%02d", Integer.parseInt(timeParts[0]), Integer.parseInt(timeParts[1]));
        txtTime.setText("Thời gian: " + formattedTime);

        return itemView;
    }
    private int convertTimeStringToInteger(String timeString) {
        String[] parts = timeString.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        return minutes * 60 + seconds; // Chuyển đổi thành số giây
    }
}
