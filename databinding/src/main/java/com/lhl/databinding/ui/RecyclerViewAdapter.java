package com.lhl.databinding.ui;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.lhl.databinding.BindData;
import com.lhl.databinding.widget.CardLayoutManager;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerViewAdapter<T> extends RecyclerView.Adapter implements IAdapter {
    private List<T> objects = new ArrayList<>();
    protected RecyclerView recyclerView;
    protected Context context;
    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private LayoutInflater inflater;

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    public RecyclerViewAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void notifyDataSetChanged(int position) {
        if (recyclerView == null)
            return;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null)
            return;
        if (manager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) manager;
            int first = linearLayoutManager.findFirstVisibleItemPosition();
            int last = linearLayoutManager.findLastVisibleItemPosition();
            if (position >= first && position <= last)
                notifyItemChanged(position);
        } else if (manager instanceof GridLayoutManager) {
            GridLayoutManager linearLayoutManager = (GridLayoutManager) manager;
            int first = linearLayoutManager.findFirstVisibleItemPosition();
            int last = linearLayoutManager.findLastVisibleItemPosition();
            if (position >= first && position <= last)
                notifyItemChanged(position);
        }
    }

    @Override
    public void setSelection(int position) {
        if (recyclerView == null)
            return;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null)
            return;
        manager.scrollToPosition(position);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new VH(getDataBinding(viewType, viewGroup, inflater), viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        VH vh = (VH) viewHolder;
        vh.bindView(position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    @Override
    public int getCount() {
        return objects.size();
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
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager != null && layoutManager instanceof CardLayoutManager) {
                CardLayoutManager cardLayoutManager = (CardLayoutManager) layoutManager;
                cardLayoutManager.resetSize();
            }
        }
        notifyDataSetChanged();
    }


    @Override
    public void addItem(List list) {
        if (list == null || list.size() <= 0)
            return;
        objects.addAll(list);
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager != null && layoutManager instanceof CardLayoutManager) {
                CardLayoutManager cardLayoutManager = (CardLayoutManager) layoutManager;
                cardLayoutManager.resetSize();
            }
        }
        notifyDataSetChanged();
    }

    @Override
    public void remove(Object o) {
        if (o == null)
            return;
        if (objects.remove(o)) {
            if (recyclerView != null) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager instanceof CardLayoutManager) {
                    CardLayoutManager cardLayoutManager = (CardLayoutManager) layoutManager;
                    cardLayoutManager.resetSize();
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void remove(List list) {
        if (list == null || list.size() <= 0)
            return;
        if (objects.removeAll(list)) {
            if (recyclerView != null) {
                RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                if (layoutManager != null && layoutManager instanceof CardLayoutManager) {
                    CardLayoutManager cardLayoutManager = (CardLayoutManager) layoutManager;
                    cardLayoutManager.resetSize();
                }
            }
            notifyDataSetChanged();
        }
    }

    @Override
    public void clean() {
        objects.clear();
        if (recyclerView != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager != null && layoutManager instanceof CardLayoutManager) {
                CardLayoutManager cardLayoutManager = (CardLayoutManager) layoutManager;
                cardLayoutManager.resetSize();
            }
        }
        notifyDataSetChanged();
    }

    private class VH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private ViewHolder viewHolder;
        private int position;
        private long clickTime = 0;

        public VH(@NonNull DataBinding binding, int itemType) {
            super(binding.getRoot());
            viewHolder = new ViewHolder();
            viewHolder.setView(binding);
            viewHolder.setId(getModelId(itemType));
            viewHolder.setAdapter(RecyclerViewAdapter.this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
            Application application = getApplication();
            if (application == null)
                return;
            viewHolder.setContext(application, applicationId());
        }

        public void bindView(int position) {
            this.position = position;
            viewHolder.setContext(position);
        }

        @Override
        public void onClick(View v) {
            long time = System.currentTimeMillis();
            if (time - clickTime < BindData.MIN_CLICK_TIME)
                return;
            clickTime = time;
            if (onItemClickListener != null)
                onItemClickListener.onItemClick(VH.this.itemView, position, RecyclerViewAdapter.this.getItemId(position));
        }

        @Override
        public boolean onLongClick(View v) {
            if (onItemLongClickListener != null)
                return onItemLongClickListener.onItemLongClick(itemView, position, RecyclerViewAdapter.this.getItemId(position));
            return false;
        }
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position, long id);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position, long id);
    }


    public abstract DataBinding getDataBinding(int itemType, ViewGroup group, LayoutInflater inflater);

    public abstract int getModelId(int itemType);

    public Application getApplication() {
        return null;
    }

    public int applicationId() {
        return 0;
    }
}
