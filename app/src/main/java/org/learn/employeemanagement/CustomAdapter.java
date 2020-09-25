package org.learn.employeemanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import java.util.List;

public class CustomAdapter extends BaseAdapter {
    private List<Employee> mEmployeeList;
    private Context mContext;

    public CustomAdapter(List<Employee> employeeList, Context context){
        mEmployeeList = employeeList;
        mContext = context;
    }

    @Override
    public int getCount() {
        return this.mEmployeeList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.mEmployeeList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.listview_row_layout, null);

            viewHolder = new ViewHolder();
            viewHolder.id = (TextView) convertView.findViewById(R.id.text_employeeID);
            viewHolder.name = (TextView) convertView.findViewById(R.id.text_employeeName);
            viewHolder.department = (TextView) convertView.findViewById(R.id.text_employeeDepartment);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Employee employee = mEmployeeList.get(position);
        viewHolder.id.setText(employee.getId());
        viewHolder.name.setText(employee.getName());
        viewHolder.department.setText(employee.getDepartment());

        return convertView;
    }

    private static class ViewHolder{
        public TextView id;
        public TextView name;
        public TextView department;
    }
}
