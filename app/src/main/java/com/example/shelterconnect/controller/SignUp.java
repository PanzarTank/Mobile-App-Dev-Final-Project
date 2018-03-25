package com.example.shelterconnect.controller;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.example.shelterconnect.database.Api;
import com.example.shelterconnect.database.RequestHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    EditText signInEmail, signInPassword, userName, userPhone, userAddress, verifyPassword;
    ProgressBar progressBar;
    RadioButton donor, organizer, employee;

    AlertDialog builder;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        builder = new AlertDialog.Builder(SignUp.this).create();
        builder.setTitle("Alert");
        builder.setMessage("Please select your position at the bottom");

        signInEmail = findViewById(R.id.signInEmail);
        signInPassword = findViewById(R.id.signInPassword);
        userName = findViewById(R.id.signup_name);
        userPhone = findViewById(R.id.signup_phone);
        userAddress = findViewById(R.id.signup_address);
        verifyPassword = findViewById(R.id.signup_vpassword);
        progressBar = findViewById(R.id.progressBarSignUp);
        donor = findViewById(R.id.radioDonor);
        organizer = findViewById(R.id.radioOrganizer);
        employee = findViewById(R.id.radioEmployee);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signup_button).setOnClickListener(this);
        findViewById(R.id.toLogin_button).setOnClickListener(this);
    }

    private void createUser() {
        String email = signInEmail.getText().toString().trim();
        String name = userName.getText().toString().trim();
        String phone = userPhone.getText().toString().trim();
        String address = userAddress.getText().toString().trim();
        String position;

        if (donor.isChecked()) {
            position = "0";
        } else if (employee.isChecked()) {
            position = "1";
        } else if (organizer.isChecked()) {
            position = "2";
        } else {
            donor.setError("Please select your role");
            donor.requestFocus();
            return;
        }

        HashMap<String, String> paramsDonor = new HashMap<>();
        paramsDonor.put("name", name);
        paramsDonor.put("phone", phone);
        paramsDonor.put("address", address);
        paramsDonor.put("email", email);


        HashMap<String, String> paramsEmployee = new HashMap<>();
        paramsEmployee.put("name", name);
        paramsEmployee.put("position", position);
        paramsEmployee.put("phone", phone);
        paramsEmployee.put("address", address);
        paramsEmployee.put("email", email);

        if (position.equals("0")) {
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_DONOR, paramsDonor, Api.CODE_POST_REQUEST);
            request.execute();
        } else if (position.equals("1") || position.equals("2")) {
            PerformNetworkRequest request = new PerformNetworkRequest(Api.URL_CREATE_EMPLOYEE, paramsEmployee, Api.CODE_POST_REQUEST);
            request.execute();
        }
    }

    private void registerUser() {
        String email = signInEmail.getText().toString().trim();
        String password = signInPassword.getText().toString().trim();
        String vPassword = verifyPassword.getText().toString().trim();
        String name = userName.getText().toString().trim();
        String phone = userPhone.getText().toString().trim();
        String address = userAddress.getText().toString().trim();
        String position;

        if (donor.isChecked()) {
            position = "0";
        } else if (employee.isChecked()) {
            position = "1";
        } else if (organizer.isChecked()) {
            position = "2";
        } else {
            builder.show();
            return;
        }

        //Check if name is empty
        if (name.isEmpty()) {
            userName.setError("Please enter your name");
            userName.requestFocus();
            return;
        }

        //Check if phone number is empty
        if (phone.isEmpty()) {
            userPhone.setError("Please enter your phone number");
            userPhone.requestFocus();
            return;
        }

        //Check if address is empty
        if (address.isEmpty()) {
            userAddress.setError("Please enter your address");
            userAddress.requestFocus();
            return;
        }

        //Check if email is empty
        if (email.isEmpty()) {
            signInEmail.setError("Email is required.");
            signInEmail.requestFocus();
            return;
        }

        //Check if a valid email is used
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            signInEmail.setError("Please enter a valid E-mail");
            signInEmail.requestFocus();
            return;
        }

        //Check if password is empty
        if (password.isEmpty()) {
            signInPassword.setError("Password is required");
            signInPassword.requestFocus();
            return;
        }

        //Check if password is at least 6 characters
        if (password.length() < 6) {
            signInPassword.setError("Minimum length of password must be 6 characters");
            signInPassword.requestFocus();
            return;
        }

        //Check for password verification
        if (!password.equals(vPassword)) {
            signInPassword.setError("Passwords do not match");
            signInPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    createUser();
                    Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "Email already in use", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
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
                    //Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(SignUp.this, DonorHomePage.class);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.signup_button:
                registerUser();
                break;

            case R.id.toLogin_button:
                startActivity(new Intent(this, LoginActivity.class));
                break;

        }
    }
}
