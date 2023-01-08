package com.example.intelligent_library.db_sqlite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelperMenu extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "book_list.db";
    public static final int DATABASE_VERSION = 1;
    public static final String CREATE_TABLE_SQL =
            "CREATE TABLE " +
                    "book_list (_id INTEGER PRIMARY KEY, bookname TEXT, bookauthor TEXT, bookcategory TEXT, " +
                    "image BLOB ,borrow_state Boolean default CURRENT_TIMESTAMP);";
    public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS book_list";

    public static final String CREATE_TABLE_SQL2 =
            "CREATE TABLE " +
                    "borrow_record (_id INTEGER PRIMARY KEY, useraccount TEXT,book_id INTEGER," +
                    "borrow_time DATE, return_time DATE default CURRENT_TIMESTAMP);";
    public static final String DROP_TABLE_SQL2 = "DROP TABLE IF EXISTS borrow_record";

    public static final String CREATE_TABLE_SQL3 =
            "CREATE TABLE " +
                    "user (_id INTEGER PRIMARY KEY,username TEXT, useraccount TEXT,userpassword TEXT default CURRENT_TIMESTAMP);";

    public static final String DROP_TABLE_SQL3 = "DROP TABLE IF EXISTS user";

    public DBHelperMenu(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelperMenu(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    public void onCreate_record(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL2);
    }

    public void onCreate_user(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onDropTable(db);
        onCreate(db);
    }

    public void onDropTable(SQLiteDatabase db) {
        db.execSQL(DROP_TABLE_SQL);
    }

    public void onDropTable_record(SQLiteDatabase db) {
        db.execSQL(DROP_TABLE_SQL2);
    }

    public void onDropTable_user(SQLiteDatabase db) {
        db.execSQL(DROP_TABLE_SQL3);
    }
}

