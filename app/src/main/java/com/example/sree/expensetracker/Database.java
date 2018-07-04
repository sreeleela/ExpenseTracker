package com.example.sree.expensetracker;

/**
 * Created by Sree on 4/22/2018.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ExpenseTracker.db";
    public static final String USERS_TABLE_NAME = "users";
    public static final String USERS_COLUMN_FIRST_NAME = "firstName";
    public static final String USERS_COLUMN_LAST_NAME = "lastName";
    public static final String USERS_COLUMN_EMAILID = "emailId";
    public static final String USERS_COLUMN_PASSWORD = "password";

    public static final String EXPENSES_TABLE_NAME = "expenses";
    public static final String EXPENSES_COLUMN_ID = "id";
    public static final String EXPENSES_COLUMN_USER = "useremail";
    public static final String EXPENSES_COLUMN_DESCRIPTION = "description";
    public static final String EXPENSES_COLUMN_AMOUNT = "amount";
    public static final String EXPENSES_COLUMN_DATE = "date";

    public Database(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(
                "create table "+USERS_TABLE_NAME+
                        "("+USERS_COLUMN_EMAILID+" text primary key, "+
                        USERS_COLUMN_FIRST_NAME+" text, "+
                        USERS_COLUMN_LAST_NAME+" text, "+
                        USERS_COLUMN_PASSWORD+" text)"
        );
        db.execSQL(
                "create table "+EXPENSES_TABLE_NAME+
                        "("+EXPENSES_COLUMN_ID+" integer primary key autoincrement, "+
                        EXPENSES_COLUMN_USER+" text , "+
                        EXPENSES_COLUMN_DESCRIPTION+" text, "+
                        EXPENSES_COLUMN_AMOUNT+" real, "+
                        EXPENSES_COLUMN_DATE+" date," +
                        "FOREIGN KEY ("+EXPENSES_COLUMN_USER+") REFERENCES "+USERS_TABLE_NAME+"("+USERS_COLUMN_EMAILID+"))"
        );
        insertAdmin(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public void insertAdmin (SQLiteDatabase db)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COLUMN_EMAILID, "admin@mail.com");
        contentValues.put(USERS_COLUMN_FIRST_NAME, "admin");
        contentValues.put(USERS_COLUMN_LAST_NAME, "admin");
        contentValues.put(USERS_COLUMN_PASSWORD, "password");
        db.insert(USERS_TABLE_NAME, null, contentValues);

        ContentValues contentValuesTwo = new ContentValues();
        contentValuesTwo.put(USERS_COLUMN_EMAILID, "sree@mail.com");
        contentValuesTwo.put(USERS_COLUMN_FIRST_NAME, "sree");
        contentValuesTwo.put(USERS_COLUMN_LAST_NAME, "leela");
        contentValuesTwo.put(USERS_COLUMN_PASSWORD, "password");
        db.insert(USERS_TABLE_NAME, null, contentValuesTwo);
    }

    public String getPassword(String emailId)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from users where emailId='"+emailId+"'", null );
        res.moveToFirst();
        if(res.getCount() == 0)
            return null+":"+null+":"+null+":"+null;
        else
        {
            String email = res.getString(res.getColumnIndex(USERS_COLUMN_EMAILID));
            String password = res.getString(res.getColumnIndex(USERS_COLUMN_PASSWORD));
            String firstName = res.getString(res.getColumnIndex(USERS_COLUMN_FIRST_NAME));
            String lastName = res.getString(res.getColumnIndex(USERS_COLUMN_LAST_NAME));
            String login = email+":"+password+":"+firstName+":"+lastName;
            return email+":"+password+":"+firstName+":"+lastName;
        }
    }

    public boolean insertExpense (SQLiteDatabase db,String email, String description,Float amount,String date)
    {
        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(EXPENSES_COLUMN_USER, email);
            contentValues.put(EXPENSES_COLUMN_DESCRIPTION, description);
            contentValues.put(EXPENSES_COLUMN_AMOUNT, amount);
            contentValues.put(EXPENSES_COLUMN_DATE, date);
            db.insert(EXPENSES_TABLE_NAME, null, contentValues);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public ArrayList<Expense> getExpenses (String email)
    {
        ArrayList<Expense> expensesList = new ArrayList<Expense>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from expenses where useremail='"+email+"' order by date desc", null );
        res.moveToFirst();
        while(res.isAfterLast() == false)
        {
            Expense expenseObj = new Expense();
            expenseObj.setId(res.getInt(res.getColumnIndex(EXPENSES_COLUMN_ID)));
            expenseObj.setUseremail(res.getString(res.getColumnIndex(EXPENSES_COLUMN_USER)));
            expenseObj.setDescription(res.getString(res.getColumnIndex(EXPENSES_COLUMN_DESCRIPTION)));
            expenseObj.setAmount(res.getFloat(res.getColumnIndex(EXPENSES_COLUMN_AMOUNT)));
            expenseObj.setDate(res.getString(res.getColumnIndex(EXPENSES_COLUMN_DATE)));
            expensesList.add(expenseObj);
            res.moveToNext();
        }
        return expensesList;
    }
    public ArrayList<String> getDates (String email)
    {
        ArrayList<String> dateList = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select distinct date from expenses where useremail='"+email+"' order by date desc", null );
        res.moveToFirst();
        while(res.isAfterLast() == false)
        {
            String dt = res.getString(res.getColumnIndex(EXPENSES_COLUMN_DATE));
            dateList.add(dt);
            res.moveToNext();
        }
        return dateList;
    }
    public boolean updateAccount (String firstName,String lastName,String email, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(USERS_COLUMN_FIRST_NAME, firstName);
            contentValues.put(USERS_COLUMN_LAST_NAME, lastName);
            contentValues.put(USERS_COLUMN_PASSWORD, password);
            int a = db.update(USERS_TABLE_NAME, contentValues, USERS_COLUMN_EMAILID + "='" + email + "'", null);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public boolean deleteExpense (String email,int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        try
        {
            db.delete(EXPENSES_TABLE_NAME, EXPENSES_COLUMN_USER + "='" + email + "' and id=" + id, null);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public boolean updateExpense (String email,int id,String description,Float amount,String date)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        try
        {
            ContentValues contentValues = new ContentValues();
            contentValues.put(EXPENSES_COLUMN_DESCRIPTION, description);
            contentValues.put(EXPENSES_COLUMN_AMOUNT, amount);
            contentValues.put(EXPENSES_COLUMN_DATE, date);
            db.update(EXPENSES_TABLE_NAME, contentValues, EXPENSES_COLUMN_USER + "='" + email + "' and id=" + id, null);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
}
