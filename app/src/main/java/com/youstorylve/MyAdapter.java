package com.youstorylve;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {
    private ArrayList<Button> mBtns = null;
    private int mColumnWidth, mColumnHeight;

    public MyAdapter(ArrayList<Button> buttons, int columnWidth, int columnHeight) {
        mBtns = buttons;
        mColumnWidth = columnWidth;
        mColumnHeight = columnHeight;
    }

    @Override
    public int getCount() { return mBtns.size(); }

    @Override
    public Object getItem(int position) {return mBtns.get(position);}

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;

        if (convertView == null) {
            button = mBtns.get(position);
        } else {
            button = (Button) convertView;
        }
        android.widget.AbsListView.LayoutParams params =
                new android.widget.AbsListView.LayoutParams(mColumnWidth, mColumnHeight);
        button.setLayoutParams(params);
        return button;
    }
}
