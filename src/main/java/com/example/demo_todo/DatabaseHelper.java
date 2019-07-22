package com.example.demo_todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private  static final String DATABASE_NAME ="Tasks.db";
    private  static final String TABLE_NAME ="active_tasks";
    private static final String TABlE_NAME2="archieve_tasks";
    private  static final String col1="TITLE";
    private  static final String col2="DATE";


    DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db=this.getWritableDatabase();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ TABLE_NAME +" (TITLE TEXT, DATE TEXT)");
        db.execSQL("create table "+ TABlE_NAME2+" (TITLE TEXT, DATE TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+ TABlE_NAME2);
        onCreate(db);
    }
    public boolean insertData(String title)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
        Date date = new Date();
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(col1,title);
        contentValues.put(col2,dateFormat.format(date));
        long val=db.insert(TABLE_NAME,null,contentValues);
        if (val==-1)
            return false;
        else
            return true;
    }
    public Cursor getData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("select TITLE from "+TABLE_NAME,null);

        return result;
    }
    public int deleteData(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        addToArchieve(id);
        int n=db.delete(TABLE_NAME,"TITLE=?",new String[]{id});

        return n;
    }
    public void addToArchieve(String title){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
        Date date = new Date();
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(col1,title);
        contentValues.put(col2,dateFormat.format(date));
        long val=db.insert(TABlE_NAME2,null,contentValues);
    }
    Cursor getArchievedData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery("select TITLE from "+TABlE_NAME2,null);

        return result;
    }
    public int arc_deleteTask(String id){
        SQLiteDatabase db=this.getWritableDatabase();
        addToArchieve(id);
        int n=db.delete(TABlE_NAME2,"TITLE=?",new String[]{id});

        return n;
    }
    public long getActiveTaskCount(){
        SQLiteDatabase db=this.getReadableDatabase();
        long count= DatabaseUtils.queryNumEntries(db,TABLE_NAME);
        db.close();
        return count;
    }
}
