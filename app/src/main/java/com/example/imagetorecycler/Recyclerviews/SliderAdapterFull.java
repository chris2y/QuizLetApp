package com.example.imagetorecycler.Recyclerviews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.imagetorecycler.R;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

public class SliderAdapterFull extends SliderViewAdapter<SliderAdapterFull.Holder>{

    ArrayList<String> images;

    public SliderAdapterFull(ArrayList<String> images){
        this.images = images;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.slider_item,parent,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder viewHolder, int position) {

        Glide.with(viewHolder.imageView.getContext())
                .load(images.get(position))
                .into(viewHolder.imageView);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    public class Holder extends ViewHolder{

        ImageView imageView;

        public Holder(View itemView){
            super(itemView);
            imageView = itemView.findViewById(R.id.image_view);

        }
    }
}

