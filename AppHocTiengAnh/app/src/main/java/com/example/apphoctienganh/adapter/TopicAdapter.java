package com.example.apphoctienganh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apphoctienganh.R;
import com.example.apphoctienganh.model.TopicModel;

import java.util.ArrayList;
import java.util.List;

public class TopicAdapter extends BaseAdapter {
    private List<TopicModel> list;
    private Context context;

    public TopicAdapter(Context context, List<TopicModel> list) {
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

        TopicModel topicModel = list.get(position);
        // Safely set topic data
        textTopic.setText("Chủ đề: " + (topicModel.getTopic() != null ? topicModel.getTopic() : "N/A"));

        // Set image resource with fallback
        int imageResId = topicModel.getImageView() != 0 ? topicModel.getImageView() : R.drawable.default_topic;
        image.setImageResource(imageResId);

        return itemView;
    }
}