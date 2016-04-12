package com.choinisae.donghaechoi.change_background_1.adapter;

import android.database.Cursor;
import android.database.CursorJoiner;
import android.graphics.BitmapFactory;
import android.provider.BaseColumns;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.choinisae.donghaechoi.change_background_1.R;
import com.choinisae.donghaechoi.change_background_1.db.ImageContract;

/**
 * Created by donghaechoi on 2016. 3. 22..
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {

    private OnItemClickListener mListener;

    public Cursor getItem(int position) {
        Cursor cursor = mCursor;
        cursor.moveToPosition(position);
        return cursor;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private Cursor mCursor;

    public MyRecyclerAdapter(Cursor cursor) {
        mCursor = cursor;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_imageview, parent, false);
        final ViewHolder holder = new ViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(itemView, holder.getAdapterPosition());
                }
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    mListener.onItemLongClick(itemView, holder.getAdapterPosition());
                }
                return true;
            }
        });
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cursor cursor = mCursor;
        cursor.moveToPosition(position);
        String path = cursor.getString(cursor.getColumnIndexOrThrow(ImageContract.ImageEntry.COLUMN_NAME_PATH));
        holder.image.setImageBitmap(BitmapFactory.decodeFile(path));

    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
//        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public long getItemId(int position) {
        Cursor cursor = getItem(position);
        return cursor.getLong(cursor.getColumnIndexOrThrow(BaseColumns._ID));
    }

    public void swapCursor(Cursor data) {
        Cursor oldCursor = mCursor;
        if (oldCursor != null && data != null && !oldCursor.isClosed() && !data.isClosed()) {
            String[] columns = {BaseColumns._ID};
            CursorJoiner joiner = new CursorJoiner(oldCursor, columns, data, columns);
            for (CursorJoiner.Result result : joiner) {
                switch (result) {
                    case LEFT:
                        notifyItemRemoved(data.getPosition());
                        Log.d("swapCursor", "LEFT");
                        break;
                    case RIGHT:
                        notifyItemInserted(data.getPosition());
                        Log.d("swapCursor", "RIGHT");
                        break;
                    case BOTH:
                        if (getRowHash(oldCursor) != getRowHash(data)) {
                            notifyItemChanged(data.getPosition());
                            Log.d("swapCursor", "BOTH");
                        }
                        break;
                }
            }
            oldCursor.close();
        }

        mCursor = data;
    }

    private int getRowHash(Cursor cursor) {
        StringBuilder result = new StringBuilder("row");
        for (int i = 0; i < cursor.getColumnCount(); i++) {
            result.append(cursor.getString(i));
        }
        return result.toString().hashCode();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image_view);
        }
    }

}
