package com.lhl.databinding.ui;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;

import com.lhl.databinding.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter implements IAdapter<Fragment> {
    List<Fragment> mFragments = new ArrayList<>();
    private ViewPager viewPager;
    private FragmentManager fm;
    private List<Fragment> removeOrUpdate = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentActivity activity) {
        this(activity.getSupportFragmentManager());
    }

    public ViewPagerAdapter(@NonNull Fragment fm) {
        this(fm.getChildFragmentManager());
    }

    public ViewPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fm = fm;
    }

    @Override
    public void startUpdate(@NonNull ViewGroup container) {
        super.startUpdate(container);
        viewPager = (ViewPager) container;
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

    @Override
    public void addItem(Fragment fragment) {
        mFragments.add(fragment);
        notifyDataSetChanged();
    }

    @Override
    public void addItem(List<Fragment> fragments) {
        mFragments.addAll(fragments);
        notifyDataSetChanged();
    }

    @Override
    public void remove(Fragment fragment) {
        mFragments.remove(fragment);
        if (!fm.getFragments().contains(fragment))
            return;
        removeOrUpdate.add(fragment);
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.remove(fragment);
        transaction.commit();
        notifyDataSetChanged();
    }

    @Override
    public void remove(List<Fragment> fragments) {
        if (fragments == null || fragments.isEmpty())
            return;
        mFragments.removeAll(fragments);
        boolean notify = false;
        FragmentTransaction transaction = fm.beginTransaction();
        List<Fragment> fmFragments = fm.getFragments();
        for (Fragment fragment : fragments) {
            if (fmFragments.contains(fragment)) {
                removeOrUpdate.add(fragment);
                transaction.remove(fragment);
                notify = true;
            }
        }
        if (!notify)
            return;
        transaction.commit();
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        if (removeOrUpdate.remove(object))
            return POSITION_NONE;
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public long getItemId(int position) {
        return mFragments.get(position).hashCode();
    }

    @Override
    public void clean() {
        remove(new ArrayList<>(mFragments));
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void notifyDataSetChanged(int position) {
        removeOrUpdate.add(mFragments.get(position));
        notifyDataSetChanged();
    }

    @Override
    public void setSelection(int position) {
        if (viewPager != null)
            viewPager.setCurrentItem(position);
    }
}
