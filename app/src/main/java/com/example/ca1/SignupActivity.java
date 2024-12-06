package com.example.ca1;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText email, password;
    private String emailStr, passwordStr;
    private FirebaseAuth mAuth;
    private String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,254}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.signupEmail);
        password = (EditText) findViewById(R.id.signupPassword);
    }

    @Override
    public void onClick(View v) {
        emailStr = email.getText().toString().trim();
        passwordStr = password.getText().toString().trim();

        switch (v.getId()) {
            case R.id.gotoLogin:
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;
            case R.id.signupButton:
                Pattern pat = Pattern.compile(emailRegex);

                // validate email & password
                if(emailStr == "" && passwordStr == "") {
                    Toast.makeText(SignupActivity.this, "Please fill out all fields",
                            Toast.LENGTH_SHORT).show();
                }
                else if(!pat.matcher(emailStr).matches()) {
                    Toast.makeText(SignupActivity.this, "Email is in the wrong format",
                            Toast.LENGTH_SHORT).show();
                }
                else if(passwordStr.length() < 6) {
                    Toast.makeText(SignupActivity.this, "Password must be at least 6 characters long",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    signup(emailStr, passwordStr);
                }
                break;
        }

    }

    public void signup(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign up success, redirect to MainActivity
                        Log.d("Dark says", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent i2 = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(i2);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("Dark says", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SignupActivity.this, "Authentication failed",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}