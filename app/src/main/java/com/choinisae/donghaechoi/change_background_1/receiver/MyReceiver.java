package com.choinisae.donghaechoi.change_background_1.receiver;

import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.choinisae.donghaechoi.change_background_1.facade.ImageFacade;

import java.io.IOException;
import java.util.Random;

/**
 * Created by donghaechoi on 2016. 3. 14..
 */
public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(context);
            ImageFacade facade = new ImageFacade(context);
            Cursor cursor = facade.queryAllpaths();
            int random = new Random().nextInt(cursor.getCount()) + 1;
            cursor.move(random);
            String picturePath = cursor.getString(1);


//            DisplayMetrics dm = context.getResources().getDisplayMetrics();
//            int width = dm.widthPixels;
//            int height = dm.heightPixels;

//            BitmapFactory.Options options = new BitmapFactory.Options();
//            options.inSampleSize = 2;

//            float widthScale = options.outWidth / width;
//            float heightScale = options.outHeight / height;
//            float scale = widthScale > heightScale ? widthScale : heightScale;

//            if (scale >= 8) {
//                options.inSampleSize = 8;
//            } else if (scale >= 6) {
//                options.inSampleSize = 6;
//            } else if (scale >= 4) {
//                options.inSampleSize = 4;
//            }
            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            try {
                wallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {

            }
        }
    }
}
