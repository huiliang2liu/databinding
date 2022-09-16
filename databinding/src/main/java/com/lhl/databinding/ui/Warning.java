package com.lhl.databinding.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;


class Warning {
    private static int statusBarHeight = -1;

    Warning(Context context, Window window) {
        ViewGroup group = (ViewGroup) window.getDecorView();
        TextView warning = new TextView(context);
        warning.setGravity(Gravity.CENTER);
        warning.setBackgroundColor(Color.RED);
        warning.setTextColor(Color.WHITE);
        warning.setText("您正在直接使用布局，可能会存在错误");
        warning.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Resources.getSystem().getDisplayMetrics().density * 30 + 0.5f));
        if ((window.getDecorView().getSystemUiVisibility() & View.INVISIBLE) != View.INVISIBLE) {
            if (statusBarHeight <= 0) {
                Resources resources = Resources.getSystem();
                int resourceId = resources.getIdentifier(
                        "status_bar_height", "dimen", "android");
                statusBarHeight = resources
                        .getDimensionPixelSize(resourceId);
            }
            params.topMargin = statusBarHeight;
        }
        group.addView(warning, params);

    }
}
