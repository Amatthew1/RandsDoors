package com.lltsbuildingsupply.randsdoors.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Admin on 12/11/2016.
 */

public class DoorContract {

    public static final String CONTENT_AUTHORITY = "com." +
            "lltsbuildingsupply.randsdoors";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_DOORS = "doors";

    // no implementation because nothing should be calling this class
    private DoorContract() {
    }

    public static final class DoorEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_DOORS);

        // MIME TYPE for the full list of doors
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DOORS;
        //MIME TYPE for single door entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DOORS;

        public final static String TABLE_NAME = "doors";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_DOOR_NAME = "name";
        public final static String COLUMN_DOOR_SWING = "swing";
        public final static String COLUMN_DOOR_HEIGHT = "height";
        public final static String COLUMN_DOOR_STYLE = "style";
        public final static String COLUMN_DOOR_WIDTH = "width";
        public final static String COLUMN_DOOR_COLOR_TEXTURE = "color_texture";
        public final static String COLUMN_DOOR_PRICE = "price";
        public final static String COLUMN_DOOR_MANUFACTURER = "manufacturer";
        public final static String COLUMN_DOOR_COUNT = "count";
        public final static String COLUMN_DOOR_INT_EXT = "int_ext";
        public final static String COLUMN_DOOR_PICTURE = "picture";

        public static final int SWING_UNHUNG = 0;
        public static final int SWING_RIGHT_HAND = 1;
        public static final int SWING_LEFT_HAND = 2;

        public static final int INTERIOR = 0;
        public static final int EXTERIOR = 1;

        public static final int JELDWEN = 0;
        public static final int MASONITE = 1;
        public static final int OTHER = 2;


    }

}
