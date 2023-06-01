package com.example.imagetorecycler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.imagetorecycler.Recyclerviews.DataClassAssignment;
import com.example.imagetorecycler.Recyclerviews.MyAdapterAssignment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayAssignmentData extends AppCompatActivity {

    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<DataClassAssignment> dataList;
    MyAdapterAssignment adapter;

    private FirebaseAuth auth;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_assignment_data);

        recyclerView = findViewById(R.id.recyclerView1);

        auth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        if (isUserAuthenticated()) {
            proceedToLogin();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationAssignment);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                return true;
            } else if (item.getItemId() == R.id.nav_assignment) {
                startActivity(new Intent(getApplicationContext(), DisplayQuizData.class));
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            }
            else if(item.getItemId() == R.id.nav_upload){

                startActivity(new Intent(getApplicationContext(), UploadData.class));
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;

            }
            else if(item.getItemId() == R.id.nav_account){

                startActivity(new Intent(getApplicationContext(), ActivityAccount.class));
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;

            }

            else {
                return false;
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(DisplayAssignmentData.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayAssignmentData.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();


        dataList = new ArrayList<>();
        adapter = new MyAdapterAssignment(DisplayAssignmentData.this, dataList);
        recyclerView.setAdapter(adapter);


        databaseReference = FirebaseDatabase.getInstance().getReference("Assignment Data");
        //dialog.show();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    DataClassAssignment dataClassAssignment = itemSnapshot.getValue(DataClassAssignment.class);
                    dataClassAssignment.setKey(itemSnapshot.getKey());
                    dataList.add(dataClassAssignment);
                }

                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                dialog.dismiss();
            }
        });


    }

    private boolean isUserAuthenticated() {
        String authToken = sharedPreferences.getString("authToken", null);
        String skip = sharedPreferences.getString("skip", null);

        Log.d("Sharedprefvalue", authToken + skip);
        return (authToken == null || authToken.isEmpty()) && (skip == null || skip.isEmpty());
    }


    private void proceedToLogin() {
        startActivity(new Intent(DisplayAssignmentData.this, ActivityLogin.class));
        finish();
    }

}


