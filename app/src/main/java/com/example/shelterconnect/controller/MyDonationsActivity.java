package com.example.shelterconnect.controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import com.example.shelterconnect.R;
import com.example.shelterconnect.adapters.ListViewAdapter;
import com.example.shelterconnect.adapters.RequestAdapter;
import com.example.shelterconnect.database.Api;
import com.example.shelterconnect.database.RequestHandler;
import com.example.shelterconnect.model.Donation;
import com.example.shelterconnect.model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyDonationsActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Donation> donations;
    private ListView donationList;
    // Create a list adapter for Donations. For this activity, ListView, set adapter to the adapter I created.
    // API call already in place, create front end to connect to back end, have this point to API.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donations);

        this.donationList = (ListView) findViewById(R.id.listViewDonations);

        this.donations = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    private void read() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_EMPLOYEE, null, Api.CODE_GET_REQUEST);
    }

    private class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);

                if (!object.getBoolean("error")) {
                    refreshItemList(object.getJSONArray("donations"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == Api.CODE_POST_REQUEST) {
                return requestHandler.sendPostRequest(url, params);
            }

            if (requestCode == Api.CODE_GET_REQUEST) {
                return requestHandler.sendGetRequest(url);
            }

            return null;
        }
    }

    private void refreshItemList(JSONArray items) throws JSONException {
        donations.clear();
        //String itemName = "";

        for (int i = 0; i < items.length(); i++) {
            JSONObject obj = items.getJSONObject(i);

            System.out.println(obj);

            /* Attempt at matching id to name
            for (Item item : itemList) {
                if (obj.getInt("requestID") == item.getItemID()) {
                    itemName = item.getName();
                }
            }
            */

            donations.add(new Donation(
                    obj.getInt("donationID"),
                    obj.getString("creditCardNum"),
                    obj.getString("expDate"),
                    obj.getInt("ccv"),
                    obj.getInt("donorID"),
                    obj.getInt("requestID"),
                    obj.getDouble("amountDonated")
            ));
        }

        ListViewAdapter adapter = new ListViewAdapter(this, this.donations);
        this.donationList.setAdapter(adapter);
    }

    public void onClick(View v) {

    }
}
