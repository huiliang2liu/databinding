package com.lhl.databing;

import android.os.Bundle;

import androidx.databinding.ObservableField;

import com.lhl.databinding.ui.BaseFragment;
import com.lhl.databing.R;
import com.lhl.databing.BR;

public class MyFragment extends BaseFragment {
    private static int index = -1;
    public ObservableField<String> text = new ObservableField<>();

    @Override
    public int layout() {
        return R.layout.fragment_my;
    }

    @Override
    protected void update(Bundle savedInstanceState) {
        super.update(savedInstanceState);
        index++;
        text.set(index + "");
    }

    @Override
    protected void bindModel() {
        super.bindModel();
        bindModel(BR.fragment, this);
    }
}
