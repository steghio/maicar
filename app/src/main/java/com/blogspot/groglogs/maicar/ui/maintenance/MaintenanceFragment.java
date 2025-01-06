package com.blogspot.groglogs.maicar.ui.maintenance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.blogspot.groglogs.maicar.ui.fragment.AbstractFragment;

public class MaintenanceFragment extends AbstractFragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return buildView(inflater, container, false);
    }
}