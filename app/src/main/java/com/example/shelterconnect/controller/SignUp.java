package com.example.shelterconnect.controller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shelterconnect.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    EditText signInEmail, signInPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signInEmail = findViewById(R.id.signInEmail);
        signInPassword = findViewById(R.id.signInPassword);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.signup_button).setOnClickListener(this);
        findViewById(R.id.toLogin_button).setOnClickListener(this);
    }

    private void registerUser() {
        String email = signInEmail.getText().toString().trim();
        String password = signInPassword.getText().toString().trim();

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

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "User Registrered Successfully", Toast.LENGTH_SHORT).show();

                }
            }
        });
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
