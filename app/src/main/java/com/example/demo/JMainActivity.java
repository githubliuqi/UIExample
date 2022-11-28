package com.example.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo.view.TetrisTestView;

public class JMainActivity extends AppCompatActivity {

    private LinearLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = createView();
        setContentView(rootView);
        addViews();
    }

    private LinearLayout createView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        ll.setBackgroundColor(0xFF13223F);
        return ll;
    }

    private void addViews() {
        add(new TetrisTestView(this));
    }

    private JMainActivity add(View view) {
        rootView.addView(view);
        return this;
    }
}