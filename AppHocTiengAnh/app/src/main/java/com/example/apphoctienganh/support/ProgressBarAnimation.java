package com.example.apphoctienganh.support;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProgressBarAnimation extends Animation {
    private final ProgressBar progressBar;
    private final TextView textView;
    private final float from;
    private final float to;

    public ProgressBarAnimation(ProgressBar progressBar, TextView textView, float from, float to) {
        this.progressBar = progressBar;
        this.textView = textView;
        this.from = from;
        this.to = to;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int value = (int) (from + (to - from) * interpolatedTime);
        progressBar.setProgress(value);
        textView.setText(value + "%");
    }
}
