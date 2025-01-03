package com.blogspot.groglogs.maicar.ui.adapter;

import android.os.Parcelable;

import java.util.List;

public interface AbstractAdapter {

    List<? extends Parcelable> getItems();

    String getActivityType();

}
