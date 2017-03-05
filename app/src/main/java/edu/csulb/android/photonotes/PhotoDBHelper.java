package edu.csulb.android.photonotes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by manikhanuja on 3/5/17.
 */

//Upgrade and Create the PhotoInfo Database
public class PhotoDBHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "photoinfo.db";
    public static final int DB_VERSION = 1;

    public static final String TABLE_PHOTOINFO = "PHOTOINFO";
    public static final String COLUMN_ID = "_ID";
    public static final String COLUMN_CAPTION = "CAPTION";
    public static final String COLUMN_PATH = "PATH";

    public static final String PHOTO_TABLE_CREATE =
            "CREATE TABLE " + TABLE_PHOTOINFO + "(" + COLUMN_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CAPTION + "TEXT, " +
                    COLUMN_PATH + "TEXT, " + ")";

    public PhotoDBHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(PHOTO_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOINFO);
        onCreate(db);
    }
}
