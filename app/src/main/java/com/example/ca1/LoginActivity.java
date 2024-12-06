package com.example.ca1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email, password;
    private String emailStr, passwordStr;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.loginEmail);
        password = (EditText) findViewById(R.id.loginPassword);
    }

    @Override
    public void onClick(View v) {
        emailStr = email.getText().toString().trim();
        passwordStr = password.getText().toString().trim();

        switch (v.getId()) {
            case R.id.gotoSignup:
                Intent i = new Intent(this, SignupActivity.class);
                startActivity(i);
                break;
            case R.id.loginButton:
                login(emailStr, passwordStr);
                break;
        }

    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, redirect to MainActivity
                        Log.d("Dark says", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent i2 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i2);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Dark says", "signInWithEmail:failure", task.getException());
                        Toast.makeText(LoginActivity.this, "Authentication failed",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
