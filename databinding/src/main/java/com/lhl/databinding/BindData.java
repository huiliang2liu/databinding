package com.lhl.databinding;

import android.text.method.MovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.lhl.databinding.widget.RecyclerView;
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
    public static final int CLOSE_LOAD_MORE = CLOSE_HEADER_OR_FOOTER << 1;
    public static final int OPEN_LOAD_MORE = CLOSE_LOAD_MORE << 1;
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
        if ((type & CLOSE_LOAD_MORE) == CLOSE_LOAD_MORE)
            layout.setEnableLoadMore(false);
        if ((type & OPEN_LOAD_MORE) == OPEN_LOAD_MORE)
            layout.setEnableLoadMore(true);
    }

    @BindingAdapter("setEnableAutoLoadMore")
    public static void setEnableAutoLoadMore(SmartRefreshLayout layout,boolean autoLadMore){
        layout.setEnableAutoLoadMore(autoLadMore);
    }

    @BindingAdapter("onLoadMoreListener")
    public static void onLoadMoreListener(SmartRefreshLayout layout, OnLoadMoreListener listener) {
        layout.setOnLoadMoreListener(listener);
    }

    @BindingAdapter("onRefreshListener")
    public static void onRefreshListener(SmartRefreshLayout layout, OnRefreshListener listener) {
        layout.setOnRefreshListener(listener);
    }

    @BindingAdapter("onRefreshLoadMoreListener")
    public static void onRefreshLoadMoreListener(SmartRefreshLayout layout, OnRefreshLoadMoreListener listener) {
        layout.setOnRefreshLoadMoreListener(listener);
    }

    @BindingAdapter("layout_height")
    public static void setHeight(View view, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null)
            return;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("layout_width")
    public static void setWidth(View view, int width) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null)
            return;
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter({"layout_width", "layout_height"})
    public static void setWidthAndHeight(View view, int width, int height) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams == null)
            return;
        layoutParams.height = height;
        layoutParams.width = width;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("layout_marginBottom")
    public static void setBottomMargin(View view, int bottomMargin) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null || !(params instanceof ViewGroup.MarginLayoutParams))
            return;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) params;
        layoutParams.bottomMargin = bottomMargin;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("layout_marginTop")
    public static void setTopMargin(View view, int topMargin) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null || !(params instanceof ViewGroup.MarginLayoutParams))
            return;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) params;
        layoutParams.topMargin = topMargin;
        view.setLayoutParams(layoutParams);
    }


    @BindingAdapter("layout_marginLeft")
    public static void setLeftMargin(View view, int leftMargin) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null || !(params instanceof ViewGroup.MarginLayoutParams))
            return;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) params;
        layoutParams.leftMargin = leftMargin;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("layout_marginRight")
    public static void setRightMargin(View view, int rightMargin) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null || !(params instanceof ViewGroup.MarginLayoutParams))
            return;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) params;
        layoutParams.rightMargin = rightMargin;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("layout_margin")
    public static void setMargin(View view, int margin) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null || !(params instanceof ViewGroup.MarginLayoutParams))
            return;
        ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) params;
        layoutParams.bottomMargin = margin;
        layoutParams.topMargin = margin;
        layoutParams.rightMargin = margin;
        layoutParams.leftMargin = margin;
        view.setLayoutParams(layoutParams);
    }

    @BindingAdapter("paddingBottom")
    public static void setBottomPadding(View view, int bottomPadding) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), bottomPadding);
    }

    @BindingAdapter("paddingTop")
    public static void setTopPadding(View view, int topPadding) {
        view.setPadding(view.getPaddingLeft(), topPadding, view.getPaddingRight(), view.getPaddingBottom());
    }

    @BindingAdapter("paddingRight")
    public static void setRightPadding(View view, int rightPadding) {
        view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), rightPadding, view.getPaddingBottom());
    }

    @BindingAdapter("paddingLeft")
    public static void setLeftPadding(View view, int leftPadding) {
        view.setPadding(leftPadding, view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
    }

    @BindingAdapter("limit")
    public static void bindViewOffscreenPageLimit(ViewPager viewPager, int limit) {
        viewPager.setOffscreenPageLimit(limit);
    }

    @BindingAdapter("limit")
    public static void bindViewOffscreenPageLimit(ViewPager2 viewPager, int limit) {
        viewPager.setOffscreenPageLimit(limit);
    }

    @BindingAdapter("src")
    public static void bindSrc(ImageView iv, int res) {
        if (res <= 0)
            return;
        iv.setImageResource(res);
    }

    @BindingAdapter("textColor")
    public static void textColor(TextView tv, int color) {
        tv.setTextColor(color);
    }

    @BindingAdapter("append")
    public static void append(TextView tv, CharSequence cs) {
        if (cs == null)
            return;
        tv.append(cs);
    }

    @BindingAdapter("addLine")
    public static void addLine(TextView textView, int line) {
        textView.getPaint().setFlags(line);
    }


    @BindingAdapter("addOnPageChangeListener")
    public static void addOnPageChangeListener(ViewPager pager, ViewPager.OnPageChangeListener listener) {
        pager.addOnPageChangeListener(listener);
    }

    @BindingAdapter("scrollByTo0")
    public static void scrollByTo0(View view, boolean dy) {
        view.scrollBy(0, -view.getScrollY());
    }

    @BindingAdapter("scrollBxTo0")
    public static void scrollBxTo0(View view, boolean dy) {
        view.scrollBy(-view.getScrollX(), 0);
    }

    @BindingAdapter("setOnFocusChangeListener")
    public static void setOnFocusChangeListener(EditText editText, View.OnFocusChangeListener listener) {
        editText.setOnFocusChangeListener(listener);
    }


    @BindingAdapter("setCurrentItem")
    public static void setCurrentItem(ViewPager view, int page) {
        view.setCurrentItem(page, false);
    }

    @BindingAdapter("background")
    public static void setBackgroundColor(View view, int src) {
        try {
            view.setBackgroundResource(src);
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            view.setBackgroundColor(src);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @BindingAdapter("toCentre")
    public static void toCentre(RecyclerView recyclerView, int position) {
        if (position < 0)
            return;
        recyclerView.toCentre(position, false);
    }

    @BindingAdapter("setBackgroundResource")
    public static void setBackgroundResource(View view, int res) {
        view.setBackgroundResource(res);
    }

    @BindingAdapter("setMovementMethod")
    public static void setMovementMethod(TextView tv, MovementMethod movementMethod) {
        tv.setMovementMethod(movementMethod);
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
