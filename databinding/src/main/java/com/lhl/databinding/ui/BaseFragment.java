package com.lhl.databinding.ui;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.lhl.databinding.App;

import java.util.concurrent.atomic.AtomicReference;

public abstract class BaseFragment extends Fragment implements IUi {
    private AtomicReference<ViewModelProvider> fragmentProvider = new AtomicReference<>();
    private AtomicReference<ViewModelProvider> activityProvider = new AtomicReference<>();
    private AtomicReference<ViewModelProvider> appProvider = new AtomicReference<>();
    private DataBinding dataBinding;
    private Warning warning;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (dataBinding == null) {
            dataBinding = new DataBinding(layout(), container, inflater);
            initAppViewModel(getAppProvider());
            initActivityViewModel(getActivityProvider());
            initFragmentViewModel(getFragmentProvider());
            initOthers();
            bindModel();
        }
        update(savedInstanceState);
        return dataBinding.getRoot();
    }

    protected void update(Bundle savedInstanceState) {

    }

    protected void initActivityViewModel(ViewModelProvider provider) {

    }

    protected void initAppViewModel(ViewModelProvider provider) {

    }

    protected void initFragmentViewModel(ViewModelProvider provider) {

    }

    protected void initOthers() {
    }

    protected void bindModel() {
    }

    @Override
    public final BaseFragment bindModel(int id, Object model) {
        dataBinding.bindModel(id, model);
        return this;
    }

    public ViewModelProvider getFragmentProvider() {
        ViewModelProvider provider;
        while (true) {
            provider = fragmentProvider.get();
            if (provider != null)
                break;
            provider = new ViewModelProvider(this);
            if (fragmentProvider.compareAndSet(null, provider))
                break;
        }
        return provider;
    }

    public ViewModelProvider getActivityProvider() {
        ViewModelProvider provider;
        while (true) {
            provider = activityProvider.get();
            if (provider != null)
                break;
            Activity activity = getActivity();
            if (activity instanceof ViewModelStoreOwner) {
                provider = new ViewModelProvider((ViewModelStoreOwner) activity);
                if (activityProvider.compareAndSet(null, provider))
                    break;
            } else
                throw new RuntimeException("activity must implement ViewModelStoreOwner");
        }
        return provider;
    }

    public ViewModelProvider getAppProvider() {
        ViewModelProvider provider;
        while (true) {
            provider = appProvider.get();
            if (provider != null)
                break;
            provider = new ViewModelProvider((App.getInstance(getContext())));
            if (appProvider.compareAndSet(null, provider))
                break;
        }
        return provider;
    }

    @Override
    public final View getRoot() {
        if (getActivity() == null) {
            return null;
        }
        if (warning == null) {
            warning = new Warning(getActivity(), getActivity().getWindow());
        }
        return dataBinding.getRoot();
    }
}
