package com.example.shelterconnect.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.shelterconnect.R;
import com.example.shelterconnect.controller.items.CreateItemActivity;
import com.example.shelterconnect.controller.items.ReadItemActivity;
import com.example.shelterconnect.controller.items.UpdateItemActivity;
import com.example.shelterconnect.util.Functions;
import com.google.firebase.auth.FirebaseAuth;

public class OpenRequestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_requests);

        Toolbar toolbar = (Toolbar) findViewById(R.id.itemToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("OPEN REQUESTS");
        toolbar.setSubtitle("");
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
}
