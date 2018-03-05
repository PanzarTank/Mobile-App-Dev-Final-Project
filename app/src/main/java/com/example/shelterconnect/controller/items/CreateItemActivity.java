package com.example.shelterconnect.controller.items;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.example.shelterconnect.database.Api;
import com.example.shelterconnect.database.RequestHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class CreateItemActivity extends AppCompatActivity {

    private TextView itemName;
    private TextView itemPrice;
    private TextView itemQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.itemToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("ITEMS");
        toolbar.setSubtitle("");

        this.itemName = findViewById(R.id.editName);
        this.itemPrice = findViewById(R.id.editPrice);
        this.itemQuantity = findViewById(R.id.editQuantity);
        Button itemButton = findViewById(R.id.addItemButton);

        itemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createItem();
            }
        });
    }

    private void createItem() {
        String name = this.itemName.getText().toString().trim();
        String price = this.itemPrice.getText().toString().trim();
        String quantity = this.itemQuantity.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            this.itemName.setError("Please enter name");
            this.itemName.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(price)) {
            this.itemPrice.setError("Please enter price");
            this.itemPrice.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(quantity)) {
            this.itemQuantity.setError("Please enter quantity");
            this.itemQuantity.requestFocus();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("price", price);
        params.put("quantity", quantity);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_ITEM, params, Api.CODE_POST_REQUEST);
        request.execute();
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
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(CreateItemActivity.this, ReadItemActivity.class);
                    startActivity(myIntent);
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
