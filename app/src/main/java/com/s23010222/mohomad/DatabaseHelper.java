package com.s23010222.mohomad;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "UserLogin.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "users";
    public static final String COL_USERNAME = "username";
    public static final String COL_PASSWORD = "password";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_USERNAME + " TEXT, " +   // ← removed PRIMARY KEY to allow duplicates
                COL_PASSWORD + " TEXT)";
        db.execSQL(query);
        Log.d("DB", "Database created.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUser(String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USERNAME, username);
        values.put(COL_PASSWORD, password);

        long result = db.insert(TABLE_NAME, null, values);
        Log.d("DB", "Insert result: " + result);
        return result != -1;
    }
}
