package com.lhl.databinding;

import android.util.Log;
import android.view.View;

import androidx.databinding.BindingAdapter;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener;

import java.lang.ref.WeakReference;

public class BindData {
    //https://github.com/scwang90/SmartRefreshLayout/blob/master/art/md_property.md
    public static final int AUTO_REFRESH = 1;
    public static final int AUTO_LOAD_MORE = AUTO_REFRESH << 1;
    public static final int FINISH_REFRESH = AUTO_LOAD_MORE << 1;
    public static final int FINISH_LOAD_MORE = FINISH_REFRESH << 1;
    public static final int AUTO_REFRESH_ANIMATION_ONLY = FINISH_LOAD_MORE << 1;
    public static final int AUTO_LOAD_MORE_ANIMATION_ONLY = AUTO_REFRESH_ANIMATION_ONLY << 1;
    public static final int FINISH_LOAD_MORE_WITH_NO_MORE = AUTO_LOAD_MORE_ANIMATION_ONLY << 1;
    public static final int CLOSE_HEADER_OR_FOOTER = FINISH_LOAD_MORE_WITH_NO_MORE << 1;
    public static final long MIN_CLICK_TIME = 500;

    @BindingAdapter({"click", "id"})
    public static void bindClick(View view, OnClickListener listener, int id) {
        view.setOnClickListener(new MyClickListener(listener, id));
    }

    @BindingAdapter({"type"})
    public static void autoFinish(SmartRefreshLayout layout, int type) {
        Log.e("autoFinish", "type:" + type);
        if (type <= 0)
            return;
        if ((type & AUTO_REFRESH) == AUTO_REFRESH)
            layout.autoRefresh();
        if ((type & AUTO_LOAD_MORE) == AUTO_LOAD_MORE)
            layout.autoLoadMore();
        if ((type & AUTO_REFRESH_ANIMATION_ONLY) == AUTO_REFRESH_ANIMATION_ONLY)
            layout.autoRefreshAnimationOnly();//自动刷新，只显示动画不执行刷新
        if ((type & AUTO_LOAD_MORE_ANIMATION_ONLY) == AUTO_LOAD_MORE_ANIMATION_ONLY)
            layout.autoLoadMoreAnimationOnly();//自动加载，只显示动画不执行加载
        if ((type & FINISH_REFRESH) == FINISH_REFRESH)
            layout.finishRefresh();//结束刷新
        if ((type & FINISH_LOAD_MORE) == FINISH_LOAD_MORE)
            layout.finishLoadMore();//结束加载
        if ((type & FINISH_LOAD_MORE_WITH_NO_MORE) == FINISH_LOAD_MORE_WITH_NO_MORE)
            layout.finishLoadMoreWithNoMoreData();//完成加载并标记没有更多数据 1.0.4
        if ((type & CLOSE_HEADER_OR_FOOTER) == CLOSE_HEADER_OR_FOOTER)
            layout.closeHeaderOrFooter();//关闭正在打开状态的 Header 或者 Footer（1.1.0）
    }
    @BindingAdapter("onLoadMoreListener")
    public static void onLoadMoreListener(SmartRefreshLayout layout , OnLoadMoreListener listener){
        layout.setOnLoadMoreListener(listener);
    }

    @BindingAdapter("onRefreshListener")
    public static void onRefreshListener(SmartRefreshLayout layout , OnRefreshListener listener){
        layout.setOnRefreshListener(listener);
    }

    @BindingAdapter("onRefreshLoadMoreListener")
    public static void onRefreshLoadMoreListener(SmartRefreshLayout layout , OnRefreshLoadMoreListener listener){
        layout.setOnRefreshLoadMoreListener(listener);
    }


    public interface OnClickListener {
        void onClick(int id);
    }

    private static class MyClickListener implements View.OnClickListener {
        private WeakReference<OnClickListener> listenerWeakReference;
        private int id;
        private long lastClickTime = 0;

        MyClickListener(OnClickListener listener, int id) {
            listenerWeakReference = new WeakReference<>(listener);
            this.id = id;
        }

        @Override
        public void onClick(View v) {
            OnClickListener clickListener = listenerWeakReference.get();
            if (clickListener == null)
                return;
            long time = System.currentTimeMillis();

            if (time - lastClickTime < MIN_CLICK_TIME)
                return;
            lastClickTime = time;
            clickListener.onClick(id);
        }
    }
}
