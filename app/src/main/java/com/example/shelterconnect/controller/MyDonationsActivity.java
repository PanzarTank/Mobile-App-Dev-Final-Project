package com.example.shelterconnect.controller;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.example.shelterconnect.adapters.DonationAdapter;
import com.example.shelterconnect.database.Api;
import com.example.shelterconnect.database.RequestHandler;
import com.example.shelterconnect.model.Donation;
import com.example.shelterconnect.util.Functions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MyDonationsActivity extends AppCompatActivity {

    private ArrayList<Donation> donations;
    private ListView donationList;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user!= null) {
            this.email = user.getEmail();
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_GET_DONATIONS_FOR_USER+this.email, null, Api.CODE_GET_REQUEST);
            request.execute();
        }

        setContentView(R.layout.activity_my_donations);

        this.donationList = (ListView) findViewById(R.id.listViewDonations);
        this.donations = new ArrayList<>();

        Toolbar toolbar = (Toolbar) findViewById(R.id.donorToolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setTitle("MAKE A DONATION");
        toolbar.setSubtitle("");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_donor, menu);
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

        } else if (id == R.id.logout) {

            FirebaseAuth.getInstance().signOut();
            getSharedPreferences("userLevel", Context.MODE_PRIVATE).edit().putString("position", "-1").apply();
            Intent myIntent = new Intent(this, LoginActivity.class);
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

        for (int i = 0; i < items.length(); i++) {
            JSONObject obj = items.getJSONObject(i);

            System.out.println(obj);

            donations.add(new Donation(
                    obj.getInt("donorID"),
                    obj.getInt("requestID"),
                    obj.getDouble("amountDonated"),
                    obj.getString("donationDate")
            ));
        }

        DonationAdapter adapter = new DonationAdapter(this, this.donations);
        this.donationList.setAdapter(adapter);
    }
}
