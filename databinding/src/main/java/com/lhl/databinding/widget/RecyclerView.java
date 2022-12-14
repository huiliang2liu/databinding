package com.lhl.databinding.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.lhl.databinding.R;

public class RecyclerView extends androidx.recyclerview.widget.RecyclerView {
    private static final String TAG = "RecyclerView";
    private static final int VERTICAL = 0;
    private static final int HORIZONTAL = 1;
    private static final int GRID_VERTICAL = 2;
    private static final int GRID_HORIZONTAL = 3;
    private static final int STAGGERED_VERTICAL = 4;
    private static final int STAGGERED_HORIZONTAL = 5;
    private static final int CARD = 6;
    private static final int LOOPER = 7;

    public RecyclerView(@NonNull Context context) {
        super(context);
        init(null, 0);
    }

    public RecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    @BindingAdapter("scrollToOffset")
    public static void scrollToOffset(RecyclerView recyclerView,int position){
        recyclerView.scrollToOffset(position);
    }

    public void scrollToOffset(int position) {
        LayoutManager manager = getLayoutManager();
        if (manager instanceof StaggeredGridLayoutManager || manager instanceof LinearLayoutManager) {
            if (manager instanceof StaggeredGridLayoutManager)
                ((StaggeredGridLayoutManager) manager).scrollToPositionWithOffset(position, 0);
            else
                ((LinearLayoutManager) manager).scrollToPositionWithOffset(position, 0);
        }
    }

    @BindingAdapter({"position","visible"})
    public static void toCentre(RecyclerView recyclerView,int position,boolean visible){
        recyclerView.toCentre(position,visible);
    }

    public void toCentre(final int position, boolean visible) {
        if (!visible) {
            scrollToOffset(position);
            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT > 15)
                        getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    else
                        getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    toCentre(position);
                }
            });
        } else
            toCentre(position);
    }

    @BindingAdapter("position")
    public static void toCentre(RecyclerView recyclerView,int position){
        recyclerView.toCentre(position);
    }

    public void toCentre(int position) {
        LayoutManager manager = getLayoutManager();
        Rect rect = new Rect();
        this.getGlobalVisibleRect(rect);
        View view = manager.findViewByPosition(position);
        if ((manager instanceof LinearLayoutManager && ((LinearLayoutManager) manager).getOrientation() == LinearLayoutManager.HORIZONTAL)) {
            int reWidth = rect.right - rect.left - view.getWidth();
            int left = view.getLeft();//?????????????????????Item???????????????
            int half = reWidth / 2;//?????????????????????
            int moveDis = left - half;//????????????????????????
            this.smoothScrollBy(moveDis, 0);
        } else {
            int reWidth = rect.right - rect.left - view.getHeight();
            int left = view.getTop();//?????????????????????Item???????????????
            int half = reWidth / 2;//?????????????????????
            int moveDis = left - half;//????????????????????????
            this.smoothScrollBy(0, moveDis);
        }

    }


    private void init(AttributeSet attrs, int defStyle) {
        LayoutManager manager = null;
        int direction = VERTICAL;
        int count = 4;
        Decoration decoration = null;
        int line = -1;
        int lineHigh = -1;
        int lineWidth = -1;
        Drawable drawable = null;
        Drawable drawableVertical = null;
        Drawable drawableHorizontal = null;
        if (attrs != null) {
            TypedArray a = getResources().obtainAttributes(attrs, R.styleable.RecyclerView);
            direction = a.getInt(R.styleable.RecyclerView_direction, VERTICAL);
            count = a.getInt(R.styleable.RecyclerView_count, 4);
            line = (int) a.getDimensionPixelSize(R.styleable.RecyclerView_line, -1);
            lineHigh = (int) a.getDimensionPixelSize(R.styleable.RecyclerView_lineHeight, -1);
            lineWidth = (int) a.getDimensionPixelSize(R.styleable.RecyclerView_lineWidth, -1);
            drawable = a.getDrawable(R.styleable.RecyclerView_lineColor);
            drawableVertical = a.getDrawable(R.styleable.RecyclerView_lineVerticalColor);
            drawableHorizontal = a.getDrawable(R.styleable.RecyclerView_lineHorizontalColor);
            a.recycle();
        }
        Log.d(TAG, String.format("direction:%d", direction));
        switch (direction) {
            case VERTICAL:
                manager = new LinearLayoutManager(getContext());
                decoration = new Decoration(getContext(), Decoration.Orientation.VERTICAL);
                Log.d(TAG, "vertical");
                break;
            case HORIZONTAL:
                Log.d(TAG, "horizontal");
                manager = new LinearLayoutManager(getContext());
                ((LinearLayoutManager) manager).setOrientation(LinearLayoutManager.HORIZONTAL);
                decoration = new Decoration(getContext(), Decoration.Orientation.HORIZONTAL);
                break;
            case GRID_VERTICAL:
                Log.d(TAG, "grid vertical");
                manager = new GridLayoutManager(getContext(), count);
                ((GridLayoutManager) manager).setOrientation(LinearLayoutManager.VERTICAL);
                decoration = new Decoration(getContext(), Decoration.Orientation.GRID);
                break;
            case GRID_HORIZONTAL:
                Log.d(TAG, "grid horizontal");
                manager = new GridLayoutManager(getContext(), count);
                ((GridLayoutManager) manager).setOrientation(LinearLayoutManager.HORIZONTAL);
                decoration = new Decoration(getContext(), Decoration.Orientation.GRID);
                break;
            case STAGGERED_VERTICAL:
                Log.d(TAG, "staggered vertical");
                manager = new StaggeredGridLayoutManager(count, StaggeredGridLayoutManager.VERTICAL);
                decoration = new Decoration(getContext(), Decoration.Orientation.GRID);
                break;
            case STAGGERED_HORIZONTAL:
                Log.d(TAG, "staggered horizontal");
                manager = new StaggeredGridLayoutManager(count, StaggeredGridLayoutManager.HORIZONTAL);
                decoration = new Decoration(getContext(), Decoration.Orientation.GRID);
                break;
            case CARD:
                Log.d(TAG, "card");
                manager = new CardLayoutManager(this);
                break;
            case LOOPER:
                Log.d(TAG, "looper");
                manager = new LooperLayoutManager();
                break;
            default:
                Log.d(TAG, "vertical");
                manager = new LinearLayoutManager(getContext());
                decoration = new Decoration(getContext(), Decoration.Orientation.VERTICAL);
                break;
        }
        setLayoutManager(manager);
        if (decoration == null)
            return;
        if (line <= 0 && lineHigh <= 0 && lineWidth <= 0)
            return;
        if (line > 0)
            decoration.setLine(line);
        if (lineHigh > 0)
            decoration.setHeight(lineHigh);
        if (lineWidth > 0)
            decoration.setWidth(lineWidth);
        if (drawable != null)
            decoration.setDrawable(drawable);
        if (drawableVertical != null)
            decoration.setVerticalDrawable(drawableVertical);
        if (drawableHorizontal != null)
            decoration.setHorizontalDrawable(drawableHorizontal);
        addItemDecoration(decoration);
    }
}
