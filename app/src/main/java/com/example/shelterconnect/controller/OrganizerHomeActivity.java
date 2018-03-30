package com.example.shelterconnect.controller;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.example.shelterconnect.controller.items.CreateItemActivity;
import com.example.shelterconnect.controller.items.ReadItemActivity;
import com.example.shelterconnect.controller.items.UpdateItemActivity;
import com.example.shelterconnect.database.Api;
import com.example.shelterconnect.database.RequestHandler;
import com.example.shelterconnect.model.Item;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import com.example.shelterconnect.adapters.RequestAdapter;
import com.example.shelterconnect.model.Request;
import com.example.shelterconnect.util.Functions;
import com.google.firebase.auth.FirebaseAuth;

public class OrganizerHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Request> requests;
    private ArrayList<Item> itemList;
    private ListView requestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_organizer_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.itemToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("ORGANIZER HOMEPAGE");
        toolbar.setSubtitle("");

        findViewById(R.id.viewItemsButton).setOnClickListener(this);

        this.requestList = (ListView) findViewById(R.id.requestList);

        this.requests = new ArrayList<Request>();
        this.itemList = new ArrayList<Item>();

        findViewById(R.id.viewItemsButton).setOnClickListener(this);
        findViewById(R.id.viewWorkersButton).setOnClickListener(this);
        findViewById(R.id.viewCompletedRequestsButton).setOnClickListener(this);
        findViewById(R.id.requestItemsButton).setOnClickListener(this);

        readRequests();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.home) {

            int userLevel = Functions.getUserLevel(this);

            if (userLevel == -1) {
                Toast.makeText(getApplicationContext(), "Please sign in to go to your homepage", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent(this, LoginActivity.class);
                startActivity(myIntent);
                return true;
            } else if (userLevel == 0) {
                Intent myIntent = new Intent(this, DonorHomeActivity.class);
                startActivity(myIntent);
                return true;
            } else if (userLevel == 1) {
                Intent myIntent = new Intent(this, WorkerHomeActivity.class);
                startActivity(myIntent);
                return true;
            } else if (userLevel == 2) {
                Intent myIntent = new Intent(this, OrganizerHomeActivity.class);
                startActivity(myIntent);
                return true;
            }

        } else if (id == R.id.listItems) {
            Intent myIntent = new Intent(this, ReadItemActivity.class);
            startActivity(myIntent);
            return true;

        } else if (id == R.id.addItem) {
            Intent myIntent = new Intent(this, CreateItemActivity.class);
            startActivity(myIntent);
            return true;

        } else if (id == R.id.editItems) {
            Intent myIntent = new Intent(this, UpdateItemActivity.class);
            startActivity(myIntent);
            return true;
        } else if (id == R.id.logout) {

            FirebaseAuth.getInstance().signOut();
            getSharedPreferences("userLevel", Context.MODE_PRIVATE).edit().putString("position", "-1").apply();
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readRequests() {
        OrganizerHomeActivity.PerformNetworkRequest request = new OrganizerHomeActivity.PerformNetworkRequest(Api.URL_READ_REQUESTS, null, Api.CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshRequestList(JSONArray items) throws JSONException {
        requests.clear();
        //String itemName = "";

        for (int i = 0; i < items.length(); i++) {
            JSONObject obj = items.getJSONObject(i);

            System.out.println(obj);

            int activeInt = obj.getInt("active");
            boolean active = false;

            if (activeInt == 0) {
                active = true;
            } else if (activeInt == 1) {
                active = false;
            }

            /* Attempt at matching id to name
            for (Item item : itemList) {
                if (obj.getInt("requestID") == item.getItemID()) {
                    itemName = item.getName();
                }
            }
            */

            requests.add(new Request(
                    obj.getInt("requestID"),
                    obj.getInt("quantity"),
                    obj.getDouble("amountNeeded"),
                    obj.getDouble("amountRaised"),
                    obj.getInt("workerID"),
                    obj.getInt("itemID"),
                    active
            ));
        }

        RequestAdapter adapter = new RequestAdapter(this, this.requests);
        this.requestList.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewItemsButton:
                startActivity(new Intent(this, OpenRequestsActivity.class));

                break;
            case R.id.viewWorkersButton:
                startActivity(new Intent(this, WorkerListActivity.class));

                break;
            case R.id.viewCompletedRequestsButton:
                startActivity(new Intent(this, ClosedRequestsActivity.class));

                break;
            case R.id.requestItemsButton:
                startActivity(new Intent(this, CreateItemActivity.class));

                break;
        }
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
                    refreshRequestList(object.getJSONArray("requests"));
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