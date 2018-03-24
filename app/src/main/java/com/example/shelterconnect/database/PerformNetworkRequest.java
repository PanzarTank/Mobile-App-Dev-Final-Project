package com.example.shelterconnect.database;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.shelterconnect.controller.items.ReadItemActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by parth on 18-03-2018.
 */

public class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
    String url;
    HashMap<String, String> params;
    int requestCode;
    private NetworkRequestListener listener;

    Context context;

    public interface NetworkRequestListener {
        void onPostExecute(String s);
    }

    public PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode, Context context) {
        this.url = url;
        this.params = params;
        this.requestCode = requestCode;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
//        try {
//            JSONObject object = new JSONObject(s);
//
//            if (!object.getBoolean("error")) {
//                Toast.makeText(context, object.getString("message"), Toast.LENGTH_SHORT).show();
////                Intent myIntent = new Intent(Context, com.example.shelterconnect.controller.items.CreateItemActivity.this, ReadItemActivity.class);
////                startActivity(myIntent);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        if(listener != null) {
            listener.onPostExecute(s);
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

    public void setListener(NetworkRequestListener listener) {
        this.listener = listener;
    }
}

