package com.lltsbuildingsupply.randsdoors;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lltsbuildingsupply.randsdoors.data.DoorContract;


public class InventoryDisplay extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DOOR_LOADER_ID = 0;
    DoorCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_display);
        FloatingActionButton door_fab = (FloatingActionButton) findViewById(R.id.door_fab);
        door_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InventoryDisplay.this, ProductEditor.class);
                startActivity(intent);
            }
        });
        ListView doorListView = (ListView) findViewById(R.id.list);
        View emptyView = findViewById(R.id.empty_view);
        doorListView.setEmptyView(emptyView);

        mCursorAdapter = new DoorCursorAdapter(this, null);
        doorListView.setAdapter(mCursorAdapter);
        doorListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(InventoryDisplay.this, ProductEditor.class);
                Uri currentDoorUri = ContentUris.withAppendedId(DoorContract.DoorEntry.CONTENT_URI, id);
                intent.setData(currentDoorUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(DOOR_LOADER_ID, null, this);
    }

    private void insertDoor() {
        ContentValues values = new ContentValues();
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_NAME, "RANDS TEST DOOR");
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_STYLE, "BIG ORK DOOR");
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_SWING, DoorContract.DoorEntry.SWING_RIGHT_HAND);
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_HEIGHT, 80);
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_WIDTH, 36);
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_MANUFACTURER, DoorContract.DoorEntry.JELDWEN);
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_PRICE, 80);
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_COLOR_TEXTURE, "WHITE");
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_COUNT, "0");
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_INT_EXT, DoorContract.DoorEntry.INTERIOR);
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_PICTURE, 0);

        Uri newUri = getContentResolver().insert(DoorContract.DoorEntry.CONTENT_URI, values);
    }

    private void deleteAllDoors() {
        int rowsDeleted = getContentResolver().delete(DoorContract.DoorEntry.CONTENT_URI, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_test_data:
                insertDoor();
                return true;
            case R.id.action_delete_all_entries:
                deleteAllDoors();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DoorContract.DoorEntry._ID,
                DoorContract.DoorEntry.COLUMN_DOOR_NAME,
                DoorContract.DoorEntry.COLUMN_DOOR_SWING,
                DoorContract.DoorEntry.COLUMN_DOOR_HEIGHT,
                DoorContract.DoorEntry.COLUMN_DOOR_STYLE,
                DoorContract.DoorEntry.COLUMN_DOOR_COUNT,
                DoorContract.DoorEntry.COLUMN_DOOR_MANUFACTURER,
                DoorContract.DoorEntry.COLUMN_DOOR_PRICE,
                DoorContract.DoorEntry.COLUMN_DOOR_COLOR_TEXTURE,
                DoorContract.DoorEntry.COLUMN_DOOR_WIDTH,
                DoorContract.DoorEntry.COLUMN_DOOR_INT_EXT
        };
        return new CursorLoader(this, DoorContract.DoorEntry.CONTENT_URI, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
