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
import com.lhl.databinding.ui.BaseFragmentActivity;
import com.lhl.databinding.ui.ViewPagerAdapter;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

public class MainActivity extends BaseFragmentActivity implements BindData.OnClickListener, OnLoadMoreListener, OnRefreshListener {
    public ObservableInt type = new ObservableInt(-1);
    private Handler handler = new Handler(Looper.getMainLooper());
    public ViewPagerAdapter adapter;

    @Override
    protected void bindModel() {
        super.bindModel();
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
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
            adapter.addItem(new MyFragment());
            return;
        }
        if (id == 2) {
            adapter.addItem(new MyFragment());
            adapter.addItem(new MyFragment());
            return;
        }
        if (id == 3) {
            adapter.remove(adapter.getItem(0));
//            type.set(BindData.FINISH_REFRESH);
            return;
        }
        if (id == 4) {
            adapter.clean();
            return;
        }
        if(id==5){
            adapter.setSelection(1);
            return;
        }
        adapter.notifyDataSetChanged(0);
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