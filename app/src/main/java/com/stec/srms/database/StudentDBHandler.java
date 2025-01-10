package com.stec.srms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class StudentDBHandler extends SQLiteOpenHelper {
    private static StudentDBHandler instance;
    private static final String DATABASE_NAME = "SRMS_DB.db";
    private static final int DATABASE_VERSION = 1;

    public StudentDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized StudentDBHandler getInstance(Context context) {
        if (instance == null) {
            instance = new StudentDBHandler(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        if (oldVersion < 2) {}
//        if (oldVersion < 3) {}
    }
}
