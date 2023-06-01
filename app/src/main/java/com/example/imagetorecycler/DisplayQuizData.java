package com.example.imagetorecycler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import com.example.imagetorecycler.Recyclerviews.DataClassQuiz;
import com.example.imagetorecycler.Recyclerviews.MyAdapterQuiz;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayQuizData extends AppCompatActivity {
    DatabaseReference databaseReference;
    ValueEventListener eventListener;
    RecyclerView recyclerView;
    List<DataClassQuiz> dataList;
    MyAdapterQuiz adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_quiz_data);

        recyclerView = findViewById(R.id.recyclerView1);





        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_assignment);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_assignment) {
                return true;
            } else if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), DisplayAssignmentData.class));
               // overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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



        GridLayoutManager gridLayoutManager = new GridLayoutManager(DisplayQuizData.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayQuizData.this);
        builder.setCancelable(false);
        builder.setView(R.layout.progress_layout);
        AlertDialog dialog = builder.create();


        dataList = new ArrayList<>();
        adapter = new MyAdapterQuiz(DisplayQuizData.this, dataList);
        recyclerView.setAdapter(adapter);


        databaseReference = FirebaseDatabase.getInstance().getReference("Questions Data");
        dialog.show();
        eventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    DataClassQuiz dataClassQuiz = itemSnapshot.getValue(DataClassQuiz.class);
                    dataClassQuiz.setKey(itemSnapshot.getKey());
                    dataList.add(dataClassQuiz);
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

}