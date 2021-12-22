package com.elvis.gogojohnny.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHandler {

    public static final String DATABASE_NAME = "elvisgogojohnnyhighscore.db";
    public static final String HIGH_SCORE_TABLE = "HighScoreTable";
    public static final String HIGH_SCORE_VALUE = "highScoreValue";
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_CREATE = "CREATE TABLE IF NOT EXISTS " +
            HIGH_SCORE_TABLE + "(" +
            HIGH_SCORE_VALUE + " TEXT" + ")";

    DataBaseHelper dbHelper;
    Context context;
    SQLiteDatabase db;

    public DataHandler (Context context) {
        this.context = context;
        dbHelper = new DataBaseHelper(context);
    }

    private static class DataBaseHelper extends SQLiteOpenHelper {

        public DataBaseHelper (Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE);

            ContentValues cv = new ContentValues();
            cv.put(HIGH_SCORE_VALUE, 0);
            db.insert(HIGH_SCORE_TABLE, null, cv);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + HIGH_SCORE_TABLE);
            onCreate(db);
        }
    }

    public DataHandler open () {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close () {
        dbHelper.close();
    }

    public long insertData (int data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(HIGH_SCORE_VALUE, data);
        return db.insertOrThrow(HIGH_SCORE_TABLE, null, contentValues);
    }

    public Cursor returnData () {
        return db.query(HIGH_SCORE_TABLE, new String[]{HIGH_SCORE_VALUE}, null, null, null, null, null);
    }

    public void deleteData()
    {
        db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(HIGH_SCORE_TABLE, new String[]{HIGH_SCORE_VALUE}, null, null, null, null, null);
        int rows = cursor.getCount();
        if (rows > 1) {
            int i = 0;
            do {
                if (i > 1) {
                    db.delete(HIGH_SCORE_TABLE, HIGH_SCORE_VALUE + "=?", new String[]{HIGH_SCORE_VALUE});
                }
                i++;
            }
            while (i < rows);
        }
    }
}
