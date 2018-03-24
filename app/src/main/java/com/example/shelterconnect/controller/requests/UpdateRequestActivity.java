package com.example.shelterconnect.controller.requests;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.example.shelterconnect.adapters.EditRequestAdapter;
import com.example.shelterconnect.database.Api;
import com.example.shelterconnect.database.RequestHandler;
import com.example.shelterconnect.model.Requestnew;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by parth on 18-03-2018.
 */


public class UpdateRequestActivity extends AppCompatActivity {

    ArrayList<Requestnew> requestList;
    private ListView myListView;
    private RecyclerView rv;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_request);

        Toolbar toolbar = (Toolbar) findViewById(R.id.itemToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("EDIT REQUESTS");
        toolbar.setSubtitle("");

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        this.myListView = (ListView) findViewById(R.id.requestList);
        this.requestList = new ArrayList<>();
        this.readRequests();

        Button submitButton = findViewById(R.id.submitChangesButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRequests();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_requests, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem request) {

        int id = request.getItemId();

        if (id == R.id.addRequest) {
            Toast.makeText(this, "Action clicked", Toast.LENGTH_LONG).show();

            Intent myIntent = new Intent(this, CreateRequestActivity.class);
            startActivity(myIntent);

            return true;

        } else if (id == R.id.editRequest) {
            Intent myIntent = new Intent(this, UpdateRequestActivity.class);
            startActivity(myIntent);

            return true;
        }

        return super.onOptionsItemSelected(request);
    }


    private void readRequests() {
        com.example.shelterconnect.controller.requests.UpdateRequestActivity.PerformNetworkRequest request = new com.example.shelterconnect.controller.requests.UpdateRequestActivity.PerformNetworkRequest(Api.URL_READ_ITEMS, null, Api.CODE_GET_REQUEST);
        request.execute();
    }

    private void updateRequests() {

        for (Requestnew currItem : this.requestList) {
            if (currItem.hasBeenEdited()) {

                System.out.println("GOT TO UPDATE REQUEST LIST!");

                HashMap<String, String> params = new HashMap<>();
                params.put("rID", Integer.toString(currItem.getRequestID()));
               // params.put("name", currItem.getName());
                params.put("quantity", Integer.toString(currItem.getQuantity()));
                params.put("amountRaised", Double.toString(currItem.getAmountRaised()));
                params.put("amountNeeded", Double.toString(currItem.getAmountNeeded()));
                params.put("workerID", Integer.toString(currItem.getEmployeeID()));
                params.put("itemID", Integer.toString(currItem.getItemID()));
                params.put("active", Boolean.toString(currItem.isActive()));

                PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_REQUEST, params, Api.CODE_POST_REQUEST);
                request.execute();

                Intent myIntent = new Intent(this, GetRequestActivity.class);
                startActivity(myIntent);
            }
        }

    }

    private void refreshRequestList(JSONArray requests) throws JSONException {
        requestList.clear();

        for (int i = 0; i < requests.length(); i++) {
            JSONObject obj = requests.getJSONObject(i);
            System.out.println(obj);

            requestList.add(new Requestnew(
                    obj.getInt("rID"),
                    obj.getInt("quantity"),
                    obj.getDouble("amountNeeded"),
                    obj.getDouble("amountRaised"),
                    obj.getInt("workerID"),
                    obj.getInt("itemID"),
                    obj.getBoolean("active")

            ));
        }

        EditRequestAdapter adapter = new EditRequestAdapter(this.requestList);
        this.rv.setAdapter(adapter);
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
                    refreshRequestList(object.getJSONArray("message"));
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
}