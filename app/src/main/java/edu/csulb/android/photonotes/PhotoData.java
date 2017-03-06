package edu.csulb.android.photonotes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by manikhanuja on 3/5/17.
 */

public class PhotoData {

    public static final String DEBUG_TAG = "PhotoData";
    private static final String[] ALL_COLUMNS = {
            PhotoDBHelper.COLUMN_ID,
            PhotoDBHelper.COLUMN_CAPTION,
            PhotoDBHelper.COLUMN_PATH
    };
    private SQLiteDatabase db;
    private SQLiteOpenHelper photoDbHelper;

    //PhotoDBHelper object will allow to access the database
    public PhotoData(Context context) {
        this.photoDbHelper = new PhotoDBHelper(context);
    }

    //This is where the database is created with the getWritableDatabase() method.
    public void open() {
        db = photoDbHelper.getWritableDatabase();
    }

    //This is for closing the database
    public void close() {

        if (photoDbHelper != null) {
            photoDbHelper.close();
        }

    }

    //This is for inserting the data into the PhotoInfo table
    public void insert(String caption, String path) {
        open();
        ContentValues photoContentValues = new ContentValues();
        photoContentValues.put(PhotoDBHelper.COLUMN_CAPTION, caption);
        photoContentValues.put(PhotoDBHelper.COLUMN_PATH, path);
        db.insert(PhotoDBHelper.TABLE_PHOTOINFO, null, photoContentValues);
        Log.d(DEBUG_TAG, "Successfully Added data to the table: Caption: " + caption + " & Path: " + path);
        close();
    }

    public ArrayList<String> getPhotoCaption() {
        ArrayList<String> notes = new ArrayList<>();
        open();
        Cursor cursor = null;
        try {
            cursor = db.query(PhotoDBHelper.TABLE_PHOTOINFO, ALL_COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String caption = cursor.getString(1);
                    notes.add(caption);
                    Log.d(DEBUG_TAG, "Get Photo Caption Method: " + caption);
                }
            }
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "getPhotoCaption method: Exception Raised with a value of " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        close();
        return notes;
    }

    public String getPhotoURI(String caption) {
        String notesUri = null;
        String whereClause = PhotoDBHelper.COLUMN_CAPTION + "=?";
        String[] whereArgs = {caption};
        open();
        Cursor cursor = null;
        try {
            cursor = db.query(PhotoDBHelper.TABLE_PHOTOINFO, ALL_COLUMNS, whereClause, whereArgs, null, null, null);
            int index = cursor.getColumnIndex(PhotoDBHelper.COLUMN_PATH);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    notesUri = cursor.getString(index);
                    Log.d(DEBUG_TAG, "Get Photo URI Method: " + notesUri);
                }
            }
        } catch (Exception e) {
            Log.d(DEBUG_TAG, "getPhotoURI method: Exception Raised with a value of " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        close();
        return notesUri;
    }
}
