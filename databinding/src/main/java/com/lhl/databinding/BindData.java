package com.lhl.databinding;

import android.view.View;

import androidx.databinding.BindingAdapter;

import java.lang.ref.WeakReference;

public class BindData {
    public static final long MIN_CLICK_TIME = 500;
    @BindingAdapter({"click", "id"})
    public static void bindClick(View view, OnClickListener listener, int id) {
        view.setOnClickListener(new MyClickListener(listener, id));
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
