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

            // 디바이스 가로 세로 얻는 코드
//            DisplayMetrics dm = context.getResources().getDisplayMetrics();
//            int width = dm.widthPixels;
//            int height = dm.heightPixels;

            Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
            try {
                wallpaperManager.setBitmap(bitmap);
            } catch (IOException e) {

            }
        }
    }
}
