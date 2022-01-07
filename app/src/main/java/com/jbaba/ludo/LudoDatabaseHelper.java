package com.jbaba.ludo;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.Serializable;

public class LudoDatabaseHelper extends SQLiteOpenHelper
{
    private final static String DB_NAME = "ludo";
    private final static int DB_VERSION = 1;

    public LudoDatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        updateMyDatabase(db, 1, DB_VERSION);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //db.execSQL("DROP TABLE IF EXISTS SCORE");
        //onCreate(db);
    }

    public static void insertScore(SQLiteDatabase db, int playerScore, int computerScore)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put("PLAYER_SCORE", playerScore);
        contentValues.put("COMPUTER_SCORE", computerScore);
        db.insert("SCORE", null, contentValues);
    }

    public static void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        if(oldVersion < 1)
        {
            db.execSQL("CREATE TABLE SCORE(_id INTEGER PRIMARY KEY AUTOINCREMENT, "+" PLAYER_SCORE INTEGER, "+" COMPUTER_SCORE INTEGER);");
            insertScore(db, 0, 0);
        }
    }
}
