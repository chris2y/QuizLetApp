package com.example.imagetorecycler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.imagetorecycler.Recyclerviews.SliderAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class DetailSelectedItemAssignment extends AppCompatActivity {


    TextView detailDepartment, detailYear, detailDescription,detailCourse,seenCounter,detailDate,detailUsername;
    String userPassedID;
    ImageButton backButton;
    ImageView userImage;
    SliderView sliderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_selected_item_assignment);
        detailDepartment = findViewById(R.id.detailDepartment);
        detailYear = findViewById(R.id.detailYear);
        detailDescription = findViewById(R.id.detailDescription);
        detailCourse = findViewById(R.id.detailCourse);
        seenCounter = findViewById(R.id.dataSeenCounter);
        detailDate = findViewById(R.id.dataDate);
        sliderView = findViewById(R.id.detailImageSlider1);
        backButton = findViewById(R.id.backButton);
        detailUsername = findViewById(R.id.detailTitle);
        userImage = findViewById(R.id.profileImage);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });








        Bundle bundle = getIntent().getExtras();
        if (bundle != null){

            detailDepartment.setText("Department: " + bundle.getString("Department"));
            detailYear.setText("Year: " + bundle.getString("Year"));
            detailDescription.setText("Description: " + bundle.getString("Description"));
            detailCourse.setText("Course: " + bundle.getString("Course"));
            seenCounter.setText("Seen: " + bundle.getString("SeenCount"));
            detailDate.setText("Date: " + bundle.getString("Date"));
            userPassedID = bundle.get("UserID").toString();


            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userPassedID);


            ValueEventListener valueEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // Retrieve the user's name from the dataSnapshot
                    String userName = dataSnapshot.child("dataFullName").getValue(String.class);
                    String imageUrl = dataSnapshot.child("dataProfileImage").getValue(String.class);


                    if(imageUrl != null){
                        Glide.with(getApplicationContext())
                                .load(imageUrl)
                                .thumbnail(0.1f) // load a thumbnail that is 50% the size of the full-quality image
                                .override(50, 50) // load a thumbnail that is 200x200 pixels in size
                                .into(userImage);
                         }


                    // Update the TextView with the user's name
                    detailUsername.setText(userName);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Handle the error
                }
            };
// Add the value event listener to the database reference
            databaseReference.addValueEventListener(valueEventListener);



            ArrayList<String> imageList = bundle.getStringArrayList("ImageList");

            if (imageList != null && imageList.size() > 0) {

                SliderAdapter sliderAdapter = new SliderAdapter(imageList);

                sliderView.setSliderAdapter(sliderAdapter);


            }





        }

    }
}