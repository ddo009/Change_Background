package com.choinisae.donghaechoi.change_background_1.facade;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.choinisae.donghaechoi.change_background_1.db.ImageContract;
import com.choinisae.donghaechoi.change_background_1.db.ImageDBHelper;

/**
 * Created by donghaechoi on 2016. 3. 14..
 */
public class ImageFacade {

    private ImageDBHelper mHelper;

    public ImageFacade(Context context) {
        mHelper = new ImageDBHelper(context);
    }


    public long insertPath(String path) {
        // 쓰기 모드로 db 저장소를 얻기
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        if (path.length() != 0) {
            values.put(ImageContract.ImageEntry.COLUMN_NAME_PATH, path);
        }
        if (path.length() != 0) {
            return db.insert(ImageContract.ImageEntry.TABLE_NAME,
                    null,
                    values);
        }
        return -1;
    }

    public Cursor queryAllpaths() {
        SQLiteDatabase db = mHelper.getReadableDatabase();

        return db.query(ImageContract.ImageEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);
    }

    public int deleteImage(String whereClause, String[] whereArgs) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        return db.delete(ImageContract.ImageEntry.TABLE_NAME,
                whereClause,
                whereArgs);
    }

}
