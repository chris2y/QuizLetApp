package com.example.imagetorecycler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.example.imagetorecycler.Recyclerviews.SliderAdapter;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class DetailSelectedItemQuiz extends AppCompatActivity {

    TextView detailDepartment, detailYear, detailExamType,detailUniversity,detailCourse,seenCounter,detailDate;
    ImageButton backButton;

    SliderView sliderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_selected_item_quize);
        detailDepartment = findViewById(R.id.detailDepartment);
        detailYear = findViewById(R.id.detailYear);
        detailExamType = findViewById(R.id.detailExamType);
        detailCourse = findViewById(R.id.detailCourse);
        seenCounter = findViewById(R.id.dataSeenCounter);
        detailDate = findViewById(R.id.dataDate);
        detailUniversity = findViewById(R.id.detailUniversity);
        sliderView = findViewById(R.id.detailImageSlider1);
        backButton = findViewById(R.id.backButton);



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
                detailExamType.setText("ExamType: " + bundle.getString("ExamType"));
                detailUniversity.setText("University: " + bundle.getString("University"));
                detailCourse.setText("Course: " + bundle.getString("Course"));
                seenCounter.setText("Seen: " + bundle.getString("SeenCount"));
                detailDate.setText("Date: " + bundle.getString("Date"));



                ArrayList<String> imageList = bundle.getStringArrayList("ImageList");

                if (imageList != null && imageList.size() > 0) {

                    SliderAdapter sliderAdapter = new SliderAdapter(imageList);

                    sliderView.setSliderAdapter(sliderAdapter);


                }





        }

    }
}