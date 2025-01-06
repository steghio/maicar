package com.blogspot.groglogs.maicar.ui.fuel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blogspot.groglogs.maicar.ui.fragment.AbstractFragment;


public class FuelFragment extends AbstractFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return buildView(inflater, container, true);
    }
}