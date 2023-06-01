package com.example.imagetorecycler;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class ActivitySignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText signupEmail, signupPassword;
    private Button signupButton,skipButton;
    private TextView loginRedirectText;
    private FirebaseUser user;
    private SharedPreferences sharedPreferences;
    ProgressBar progressBar;
    DatabaseReference databaseReference;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signupEmail = findViewById(R.id.txtEmail);
        signupPassword = findViewById(R.id.txtPassword);
        signupButton = findViewById(R.id.btnSignUp);
        loginRedirectText = findViewById(R.id.txtLogin);
        skipButton = findViewById(R.id.btnSkip);
        progressBar = findViewById(R.id.progressBar);


        auth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = signupEmail.getText().toString().trim();
                String pass = signupPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    signupEmail.setError("Email cannot be empty");
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    signupEmail.setError("Invalid email format");
                    return;
                }
                if (pass.isEmpty()){
                    signupPassword.setError("Password cannot be empty");
                    return;
                }
                if (pass.length() != 8){
                    signupPassword.setError("Password must contain 8 characters");
                }
                else if (Patterns.EMAIL_ADDRESS.matcher(email).matches() && pass.length() == 8){
                    progressBar.setVisibility(View.VISIBLE);
                    signupButton.setVisibility(View.GONE);
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                user = auth.getCurrentUser();
                                String email = user.getEmail();
                                String uid = user.getUid();


                                // Save the user's information to shared preferences

                                editor.putString("email", email);
                                editor.putString("uid", uid);
                                editor.putString("authToken", user.getIdToken(false).getResult().getToken()); // Save authentication token
                                editor.apply();

                                Map<String, Object> data = new HashMap<>();
                                data.put("dataEmail", email);
                                databaseReference.child(uid).updateChildren(data);

                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(ActivitySignUp.this, "SignUp Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ActivitySignUp.this, DisplayAssignmentData.class));
                                finish();

                            } else {

                                progressBar.setVisibility(View.GONE);
                                signupButton.setVisibility(View.VISIBLE);
                                Toast.makeText(ActivitySignUp.this, "SignUp failed " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        loginRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivitySignUp.this, ActivityLogin.class));
                finish();
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("skip", "true");
                editor.apply();
                startActivity(new Intent(ActivitySignUp.this, DisplayAssignmentData.class));
                finish();
            }
        });
    }
}