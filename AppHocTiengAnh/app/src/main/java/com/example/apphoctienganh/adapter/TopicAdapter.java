package com.example.apphoctienganh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apphoctienganh.R;
import com.example.apphoctienganh.model.Topic;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends BaseAdapter {
    private List<Topic> list;
    private Context context;

    public TopicAdapter(Context context, List<Topic> list) {
        this.context = context;
        this.list = new ArrayList<>(list); // Create a copy to avoid modifying the original list
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
            itemView = inflater.inflate(R.layout.layout_topic, parent, false);
        } else {
            itemView = convertView;
        }

        TextView textTopic = itemView.findViewById(R.id.text_Topic);
        ImageView image = itemView.findViewById(R.id.image_vocabulary);

        Topic topic = list.get(position);
        // Safely set topic data
        textTopic.setText("Topic: " + (topic.getTopic() != null ? topic.getTopic() : "N/A"));

        // Set image resource based on topic name (since imageView from API is a string)
        int imageResId;
        switch (topic.getTopic() != null ? topic.getTopic() : "") {
            case "Sport":
                imageResId = R.drawable.sport;
                break;
            case "Enviroment":
                imageResId = R.drawable.enviroment;
                break;
            default:
                imageResId = R.drawable.default_topic;
                break;
        }
        image.setImageResource(imageResId);

        return itemView;
    }
}