package com.example.myapplication.Models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    SQLiteDatabase database;
    public DBHelper(@Nullable Context context) {
        super(context, "StudentsDataBase", null, 1);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists students(name text, surname text, age integer, course integer);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public void save(ArrayList<Student> students){
        database.execSQL("drop table if exists students;");
        database.execSQL("create table if not exists students(name text, surname text, age integer, course integer);");
        for(Student st : students) {
            ContentValues cv = new ContentValues();
            cv.put("name", st.Name);
            cv.put("surname", st.Surname);
            cv.put("age", st.Age);
            cv.put("course", st.Course);
            database.insert("students", null, cv);
        }
    }
    public ArrayList<Student> load(){
        database.execSQL("create table if not exists students(name text, surname text, age integer, course integer);");
        if (!database.isOpen()) {
            database = this.getReadableDatabase();
        }
        ArrayList<Student> list = new ArrayList<Student>();
        Cursor c = database.query("students", null, null, null, null, null, null);
        if (c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();
                do {
                    String name = c.getString(0);
                    String surname = c.getString(1);
                    int age = c.getInt(2);
                    int course = c.getInt(3);
                    list.add(new Student(name, surname, age, course));
                } while (c.moveToNext());
                c.close();
            }
        }
        return list;
    }
}
