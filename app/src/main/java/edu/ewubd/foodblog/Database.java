package edu.ewubd.foodblog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {

    public static final String DBNAME = "FoodBlog.db";

    public Database(Context context) {
        super(context, "FoodBlog.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create Table users(name TEXT, email TEXT primary key, password1 TEXT, password2 TEXT,phone TEXT, address TEXT)");
        MyDB.execSQL("create Table currentUser(currentUser TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("drop Table if exists users");
        MyDB.execSQL("drop Table if exists currentUser");
    }

    public Boolean insertData(String name, String email, String password1, String password2,String phone,String address)
    {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("password1", password1);
        contentValues.put("password2", password2);
        contentValues.put("phone", phone);
        contentValues.put("address", address);

        long result = MyDB.insert("users",null,contentValues);
        if(result==-1) return false;
        else
            return true;
    }


    public boolean checkemail(String email){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where email = ?", new String[] {email});
        if (cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public boolean checkemailpassword(String email, String password1){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where email = ? and password1 = ?", new String[] {email,password1});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public boolean currentUser(String userMail){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("currentUser", userMail);
        long result = MyDB.insert("currentUser", null, contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }

    public String getCurrentUser(){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from currentUser", null);
        cursor.moveToLast();
        return cursor.getString(0);
    }
    public void clearCurrentUser(){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        MyDB.execSQL("Delete from currentUser");
    }

    public Cursor getUserDetail(String userMail){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where email = ?", new String[] {userMail});
        return cursor;
    }

}
