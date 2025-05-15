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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class LeaderBoardAdapter extends BaseAdapter {
    private List<UserPoint> list;
    private Context context;

    public LeaderBoardAdapter(Context context, List<UserPoint> list) {
        this.context = context;
        this.list = new ArrayList<>(list);
        Collections.sort(this.list, new Comparator<UserPoint>() {
            @Override
            public int compare(UserPoint user1, UserPoint user2) {
                int compareResult = Integer.compare(user2.getPoint(), user1.getPoint());
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
        View itemView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.layout_leaderboard, parent, false);
        } else {
            itemView = convertView;
        }

        TextView txtUsername = itemView.findViewById(R.id.text_username);
        TextView txtScore = itemView.findViewById(R.id.text_score);
        TextView txtTime = itemView.findViewById(R.id.text_time);
        ImageView rank = itemView.findViewById(R.id.image_leaderboard);

        // Set rank icon based on position
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
        // Safely access username from nested User and Account objects
        String username = "Unknown";
        if (userPoint.getUser() != null && userPoint.getUser().getAccount() != null) {
            username = userPoint.getUser().getAccount().getUsername();
        }
        txtUsername.setText(username);
        txtScore.setText("Điểm số: " + userPoint.getPoint());

        // Format time
        String time = userPoint.getTime();
        if (time != null && !time.isEmpty()) {
            try {
                SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = inputFormat.parse(time);
                String formattedTime = outputFormat.format(date);
                txtTime.setText("Ngày làm: " + formattedTime);
            } catch (Exception e) {
                txtTime.setText("Ngày làm: N/A");
            }
        } else {
            txtTime.setText("Ngày làm: N/A");
        }

        return itemView;
    }

    private int convertTimeStringToInteger(String timeString) {
        if (timeString == null || !timeString.contains(":")) {
            return Integer.MAX_VALUE; // Handle invalid time gracefully
        }
        try {
            String[] parts = timeString.split(":");
            int minutes = Integer.parseInt(parts[0]);
            int seconds = Integer.parseInt(parts[1]);
            return minutes * 60 + seconds; // Convert to seconds
        } catch (NumberFormatException e) {
            return Integer.MAX_VALUE; // Handle parsing errors
        }
    }
}