package com.zxy.and2;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class QMDB extends SQLiteOpenHelper {

    public QMDB(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
//        String CREATE_QM_DB = "create table Vol ("
//                +"id integer primary key,"
//                +"title text,"
//                +"subtitle text,"
//                +"image text,"
//                +"content text,"
//                +"orgnization text,"
//                +"date text,"
//                +"area text,"
//                +"phone text)";
//        sqLiteDatabase.execSQL(CREATE_QM_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
