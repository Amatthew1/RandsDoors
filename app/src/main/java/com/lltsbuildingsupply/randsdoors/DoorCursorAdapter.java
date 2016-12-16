package com.lltsbuildingsupply.randsdoors;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lltsbuildingsupply.randsdoors.data.DoorContract;

/**
 * Created by Admin on 12/12/2016.
 */

public class DoorCursorAdapter extends CursorAdapter {

    protected ListView mListView;

    public DoorCursorAdapter(Context context, Cursor cursor) {

        super(context, cursor, 0);

    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        ///MANUFACTURER IS NOT INCLUDED IN THIS VIEW///
        TextView nameTextView = (TextView) view.findViewById(R.id.door_name);
        TextView swingTextView = (TextView) view.findViewById(R.id.door_swing);
        TextView styleTextView = (TextView) view.findViewById(R.id.door_style);
        TextView heightTextView = (TextView) view.findViewById(R.id.door_height);
        TextView widthTextView = (TextView) view.findViewById(R.id.door_width);
        TextView colorTextView = (TextView) view.findViewById(R.id.door_color);
        TextView priceTextView = (TextView) view.findViewById(R.id.door_price);
        TextView int_extTextView = (TextView) view.findViewById(R.id.door_int_ext);
        final TextView countTextView = (TextView) view.findViewById(R.id.door_count);

        int rowIdColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_NAME);
        int swingColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_SWING);
        int styleColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_STYLE);
        int heightColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_HEIGHT);
        int widthColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_WIDTH);
        int colorColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_COLOR_TEXTURE);
        int priceColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_PRICE);
        int intExtColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_INT_EXT);
        int countColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_COUNT);

        final int rowId = cursor.getInt(rowIdColumnIndex);
        String door_name = cursor.getString(nameColumnIndex);
        String door_style = cursor.getString(styleColumnIndex);
        int door_swing_int = cursor.getInt(swingColumnIndex);
        int door_height = cursor.getInt(heightColumnIndex);
        int door_width = cursor.getInt(widthColumnIndex);
        String door_color = cursor.getString(colorColumnIndex);
        int door_price = cursor.getInt(priceColumnIndex);
        final int door_count = cursor.getInt(countColumnIndex);
        int door_int_ext = cursor.getInt(intExtColumnIndex);

        nameTextView.setText(door_name);

        if (TextUtils.isEmpty(door_style)) {
            door_style = "default";
        }
        styleTextView.setText(door_style);
        if (TextUtils.isEmpty(door_color)) {
            door_color = "default";
        }
        colorTextView.setText(door_color);

        heightTextView.setText(String.valueOf(door_height));
        widthTextView.setText(String.valueOf(door_width));
        priceTextView.setText(String.valueOf((door_price * door_count)));
        countTextView.setText(String.valueOf(door_count));

        String door_swing_string;
        switch (door_swing_int) {
            case 0:
                door_swing_string = "not hung";
                break;
            case 1:
                door_swing_string = "Right Hand";
                break;
            case 2:
                door_swing_string = "Left Hand";
                break;
            default:
                door_swing_string = "door swing is broken in adapter";
        }
        swingTextView.setText(door_swing_string);

        String door_int_ext_string;
        switch (door_int_ext) {
            case 0:
                door_int_ext_string = "Interior";
                break;
            case 1:
                door_int_ext_string = "Exterior";
                break;
            default:
                door_int_ext_string = "door int/ext broken in adapter";
        }
        int_extTextView.setText(door_int_ext_string);

        //BUTTONS

        Button orderButton = (Button) view.findViewById(R.id.door_order);
        Button receiveButton = (Button) view.findViewById(R.id.door_receive);

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int doorCount = Integer.parseInt(countTextView.getText().toString());
                if (door_count > 0) {
                    doorCount = doorCount - 1;
                    countTextView.setText(Integer.toString(doorCount));
                    ContentValues values = new ContentValues();
                    values.put(DoorContract.DoorEntry.COLUMN_DOOR_COUNT, doorCount);
                    Uri mCurrentDoorUri = ContentUris.withAppendedId(DoorContract.DoorEntry.CONTENT_URI, rowId);
                    int rowsAffected = context.getContentResolver().update(mCurrentDoorUri, values, null, null);
                    if (rowsAffected == 0) {
                        Toast.makeText(context.getApplicationContext(), "error updating count", Toast.LENGTH_SHORT);
                    }
                }
            }
        });
        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int doorCount = Integer.parseInt(countTextView.getText().toString());
                doorCount = doorCount + 1;
                countTextView.setText(Integer.toString(doorCount));
                ContentValues values = new ContentValues();
                values.put(DoorContract.DoorEntry.COLUMN_DOOR_COUNT, doorCount);
                Uri mCurrentDoorUri = ContentUris.withAppendedId(DoorContract.DoorEntry.CONTENT_URI, rowId);
                int rowsAffected = context.getContentResolver().update(mCurrentDoorUri, values, null, null);
                if (rowsAffected == 0) {
                    Toast.makeText(context.getApplicationContext(), "error updating count", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

    }

    protected static class RowViewHolder {
        public TextView mOrder_Sale;
        public TextView mReceive;
    }


}