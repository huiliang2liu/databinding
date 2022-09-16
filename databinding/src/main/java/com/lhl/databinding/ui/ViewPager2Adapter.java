package com.lhl.databinding.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPager2Adapter extends FragmentStateAdapter {
    List<Fragment> mFragments = new ArrayList<>();

    public ViewPager2Adapter(@NonNull FragmentActivity activity, @NonNull List<Fragment> fragments) {
        this(activity.getSupportFragmentManager(), fragments, activity.getLifecycle());
    }

    public ViewPager2Adapter(@NonNull Fragment fm, @NonNull List<Fragment> fragments) {
        this(fm.getChildFragmentManager(), fragments, fm.getLifecycle());
    }

    public ViewPager2Adapter(@NonNull FragmentManager fm, @NonNull List<Fragment> fragments, Lifecycle lifecycle) {
        super(fm, lifecycle);
        if (fragments == null || fragments.isEmpty())
            throw new RuntimeException("fragments is empty");
        mFragments.addAll(fragments);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getItemCount() {
        return mFragments.size();
    }
}
