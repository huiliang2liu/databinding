package com.lhl.databinding;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;


public class App implements ViewModelStoreOwner, HasDefaultViewModelProviderFactory {
    private Application context;
    private ViewModelStore store = new ViewModelStore();
    private ViewModelProvider.Factory factory = new ViewModelProvider.Factory() {
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (AndroidViewModel.class.isAssignableFrom(modelClass)) {
                try {
                    return modelClass.getDeclaredConstructor(Application.class).newInstance(context);
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
    volatile static App app;

    public static App getInstance(Context context) {
        if (app != null)
            return app;
        synchronized (App.class) {
            if (app != null)
                return app;
            app = new App(context);
            return app;
        }
    }

    private App(Context context) {
        this.context = (Application) context.getApplicationContext();
    }


    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return store;
    }

    @NonNull
    @Override
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        return factory;
    }
}
