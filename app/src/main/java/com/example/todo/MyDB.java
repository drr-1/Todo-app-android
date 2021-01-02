package com.example.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.todo.model.ToDo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyDB {

    myDBHelper myDBHelper;

    public MyDB(Context context) {
        myDBHelper = new myDBHelper(context);
    }


    public boolean update(String name, ToDo newModel) {
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        Log.i("is","Done :n"+newModel.getIsdone());
        ContentValues values = new ContentValues();
        values.put(myDBHelper.COLUMN_NAME, newModel.getName());
        values.put(myDBHelper.COLUMN_DESCRIPTION, newModel.getDescription());
        values.put(myDBHelper.COLUMN_TYPE, newModel.getType());
        values.put(myDBHelper.COLUMN_PROGRESS, newModel.getProgress());
        values.put(myDBHelper.COLUMN_ISDONE, newModel.getIsdone());

//        String query= "UPDATE "+myDBHelper.TABLE_NAME+ " SET "+myDBHelper.COLUMN_ISDONE + " = 1 " +" WHERE "+myDBHelper.COLUMN_NAME +"= '" +name+"'";
//        db.rawQuery(query,null);
        int i = db.update(myDBHelper.TABLE_NAME, values, myDBHelper.COLUMN_NAME + " = " + "'"+name+"'", null);
        if (i > -1)
            return true;
        else
            return false;
//        return true;
    }


        public ToDo findData(String name){ // ene function ni ogogdliin sangaas ner pass aar ni haina
        String findQuery= "SELECT * FROM "+myDBHelper.TABLE_NAME+" WHERE "+ myDBHelper.COLUMN_NAME +"="+"'"+name+"'";
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(findQuery,null);
        ToDo toDo=new ToDo();
        if (cursor.moveToFirst()){
            toDo.setId(cursor.getInt(0));
            Log.i("cursor", "user info: " + toDo.getId());
            toDo.setName(cursor.getString(1));
            Log.i("cursor", "user info: " + toDo.getName());
            toDo.setDescription(cursor.getString(2));
            Log.i("cursor", "user info: " + toDo.getDescription());
            toDo.setType(cursor.getString(4));
            Log.i("cursor", "user info: " + toDo.getType());
            toDo.setProgress(cursor.getInt(5));
            Log.i("cursor", "user info: " + toDo.getProgress());
            toDo.setIsdone(Boolean.valueOf(cursor.getString(6)));
            Log.i("cursor", "user info: " + toDo.getIsdone());
            toDo.setParentId(cursor.getInt(7));
            Log.i("cursor", "user info: " + toDo.getParentId());
            cursor.close();
        }
        else
            toDo=null;
        return toDo;

    }
    public ArrayList<ToDo> findAll() {
        String findQuery = "SELECT * FROM " + myDBHelper.TABLE_NAME+ " WHERE " + myDBHelper.COLUMN_PARENTID + " IS NULL" ;
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(findQuery, null);
        ArrayList<ToDo> allTodo = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ToDo toDo = new ToDo();
                toDo.setId(cursor.getInt(0));
                Log.i("cursor", "user info: " + toDo.getId());
                toDo.setName(cursor.getString(1));
                Log.i("cursor", "user info: " + toDo.getName());
                toDo.setDescription(cursor.getString(2));
                Log.i("cursor", "user info: " + toDo.getDescription());
                toDo.setType(cursor.getString(4));
                Log.i("cursor", "user info: " + toDo.getType());
                toDo.setProgress(cursor.getInt(5));
                Log.i("cursor", "user info: " + toDo.getProgress());
                toDo.setIsdone(Boolean.valueOf(cursor.getString(6)));
                Log.i("cursor", "user info: " + toDo.getIsdone());
                toDo.setParentId(cursor.getInt(7));
                Log.i("cursor", "user info: " + toDo.getParentId());
                allTodo.add(toDo);
                cursor.moveToNext();
            }
        } else
            allTodo = null;
        return allTodo;
    }
    public ArrayList<String> findNameByAll() {
        String findQuery = "SELECT * FROM " + myDBHelper.TABLE_NAME+ " WHERE " + myDBHelper.COLUMN_PARENTID + " IS NULL" ;
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(findQuery, null);
        ArrayList<String> allTodo = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ToDo toDo = new ToDo();
                toDo.setId(cursor.getInt(0));
                Log.i("cursor", "user info: " + toDo.getId());
                toDo.setName(cursor.getString(1));
                Log.i("cursor", "user info: " + toDo.getName());
                toDo.setDescription(cursor.getString(2));
                Log.i("cursor", "user info: " + toDo.getDescription());
                toDo.setType(cursor.getString(4));
                Log.i("cursor", "user info: " + toDo.getType());
                toDo.setProgress(cursor.getInt(5));
                Log.i("cursor", "user info: " + toDo.getProgress());
                toDo.setIsdone(Boolean.valueOf(cursor.getString(6)));
                Log.i("cursor", "user info: " + toDo.getIsdone());
                toDo.setParentId(cursor.getInt(7));
                Log.i("cursor", "user info: " + toDo.getParentId());
                allTodo.add(toDo.getName());
                cursor.moveToNext();
            }
        } else
            allTodo = null;
        return allTodo;
    }

    public void delete(String name){
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        db.delete(myDBHelper.TABLE_NAME,myDBHelper.COLUMN_NAME + "="+ "'"+name+"'", null);
    }
    public ArrayList<ToDo> findSubs(Integer id) {
        String findQuery = "SELECT * FROM " + myDBHelper.TABLE_NAME + " WHERE " + myDBHelper.COLUMN_PARENTID + "=" + id  ;
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(findQuery, null);
        ArrayList<ToDo> subs = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                ToDo toDo = new ToDo();
                toDo.setId(cursor.getInt(0));
                Log.i("cursor", "user info: " + toDo.getId());
                toDo.setName(cursor.getString(1));
                Log.i("cursor", "user info: " + toDo.getName());
                toDo.setDescription(cursor.getString(2));
                Log.i("cursor", "user info: " + toDo.getDescription());
                toDo.setType(cursor.getString(4));
                Log.i("cursor", "user info: " + toDo.getType());
                toDo.setProgress(cursor.getInt(5));
                Log.i("cursor", "user info: " + toDo.getProgress());
                boolean val=false;
                if(cursor.getInt(6) == 1)
                    val=true;
                toDo.setIsdone(val);
                Log.i("cursor", "user info: " + toDo.getIsdone());
                toDo.setParentId(cursor.getInt(7));
                Log.i("cursor", "user info: " + toDo.getParentId());
                subs.add(toDo);
                cursor.moveToNext();
            }

        } else
            subs = null;
        return subs;

    }

    public Long insertData(ToDo toDo) {// ene function ni database ruu shine ehreglegch nemne
        SQLiteDatabase db = myDBHelper.getWritableDatabase();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = toDo.getDate();
        ContentValues values = new ContentValues();
        values.put(myDBHelper.COLUMN_NAME, toDo.getName());
        values.put(myDBHelper.COLUMN_DESCRIPTION, toDo.getDescription());
        values.put(myDBHelper.COLUMN_DATE, dateFormat.format(date));
        values.put(myDBHelper.COLUMN_TYPE, toDo.getType());
        values.put(myDBHelper.COLUMN_PROGRESS, toDo.getProgress());
        values.put(myDBHelper.COLUMN_ISDONE, toDo.getIsdone());
        values.put(myDBHelper.COLUMN_PARENTID, toDo.getParentId());

        Long id = db.insert(myDBHelper.TABLE_NAME, null, values);
        return id;
    }

    static class myDBHelper extends SQLiteOpenHelper {

        public static final String TABLE_NAME = "tasks";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_PROGRESS = "progress";
        public static final String COLUMN_ISDONE = "isdone";
        public static final String COLUMN_PARENTID = "parent_id";
        public static final int Database_version = 2;
        public static final String Database_name = "todo";
        private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
                " ( " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                COLUMN_NAME + " VARCHAR(255) ," +
                COLUMN_DESCRIPTION + " VARCHAR(255) ," +
                COLUMN_DATE + " DATETIME ," +
                COLUMN_TYPE + " VARCHAR(255) ," +
                COLUMN_PROGRESS + " INTEGER," +
                COLUMN_ISDONE + " BOOLEAN ,"+
                COLUMN_PARENTID + " INTEGER )";
        private static final String DROP_TABLE =
                " DROP TABLE IF EXISTS " + TABLE_NAME;

        public myDBHelper(@Nullable Context context) {
            super(context, Database_name, null, Database_version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer) {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

}

