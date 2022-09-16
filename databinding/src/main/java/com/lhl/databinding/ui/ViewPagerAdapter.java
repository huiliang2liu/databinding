package com.lhl.databinding.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    List<Fragment> mFragments = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentActivity activity, @NonNull List<Fragment> fragments) {
        this(activity.getSupportFragmentManager(), fragments);
    }

    public ViewPagerAdapter(@NonNull Fragment fm, @NonNull List<Fragment> fragments) {
        this(fm.getChildFragmentManager(), fragments);
    }

    public ViewPagerAdapter(@NonNull FragmentManager fm, @NonNull List<Fragment> fragments) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        if (fragments == null || fragments.isEmpty())
            throw new RuntimeException("fragments is empty");
        mFragments.addAll(fragments);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
