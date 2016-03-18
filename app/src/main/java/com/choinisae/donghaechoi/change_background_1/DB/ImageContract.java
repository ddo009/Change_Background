package com.choinisae.donghaechoi.change_background_1.db;

import android.provider.BaseColumns;

/**
 * Created by donghaechoi on 2016. 3. 14..
 */
public class ImageContract {

    public static abstract class ImageEntry implements BaseColumns {
        public static final String TABLE_NAME = "Image";

        public static final String COLUMN_NAME_PATH = "path";

        public static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + ImageEntry.TABLE_NAME + " (" +
                ImageEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ImageEntry.COLUMN_NAME_PATH + " TEXT NOT NULL" +
                ");";
    }

}
