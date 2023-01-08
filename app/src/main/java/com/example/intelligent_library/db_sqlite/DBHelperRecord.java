package com.example.intelligent_library.db_sqlite;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelperRecord extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "book_list.db";
    public static final int DATABASE_VERSION = 1;
    public static final String CREATE_TABLE_SQL =
            "CREATE TABLE " +
                    "book_list (_id INTEGER PRIMARY KEY, useraccount TEXT,bookname TEXT, bookauthor TEXT, bookcategory TEXT, " +
                    "image BLOB ,borrow_time TIMESTAMP, return_time TIMESTAMP default CURRENT_TIMESTAMP);";
    public static final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS book_list";

    public DBHelperRecord(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DBHelperRecord(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onDropTable(db);
        onCreate(db);
    }

    public void onDropTable(SQLiteDatabase db) {
        db.execSQL(DROP_TABLE_SQL);
    }
}

