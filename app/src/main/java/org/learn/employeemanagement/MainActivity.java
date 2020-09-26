package org.learn.employeemanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int UPDATE_ACTIVITY_CODE = 1;
    private static final int INSERT_ACTIVITY_CODE = 2;
    private String mMessage;
    private int mResultInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list_employees);
        TextView emptyText = (TextView)findViewById(R.id.emptyElement);
        listView.setEmptyView(emptyText);
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.add_employee);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Employee employee = (Employee) adapterView.getItemAtPosition(position);

                Intent intent = new Intent(getApplicationContext(), UpdateEmployeeActivity.class);
                intent.putExtra("Employee", employee);
                startActivityForResult(intent, UPDATE_ACTIVITY_CODE);
            }
        });

        add.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), InsertActivity.class);
                startActivityForResult(intent, INSERT_ACTIVITY_CODE);
            }
        });
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

        fillListview();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (mResultInt == 1) {
            Toast.makeText(getApplicationContext(), mMessage, Toast.LENGTH_LONG).show();
            mResultInt = 0;
        }
    }



    private void fillListview() {
        ListView listView = findViewById(R.id.list_employees);
        Cursor employeeCursor = getAllEmployeesCursor();

        CustomCursorAdapter customCursorAdapter = new CustomCursorAdapter(this, employeeCursor, 0);
        listView.setAdapter(customCursorAdapter);

    }

    private Cursor getAllEmployeesCursor() {
        Cursor employeeCursor = getContentResolver().query(EmployeeContentProvider.CONTENT_URI, null, null, null, "");

        return employeeCursor;
    }
}