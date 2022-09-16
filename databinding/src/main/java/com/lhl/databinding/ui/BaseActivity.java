package com.lhl.databinding.ui;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.lhl.databinding.App;

import java.util.concurrent.atomic.AtomicReference;


public abstract class BaseActivity extends Activity implements IUi, ViewModelStoreOwner, HasDefaultViewModelProviderFactory {
    private DataBinding dataBinding;
    private Warning warning;
    private AtomicReference<ViewModelProvider> activityProvider = new AtomicReference<>();
    private AtomicReference<ViewModelProvider> appProvider = new AtomicReference<>();
    private ViewModelStore store = new ViewModelStore();
    private ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (AndroidViewModel.class.isAssignableFrom(modelClass)) {
                try {
                    return modelClass.getDeclaredConstructor(Application.class).newInstance(getApplication());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            try {
                return modelClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };

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

    public final BaseActivity bindModel(int id, Object model) {
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

    @NonNull
    @Override
    public final ViewModelStore getViewModelStore() {
        return store;
    }

    @NonNull
    @Override
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        return factory;
    }
}
