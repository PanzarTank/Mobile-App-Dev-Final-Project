package com.example.shelterconnect.controller.items;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.shelterconnect.R;
import com.example.shelterconnect.adapters.ItemAdapter;
import com.example.shelterconnect.controller.DonorHomeActivity;
import com.example.shelterconnect.controller.LoginActivity;
import com.example.shelterconnect.controller.OrganizerHomeActivity;
import com.example.shelterconnect.controller.WorkerHomeActivity;
import com.example.shelterconnect.database.Api;
import com.example.shelterconnect.database.RequestHandler;
import com.example.shelterconnect.model.Item;
import com.example.shelterconnect.util.Functions;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ReadItemActivity extends AppCompatActivity {


    ArrayList<Item> itemList;
    private ListView myListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.itemToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("ITEMS");
        toolbar.setSubtitle("");

        this.myListView = (ListView) findViewById(R.id.itemList);

        this.itemList = new ArrayList<>();

        readItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.home){

            int userLevel = Functions.getUsetLevel(this);

            if(userLevel == 0 | userLevel == -1){
                Intent myIntent = new Intent(this, DonorHomeActivity.class);
                startActivity(myIntent);
                return true;
            } else  if(userLevel == 1){
                Intent myIntent = new Intent(this, WorkerHomeActivity.class);
                startActivity(myIntent);
                return true;
            } else  if(userLevel == 2){
                Intent myIntent = new Intent(this, OrganizerHomeActivity.class);
                startActivity(myIntent);
                return true;
            }

        } else if(id == R.id.listItems){
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
        } else if(id == R.id.logout){

            FirebaseAuth.getInstance().signOut();
            getSharedPreferences("userLevel", Context.MODE_PRIVATE).edit().putString("position", "").apply();
            Intent myIntent = new Intent(this, LoginActivity.class);
            startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readItems() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_ITEMS, null, Api.CODE_GET_REQUEST);
        request.execute();
    }

    private void refreshItemList(JSONArray items) throws JSONException {
        itemList.clear();

        for (int i = 0; i < items.length(); i++) {
            JSONObject obj = items.getJSONObject(i);

            System.out.println(obj);

            itemList.add(new Item(
                    obj.getInt("itemID"),
                    obj.getString("name"),
                    obj.getDouble("price"),
                    obj.getInt("quantity")
            ));
        }

        ItemAdapter adapter = new ItemAdapter(this, this.itemList);
        this.myListView.setAdapter(adapter);
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
