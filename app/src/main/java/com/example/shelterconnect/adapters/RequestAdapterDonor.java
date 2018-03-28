package com.example.shelterconnect.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.shelterconnect.R;
import com.example.shelterconnect.model.Request;
import com.example.shelterconnect.adapters.ItemAdapter;
import java.util.ArrayList;

/**
 * Created by parth on 20-03-2018.
 */

public class RequestAdapterDonor extends ArrayAdapter<Request> {

    private ArrayList<Request> requests;
    private Context adapterContext;

    public RequestAdapterDonor(Context context, ArrayList<Request> requests) {
        super(context, R.layout.activity_open_requests, requests);
        adapterContext = context;
        this.requests = requests;
    }

    @Override
    public View getView(int indexPosition, View convertView, ViewGroup parent) {
        View currentView = convertView;

        try {
            Request currRequest = this.requests.get(indexPosition);

            if (currentView == null) {
                LayoutInflater vi = (LayoutInflater) this.adapterContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                currentView = vi.inflate(R.layout.request_list, null);
            }




            TextView itemName = currentView.findViewById(R.id.requestID);
            itemName.setText(currRequest.getName());

            TextView requestNeeded = currentView.findViewById(R.id.required);
            requestNeeded.setText(Double.toString(currRequest.getAmountNeeded()));

            TextView requestRaised = currentView.findViewById(R.id.achieved);
            requestRaised.setText(Double.toString(currRequest.getAmountRaised()));

            //  TextView requestWorkerID = (TextView) currentView.findViewById(R.id.empID);
            //  requestWorkerID.setText(Integer.toString(currRequest.getEmployeeID()));

            TextView requestActive = currentView.findViewById(R.id.act);
            requestActive.setText(Boolean.toString(currRequest.isActive()));

            Boolean x = (currRequest.isActive());

            if (x == true)
            {itemName.setTextColor(Color.parseColor("RED"));
                requestNeeded.setTextColor(Color.parseColor("RED"));
                requestRaised.setTextColor(Color.parseColor("RED"));
                //      requestWorkerID.setTextColor(Color.parseColor("RED"));
            }
            else {
                itemName.setVisibility(View.GONE);
                requestNeeded.setVisibility(View.GONE);
                requestRaised.setVisibility(View.GONE);
                //     requestWorkerID.setTextColor(Color.parseColor("BLUE"));

            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

        return currentView;
    }
}