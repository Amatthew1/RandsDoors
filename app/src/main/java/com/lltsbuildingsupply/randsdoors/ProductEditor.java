package com.lltsbuildingsupply.randsdoors;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.lltsbuildingsupply.randsdoors.data.DoorContract;

/**
 * Created by Admin on 12/11/2016.
 */

public class ProductEditor extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_DOOR_LOADER = 1;
    private EditText mNameEditText;
    private EditText mStyleEditText;
    private EditText mHeightEditText;
    private EditText mWidthEditText;
    private EditText mColorEditText;
    private EditText mPriceEditText;
    private EditText mCountEditText;
    private Spinner mIntExtSpinner;
    private Spinner mManufactureSpinner;
    private Spinner mSwingSpinner;
    private ImageView mImageEditImage;
    private Button mPlusButton;
    private Button mMinusButton;

    private Uri imageUri;
    private int mIntExt = 0;
    private int mSwing = 0;
    private int mManufacture = 0;
    private Uri mCurrentDoorUri;
    private boolean mDoorHasChanged = false;
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            mDoorHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_editor);
        Intent intent = getIntent();
        mCurrentDoorUri = intent.getData();
        if (mCurrentDoorUri == null) {
            setTitle("Add a door");
            invalidateOptionsMenu();
        } else {
            setTitle("Edit this door");
            getLoaderManager().initLoader(EXISTING_DOOR_LOADER, null, this);
        }
        mNameEditText = (EditText) findViewById(R.id.edit_door_name);
        mStyleEditText = (EditText) findViewById(R.id.edit_door_style);
        mHeightEditText = (EditText) findViewById(R.id.edit_door_height);
        mWidthEditText = (EditText) findViewById(R.id.edit_door_width);
        mColorEditText = (EditText) findViewById(R.id.edit_door_color);
        mCountEditText = (EditText) findViewById(R.id.door_count);
        mPriceEditText = (EditText) findViewById(R.id.sales_price);
        mImageEditImage = (ImageView) findViewById(R.id.edit_door_image);
        mPlusButton = (Button) findViewById(R.id.plusButton);
        mPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mCount = (EditText) findViewById(R.id.door_count);
                int currentCount = Integer.parseInt(mCount.getText().toString().trim());
                currentCount += 1;
                mCount.setText(Integer.toString(currentCount));
            }
        });
        mMinusButton = (Button) findViewById(R.id.minusButton);
        mMinusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mCount = (EditText) findViewById(R.id.door_count);
                int currentCount = Integer.parseInt(mCount.getText().toString().trim());
                if (currentCount > 0) {
                    currentCount -= 1;
                }
                mCount.setText(Integer.toString(currentCount));
            }
        });


        mIntExtSpinner = (Spinner) findViewById(R.id.spinner_int_ext);
        mManufactureSpinner = (Spinner) findViewById(R.id.spinner_manufacturer);
        mSwingSpinner = (Spinner) findViewById(R.id.spinner_swing);
        setupSwingSpinner();
        setupManufacturersSpinner();
        setupIntExtSpinner();
        mNameEditText.setOnTouchListener(mTouchListener);
        mHeightEditText.setOnTouchListener(mTouchListener);
        mWidthEditText.setOnTouchListener(mTouchListener);
        mColorEditText.setOnTouchListener(mTouchListener);
        mCountEditText.setOnTouchListener(mTouchListener);
        mStyleEditText.setOnTouchListener(mTouchListener);
        mPriceEditText.setOnTouchListener(mTouchListener);
        mIntExtSpinner.setOnTouchListener(mTouchListener);
        mManufactureSpinner.setOnTouchListener(mTouchListener);
        mSwingSpinner.setOnTouchListener(mTouchListener);
        mImageEditImage.setOnTouchListener(mTouchListener);


    }

    private void setupSwingSpinner() {
        ArrayAdapter swingSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_swing_options, android.R.layout.simple_spinner_item);
        swingSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mSwingSpinner.setAdapter(swingSpinnerAdapter);
        mSwingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.swing_right))) {
                        mSwing = DoorContract.DoorEntry.SWING_RIGHT_HAND;
                    } else if (selection.equals(getString(R.string.swing_left))) {
                        mSwing = DoorContract.DoorEntry.SWING_LEFT_HAND;
                    } else {
                        mSwing = DoorContract.DoorEntry.SWING_UNHUNG;
                    }
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mSwing = 0;
            }
        });
    }

    private void setupIntExtSpinner() {
        ArrayAdapter intExtSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_int_ext_options, android.R.layout.simple_spinner_item);
        intExtSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mIntExtSpinner.setAdapter(intExtSpinnerAdapter);
        mIntExtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.interior))) {
                        mIntExt = DoorContract.DoorEntry.INTERIOR;
                    } else if (selection.equals(getString(R.string.exterior))) {
                        mIntExt = DoorContract.DoorEntry.EXTERIOR;
                    }
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mIntExt = 0;
            }
        });
    }

    private void setupManufacturersSpinner() {
        ArrayAdapter manufacturersSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.array_manufactorer_options, android.R.layout.simple_spinner_item);
        manufacturersSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mManufactureSpinner.setAdapter(manufacturersSpinnerAdapter);
        mManufactureSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {

                    if (selection.equals(getString(R.string.jeldwen))) {
                        mManufacture = DoorContract.DoorEntry.JELDWEN;
                    } else if (selection.equals(getString(R.string.masonite))) {
                        mManufacture = DoorContract.DoorEntry.MASONITE;
                    } else {
                        mManufacture = DoorContract.DoorEntry.OTHER;
                    }
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mManufacture = 0;
            }
        });
    }

    private void orderDoor() {
        String nameString = mNameEditText.getText().toString().trim();
        String styleString = mStyleEditText.getText().toString().trim();
        String colorString = mColorEditText.getText().toString().trim();
        String heightString = mHeightEditText.getText().toString().trim();
        String widthString = mWidthEditText.getText().toString().trim();
        String countString = mCountEditText.getText().toString().trim();

        String email = getString(R.string.sales);
        switch (mManufacture) {
            case 0:
                email = email + getString(R.string.jeldwen);
                break;
            case 1:
                email = email + getString(R.string.masonite);
                break;
            case 2:
                email = email + getString(R.string.other);
                break;
        }
        email = email + getString(R.string.com);


        String subject = getString(R.string.subject) + nameString;

        String body = getString(R.string.category_door_style) + styleString + getString(R.string.category_door_color)
                + colorString + getString(R.string.category_door_height) + heightString + getString(R.string.category_door_width)
                + widthString + getString(R.string.category_door_count) + countString;


        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);


        startActivity(emailIntent);
    }

    private void showOrderConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.order_dialog_message);
        builder.setPositiveButton(R.string.order_door_pos, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                orderDoor();
                return;
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void saveDoor() {
        String nameString = mNameEditText.getText().toString().trim();
        String styleString = mStyleEditText.getText().toString().trim();
        String colorString = mColorEditText.getText().toString().trim();
        /////////////////////////////////////////////////////////////////////////
        String heightString = mHeightEditText.getText().toString().trim();
        int heightInt = Integer.parseInt(heightString);
        String widthString = mWidthEditText.getText().toString().trim();
        int widthInt = Integer.parseInt(widthString);

        String priceString = mPriceEditText.getText().toString().trim();
        int priceInt = Integer.parseInt(priceString);
        String countString = mCountEditText.getText().toString().trim();
        int countInt = Integer.parseInt(countString);

        String imageString = "";
        if (imageUri != null) {
            imageString = imageUri.toString();
        }

        //////////
        ///////// to do: add the rest of the checks to see if door has been edited
        /////////
        if (mCurrentDoorUri == null && TextUtils.isEmpty(nameString) && TextUtils.isEmpty(styleString) && TextUtils.isEmpty(heightString) && mSwing == DoorContract.DoorEntry.SWING_UNHUNG) {
            return;
        }
        ContentValues values = new ContentValues();

        values.put(DoorContract.DoorEntry.COLUMN_DOOR_NAME, nameString);
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_STYLE, styleString);
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_HEIGHT, heightInt);
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_WIDTH, widthInt);
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_COUNT, countInt);
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_PRICE, priceInt);
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_COLOR_TEXTURE, colorString);
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_SWING, mSwing);
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_INT_EXT, mIntExt);
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_MANUFACTURER, mManufacture);
        values.put(DoorContract.DoorEntry.COLUMN_DOOR_PICTURE, imageString);

        if (mCurrentDoorUri == null) {
            Uri newRowUri = getContentResolver().insert(DoorContract.DoorEntry.CONTENT_URI, values);
        } else {
            int newUri = getContentResolver().update(mCurrentDoorUri, values, null, null);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mCurrentDoorUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        ImageView selectPicture = (ImageView) findViewById(R.id.edit_door_image);

        selectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pictureIntent;

                if (Build.VERSION.SDK_INT < 19) {
                    pictureIntent = new Intent(Intent.ACTION_GET_CONTENT);
                } else {
                    pictureIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    pictureIntent.addCategory(Intent.CATEGORY_OPENABLE);
                }
                pictureIntent.setType("image/*");
                startActivityForResult(Intent.createChooser(pictureIntent, "Select Picture"), 1);
                mDoorHasChanged = true;
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            // action cancelled
        }
        if (resultCode == RESULT_OK) {
            Uri selectedPicture = data.getData();
            imageUri = selectedPicture;


            mImageEditImage.setImageURI(imageUri);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveDoor();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case R.id.action_order:
                showOrderConfirmationDialog();
                return true;
            case R.id.home:
                if (!mDoorHasChanged) {
                    NavUtils.navigateUpFromSameTask(this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NavUtils.navigateUpFromSameTask(ProductEditor.this);
                            }
                        };
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton("discard", discardButtonClickListener);
        builder.setNegativeButton("keep editing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!mDoorHasChanged) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    private void showDeleteConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                deleteDoor();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });


        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteDoor() {
        if (mCurrentDoorUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentDoorUri, null, null);
            finish();
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                DoorContract.DoorEntry._ID,
                DoorContract.DoorEntry.COLUMN_DOOR_NAME,
                DoorContract.DoorEntry.COLUMN_DOOR_STYLE,
                DoorContract.DoorEntry.COLUMN_DOOR_SWING,
                DoorContract.DoorEntry.COLUMN_DOOR_HEIGHT,
                DoorContract.DoorEntry.COLUMN_DOOR_COUNT,
                DoorContract.DoorEntry.COLUMN_DOOR_INT_EXT,
                DoorContract.DoorEntry.COLUMN_DOOR_WIDTH,
                DoorContract.DoorEntry.COLUMN_DOOR_COLOR_TEXTURE,
                DoorContract.DoorEntry.COLUMN_DOOR_PRICE,
                DoorContract.DoorEntry.COLUMN_DOOR_MANUFACTURER,
                DoorContract.DoorEntry.COLUMN_DOOR_PICTURE};
        return new CursorLoader(this, mCurrentDoorUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_NAME);
            int swingColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_SWING);
            int styleColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_STYLE);
            int heightColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_HEIGHT);
            int widthColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_WIDTH);
            int colorColumIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_COLOR_TEXTURE);
            int priceColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_PRICE);
            int intExtColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_INT_EXT);
            int countColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_COUNT);
            int manufacturerColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_MANUFACTURER);
            int imageColumnIndex = cursor.getColumnIndex(DoorContract.DoorEntry.COLUMN_DOOR_PICTURE);

            String door_name = cursor.getString(nameColumnIndex);
            String door_style = cursor.getString(styleColumnIndex);
            int door_swing_int = cursor.getInt(swingColumnIndex);
            int door_height = cursor.getInt(heightColumnIndex);
            int door_width = cursor.getInt(widthColumnIndex);
            String door_color = cursor.getString(colorColumIndex);
            int door_price = cursor.getInt(priceColumnIndex);
            int door_count = cursor.getInt(countColumnIndex);
            int door_int_ext = cursor.getInt(intExtColumnIndex);
            int door_manufacturer = cursor.getInt(manufacturerColumnIndex);
            String door_image = cursor.getString(imageColumnIndex);
            imageUri = Uri.parse(door_image);

            mNameEditText.setText(door_name);
            mStyleEditText.setText(door_style);
            mColorEditText.setText(door_color);

            mHeightEditText.setText(Integer.toString(door_height));
            mWidthEditText.setText(Integer.toString(door_width));
            mPriceEditText.setText(Integer.toString(door_price));
            mCountEditText.setText(Integer.toString(door_count));

            mImageEditImage.setImageURI(imageUri);

            switch (door_swing_int) {
                case DoorContract.DoorEntry.SWING_RIGHT_HAND:
                    mSwingSpinner.setSelection(1);
                    break;
                case DoorContract.DoorEntry.SWING_LEFT_HAND:
                    mSwingSpinner.setSelection(2);
                    break;
                case DoorContract.DoorEntry.SWING_UNHUNG:
                    mSwingSpinner.setSelection(0);
                    break;
            }
            switch (door_int_ext) {
                case DoorContract.DoorEntry.INTERIOR:
                    mIntExtSpinner.setSelection(0);
                    break;
                case DoorContract.DoorEntry.EXTERIOR:
                    mIntExtSpinner.setSelection(1);
                    break;
            }
            switch (door_manufacturer) {
                case DoorContract.DoorEntry.JELDWEN:
                    mManufactureSpinner.setSelection(0);
                    break;
                case DoorContract.DoorEntry.MASONITE:
                    mManufactureSpinner.setSelection(1);
                    break;
                case DoorContract.DoorEntry.OTHER:
                    mManufactureSpinner.setSelection(2);
                    break;
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mNameEditText.setText("");
        mStyleEditText.setText("");
        mColorEditText.setText("");
        mCountEditText.setText("");
        mWidthEditText.setText("");
        mHeightEditText.setText("");
        Resources res = getResources();
        mImageEditImage.setImageDrawable(res.getDrawable(R.drawable.no_image));
        mSwingSpinner.setSelection(DoorContract.DoorEntry.SWING_UNHUNG);
        mManufactureSpinner.setSelection(DoorContract.DoorEntry.OTHER);
        mIntExtSpinner.setSelection(DoorContract.DoorEntry.INTERIOR);
    }


}
