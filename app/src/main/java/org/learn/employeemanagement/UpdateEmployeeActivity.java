package org.learn.employeemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UpdateEmployeeActivity extends AppCompatActivity {
    private static final String TAG = "UpdateEmployeeActivity";
    private Employee mEmployee;
    private EditText mId;
    private EditText mName;
    private EditText mDesignation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_employee);
        mEmployee = (Employee) getIntent().getSerializableExtra("Employee");
        mId = findViewById(R.id.edittext_id);
        mName = findViewById(R.id.edittext_name);
        mDesignation = findViewById(R.id.edittext_department);

        mId.setText(mEmployee.getId());
        mId.setEnabled(false);
        mName.setText(mEmployee.getName());
        mDesignation.setText(mEmployee.getDepartment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        String returnMessage = "";
        Button update = findViewById(R.id.button_employee_update);
        Button delete = findViewById(R.id.button_employee_delete);

        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                updateEmployee();
                returnToCaller("Employee: " + mEmployee.getId() + " updated!");
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEmployee();
                returnToCaller("Employee: " + mEmployee.getId() + " deleted!");
            }
        });
    }

    private void returnToCaller(String returnMessage) {
        Intent intent = new Intent();
        intent.putExtra("MESSAGE", returnMessage);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private int updateEmployee() {
        ContentValues contentValues = new ContentValues();
        String name = ((EditText)findViewById(R.id.edittext_name)).getText().toString();
        String department = ((EditText)findViewById(R.id.edittext_department)).getText().toString();
        contentValues.put(EmployeeContentProvider.NAME, name);
        contentValues.put(EmployeeContentProvider.DEPARTMENT, department);

        String selectionClause = "_ID = ?";
        String[] selectionArgs = {mEmployee.getId()};

        int count = getContentResolver().update(EmployeeContentProvider.CONTENT_URI, contentValues, selectionClause, selectionArgs);

        return count;
    }

    private int deleteEmployee() {
        String selectionClause = "_ID = ?";
        String[] selectionArgs = {mEmployee.getId()};
        int count = getContentResolver().delete(EmployeeContentProvider.CONTENT_URI, selectionClause, selectionArgs);

        return count;
    }

}