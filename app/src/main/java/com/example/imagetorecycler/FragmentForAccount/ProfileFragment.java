package com.example.imagetorecycler.FragmentForAccount;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.imagetorecycler.DisplayQuizData;
import com.example.imagetorecycler.EditProfileDetail;
import com.example.imagetorecycler.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    Button editProfile;
    TextView txtPhoneNumber,txtEmail,txtFullName,txtDescription;

    String emailFromFirebase,phoneFromFirebase,nameFromFirebase,descriptionFromFirebase;
    private String userID;
    DatabaseReference databaseReference;

    private FirebaseAuth auth;
    ValueEventListener eventListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        editProfile = view.findViewById(R.id.btnEditProfile);
        txtPhoneNumber = view.findViewById(R.id.txtPhoneLoad);
        txtEmail = view.findViewById(R.id.txtEmailLoad);
        txtFullName = view.findViewById(R.id.txtEmailLoad);
        txtDescription =view.findViewById(R.id.txtAboutLoad);

        userID = auth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);


        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    emailFromFirebase = dataSnapshot.child("dataEmail").getValue(String.class);
                    phoneFromFirebase = dataSnapshot.child("dataPhoneNumber").getValue(String.class);
                    nameFromFirebase = dataSnapshot.child("dataFullName").getValue(String.class);
                    descriptionFromFirebase = dataSnapshot.child("dataDescription").getValue(String.class);


                    txtEmail.setText(emailFromFirebase);
                    txtPhoneNumber.setText(phoneFromFirebase);
                    txtDescription.setText(descriptionFromFirebase);



                } else {
                    // The specified user ID does not exist in the database
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors
            }
        };

        databaseReference.addValueEventListener(eventListener);



        editProfile.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), EditProfileDetail.class);
            Bundle extras = new Bundle();
            extras.putString("email", emailFromFirebase);
            extras.putString("phone", phoneFromFirebase);
            extras.putString("name", nameFromFirebase);
            extras.putString("description", descriptionFromFirebase);
            intent.putExtras(extras);
            startActivity(intent);
        });

        return view;
    }
}