package com.choinisae.donghaechoi.change_background_1;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.suwonsmartapp.abl.AsyncBitmapLoader;

/**
 * Created by donghaechoi on 2016. 3. 14..
 */
public class MyCursorAdapter extends CursorAdapter {

    private final LayoutInflater mInflator;
    private final AsyncBitmapLoader mAsyncBitmapLoader;
    private BitmapFactory.Options options = new BitmapFactory.Options();

    public MyCursorAdapter(Context context, Cursor c) {
        super(context, c, false);
        mInflator = LayoutInflater.from(context);
        mAsyncBitmapLoader = new AsyncBitmapLoader(context);
        mAsyncBitmapLoader.setBitmapLoadListener(new AsyncBitmapLoader.BitmapLoadListener() {
            @Override
            public Bitmap getBitmap(String position) {
                // ListView 등의 position 에 표시할 Bitmap을 정의하여 리턴
                options.inSampleSize = 2;
                return BitmapFactory.decodeFile(position , options);
            }
        });
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View convertView = mInflator.inflate(R.layout.activity_imageview, parent, false);
        ViewHolder holder = new ViewHolder();
        holder.image = (ImageView) convertView.findViewById(R.id.image_view);
        convertView.setTag(holder);
        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        mAsyncBitmapLoader.loadBitmap(cursor.getString(1), holder.image);
    }

    private static class ViewHolder {
        ImageView image;
    }

}
