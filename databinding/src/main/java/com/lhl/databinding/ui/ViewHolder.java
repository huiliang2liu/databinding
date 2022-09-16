package com.lhl.databinding.ui;

import android.view.View;


/**
 * com.tvblack.lamp.adapter.tag
 * 2019/2/18 16:40
 * instructions：
 * author:liuhuiliang  email:825378291@qq.com
 **/
public class ViewHolder<T> {
    protected IAdapter<T> adapter;
    protected int position = -1;
    private DataBinding context;
    private int id;

    public final void setId(int id) {
        this.id = id;
    }

    public final void setAdapter(IAdapter<T> adapter) {
        this.adapter = adapter;
    }

    public void setView(DataBinding context) {
        this.context = context;
    }

    final void setContext(int position) {
        this.position = position;
        setContext(adapter.getItem(position));
    }

    public void setContext(T t) {
        setContext(t, id);
    }

    public void setContext(T t, int id) {
        context.bindModel(id, t);
    }

}