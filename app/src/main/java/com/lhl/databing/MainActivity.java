package com.lhl.databing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.ObservableInt;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.lhl.databinding.BindData;
import com.lhl.databinding.ui.BaseActivity;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

public class MainActivity extends BaseActivity implements BindData.OnClickListener, OnLoadMoreListener, OnRefreshListener {
    public ObservableInt type = new ObservableInt(-1);
    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void bindModel() {
        super.bindModel();
        bindModel(BR.activity,this);
    }

    @Override
    public int layout() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(int id) {
        type.set(-1);
        if (id == 1) {
            type.set(BindData.AUTO_REFRESH);
            return;
        }
        if (id == 2) {
            type.set(BindData.AUTO_LOAD_MORE);
            return;
        }
        if (id == 3) {
            type.set(BindData.FINISH_REFRESH);
            return;
        }
        if (id == 4) {
            type.set(BindData.FINISH_LOAD_MORE);
            return;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        Log.e("====","onLoadMore");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                type.set(-1);
                type.set(BindData.FINISH_LOAD_MORE);
            }
        }, 3000);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        Log.e("====","onRefresh");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                type.set(-1);
                type.set(BindData.FINISH_REFRESH);
            }
        }, 3000);
    }
}