package org.learn.employeemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class InsertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        Button button = findViewById(R.id.button_employee_insert);

        button.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String name = ((EditText) findViewById(R.id.insert_edit_name)).getText().toString();
                String department = ((EditText) findViewById(R.id.insert_edit_department)).getText().toString();

                ContentValues contentValues = new ContentValues();
                contentValues.put(EmployeeContentProvider.NAME, name);
                contentValues.put(EmployeeContentProvider.DEPARTMENT, department);

                Uri uri = getContentResolver().insert(EmployeeContentProvider.CONTENT_URI, contentValues);
                returnToCaller("New User created with ID: " + uri.getPathSegments().get(uri.getPathSegments().size() - 1));
            }
        });
    }

    private void returnToCaller(String returnMessage) {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", returnMessage);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}