package com.choinisae.donghaechoi.change_background_1.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by donghaechoi on 2016. 3. 14..
 */
public class ImageDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "nisaeImageDb.db";
    private static final int DATABASE_VERSION = 1;

    public ImageDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 최초에 db가 열리는 타이밍에 table 생성
        db.execSQL(ImageContract.ImageEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
