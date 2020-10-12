package org.learn.employeemanagement;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomCursorAdapter extends CursorAdapter {
    private static final String TAG = "CustomCursorAdapter";

    public CustomCursorAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        if (MainActivity.LIST_VIEW_TYPE == 0)
            return LayoutInflater.from(context).inflate(R.layout.listview_row_layout, parent, false);
        else
            return LayoutInflater.from(context).inflate(R.layout.listview_expanded_row_layout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int idId;
        int idName;
        int idDepartment;
        int idPicture;
        switch (MainActivity.LIST_VIEW_TYPE) {
            case 1:
                idId = R.id.expanded_text_employeeID;
                idName = R.id.expanded_text_employeeName;
                idDepartment = R.id.expanded_text_employeeDepartment;
                idPicture = R.id.imageview_list_picture_preview;
                break;
            default:
                idId = R.id.text_employeeID;
                idName = R.id.text_employeeName;
                idDepartment = R.id.text_employeeDepartment;
                idPicture = R.id.imageview_list_picture_preview;
                break;

        }
        TextView employeeIdTextView = (TextView) view.findViewById(idId);
        TextView employeeNameTextView = (TextView) view.findViewById(idName);
        TextView employeeDepartmentTextView = (TextView) view.findViewById(idDepartment);
        ImageView employeePictureImageView = (ImageView) view.findViewById(idPicture);


        int id = cursor.getInt(cursor.getColumnIndex(EmployeeContentProvider._ID));
        String name = cursor.getString(cursor.getColumnIndex(EmployeeContentProvider.NAME));
        String department = cursor.getString(cursor.getColumnIndex(EmployeeContentProvider.DEPARTMENT));
        String picture = cursor.getString(cursor.getColumnIndex(EmployeeContentProvider.PICTURE));

        employeeIdTextView.setText(String.valueOf(id));
        employeeNameTextView.setText(name);
        employeeDepartmentTextView.setText(department);
        if (MainActivity.LIST_VIEW_TYPE == 1)
            CameraUtils.drawImage(picture, employeePictureImageView);
    }
}
