package com.example.imagetorecycler.Recyclerviews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.imagetorecycler.DetailSelectedItemAssignment;
import com.example.imagetorecycler.DetailSelectedItemQuiz;
import com.example.imagetorecycler.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterAssignment extends RecyclerView.Adapter<MyViewHolderAssignment> {

    private Context context;
    private List<DataClassAssignment> dataList;

    public MyAdapterAssignment(Context context, List<DataClassAssignment> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolderAssignment onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_display_assignment, parent, false);
        return new MyViewHolderAssignment(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolderAssignment holder, int position) {

        if (dataList.get(position).getDataImage() != null && !dataList.get(position).getDataImage().isEmpty()) {
            Glide.with(context)
                    .load(dataList.get(position).getDataImage().get(0))
                    .thumbnail(0.1f) // load a thumbnail that is 50% the size of the full-quality image
                    .override(100, 100) // load a thumbnail that is 200x200 pixels in size
                    .into(holder.recImage);
        }


        //Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);
        holder.recDepartment.setText("Dept: " + dataList.get(position).getDataDepartment());
        holder.recCourse.setText("Course: " + dataList.get(position).getDataCourse());
        holder.recYear.setText("Year: " + dataList.get(position).getDataYear());




        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String itemId = dataList.get(holder.getAdapterPosition()).getKey();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Assignment Data").child(itemId).child("seenCount");
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Long seenCount = 0L;
                        if (dataSnapshot.exists()) {
                            seenCount = dataSnapshot.getValue(Long.class) + 1;
                        }
                        // Increment the "seenCount" value by 1 and update it in the database
                        databaseReference.runTransaction(new Transaction.Handler() {
                            @Override
                            public Transaction.Result doTransaction(MutableData mutableData) {
                                Long currentValue = mutableData.getValue(Long.class);
                                if (currentValue == null) {
                                    mutableData.setValue(1);
                                } else {
                                    mutableData.setValue(currentValue + 1);
                                }
                                return Transaction.success(mutableData);
                            }

                            @Override
                            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                                if (databaseError != null) {
                                    // Handle error
                                } else {
                                    // Handle success
                                }
                            }
                        });

                        Intent intent = new Intent(context, DetailSelectedItemAssignment.class);
                        intent.putExtra("Type","Assignment");
                        intent.putStringArrayListExtra("ImageList", (ArrayList<String>) dataList.get(holder.getAdapterPosition()).getDataImage());
                        intent.putExtra("Year", dataList.get(holder.getAdapterPosition()).getDataYear());
                        intent.putExtra("Department", dataList.get(holder.getAdapterPosition()).getDataDepartment());
                        intent.putExtra("Key", dataList.get(holder.getAdapterPosition()).getKey());
                        intent.putExtra("Description", dataList.get(holder.getAdapterPosition()).getDataDescription());
                        intent.putExtra("Course", dataList.get(holder.getAdapterPosition()).getDataCourse());
                        intent.putExtra("Date", dataList.get(holder.getAdapterPosition()).getDataDate());
                        intent.putExtra("UserID",dataList.get(holder.getAdapterPosition()).getDataUserID());
                        intent.putExtra("SeenCount", seenCount.toString());

                        context.startActivity(intent);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle any errors
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


}

class MyViewHolderAssignment extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recDepartment, recYear,recCourse;
    CardView recCard;

    public MyViewHolderAssignment(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recYear = itemView.findViewById(R.id.recYear);
        recDepartment = itemView.findViewById(R.id.recDepartment);
        recCourse = itemView.findViewById(R.id.recCourse);


    }
}