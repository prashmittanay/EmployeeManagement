package org.learn.employeemanagement;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class CustomCursorAdapter extends CursorAdapter {
    private static final String TAG = "CustomCursorAdapter";

    public CustomCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.listview_row_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView employeeIdTextView = (TextView) view.findViewById(R.id.text_employeeID);
        TextView employeeNameTextView = (TextView) view.findViewById(R.id.text_employeeName);
        TextView employeeDepartmentTextView = (TextView) view.findViewById(R.id.text_employeeDepartment);

        int id = cursor.getInt(cursor.getColumnIndex(EmployeeContentProvider._ID));
        String name = cursor.getString(cursor.getColumnIndex(EmployeeContentProvider.NAME));
        String department = cursor.getString(cursor.getColumnIndex(EmployeeContentProvider.DEPARTMENT));

        employeeIdTextView.setText(String.valueOf(id));
        employeeNameTextView.setText(name);
        employeeDepartmentTextView.setText(department);
    }
}
