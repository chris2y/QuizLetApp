package com.example.imagetorecycler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.imagetorecycler.Recyclerviews.SliderAdapterFull;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class ImageFullscrenActivity extends AppCompatActivity {

    SliderView sliderView;
    ArrayList<String> imageList;
    ImageView backButton,downloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image__full_screen);
        backButton = findViewById(R.id.xTheIMage);
        //downloadButton = findViewById(R.id.downloadButtonImage);
        sliderView = findViewById(R.id.detailImageSlider2);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        /*downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ImageFullscrenActivity.this,"Comming Soon",Toast.LENGTH_SHORT).show();
            }
        });*/


        imageList = getIntent().getStringArrayListExtra("imageList");
        int position = getIntent().getIntExtra("position", 0);


        SliderAdapterFull sliderAdapterFull = new SliderAdapterFull(imageList);

        sliderView.setSliderAdapter(sliderAdapterFull);
        sliderView.setCurrentPagePosition(position);
    }
}
