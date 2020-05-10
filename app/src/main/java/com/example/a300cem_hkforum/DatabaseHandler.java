package com.example.a300cem_hkforum;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class DatabaseHandler extends SQLiteOpenHelper {

    public DatabaseHandler(Context context){
        super(context, "recordDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS recordTable ( colID, colTitle, colDate, colUser)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public void addContact(Contact contact) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        ContentValues contentValues = new ContentValues(4);
        contentValues.put("colID", contact.getId());
        contentValues.put("colTitle", contact.getTitle());
        contentValues.put("colDate", contact.getDate());
        contentValues.put("colUser", contact.getUser());

        long result = sqLiteDatabase.insert("recordTable", null, contentValues);

        if (result > 0) {
            Log.d("dbhelper", "inserted successfully");
        } else {
            Log.d("dbhelper", "failed to insert");
        }
        sqLiteDatabase.close();
    }
    public List<Contact> getAllPosts() {
        List<Contact> posts = new ArrayList<>();
        String POSTS_SELECT_QUERY =
                String.format("SELECT * FROM recordTable",
                         "colID",
                        "colTitle",
                        "colDate",
                        "colUser");
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(POSTS_SELECT_QUERY, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Contact newPost = new Contact(cursor.getInt(cursor.getColumnIndex("colID")),cursor.getString(cursor.getColumnIndex("colTitle")),cursor.getString(cursor.getColumnIndex("colDate")),cursor.getString(cursor.getColumnIndex("colUser")));
                    posts.add(newPost);
                } while(cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error while trying to get posts from database");
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return posts;
    }
}
