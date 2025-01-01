package com.blogspot.groglogs.maicar.model.entity;

import androidx.annotation.NonNull;

import com.blogspot.groglogs.maicar.R;

public enum MaintenanceTypeEnum {
    TYRE,
    CHECK,
    OIL,
    BATTERY,
    WIPERS,
    OTHER;

    public static int getEnumIcon(MaintenanceTypeEnum maintenanceTypeEnum){
        switch (maintenanceTypeEnum){
            case OIL -> {
                return R.drawable.ic_oil_24dp;
            }
            case TYRE -> {
                return R.drawable.ic_tyre_24dp;
            }
            case CHECK -> {
                return R.drawable.ic_mechanic_24dp;
            }
            case BATTERY -> {
                return R.drawable.ic_battery_24dp;
            }
            case WIPERS -> {
                return R.drawable.ic_wiper_24dp;
            }
            default -> {
                return -1;
            }
        }
    }

    @NonNull
    @Override
    public String toString() {
        switch (this){
            case OIL -> {
                return "Oil";
            }
            case TYRE -> {
                return "Tyres";
            }
            case CHECK -> {
                return "Check";
            }
            case BATTERY -> {
                return "Battery";
            }
            case WIPERS -> {
                return "Wipers";
            }
            default -> {
                return "Other";
            }
        }
    }

    public int getSelection(){
        return this.ordinal();
    }
}
