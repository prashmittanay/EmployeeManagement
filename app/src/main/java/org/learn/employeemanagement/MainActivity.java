package org.learn.employeemanagement;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int UPDATE_ACTIVITY_CODE = 1;
    private String mMessage;
    private int mResultInt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list_employees);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Employee employee = (Employee) adapterView.getItemAtPosition(position);

                Intent intent = new Intent(getApplicationContext(), UpdateEmployeeActivity.class);
                intent.putExtra("Employee", employee);
                startActivityForResult(intent, UPDATE_ACTIVITY_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UPDATE_ACTIVITY_CODE) {
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
        List<Employee> allEmployees = getAllEmployees();

        CustomAdapter myAdapter = new CustomAdapter(allEmployees, this);
        listView.setAdapter(myAdapter);

    }

    private List<Employee> getAllEmployees() {
        Cursor cursor = getContentResolver().query(EmployeeContentProvider.CONTENT_URI, null, null, null, "");
        List<Employee> employeeList = new ArrayList<Employee>();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(EmployeeContentProvider._ID));
                String name = cursor.getString(cursor.getColumnIndex(EmployeeContentProvider.NAME));
                String department = cursor.getString(cursor.getColumnIndex(EmployeeContentProvider.DEPARTMENT));

                employeeList.add(new Employee(String.valueOf(id), name, department));
            } while (cursor.moveToNext());
        }

        return employeeList;
    }
}