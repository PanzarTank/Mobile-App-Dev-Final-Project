package com.example.shelterconnect.controller.items;

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
import com.example.shelterconnect.adapters.ItemEditAdapter;
import com.example.shelterconnect.database.Api;
import com.example.shelterconnect.database.RequestHandler;
import com.example.shelterconnect.model.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class UpdateItemActivity extends AppCompatActivity {

    ArrayList<Item> itemList;
    private ListView myListView;
    private RecyclerView rv;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.itemToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("EDIT ITEMS");
        toolbar.setSubtitle("");

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);

        this.myListView = (ListView) findViewById(R.id.itemList);
        this.itemList = new ArrayList<>();
        this.readItems();

        Button submitButton = findViewById(R.id.submitChangesButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateItems();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.addItem) {
            Toast.makeText(this, "Action clicked", Toast.LENGTH_LONG).show();
            Intent myIntent = new Intent(this, CreateItemActivity.class);
            startActivity(myIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void readItems() {
        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_ITEMS, null, Api.CODE_GET_REQUEST);
        request.execute();
    }

    private void updateItems() {

        for (Item currItem : this.itemList) {
            if (currItem.hasBeenEdited()) {

                System.out.println("GOT TO UPDATE ITEMS!");

                HashMap<String, String> params = new HashMap<>();
                params.put("itemID", Integer.toString(currItem.getItemID()));
                params.put("name", currItem.getName());
                params.put("price", Double.toString(currItem.getPrice()));
                params.put("quantity", Integer.toString(currItem.getQuantity()));

                PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_ITEM, params, Api.CODE_POST_REQUEST);
                request.execute();

                Intent myIntent = new Intent(this, ReadItemActivity.class);
                startActivity(myIntent);
            }
        }

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

        ItemEditAdapter adapter = new ItemEditAdapter(this.itemList);
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
            } else if (requestCode == Api.CODE_GET_REQUEST) {
                return requestHandler.sendGetRequest(url);
            }

            return null;
        }
    }

}