package com.choinisae.donghaechoi.change_background_1;

import android.app.WallpaperManager;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.GridView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final int GALLARY_PICK = 1000;
    private GridView mGridView;
    private List<MyItem> mData;
    private MyAdapter mMyAdapter;
    private Bitmap bitmap;
    private ArrayList<Bitmap> mArray;
    private Bitmap resized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.add_btn).setOnClickListener(this);
        findViewById(R.id.comp_btn).setOnClickListener(this);

        mData = new ArrayList<>();
        mArray = new ArrayList<>();
        mMyAdapter = new MyAdapter(mData, getApplicationContext());
        mGridView = (GridView) findViewById(R.id.grid_view);
        mGridView.setAdapter(mMyAdapter);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == findViewById(R.id.add_btn).getId()) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, GALLARY_PICK);


        } else if (v.getId() == findViewById(R.id.comp_btn).getId()) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
            double random = Math.random() * mData.size();
            try {
                wallpaperManager.setBitmap(mArray.get((int) random));
            } catch (IOException e) {

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLARY_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectImage,
                    filePathColumn, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                bitmap = BitmapFactory.decodeFile(picturePath, options);
//                int height = bitmap.getHeight();
//                int width = bitmap.getWidth();
//                DisplayMetrics display = getApplicationContext().getResources().getDisplayMetrics();
//                int deviceWidth = display.widthPixels;
//                int deviceHeight = display.heightPixels;
//                resized = Bitmap.createScaledBitmap(bitmap, deviceWidth, deviceHeight, true);
                mArray.add(bitmap);
                mData.add(new MyItem(picturePath));
                mMyAdapter.notifyDataSetChanged();
            }
        }
    }
}
