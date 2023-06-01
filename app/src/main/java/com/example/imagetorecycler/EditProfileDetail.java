package com.example.imagetorecycler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditProfileDetail extends AppCompatActivity {

    EditText fullname,email,description,number;
    Button done;
    TextView cancel;
    String userID;
    private FirebaseAuth auth;

    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_detail);

        fullname = findViewById(R.id.txtFullName);
        description = findViewById(R.id.txtDescription);
        email = findViewById(R.id.txtEmail);
        number = findViewById(R.id.txtPhoneNumber);
        done = findViewById(R.id.doneBtn);
        cancel = findViewById(R.id.btnCancel);


        userID = auth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String emailR = extras.getString("email");
            String phoneR = extras.getString("phone");
            String nameR = extras.getString("name");
            String descriptionR = extras.getString("description");

            email.setText(emailR);
            number.setText(phoneR);
            fullname.setText(nameR);
            description.setText(descriptionR);

        }

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, Object> data = new HashMap<>();
                data.put("dataEmail", email.getText().toString());
                data.put("dataPhoneNumber", number.getText().toString());
                data.put("dataDescription", description.getText().toString());
                data.put("dataFullName", fullname.getText().toString());
                databaseReference.updateChildren(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(EditProfileDetail.this, "Updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ActivityAccount.class));
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });



    }
}