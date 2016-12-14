package com.lltsbuildingsupply.randsdoors;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.lltsbuildingsupply.randsdoors.data.DoorContract;

/**
 * Created by Admin on 12/12/2016.
 */

public class DoorCursorAdapter extends CursorAdapter{

    public DoorCursorAdapter(Context context, Cursor cursor){ super (context, cursor, 0);}

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ///MANUFACTURER IS NOT INCLUDED IN THIS VIEW///
        TextView nameTextView = (TextView) view.findViewById(R.id.door_name);
        TextView swingTextView = (TextView) view.findViewById(R.id.door_swing);
        TextView styleTextView = (TextView) view.findViewById(R.id.door_style);
        TextView heightTextView = (TextView) view.findViewById(R.id.door_height);
        TextView widthTextView = (TextView) view.findViewById(R.id.door_width);
        TextView colorTextView = (TextView) view.findViewById(R.id.door_color);
        TextView priceTextView = (TextView) view.findViewById(R.id.door_price);
        TextView int_extTextView = (TextView) view.findViewById(R.id.door_int_ext);
        TextView countTextView = (TextView) view.findViewById(R.id.door_count);



        int nameColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_NAME);
        int swingColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_SWING);
        int styleColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_STYLE);
        int heightColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_HEIGHT);
        int widthColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_WIDTH);
        int colorColumIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_COLOR_TEXTURE);
        int priceColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_PRICE);
        int intExtColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_INT_EXT);
        int countColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_COUNT);

        String door_name = cursor.getString(nameColumnIndex);
        String door_style = cursor.getString(styleColumnIndex);
        int door_swing_int = cursor.getInt(swingColumnIndex);
        int door_height = cursor.getInt(heightColumnIndex);
        int door_width = cursor.getInt(widthColumnIndex);
        String door_color = cursor.getString(colorColumIndex);
        int door_price = cursor.getInt(priceColumnIndex);
        int door_count = cursor.getInt(countColumnIndex);
        int door_int_ext = cursor.getInt(intExtColumnIndex);

        nameTextView.setText(door_name);

        if(TextUtils.isEmpty(door_style)){door_style="default";}
        styleTextView.setText(door_style);
        if(TextUtils.isEmpty(door_color)){door_color="default";}
        colorTextView.setText(door_color);

        heightTextView.setText(String.valueOf(door_height));
        widthTextView.setText(String.valueOf(door_width));
        priceTextView.setText(String.valueOf(door_price));
        countTextView.setText(String.valueOf(door_count));

        String door_swing_string;
        switch (door_swing_int){
            case 0: door_swing_string = "not hung";
                break;
            case 1: door_swing_string = "Right Hand";
                break;
            case 2: door_swing_string = "Left Hand";
                break;
            default: door_swing_string = "door swing is broken in adapter";
        }
        swingTextView.setText(door_swing_string);

        String door_int_ext_string;
        switch (door_int_ext){
            case 0: door_int_ext_string= "Interior";
                break;
            case 1: door_int_ext_string = "Exterior";
                break;
            default: door_int_ext_string = "door int/ext broken in adapter";
        }
        int_extTextView.setText(door_int_ext_string);



    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }
}
