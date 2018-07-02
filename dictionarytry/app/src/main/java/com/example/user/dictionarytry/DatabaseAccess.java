package com.example.user.dictionarytry;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
public class DatabaseAccess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context,"words.sqlite",null,1);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all quotes from the database.
     *
     * @return a List of quotes
     */
    public String getMeaning(String a) {
        int f=0;
        String p2=" ";
        String query="SELECT * FROM words where word=?";
        Cursor cursor = database.rawQuery(query,new String[]{a+" "});

        while(cursor.moveToNext()) {
            String p1=cursor.getString(1);
            String p3=cursor.getString(2);

            p2=p2+p1+"-"+p3+"\n\n";
            f=1;

        }
        if(f==0)
            p2="Word not found";

        cursor.close();
        return p2;
    }
    public String getRandom()
    {
        String p1;
        String query="SELECT * FROM words order by RANDOM() LIMIT 1";
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToNext())
        {
             p1=cursor.getString(0);
        }
        else
            p1="Some error occured";
        return p1;
    }
}



