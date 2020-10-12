package org.learn.employeemanagement;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MainActivity";
    private static final int UPDATE_ACTIVITY_CODE = 1;
    private static final int INSERT_ACTIVITY_CODE = 2;
    private String mMessage;
    private int mResultInt = 0;

    private ListView mListView;
    private TextView mTextView;
    private FloatingActionButton mFloatingActionButton;
    private FloatingActionButton mFloatingSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called!");
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.list_employees);
        mTextView = findViewById(R.id.emptyElement);
        mListView.setEmptyView(mTextView);
        mFloatingActionButton = findViewById(R.id.add_employee);
        mFloatingSettingsButton = findViewById(R.id.button_settings);
        addListeners();
        getPermissions();
        LoaderManager.getInstance(this).initLoader(1, null, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_ACTIVITY_CODE && resultCode == -1) {
            mResultInt = 1;
            mMessage = data.getStringExtra("MESSAGE");
        } else if (requestCode == INSERT_ACTIVITY_CODE && resultCode == -1) {
            mResultInt = 1;
            mMessage = data.getStringExtra("MESSAGE");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called!");
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        if (sharedPreferences.getAll().size() == 0) {
            initPreferences(sharedPreferences);
        }
        int textDirection = sharedPreferences.getInt(getString(R.string.list_text_orientation), 3);
        String dividerColor = sharedPreferences.getString(getString(R.string.list_divider_color), "#000000");
        int dividerHeight = sharedPreferences.getInt(getString(R.string.list_divider_height), 1);
        String bgColor = sharedPreferences.getString(getString(R.string.list_background_color), "#eeeeee");
        mListView.setTextDirection(textDirection);
        mListView.setDivider(new ColorDrawable(Color.parseColor(dividerColor)));
        mListView.setDividerHeight(dividerHeight);
        mListView.setBackgroundColor(Color.parseColor(bgColor));
    }

    private void initPreferences(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor prefEditor = sharedPreferences.edit();
        prefEditor.putInt(getString(R.string.list_text_orientation), 3);
        prefEditor.putString(getString(R.string.list_divider_color), "#000000");
        prefEditor.putInt(getString(R.string.list_divider_height), 1);
        prefEditor.putString(getString(R.string.list_background_color), "#eeeeee");
        prefEditor.commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mResultInt == 1) {
            Toast.makeText(getApplicationContext(), mMessage, Toast.LENGTH_LONG).show();
            mResultInt = 0;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called!");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop called!");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called!");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called!");
    }

    private CursorLoader getAllEmployeesCursor() {
        CursorLoader cursorLoader = new CursorLoader(this, EmployeeContentProvider.CONTENT_URI,
                null, null, null, null);
        return cursorLoader;
    }

    private void getPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.INTERNET}, 1);
        }
    }

    private void addListeners() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);

                Employee employee = new Employee(String.valueOf(cursor.getInt(cursor.getColumnIndex(EmployeeContentProvider._ID))),
                        cursor.getString(cursor.getColumnIndex(EmployeeContentProvider.NAME)),
                        cursor.getString(cursor.getColumnIndex(EmployeeContentProvider.DEPARTMENT)),
                        cursor.getString(cursor.getColumnIndex(EmployeeContentProvider.PICTURE)));

                Intent intent = new Intent(getApplicationContext(), UpdateEmployeeActivity.class);
                intent.putExtra("Employee", employee);
                startActivityForResult(intent, UPDATE_ACTIVITY_CODE);
            }
        });

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InsertActivity.class);
                startActivityForResult(intent, INSERT_ACTIVITY_CODE);
            }
        });

        mFloatingSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListSettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        CursorLoader cursorLoader = new CursorLoader(this, EmployeeContentProvider.CONTENT_URI,
                null, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        cursor.moveToFirst();
        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, cursor, 0);
        mListView.setAdapter(customCursorAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        loader = null;
    }
}