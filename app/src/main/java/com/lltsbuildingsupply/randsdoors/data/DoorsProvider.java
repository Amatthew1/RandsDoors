package com.lltsbuildingsupply.randsdoors.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Admin on 12/11/2016.
 */

public class DoorsProvider extends ContentProvider {


    private static final int DOORS = 001;
    private static final int DOORS_ID = 010;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(DoorContract.CONTENT_AUTHORITY, DoorContract.PATH_DOORS, DOORS);
        sUriMatcher.addURI(DoorContract.CONTENT_AUTHORITY, DoorContract.PATH_DOORS + "/#", DOORS_ID);
    }

    private DoorDatabaseHelper mDBHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mDBHelper = new DoorDatabaseHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDBHelper.getReadableDatabase();

        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case DOORS:
                cursor = database.query(DoorContract.DoorEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case DOORS_ID:
                selection = DoorContract.DoorEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(DoorContract.DoorEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException(("Cannot query URI: " + uri + "It might be malformed"));
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DOORS:
                return DoorContract.DoorEntry.CONTENT_LIST_TYPE;
            case DOORS_ID:
                return DoorContract.DoorEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DOORS:
                return insertDoor(uri, values);
            default:
                throw new IllegalArgumentException("This uri: " + uri + "cannot be inserted");
        }
    }

    private Uri insertDoor(Uri uri, ContentValues values) {
        /////////name
        String name = values.getAsString(DoorContract.DoorEntry.COLUMN_DOOR_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Door requires a name");
        }
        /////////height
        int height = values.getAsInteger(DoorContract.DoorEntry.COLUMN_DOOR_HEIGHT);
        if (height < 1) {
            throw new IllegalArgumentException("Door requires a positive non-zero height");
        }
        /////////width
        int width = values.getAsInteger(DoorContract.DoorEntry.COLUMN_DOOR_WIDTH);
        if (width < 1) {
            throw new IllegalArgumentException("Door requires a positive non-zero height");
        }
        /////////swing
        int swing = values.getAsInteger(DoorContract.DoorEntry.COLUMN_DOOR_SWING);
        if (swing < 0 || swing > 2) {
            throw new IllegalArgumentException("Mishap on swing assignment");
        }


        /////////style           no check required
        //String style = values.getAsString(DoorContract.DoorEntry.COLUMN_DOOR_STYLE);

        /////////color/texture     no check required
        //String color_texture = values.getAsString(DoorContract.DoorEntry.COLUMN_DOOR_COLOR_TEXTURE);

        ////////price
        int price = values.getAsInteger(DoorContract.DoorEntry.COLUMN_DOOR_PRICE);
        if (price < 1) {
            throw new IllegalArgumentException("Door must have a positive value for price");
        }

        ////////count
        Integer count = values.getAsInteger(DoorContract.DoorEntry.COLUMN_DOOR_COUNT);
        if (count == null) {
            throw new IllegalArgumentException("count must have value, negative, 0 , or positive work");
        }

        ////////manufacturer
        int manufacturer = values.getAsInteger(DoorContract.DoorEntry.COLUMN_DOOR_MANUFACTURER);
        if (manufacturer < 0 || manufacturer > 2) {
            throw new IllegalArgumentException("Manufacturer must be from listed values");
        }

        ////////int/ext
        int int_ext = values.getAsInteger(DoorContract.DoorEntry.COLUMN_DOOR_INT_EXT);
        if (int_ext < 0 || int_ext > 1) {
            throw new IllegalArgumentException("int/ext must be one or the other, but not neither or both");
        }

        ////////////image
        String image = values.getAsString(DoorContract.DoorEntry.COLUMN_DOOR_PICTURE);
        if (image == null) {
            throw new IllegalArgumentException("Door requires an image");
        }

        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        long id = database.insert(DoorContract.DoorEntry.TABLE_NAME, null, values);
        Log.v("InventoryDisplay", "New row ID" + id);
        if (id == -1) {
            Toast.makeText(getContext(), "Error saving door", Toast.LENGTH_LONG).show();
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DOORS:
                getContext().getContentResolver().notifyChange(uri, null);
                return database.delete(DoorContract.DoorEntry.TABLE_NAME, selection, selectionArgs);
            case DOORS_ID:
                selection = DoorContract.DoorEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                getContext().getContentResolver().notifyChange(uri, null);
                return database.delete(DoorContract.DoorEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Delete does not work with this uri: " + uri);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DOORS:
                return updateDoor(uri, values, selection, selectionArgs);
            case DOORS_ID:
                selection = DoorContract.DoorEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateDoor(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update cannot be completed with this uri: " + uri);
        }
    }

    private int updateDoor(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = mDBHelper.getWritableDatabase();
        if (values.size() == 0) {
            return 0;
        }

        if (values.containsKey(DoorContract.DoorEntry.COLUMN_DOOR_NAME)) {
            String name = values.getAsString(DoorContract.DoorEntry.COLUMN_DOOR_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Door requires a name");
            }
        }

        if (values.containsKey(DoorContract.DoorEntry.COLUMN_DOOR_HEIGHT)) {
            int height = values.getAsInteger(DoorContract.DoorEntry.COLUMN_DOOR_HEIGHT);
            if (height < 1) {
                throw new IllegalArgumentException("Door requires a positive non-zero height");
            }
        }

        if (values.containsKey(DoorContract.DoorEntry.COLUMN_DOOR_WIDTH)) {
            int width = values.getAsInteger(DoorContract.DoorEntry.COLUMN_DOOR_WIDTH);
            if (width < 1) {
                throw new IllegalArgumentException("Door requires a positive non-zero height");
            }
        }
        if (values.containsKey(DoorContract.DoorEntry.COLUMN_DOOR_SWING)) {
            int swing = values.getAsInteger(DoorContract.DoorEntry.COLUMN_DOOR_SWING);
            if (swing < 0 || swing > 2) {
                throw new IllegalArgumentException("Mishap on swing assignment");
            }
        }

        if (values.containsKey(DoorContract.DoorEntry.COLUMN_DOOR_COUNT)) {
            Integer count = values.getAsInteger(DoorContract.DoorEntry.COLUMN_DOOR_COUNT);
            if (count == null) {
                throw new IllegalArgumentException("count must have value, negative, 0 , or positive work");
            }
        }

        if (values.containsKey(DoorContract.DoorEntry.COLUMN_DOOR_PRICE)) {
            int price = values.getAsInteger(DoorContract.DoorEntry.COLUMN_DOOR_PRICE);
            if (price < 1) {
                throw new IllegalArgumentException("Door must have a positive value for price");
            }
        }
        if (values.containsKey(DoorContract.DoorEntry.COLUMN_DOOR_MANUFACTURER)) {
            int manufacturer = values.getAsInteger(DoorContract.DoorEntry.COLUMN_DOOR_MANUFACTURER);
            if (manufacturer < 0 || manufacturer > 2) {
                throw new IllegalArgumentException("Manufacturer must be from listed values");
            }
        }

        if (values.containsKey(DoorContract.DoorEntry.COLUMN_DOOR_PICTURE)) {
            String image = values.getAsString(DoorContract.DoorEntry.COLUMN_DOOR_PICTURE);
            if (image == null) {
                throw new IllegalArgumentException("Door requires an image");
            }
        }

        if (values.containsKey(DoorContract.DoorEntry.COLUMN_DOOR_INT_EXT)) {
            int int_ext = values.getAsInteger(DoorContract.DoorEntry.COLUMN_DOOR_INT_EXT);
            if (int_ext < 0 || int_ext > 1) {
                throw new IllegalArgumentException("int/ext must be one or the other, but not neither or both");
            }
        }


        getContext().getContentResolver().notifyChange(uri, null);
        return database.update(DoorContract.DoorEntry.TABLE_NAME, values, selection, selectionArgs);
    }
}
