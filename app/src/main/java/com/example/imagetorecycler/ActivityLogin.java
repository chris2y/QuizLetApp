package com.example.imagetorecycler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class ActivityLogin extends AppCompatActivity {

    private TextView signUpRedirectText;
    private EditText loginEmail, loginPassword;
    private Button loginButton,skipButton;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private SharedPreferences sharedPreferences;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        signUpRedirectText = findViewById(R.id.txtSignup);
        loginEmail = findViewById(R.id.txtEmail);
        loginPassword = findViewById(R.id.txtPassword);
        loginButton = findViewById(R.id.btnLogin);
        skipButton = findViewById(R.id.btnSkip);
        progressBar = findViewById(R.id.progressBar);



        auth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    loginEmail.setError("Email cannot be empty");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    loginEmail.setError("Invalid email format");
                    return;
                }

                if (password.isEmpty()) {
                    loginPassword.setError("Password cannot be empty");
                    return;
                }

                if (password.length() != 8) {
                    loginPassword.setError("Password must contain 8 characters");
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.GONE);

                auth.signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {


                            @Override
                            public void onSuccess(AuthResult authResult) {
                                user = auth.getCurrentUser();
                                String email = user.getEmail();
                                String uid = user.getUid();


                                // Save the user's information to shared preferences

                                editor.putString("email", email);
                                editor.putString("uid", uid);
                                editor.putString("authToken", user.getIdToken(false).getResult().getToken()); // Save authentication token
                                editor.apply();

                                Toast.makeText(ActivityLogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ActivityLogin.this, DisplayAssignmentData.class));
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressBar.setVisibility(View.GONE);
                                loginButton.setVisibility(View.VISIBLE);
                                if (e instanceof FirebaseAuthInvalidUserException) {
                                    loginEmail.setError("User not found");
                                } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(getApplicationContext(), "Invalid password", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(ActivityLogin.this, "Login Failed", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Check if the user is already authenticated




        signUpRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityLogin.this, ActivitySignUp.class));
                finish();
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("skip", "true");
                editor.apply();
                startActivity(new Intent(ActivityLogin.this, DisplayAssignmentData.class));
                finish();
            }
        });

    }


}