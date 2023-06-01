package com.example.imagetorecycler.FragmentsForUpload;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imagetorecycler.R;
import com.example.imagetorecycler.Recyclerviews.DataClassAssignment;
import com.example.imagetorecycler.Recyclerviews.RecyclerAdapterForAssignmentUpload;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AssignmentUpload extends Fragment implements RecyclerAdapterForAssignmentUpload.countOfImages {



    Button pick,upload;

    EditText txtDepartment, txtYear, txtDescription,txtCourse;
    TextView selcetednum;

    RecyclerView recyclerView;
    RecyclerAdapterForAssignmentUpload adapter;
    private static final int READ_PERMISSION = 101;


    ArrayList<Uri> ChooseImageList = new ArrayList<>();
    ArrayList<String> UrlsList = new ArrayList<>();
    ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private String userID;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assignment_upload, container, false);

        pick = view.findViewById(R.id.pickimage);
        txtDepartment = view.findViewById(R.id.txtDepartment);
        txtYear = view.findViewById(R.id.txtYear);
        txtDescription = view.findViewById(R.id.txtDescription);
        txtCourse = view.findViewById(R.id.txtCourse);
        upload = view.findViewById(R.id.UploadBtn);
        selcetednum = view.findViewById(R.id.selectedPhotos);

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new RecyclerAdapterForAssignmentUpload(ChooseImageList,getContext(),  this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        recyclerView.setAdapter(adapter);

        userID = auth.getInstance().getCurrentUser().getUid();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading Data");
        progressDialog.setMessage("Please Wait While Uploading Your data...");










        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ChooseImageList.size() == 0){
                    Toast.makeText(getContext(),"Please select image",Toast.LENGTH_SHORT).show();
                }


                else
                    UploadIMages();


            }
        });

        pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
            }
        });

        return view;

    }




    private void UploadIMages() {

        // we need list that images urls
        for (int i = 0; i < ChooseImageList.size(); i++) {
            Uri IndividualImage = ChooseImageList.get(i);
            if (IndividualImage != null) {
                progressDialog.show();
                String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                StorageReference ImageFolder = FirebaseStorage.getInstance().getReference().child("Assignment Images");
                final StorageReference ImageName = ImageFolder.child(currentDate + " Image " + i + ": " + IndividualImage.getLastPathSegment());
                ImageName.putFile(IndividualImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                UrlsList.add(String.valueOf(uri));
                                if (UrlsList.size() == ChooseImageList.size()) {
                                    StoreLinks(UrlsList);
                                }
                            }
                        });

                    }
                });
            } else {
                Toast.makeText(getContext(), "Please fill All Field", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void StoreLinks(ArrayList<String> urlsList) {
        // now we need get text from EditText
        String dataDepartment = txtDepartment.getText().toString();
        String dataYear = txtYear.getText().toString();
        String dataDescription = txtDescription.getText().toString();
        String dataCourse = txtCourse.getText().toString();

        if (!TextUtils.isEmpty(dataYear) && !TextUtils.isEmpty(dataDepartment) &&
                !TextUtils.isEmpty(dataDescription) ) {
            // now we need a model class
            DataClassAssignment model = new DataClassAssignment(dataDepartment,dataYear, "",
                    UrlsList,dataDescription,dataCourse,"",userID);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Assignment Data");
            String itemId = databaseReference.push().getKey(); // generate unique key for the item
            String currentDate = DateFormat.getDateInstance().format(Calendar.getInstance().getTime());
            model.setItemId(itemId);
            model.setDataDate(currentDate);

            databaseReference.child(itemId).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progressDialog.dismiss();
                    ChooseImageList.clear();
                    UrlsList.clear();
                    adapter.notifyDataSetChanged();
                    selcetednum.setText("Selected Images");
                    // if data uploaded successfully then show toast
                    Toast.makeText(getContext(), "Your data Uploaded Successfully", Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(getContext(), "Please Fill All field", Toast.LENGTH_SHORT).show();
        }


    }




    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission((Activity) getContext(),
                    android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Request the permission.
                ActivityCompat.requestPermissions((Activity) getContext(), new
                        String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
                pickImageFromGallery();
            } else {
                pickImageFromGallery();
            }
        } else {
            pickImageFromGallery();
        }
    }


    int SELECT_PICTURES = 1;
    private void pickImageFromGallery() {
        // here we go to gallery and select Image
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); //allows any image file type. Change * to specific extension to limit it
//**The following line is the important one!
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PICTURES) {
            if(resultCode == Activity.RESULT_OK) {

                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for(int i = 0; i < count; i++) {

                        if(ChooseImageList.size()<8){
                            Uri imageUri = data.getClipData().getItemAt(i).getUri();
                            ChooseImageList.add(imageUri);
                        }
                        else {
                            Toast.makeText(getContext(),"You can only select 8 images",Toast.LENGTH_SHORT).show();
                        }

                    }
                } else if(data.getData() != null) {
                    if(ChooseImageList.size()<8) {

                        Uri imageUri = data.getData();
                        ChooseImageList.add(imageUri);
                    }
                    else {
                        Toast.makeText(getContext(),"You can only select 8 images",Toast.LENGTH_SHORT).show();
                    }
                }
                adapter.notifyDataSetChanged();
                selcetednum.setText("Selected Images (" + ChooseImageList.size() + ")");
            } else {
                Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void clicked(int getSize) {
        selcetednum.setText("Selected Images ("+ChooseImageList.size()+")");
    }

}