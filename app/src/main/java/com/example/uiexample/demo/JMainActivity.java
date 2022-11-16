package com.example.uiexample.demo;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.uiexample.demo.view.TetrisTestView;

public class JMainActivity extends AppCompatActivity {

    private LinearLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = createView();
        setContentView(rootView);
        add(new TetrisTestView(this));
    }

    private LinearLayout createView() {
        LinearLayout ll = new LinearLayout(this);
        return ll;
    }

    private JMainActivity add(View view) {
        rootView.addView(view);
        return this;
    }
}