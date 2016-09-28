package com.naxesa.slowly;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lee young teak on 2016-09-20.
 */
public class LogInSQLiteOpenHelper extends SQLiteOpenHelper {

    final static String tableName = "login";

    public LogInSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "create table " + tableName + " (" +
                "_id integer primary key autoincrement, " +
                "email text, " +
                "password text, " +
                ");";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "drop table if exists student";
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }
}
