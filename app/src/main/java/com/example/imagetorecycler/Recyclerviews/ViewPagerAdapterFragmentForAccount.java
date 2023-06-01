package com.example.imagetorecycler.Recyclerviews;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.imagetorecycler.FragmentForAccount.BankDetailFragment;
import com.example.imagetorecycler.FragmentForAccount.ProfileFragment;
import com.example.imagetorecycler.FragmentForAccount.ReviewFragment;
import com.example.imagetorecycler.FragmentsForUpload.AssignmentUpload;
import com.example.imagetorecycler.FragmentsForUpload.QuizUpload;


public class ViewPagerAdapterFragmentForAccount extends FragmentStateAdapter {
    public ViewPagerAdapterFragmentForAccount(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0){
            return new ProfileFragment();
        } else if (position == 1) {
            return new ReviewFragment();
        }
        else if (position == 2) {
            return new BankDetailFragment();
        }
        return new ProfileFragment();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
