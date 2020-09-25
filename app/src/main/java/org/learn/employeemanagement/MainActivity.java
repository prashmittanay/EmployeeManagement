package org.learn.employeemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list_employees);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Employee employee = (Employee) adapterView.getItemAtPosition(position);
                Log.d(TAG, employee.toString());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        fillListview();
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