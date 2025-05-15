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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        textVocabulary.setText("Từ vựng: " + (vocabulary.getWord() != null ? vocabulary.getWord() : "N/A"));
        textVocabularyAnswer.setText("Nghĩa: " + (vocabulary.getAnswer() != null ? vocabulary.getAnswer() : "N/A"));

        //Thêm ảnh ở đây
        Map<String, Integer> imageMap = new HashMap<>();
        imageMap.put("soccer", R.drawable.soccer);
        imageMap.put("badminton", R.drawable.badminton);
        imageMap.put("volleyball", R.drawable.volleyball);
        imageMap.put("tennis", R.drawable.tennis);
        imageMap.put("basketball", R.drawable.basketball);
        imageMap.put("programmer", R.drawable.it);
        imageMap.put("architect", R.drawable.architect);
        imageMap.put("chef", R.drawable.chef);
        imageMap.put("teacher", R.drawable.teacher);
        imageMap.put("bartender", R.drawable.bartender);
        imageMap.put("lion", R.drawable.lion);
        imageMap.put("tiger", R.drawable.tiger);
        imageMap.put("horse", R.drawable.horse);
        imageMap.put("cat", R.drawable.cat);
        imageMap.put("dog", R.drawable.dog);
        imageMap.put("pollution", R.drawable.pollution);
        imageMap.put("forest", R.drawable.forest);
        imageMap.put("water", R.drawable.water);
        imageMap.put("soil", R.drawable.soil);
        imageMap.put("emissions", R.drawable.emissions);

        String word = vocabulary.getImage();
        if (word != null && !word.isEmpty() && imageMap.containsKey(word.toLowerCase())) {
            image.setImageResource(imageMap.get(word.toLowerCase()));
        } else {
            image.setImageResource(R.drawable.placeholder_image);
        }

        return itemView;
    }
}