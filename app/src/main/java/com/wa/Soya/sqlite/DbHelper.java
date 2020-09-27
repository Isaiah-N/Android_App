package com.wa.Soya.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = "DbHelper";

    private static final String CREATE_TABLE = "create table Soya("
            + "id integer primary key AUTOINCREMENT," +
            "name text," +
            "height integer," +
            "age integer," +
            "date text," +
            "ps text," +
            "pic text," +
            "loc text)";
    private Context mContext;

    public DbHelper(@Nullable Context context,String name,SQLiteDatabase.CursorFactory factory,int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "创建数据库");

        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(TAG, "升级数据库");
    }
}
