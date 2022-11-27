package com.example.xlog.dialog;

import android.content.Context;
import android.graphics.Color;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuqi on 2020/6/15.
 */

public class SettingsView extends LinearLayout {

    private Context context;

    private List<ItemView> itemViewList = new ArrayList<>();

    public SettingsView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
    }

    public int addItemView(String title, String text) {
        int size = itemViewList.size();
        ItemView itemView = new ItemView(context);
        itemView.setTitle(title);
        itemView.setText(text);
        itemViewList.add(itemView);
        addView(itemView);
        return size;
    }

    public String getItemText(int index) {
        if (index < itemViewList.size()) {
            return itemViewList.get(index).getText();
        }
        return "";
    }


    private static class ItemView extends LinearLayout {

        private TextView titleView;

        private EditText editText;

        public ItemView(Context context) {
            super(context);
            setOrientation(HORIZONTAL);

            titleView = new TextView(context);
            titleView.setTextColor(Color.WHITE);
            LayoutParams layoutParams = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
            layoutParams.weight = 1;
            addView(titleView, layoutParams);

            editText = new EditText(context);
            LayoutParams layoutParams2 = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
            layoutParams2.weight = 4;
            addView(editText, layoutParams2);
        }

        public void setTitle(String title) {
            titleView.setText(title);
        }

        public void setText(String text) {
            editText.setText(text);
        }

        public String getText() {
            return editText.getText().toString();
        }
    }


}
