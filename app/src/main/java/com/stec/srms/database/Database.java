package com.stec.srms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "StudentDB";
    private static final int DATABASE_VERSION = 1;
    protected static SQLiteDatabase readableDB, WritableDB;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        readableDB = getReadableDatabase();
        WritableDB = getWritableDatabase();
    }

    @Override
    public synchronized void close() {
        readableDB.close();
        WritableDB.close();
        super.close();
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /*if (oldVersion < 2) {}
        if (oldVersion < 3) {}*/
    }
}
