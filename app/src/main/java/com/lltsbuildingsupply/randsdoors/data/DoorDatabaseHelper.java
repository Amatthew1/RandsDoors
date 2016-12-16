package com.lltsbuildingsupply.randsdoors.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.lltsbuildingsupply.randsdoors.data.DoorContract.DoorEntry;

/**
 * Created by Admin on 12/11/2016.
 */

public class DoorDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "door_shop.db";
    private static final int DATABASE_VERSION = 1;

    public DoorDatabaseHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_DOORS_TABLE = "CREATE TABLE "
                + DoorEntry.TABLE_NAME + "("
                + DoorEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DoorEntry.COLUMN_DOOR_NAME + " TEXT NOT NULL," //customer name required
                + DoorEntry.COLUMN_DOOR_STYLE + " TEXT," //door style not required
                + DoorEntry.COLUMN_DOOR_SWING + " INTEGER NOT NULL, " //swing of door, might be unhung
                + DoorEntry.COLUMN_DOOR_HEIGHT + " INTEGER NOT NULL, " //height of door in inches
                + DoorEntry.COLUMN_DOOR_WIDTH + " INTEGER NOT NULL, " //width of door in inches
                + DoorEntry.COLUMN_DOOR_COLOR_TEXTURE + " TEXT, " //color of door not required
                + DoorEntry.COLUMN_DOOR_MANUFACTURER + " INT NOT NULL, " //maker of door array manufacturer_array list
                + DoorEntry.COLUMN_DOOR_PRICE + " INTEGER NOT NULL, " //SALES price of door
                + DoorEntry.COLUMN_DOOR_INT_EXT + " INTEGER NOT NULL, " //interior or exterior door
                + DoorEntry.COLUMN_DOOR_PICTURE + " INTEGER NOT NULL DEFAULT 0, "
                + DoorEntry.COLUMN_DOOR_COUNT + " INTEGER NOT NULL);"; //current count of door
        sqLiteDatabase.execSQL(SQL_CREATE_DOORS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
