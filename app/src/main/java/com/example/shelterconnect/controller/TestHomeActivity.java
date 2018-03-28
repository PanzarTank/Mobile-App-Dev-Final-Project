package com.example.shelterconnect.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.shelterconnect.R;
import com.example.shelterconnect.controller.items.CreateItemActivity;
import com.example.shelterconnect.controller.items.ReadItemActivity;
import com.example.shelterconnect.controller.requests.CreateRequestActivity;
import com.example.shelterconnect.controller.requests.GetRequestActivity;

public class TestHomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        findViewById(R.id.goItemList).setOnClickListener(this);
        findViewById(R.id.goAddItem).setOnClickListener(this);
        findViewById(R.id.goLoginPage).setOnClickListener(this);
        findViewById(R.id.goWorkerList).setOnClickListener(this);
        findViewById(R.id.viewRequest).setOnClickListener(this);
        findViewById(R.id.newRequest).setOnClickListener(this);
        findViewById(R.id.goDonorHome).setOnClickListener(this);
        findViewById(R.id.goWorkerHome).setOnClickListener(this);
        findViewById(R.id.goOrganizerHome).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.goItemList:
                startActivity(new Intent(this, ReadItemActivity.class));

                break;
            case R.id.goAddItem:
                startActivity(new Intent(this, CreateItemActivity.class));

                break;
            case R.id.goLoginPage:
                startActivity(new Intent(this, LoginActivity.class));

                break;
            case R.id.goWorkerList:
                startActivity(new Intent(this, WorkerListActivity.class));

                break;
            case R.id.newRequest:
                startActivity(new Intent(this, CreateRequestActivity.class));

                break;

            case R.id.viewRequest:
                startActivity(new Intent(this, GetRequestActivity.class));

                break;
            case R.id.goDonorHome:
                startActivity(new Intent(this, DonorHomeActivity.class));

                break;
            case R.id.goWorkerHome:
                startActivity(new Intent(this, WorkerHomeActivity.class));

                break;
            case R.id.goOrganizerHome:
                startActivity(new Intent(this, OrganizerHomeActivity.class));

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
