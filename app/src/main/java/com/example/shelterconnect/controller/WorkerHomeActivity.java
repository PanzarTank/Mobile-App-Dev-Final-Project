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
import android.widget.ListView;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.example.shelterconnect.adapters.RequestAdapterWorker;
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

import com.example.shelterconnect.model.Request;
import com.example.shelterconnect.util.Functions;
import com.google.firebase.auth.FirebaseAuth;

public class WorkerHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayList<Request> requests;
    private ArrayList<Item> itemList;
    private ListView requestList;
    private HashMap<Integer, String> itemIDNameMap;
    private HashMap<Integer, Double> itemIDPriceMap;
    public int userLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_home);

        userLevel = Functions.getUserLevel(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.requestToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("WORKER HOMEPAGE");
        toolbar.setSubtitle("");

        findViewById(R.id.viewItemsButton).setOnClickListener(this);

        this.requestList = (ListView) findViewById(R.id.requestList);

        this.requests = new ArrayList<Request>();
        this.itemList = new ArrayList<Item>();
        this.itemIDNameMap = new HashMap<>();
        this.itemIDPriceMap = new HashMap<>();

        readRequests();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        MenuItem homeMenu = menu.findItem(R.id.home);
        MenuItem listItems = menu.findItem(R.id.listItems);
        MenuItem addItem = menu.findItem(R.id.addItem);
        MenuItem editItems = menu.findItem(R.id.editItems);
        MenuItem editWorkers = menu.findItem(R.id.editWorkers);
        MenuItem logoutMenu = menu.findItem(R.id.logout);

        if (userLevel == 0) {
            homeMenu.setVisible(true);
            listItems.setVisible(false);
            addItem.setVisible(false);
            editItems.setVisible(false);
            editWorkers.setVisible(false);
            logoutMenu.setVisible(true);
        } else if (userLevel == 1) {
            homeMenu.setVisible(true);
            listItems.setVisible(true);
            addItem.setVisible(true);
            editItems.setVisible(true);
            editWorkers.setVisible(false);
            logoutMenu.setVisible(true);
        } else if (userLevel == 2) {
            homeMenu.setVisible(true);
            listItems.setVisible(true);
            addItem.setVisible(true);
            editItems.setVisible(true);
            editWorkers.setVisible(true);
            logoutMenu.setVisible(true);
        } else {
            homeMenu.setVisible(true);
            listItems.setVisible(false);
            addItem.setVisible(false);
            editItems.setVisible(false);
            editWorkers.setVisible(false);
            logoutMenu.setVisible(true);
        }
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
        } else if (id == R.id.editWorkers) {
            startActivity(new Intent(this, WorkerListDeleteActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    private void readRequests() {
        WorkerHomeActivity.PerformNetworkRequest request = new WorkerHomeActivity.PerformNetworkRequest(Api.URL_READ_REQUESTS, null, Api.CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshItemList(JSONArray items) throws JSONException {
        itemList.clear();
        this.itemIDPriceMap.clear();
        this.itemIDNameMap.clear();

        for (int i = 0; i < items.length(); i++) {
            JSONObject obj = items.getJSONObject(i);

            System.out.println(obj);

            Item currItem = new Item(obj.getInt("itemID"), obj.getString("name"),
                    obj.getDouble("price"), obj.getInt("quantity"));

            this.itemIDNameMap.put(currItem.getItemID(), currItem.getName());
            this.itemIDPriceMap.put(currItem.getItemID(), currItem.getPrice());

            itemList.add(currItem);
        }

        System.out.println(this.itemIDPriceMap);
    }

    private void refreshRequestList(JSONArray items) throws JSONException {
        requests.clear();

        for (int i = 0; i < items.length(); i++) {
            JSONObject obj = items.getJSONObject(i);

            int activeInt = obj.getInt("active");
            boolean active = false;

            if (activeInt == 0) {
                active = true;
            } else if (activeInt == 1) {
                active = false;
            }

            int checkNeeded = obj.getInt("amountNeeded");
            int checkquantity = obj.getInt("quantity");

            if (checkNeeded <= checkquantity) {
                active = false;
            }

            Request newRequest = new Request(
                    obj.getInt("requestID"),
                    obj.getInt("quantity"),
                    obj.getDouble("amountNeeded"),
                    obj.getDouble("amountRaised"),
                    obj.getInt("workerID"),
                    obj.getInt("itemID"),
                    active
            );

            if (this.itemIDNameMap.get(newRequest.getItemID()) != null) {
                newRequest.setName(this.itemIDNameMap.get(newRequest.getItemID()));
            }

            if (this.itemIDPriceMap.get(newRequest.getItemID()) != null) {
                newRequest.setItemPrice(this.itemIDPriceMap.get(obj.getInt("itemID")));
            }

            requests.add(newRequest);
        }

        RequestAdapterWorker adapter = new RequestAdapterWorker(this, this.requests);
        this.requestList.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.viewItemsButton:
                startActivity(new Intent(this, OpenRequestsActivity.class));

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
                    refreshItemList(object.getJSONArray("items"));
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