package org.learn.employeemanagement;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "MainActivity";
    private static final int UPDATE_ACTIVITY_CODE = 1;
    private static final int INSERT_ACTIVITY_CODE = 2;
    private String mMessage;
    private int mResultInt = 0;

    private ListView mListView;
    private TextView mTextView;
    private FloatingActionButton mFloatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called!");
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list_employees);
        mTextView = (TextView) findViewById(R.id.emptyElement);
        mListView.setEmptyView(mTextView);
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.add_employee);

        addListeners();
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called!");
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

    private void addListeners() {
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);

                Employee employee = new Employee(String.valueOf(cursor.getInt(cursor.getColumnIndex(EmployeeContentProvider._ID))),
                        cursor.getString(cursor.getColumnIndex(EmployeeContentProvider.NAME)),
                        cursor.getString(cursor.getColumnIndex(EmployeeContentProvider.DEPARTMENT)));

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