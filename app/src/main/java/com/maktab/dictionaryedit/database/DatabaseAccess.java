package com.maktab.dictionaryedit.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.maktab.dictionaryedit.model.Words;

import java.util.ArrayList;

public class DatabaseAccess {
    private static final String TABLE_EN = "EN2FA";
    private static final String TABLE_FA = "FA2EN";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_EN = "EN";
    private static final String COLUMN_FA = "FA";


    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DatabaseAccess instance;
    Cursor c = null;

    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }

        return instance;
    }

    public void open() {
        this.db = openHelper.getWritableDatabase();

    }

    public void close() {
        if (db != null) {
            this.db.close();
        }
    }

    public ArrayList<Words> listEnglishWords() {
        String sql = "select * from " + TABLE_EN;
        //SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Words> storeWords = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                //    int id = cursor.getInt(0);
                String wordEn = cursor.getString(0);
                String wordFa = cursor.getString(1);
                storeWords.add(new Words(wordEn, wordFa));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeWords;
    }

    public ArrayList<Words> listPersianWords() {
        String sql = "select * from " + TABLE_FA;
        //SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Words> storeWords = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            do {
                //    int id = cursor.getInt(0);
                String wordEn = cursor.getString(0);
                String wordFa = cursor.getString(1);
                storeWords.add(new Words(wordEn, wordFa));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return storeWords;
    }


    public void updateEnglish(Words words) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EN, words.getEnword());
        values.put(COLUMN_FA, words.getFaword());
        db.update(TABLE_EN, values, COLUMN_EN + " = ?", new String[]{words.getEnword()});
    }

    public void updatePersian(Words words) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EN, words.getEnword());
        values.put(COLUMN_FA, words.getFaword());
        db.update(TABLE_FA, values, COLUMN_EN + " = ?", new String[]{words.getEnword()});
    }

    public void deleteEnglish(Words words) {
        db.delete(TABLE_EN, COLUMN_EN + " = ?", new String[]{words.getEnword()});
    }


    public void deletePersian(Words words) {
        db.delete(TABLE_FA, COLUMN_EN + " = ?", new String[]{words.getEnword()});
    }

   public void addWordEn(Words words) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EN, words.getEnword());
        values.put(COLUMN_FA, words.getFaword());
        db.insert(TABLE_EN, null, values);
    }

    public void addWordFa(Words words) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_EN, words.getEnword());
        values.put(COLUMN_FA, words.getFaword());
        db.insert(TABLE_FA, null, values);
    }


    /*
    public String getAddress(String name) {
        c = db.rawQuery("select address from Table1 where name= '" + name + "'", new String[]{});
        StringBuffer buffer = new StringBuffer();
        while (c.moveToNext()) {
            String address = c.getString(0);
            buffer.append("" + address);
        }
        return buffer.toString();
    }

     */


}



