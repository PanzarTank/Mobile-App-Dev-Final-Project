package com.example.shelterconnect.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

import com.example.shelterconnect.R;
import com.example.shelterconnect.controller.SignUp;
import com.example.shelterconnect.model.Employee;

import java.util.ArrayList;

public class WorkerItemAdapter extends ArrayAdapter<Employee> {

    private ArrayList<Employee> employee;
    private Context adapterContext;
    private Employee currEmployee;

    public WorkerItemAdapter(Context context, ArrayList<Employee> employees) {
        super(context, R.layout.activity_worker_list, employees);
        adapterContext = context;
        this.employee = employees;
    }

    /**
     * @param indexPosition
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int indexPosition, View convertView, ViewGroup parent) {
        View currentView = convertView;
        String employeePositionText = new String("");

        try {
            this.currEmployee = this.employee.get(indexPosition);

            if (currentView == null) {
                LayoutInflater vi = (LayoutInflater) this.adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                currentView = vi.inflate(R.layout.worker_list_item, null);
            }

            TextView employeeName = (TextView) currentView.findViewById(R.id.name);
            employeeName.setText(currEmployee.getName());

            TextView employeePosition = (TextView) currentView.findViewById(R.id.position);

            if (currEmployee.getPosition() == 1) {
                employeePositionText = "Employee";
            } else if (currEmployee.getPosition() == 2) {
                employeePositionText = "Organizer";
            }
            employeePosition.setText(employeePositionText);

            TextView employeeEmail = (TextView) currentView.findViewById(R.id.email);
            employeeEmail.setText(currEmployee.getEmail());

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        currentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog builder;
                String worker = "";

                if (currEmployee.getPosition() == 1) {
                    worker = ("WorkerID: " + currEmployee.getEmployeeID() + "\n\n" +
                            "Name: " + currEmployee.getName() + "\n\n" +
                            "Position: Worker" + "\n\n" +
                            "Phone: " + currEmployee.getPhone() + "\n\n" +
                            "Address: " + currEmployee.getAddress() + "\n\n" +
                            "Email: " + currEmployee.getEmail());
                } else if (currEmployee.getPosition() == 2) {
                    worker = ("WorkerID: " + currEmployee.getEmployeeID() + "\n\n" +
                            "Name: " + currEmployee.getName() + "\n\n" +
                            "Position: Organizer" + "\n\n" +
                            "Phone: " + currEmployee.getPhone() + "\n\n" +
                            "Address: " + currEmployee.getAddress() + "\n\n" +
                            "Email: " + currEmployee.getEmail());
                }

                builder = new android.support.v7.app.AlertDialog.Builder(adapterContext).create();
                builder.setTitle("Worker Information");
                builder.setMessage(worker);
                builder.show();
            }
        });

        return currentView;
    }

}