package com.example.imagetorecycler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.imagetorecycler.Recyclerviews.ViewPagerAdapterFragmentForAccount;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class ActivityAccount extends AppCompatActivity {
    ValueEventListener eventListener;
    private static final int REQUEST_SELECT_IMAGE = 100;

    TextView applied,posted,logout;
    ImageView selectImage,profileImageDisplay;
    private SharedPreferences sharedPreferences;

    private Uri croppedImageUri;
    Uri selectedImageUri;
    private String userID;
    private FirebaseAuth auth;

    DatabaseReference databaseReference;
    String profileImagesUrl,emailFromFirebase,phoneFromFirebase,nameFromFirebase;

    StorageReference imageFolder;

    FirebaseStorage storage;
    String imageUrlForStoring;

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    ViewPagerAdapterFragmentForAccount viewPagerAdapterFragmentForAccount;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        logout = findViewById(R.id.btnLogout);
        selectImage = findViewById(R.id.editImage);
        profileImageDisplay = findViewById(R.id.accountimage);
        applied = findViewById(R.id.txtApplied);
        posted = findViewById(R.id.txtPosted);




        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPager);
        viewPagerAdapterFragmentForAccount = new ViewPagerAdapterFragmentForAccount(this);
        viewPager2.setAdapter(viewPagerAdapterFragmentForAccount);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        userID = auth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationAccount);
        bottomNavigationView.setSelectedItemId(R.id.nav_account);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_account) {
                return true;
            } else if (item.getItemId() == R.id.nav_assignment) {
                startActivity(new Intent(getApplicationContext(), DisplayQuizData.class));
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_upload) {

                startActivity(new Intent(getApplicationContext(), UploadData.class));
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;

            } else if (item.getItemId() == R.id.nav_home) {
                startActivity(new Intent(getApplicationContext(), DisplayAssignmentData.class));
                //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                return true;
            } else {
                return false;
            }
        });


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                FirebaseAuth.getInstance().signOut();
                finish();
            }
        });

        applied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityAccount.this, "Applied", Toast.LENGTH_SHORT).show();
            }
        });

        posted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ActivityAccount.this, "Posted", Toast.LENGTH_SHORT).show();
            }
        });

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImageIntent();
            }
        });

        profileImageDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (profileImagesUrl == null) {
                    return;
                }
                Intent intent = new Intent(getApplicationContext(), FullScreenImageNewActivity.class);
                intent.putExtra("image_url", profileImagesUrl);
                startActivity(intent);
            }
        });


        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    profileImagesUrl = dataSnapshot.child("dataProfileImage").getValue(String.class);

                    if (profileImagesUrl != null) {
                        loadProfileImage(profileImagesUrl);
                    }
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
    }



    private void loadProfileImage(String profileImageUrl) {
            Glide.with(getApplicationContext())
                    .load(profileImageUrl)
                    .thumbnail(0.2f)
                    .override(200, 200)
                    .into(profileImageDisplay);
        }


    // Start the image selection process
    private void selectImageIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_SELECT_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_SELECT_IMAGE && resultCode == RESULT_OK) {
            selectedImageUri = data.getData();
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            String randomFileName = "cropped_" + timeStamp + "_" + new Random().nextInt(1000);
            // Start the crop activity using UCrop library
            UCrop.of(selectedImageUri, Uri.fromFile(new File(getCacheDir(), randomFileName)))
                    .withAspectRatio(1, 1)
                    .withMaxResultSize(720,720)
                    .start(this);

        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            croppedImageUri = UCrop.getOutput(data);
            uploadProfileImage();
            Log.d("Croped then passed to Upload","Success");
            // Load the cropped image into an ImageView

        }
    }

    private void uploadProfileImage() {
        String currentDate = DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        imageFolder = FirebaseStorage.getInstance().getReference().child("User Profiles");
        StorageReference imageName = imageFolder.child(currentDate + " Image: " + selectedImageUri.getLastPathSegment());

        imageName.putFile(croppedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        imageUrlForStoring = uri.toString();
                        if (profileImagesUrl != null) {
                            Log.d("deleteProfileImageFromStorage","Success");
                            deleteProfileImageFromStorage();
                        }
                        else {
                            storeLinks(imageUrlForStoring);
                            Log.d("storeLinks","Success");
                        }

                    }
                });
            }
        });
    }

    private void storeLinks(String imageUrl) {
        Map<String, Object> data = new HashMap<>();
        data.put("dataProfileImage", imageUrl);
        data.put("dataUserID", userID);

        databaseReference.updateChildren(data).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                selectedImageUri = null;
                // If data uploaded successfully, show a toast
                Toast.makeText(ActivityAccount.this, "Your data has been uploaded successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void deleteProfileImageFromStorage() {
        // Get a reference to the Firebase Storage instance
        storage = FirebaseStorage.getInstance();
        // Create a reference to the image file using its URL
        StorageReference imageRef = storage.getReferenceFromUrl(profileImagesUrl);
        // Delete the image file
        imageRef.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Image deleted successfully
                            storeLinks(imageUrlForStoring);
                            //deleteImageFromRealtimeDatabase();
                        } else {
                            // Handle the failure case when deleting the image
                            Exception e = task.getException();
                            if (e instanceof StorageException && ((StorageException) e).getErrorCode() == StorageException.ERROR_OBJECT_NOT_FOUND) {
                                // Handle the case when the object does not exist at the specified location
                                Log.e("ImageDeletionExample", "Image does not exist at the specified location");
                            } else {
                                // Handle other failure cases
                                Log.e("ImageDeletionExample", "Failed to delete image: " + e.getMessage(), e);
                            }
                        }
                    }
                });
    }

}
































   /* private void deleteImageFromRealtimeDatabase() {
        databaseReference.child("dataProfileImage").setValue(null)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Realtime Database data set to null successfully

                            Toast.makeText(ActivityAccount.this, "Deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle the failure case when setting Realtime Database data to null
                            Exception e = task.getException();
                            Log.e("ImageDeletionExample", "Failed to set Realtime Database data to null: " + e.getMessage(), e);
                        }
                    }
                });
    }*/

  /*  private void pickImageFromGalleryy() {
        // Here we go to the gallery and select an image
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*"); // Allows any image file type. Change * to specific extension to limit it
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }*/


    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data.getData() != null) {
                    Uri imageUri = data.getData();
                    ChooseImageList.add(imageUri);
                    Glide.with(this)
                            .load(ChooseImageList.get(0))
                            .into(profileImage);
                    Toast.makeText(getApplicationContext(), " Image Selected", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }*/



