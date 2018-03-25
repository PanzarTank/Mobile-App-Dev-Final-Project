package com.example.shelterconnect.controller;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.example.shelterconnect.database.Api;
import com.example.shelterconnect.database.RequestHandler;
import com.example.shelterconnect.model.Item;
import com.example.shelterconnect.model.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ReceiveItemActivity extends AppCompatActivity {

    private ArrayList<Item> itemList = new ArrayList<Item>();
    private ArrayList<Request> requestList = new ArrayList<Request>();
    private Request foundRequest = null;
    private Item foundItem = null;

    //String requestID = (String) getIntent().getExtras().get("requestID");
    private int requestIDInt = Integer.parseInt("4");
    private NumberPicker numberPicker;
    private TextView itemName;
    private Button receiveItemButton;
    private boolean update = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_item);

        PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_READ_REQUESTS, null, Api.CODE_GET_REQUEST);
        request.execute();

        this.numberPicker = findViewById(R.id.numberPicker);
        this.itemName = findViewById(R.id.itemName);
        this.receiveItemButton = findViewById(R.id.receiveItemButton);

        this.updateUI();

        this.receiveItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (foundRequest == null) {
                    //Show Toast and get out of there.
                    Toast.makeText(getApplicationContext(), "Something went wrong, unable to receive", Toast.LENGTH_LONG).show();
                } else {

                    int receivedQuantity = numberPicker.getValue();

                    double amountRaised = foundItem.getPrice() * receivedQuantity;

                    double newAmountNeeded = foundRequest.getAmountNeeded() - amountRaised;
                    foundRequest.setAmountNeeded(newAmountNeeded);

                    int newQuantity = foundRequest.getQuantity() - receivedQuantity;
                    foundRequest.setQuantity(newQuantity);

                    System.out.println("GOT TO UPDATE REQUEST LIST!");

                    String activeString = "1";

                    if (foundRequest.isActive()) {
                        activeString = "0";
                    }

                    HashMap<String, String> params = new HashMap<>();
                    params.put("rID", Integer.toString(foundRequest.getRequestID()));
                    params.put("quantity", Integer.toString(foundRequest.getQuantity()));
                    params.put("amountRaised", Double.toString(foundRequest.getAmountRaised()));
                    params.put("amountNeeded", Double.toString(foundRequest.getAmountNeeded()));
                    params.put("workerID", Integer.toString(foundRequest.getEmployeeID()));
                    params.put("itemID", Integer.toString(foundRequest.getItemID()));
                    params.put("active", activeString);

                    update = true;
                    PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_UPDATE_REQUEST, params, Api.CODE_POST_REQUEST);
                    request.execute();
                }

            }
        });


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
    }

    private void refreshRequestList(JSONArray request) throws JSONException {
        requestList.clear();

        for (int i = 0; i < request.length(); i++) {
            JSONObject obj = request.getJSONObject(i);

            System.out.println(obj);

            int activeInt = obj.getInt("active");
            boolean active = false;

            if (activeInt == 0) {
                active = true;
            } else if (activeInt == 1) {
                active = false;
            }

            requestList.add(new Request(
                    obj.getInt("requestID"),
                    obj.getInt("quantity"),
                    obj.getDouble("amountNeeded"),
                    obj.getDouble("amountRaised"),
                    obj.getInt("workerID"),
                    obj.getInt("itemID"), active)
            );
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
                if (!object.getBoolean("error") && update) {

                    Toast.makeText(getApplicationContext(), "updated request!", Toast.LENGTH_LONG).show();
                    //refreshRequestList(object.getJSONArray("requests"));

                } else if(!object.getBoolean("error") && !update){

                        refreshItemList(object.getJSONArray("items"));
                        refreshRequestList(object.getJSONArray("requests"));
                        updateUI();
                        numberPicker.invalidate();
                        itemName.invalidate();
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

    private void updateUI() {

        for (Request currRequest : this.requestList) {
            if (currRequest.getRequestID() == requestIDInt) {
                foundRequest = currRequest;
            }
        }

        if (foundRequest != null) {

            System.out.println("FOUND REQUEST " + foundRequest.getQuantity());

            for (Item currItem : this.itemList) {
                if (currItem.getItemID() == foundRequest.getItemID()) {
                    foundItem = currItem;
                }
            }

            numberPicker.setMinValue(0);
            numberPicker.setMaxValue(foundRequest.getQuantity());
        }

        if (foundItem != null) {
            TextView itemName = findViewById(R.id.itemName);
            String itemNameString = "Item Name: " + foundItem.getName();
            itemName.setText(itemNameString);
        }
    }

}
