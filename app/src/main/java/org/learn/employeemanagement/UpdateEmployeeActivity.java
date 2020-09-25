package org.learn.employeemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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

        Button update = findViewById(R.id.button_employee_update);
        update.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                updateEmployee();
            }
        });



    }

    private void updateEmployee() {
        ContentValues contentValues = new ContentValues();
        String name = ((EditText)findViewById(R.id.edittext_name)).getText().toString();
        String department = ((EditText)findViewById(R.id.edittext_department)).getText().toString();
        contentValues.put(EmployeeContentProvider.NAME, name);
        contentValues.put(EmployeeContentProvider.DEPARTMENT, department);

        String selectionClause = "_ID = ?";
        String[] selectionArgs = {mEmployee.getId()};

        int count = getContentResolver().update(EmployeeContentProvider.CONTENT_URI, contentValues, selectionClause, selectionArgs);

        Log.d(TAG, String.valueOf(count));
    }
}