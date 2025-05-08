package com.example.apphoctienganh.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.apphoctienganh.model.UserPoint;

import java.util.ArrayList;
import java.util.List;

public class DataBasePointUser extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuestionEnd2";
    private static final String TABLE_NAME = "PointsOfUser";
    private static final String ID = "Id";
    private static final String USER = "User";
    private static final String POINTS = "Points";
    private static final String TIME = "Time";

    public DataBasePointUser(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER + " TEXT, " +
                POINTS + " INTEGER, " +
                TIME + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addPoints(String user, int points, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(USER, user);
            values.put(POINTS, points);
            values.put(TIME, time);

            int currentPoints = getPoints(user);
            if (currentPoints == 0) {
                // User does not exist in the database, insert a new record
                db.insert(TABLE_NAME, null, values);
            } else if (points > currentPoints) {
                // User exists and the new points are greater, update the record
                db.update(TABLE_NAME, values, USER + "=?", new String[]{user});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    public List<UserPoint> getAllUserPoints() {
        List<UserPoint> userPointsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String user = cursor.getString(cursor.getColumnIndexOrThrow(USER));
                    int points = cursor.getInt(cursor.getColumnIndexOrThrow(POINTS));
                    String time = cursor.getString(cursor.getColumnIndexOrThrow(TIME));
                    UserPoint userPoint = new UserPoint(user, points, time);
                    userPointsList.add(userPoint);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userPointsList;
    }

    public void updatePoints(String user, int points, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues values = new ContentValues();
            values.put(POINTS, points);
            values.put(TIME, time);
            db.update(TABLE_NAME, values, USER + "=?", new String[]{user});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public void deletePoints(String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete(TABLE_NAME, USER + "=?", new String[]{user});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    public int getPoints(String user) {
        int points = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_NAME, new String[]{POINTS}, USER + "=?", new String[]{user}, null, null, null);
            if (cursor.moveToFirst()) {
                points = cursor.getInt(cursor.getColumnIndexOrThrow(POINTS));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return points;
    }
    public void close(){
        SQLiteDatabase db = this.getReadableDatabase();
        SQLiteDatabase db2 = this.getWritableDatabase();
        db.close();
        db2.close();
    }
}
