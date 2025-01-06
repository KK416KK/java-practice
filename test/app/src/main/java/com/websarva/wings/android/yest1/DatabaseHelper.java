package com.websarva.wings.android.yest1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "inputer.db";
    private static final int DATABASE_VERSION = 3;

    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        //データベースを作成
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE inputer (");
        sb.append("_id INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append("date TEXT,");
        sb.append("item TEXT,");
        sb.append("money INTEGER,");
        sb.append("zeikin TEXT,");
        sb.append("price TEXT,");
        sb.append("kanjo TEXT,");
        sb.append("kanjot INTEGER");
        sb.append(");");
        String sql = sb.toString();

        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,int newVersion){}

    public String[][] getProductsData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        String[][] data = null;
        try {
            cursor = db.rawQuery("SELECT * FROM inputer", null);

            data = new String[cursor.getCount()][cursor.getColumnCount()];
            int index = 0;

            while (cursor.moveToNext()) {  
                for (int cindex = 0; cindex < cursor.getColumnCount(); cindex++) {
                    data[index][cindex] = cursor.getString(cindex);
                }
                index++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
        cursor.close();
        return data;
    }

    public void updateitem(){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
    }
    public void deleteDataById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.delete("inputer", "_id = ?", new String[]{String.valueOf(id)});
        } finally {
            db.close();
        }
    }
}
