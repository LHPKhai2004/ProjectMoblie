package com.example.apphoctienganh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apphoctienganh.R;
import com.example.apphoctienganh.model.Vocabulary;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class VocabularyAdapter extends BaseAdapter {
    private List<Vocabulary> list;
    private Context context;

    public VocabularyAdapter(Context context, List<Vocabulary> list) {
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
            itemView = inflater.inflate(R.layout.layout_custom_vocabulary, parent, false);
        } else {
            itemView = convertView;
        }

        TextView textVocabulary = itemView.findViewById(R.id.text_Vocabulary);
        TextView textVocabularyAnswer = itemView.findViewById(R.id.text_Answer);
        ImageView image = itemView.findViewById(R.id.image_vocabulary);

        Vocabulary vocabulary = list.get(position);
        // Safely set vocabulary data
        textVocabulary.setText("Từ vựng: " + (vocabulary.getWords() != null ? vocabulary.getWords() : "N/A"));
        textVocabularyAnswer.setText("Đáp án: " + (vocabulary.getAnswer() != null ? vocabulary.getAnswer() : "N/A"));

        // Load image with Picasso
        if (vocabulary.getImage() != null && !vocabulary.getImage().isEmpty()) {
            Picasso.with(context)
                    .load(vocabulary.getImage())
                    .placeholder(R.drawable.placeholder_image) // Add a placeholder image in res/drawable
                    .error(R.drawable.error_image) // Add an error image in res/drawable
                    .into(image);
        } else {
            image.setImageResource(R.drawable.placeholder_image);
        }

        return itemView;
    }
}