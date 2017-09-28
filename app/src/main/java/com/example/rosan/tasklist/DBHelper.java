package com.example.rosan.tasklist;

/* Created by rosan on 28-9-2017. */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper{

    /* Private Strings */
    private static final String DATABASE_NAME = "todo.db";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ID = "_id";
    private static final String KEY_NAME = "task";
    private static final String KEY_COMPLETED = "completed";
    private static final String TABLE = "taskTable";

    /* Constructor */
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String CREATE_DB = "CREATE TABLE " + TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT NOT NULL, "
                + KEY_COMPLETED + " TEXT NOT NULL)";

        db.execSQL(CREATE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void create(Task task){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
        values.put(KEY_COMPLETED, "notCompleted");
        db.insert(TABLE, null, values);
        db.close();
    }

    public ArrayList<Task> read(){
        SQLiteDatabase db = getReadableDatabase();

        ArrayList<Task> tasks = new ArrayList<>();

        // Create a query
        String query = "SELECT " + KEY_ID + ", " + KEY_NAME + ", " + KEY_COMPLETED + " FROM " + TABLE;
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do {
                String name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
                String completed = cursor.getString(cursor.getColumnIndex(KEY_COMPLETED));
                int id = cursor.getInt(cursor.getColumnIndex(KEY_ID));
                /* Create a task object with the newly retrieved data */
                Task task = new Task(name, id, completed);

                tasks.add(task);
            }
            /* While there is still a next entry */
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }

    /* Used to convert notCompleted to Completed? */
    public int update(Task task){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
        values.put(KEY_COMPLETED, task.getCompleted());
        values.put(KEY_ID, task.getID());

        return db.update(TABLE, values, KEY_ID + " = ? ", new String[] {String.valueOf(task.getID())});
    }

    public void delete(Task task){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, " " + KEY_ID + " = ? ", new String[] {String.valueOf(task.getID())});
        db.close();
    }

}
