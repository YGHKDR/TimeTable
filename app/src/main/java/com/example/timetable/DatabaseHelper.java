package com.example.timetable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        //super()执行父类的构造对象
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //重载两个方法
        //创建DatabaseHelper对象的时候，创建数据库
        db.execSQL("create table courses ( id integer primary key autoincrement, course_name text, teacher_name text, class_room text, day integer, class_start integer, class_end integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
