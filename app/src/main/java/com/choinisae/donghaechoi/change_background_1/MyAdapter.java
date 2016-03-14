package com.choinisae.donghaechoi.change_background_1;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by donghaechoi on 2016. 3. 4..
 */
public class MyAdapter extends BaseAdapter {

    private List<MyItem> mmData;
    private Context mContext;
    private LayoutInflater mInflater;


    public MyAdapter(List<MyItem> mmData, Context context) {
        this.mmData = mmData;
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mmData.size();
    }

    @Override
    public Object getItem(int position) {
        return mmData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.activity_imageview, parent, false);
        }
        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_view);

        MyItem item = (MyItem) getItem(position);
        imageView.setImageBitmap(BitmapFactory.decodeFile(item.getImagePath()));

        return convertView;
    }
}
