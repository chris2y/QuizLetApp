package com.example.imagetorecycler.Recyclerviews;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.imagetorecycler.FragmentsForUpload.QuizUpload;
import com.example.imagetorecycler.FragmentsForUpload.AssignmentUpload;


public class ViewPagerAdapterFragmentForUpload extends FragmentStateAdapter {
    public ViewPagerAdapterFragmentForUpload(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0){
            return new QuizUpload();
        } else if (position == 1) {
            return new AssignmentUpload();

        }
        return new QuizUpload();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
