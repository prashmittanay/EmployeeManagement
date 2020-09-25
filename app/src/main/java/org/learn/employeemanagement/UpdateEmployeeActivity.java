package org.learn.employeemanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

public class UpdateEmployeeActivity extends AppCompatActivity {
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
        mDesignation = findViewById(R.id.edittext_designation);

        mId.setText(mEmployee.getId());
        mId.setEnabled(false);
        mName.setText(mEmployee.getName());
        mDesignation.setText(mEmployee.getDepartment());
    }


}