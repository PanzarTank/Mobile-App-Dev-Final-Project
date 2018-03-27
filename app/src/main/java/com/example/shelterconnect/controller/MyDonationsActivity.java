package com.example.shelterconnect.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.shelterconnect.R;

public class MyDonationsActivity extends AppCompatActivity {

    // Create a list adapter for Donations. For this activity, ListView, set adapter to the adapter I created.
    // API call already in place, create front end to connect to back end, have this point to API.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_donations);

    }

}
