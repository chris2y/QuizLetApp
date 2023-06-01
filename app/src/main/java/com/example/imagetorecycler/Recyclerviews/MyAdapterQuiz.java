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

public class MyAdapterQuiz extends RecyclerView.Adapter<MyViewHolderQuiz> {

    private Context context;
    private List<DataClassQuiz> dataList;

    public MyAdapterQuiz(Context context, List<DataClassQuiz> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolderQuiz onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_display_quiz, parent, false);
        return new MyViewHolderQuiz(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolderQuiz holder, int position) {

        if (dataList.get(position).getDataImage() != null && !dataList.get(position).getDataImage().isEmpty()) {
            Glide.with(context)
                    .load(dataList.get(position).getDataImage().get(0))
                    .thumbnail(0.1f) // load a thumbnail that is 50% the size of the full-quality image
                    .override(100, 100) // load a thumbnail that is 200x200 pixels in size
                    .into(holder.recImage);
        }


        //Glide.with(context).load(dataList.get(position).getDataImage()).into(holder.recImage);
        holder.recDepartment.setText("Dept: " + dataList.get(position).getDataDepartment());
        holder.recCourse.setText("Course: " + dataList.get(position).getDataYear());
        holder.recExamType.setText("Type: " + dataList.get(position).getDataExamType());
        holder.recYear.setText("Year: " + dataList.get(position).getDataYear());
        holder.recUniversity.setText("\uD83D\uDCCC" + dataList.get(position).getDataUniversity());



        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String itemId = dataList.get(holder.getAdapterPosition()).getKey();
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Questions Data").child(itemId).child("seenCount");
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
                        intent.putStringArrayListExtra("ImageList", (ArrayList<String>) dataList.get(holder.getAdapterPosition()).getDataImage());
                        intent.putExtra("Year", dataList.get(holder.getAdapterPosition()).getDataYear());
                        intent.putExtra("Department", dataList.get(holder.getAdapterPosition()).getDataDepartment());
                        intent.putExtra("Key", dataList.get(holder.getAdapterPosition()).getKey());
                        intent.putExtra("ExamType", dataList.get(holder.getAdapterPosition()).getDataExamType());
                        intent.putExtra("University", dataList.get(holder.getAdapterPosition()).getDataUniversity());
                        intent.putExtra("Course", dataList.get(holder.getAdapterPosition()).getDataCourse());
                        intent.putExtra("Date", dataList.get(holder.getAdapterPosition()).getDataDate());
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

class MyViewHolderQuiz extends RecyclerView.ViewHolder{

    ImageView recImage;
    TextView recDepartment, recExamType, recYear,recUniversity,recCourse;
    CardView recCard;

    public MyViewHolderQuiz(@NonNull View itemView) {
        super(itemView);

        recImage = itemView.findViewById(R.id.recImage);
        recCard = itemView.findViewById(R.id.recCard);
        recExamType = itemView.findViewById(R.id.recExamType);
        recYear = itemView.findViewById(R.id.recYear);
        recDepartment = itemView.findViewById(R.id.recDepartment);
        recUniversity = itemView.findViewById(R.id.recUniversity);
        recCourse = itemView.findViewById(R.id.recCourse);


    }
}