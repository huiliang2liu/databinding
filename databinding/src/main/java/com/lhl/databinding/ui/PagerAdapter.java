package com.lhl.databinding.ui;


import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

public abstract class PagerAdapter<T> extends androidx.viewpager.widget.PagerAdapter implements IAdapter {
    private static final String TAG = "PagerAdapter";
    private List<Tag> tags = new ArrayList<>();
    private List<T> objects = new ArrayList<>();
    protected Context context;
    private ViewPager viewPager;
    private LayoutInflater inflater;

    public PagerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void startUpdate(@NonNull ViewGroup container) {
        super.startUpdate(container);
        viewPager = (ViewPager) container;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == ((Tag) o).view.getRoot();
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void notifyDataSetChanged(int position) {
        for (Tag tag : tags) {
            if (!tag.pause && tag.position == position) {
                tag.viewHolder.setContext(position);
                break;
            }
        }

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        Tag tag = (Tag) object;
        tag.pause = true;
        container.removeView(tag.view.getRoot());
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        int itemType = getItemViewType(position);
        Tag mTag = null;
        for (Tag tag : tags) {
            if (tag.pause) {
                if (tag.itemType == itemType) {
                    mTag = tag;
                    break;
                }
            }
        }
        if (mTag == null) {
            mTag = new Tag();
            mTag.itemType = itemType;
            mTag.viewHolder = new ViewHolder<>();
            Application application = getApplication();
            if (application != null)
                mTag.viewHolder.setContext(application, applicationId());
            mTag.view = getDataBinding(itemType, container, inflater);
            mTag.viewHolder.setView(mTag.view);
            mTag.viewHolder.setAdapter(this);
            mTag.viewHolder.setId(getModelId(itemType));
            tags.add(mTag);
        }
        mTag.position = position;
        mTag.pause = false;
        mTag.viewHolder.setContext(position);
        container.addView(mTag.view.getRoot());
        return mTag;
    }

    public Application getApplication() {
        return null;
    }

    public int applicationId() {
        return 0;
    }

    @Override
    public void setSelection(int position) {
        if (viewPager != null)
            viewPager.setCurrentItem(position);
    }

    @Override
    public T getItem(int position) {
        return objects.get(position);
    }

    @Override
    public void addItem(Object o) {
        if (o == null)
            return;
        objects.add((T) o);
        notifyDataSetChanged();
    }

    @Override
    public void addItem(List list) {
        if (list == null || list.size() <= 0)
            return;
        objects.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        View view = (View) object;
        if (tags.indexOf(object) < 0)
            return POSITION_NONE;
        return POSITION_UNCHANGED;
    }

    @Override
    public void remove(Object o) {
        if (o == null)
            return;
        if (objects.remove(o))
            notifyDataSetChanged();
    }

    @Override
    public void remove(List list) {
        if (list == null || list.size() <= 0)
            return;
        if (objects.removeAll(list))
            notifyDataSetChanged();
    }

    @Override
    public void clean() {
        objects.clear();
        tags.clear();
        notifyDataSetChanged();
    }

    //    public abstract ViewHolder<T> getViewHolder(int itemType);
    public abstract int getModelId(int getModelId);

    public abstract DataBinding getDataBinding(int itemType, ViewGroup group, LayoutInflater inflater);

    private class Tag {
        private ViewHolder viewHolder;
        private DataBinding view;
        private boolean pause = false;
        private int itemType = 0;
        private int position;
    }
}
