package com.lhl.databinding.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import com.lhl.databinding.App;

import java.util.concurrent.atomic.AtomicReference;

public abstract class BaseFragmentActivity extends FragmentActivity implements IUi{
    private DataBinding dataBinding;
    private Warning warning;
    private AtomicReference<ViewModelProvider> activityProvider = new AtomicReference<>();
    private AtomicReference<ViewModelProvider> appProvider = new AtomicReference<>();

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = new DataBinding(layout(), this);
        setContentView(dataBinding.getRoot());
        initAppViewModel(getAppProvider());
        initActivityViewModel(getActivityProvider());
        initOthers();
        bindModel();
    }

    protected void initActivityViewModel(ViewModelProvider provider) {

    }

    protected void initAppViewModel(ViewModelProvider provider) {

    }

    protected void initOthers() {
    }

    protected void bindModel() {
    }

    public ViewModelProvider getActivityProvider() {
        ViewModelProvider provider;
        while (true) {
            provider = activityProvider.get();
            if (provider != null)
                break;
            provider = new ViewModelProvider(this);
            if (activityProvider.compareAndSet(null, provider))
                break;
        }
        return provider;
    }

    public ViewModelProvider getAppProvider() {
        ViewModelProvider provider;
        while (true) {
            provider = appProvider.get();
            if (provider != null)
                break;
            provider = new ViewModelProvider(App.getInstance(getApplication()));
            if (appProvider.compareAndSet(null, provider))
                break;
        }
        return provider;
    }

    public final BaseFragmentActivity bindModel(int id, Object model) {
        dataBinding.bindModel(id,model);
        return this;
    }

    @Override
    public View getRoot() {
        if (warning == null) {
            warning = new Warning(this, getWindow());
        }
        return dataBinding.getRoot();
    }

}
