package com.example.library_2;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class OpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Library.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "books";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_GENRE = "genre";
    public static final String COLUMN_YEAR = "year";
    public OpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE " + TABLE_NAME + "( _id integer primary key autoincrement, " + COLUMN_AUTHOR + " TEXT, " + COLUMN_TITLE + " TEXT NOT NULL, " + COLUMN_GENRE + " TEXT, " + COLUMN_YEAR + " INTEGER);";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    }
}