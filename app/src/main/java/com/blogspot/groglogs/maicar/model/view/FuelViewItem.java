package com.blogspot.groglogs.maicar.model.view;

import com.blogspot.groglogs.maicar.R;

import java.time.LocalDate;

import lombok.Data;

@Data
public class FuelViewItem {

    private long id;
    private int km;
    private double liters;
    private double price;
    private double pricePerLiter;
    private boolean full;
    private LocalDate date;

    public FuelViewItem(long id, int km, double liters, double price, boolean full, LocalDate date){
        this.id = id;
        this.km = km;
        this.liters = liters;
        this.price = price;
        this.full = full;
        this.date = date;
    }

    public double getPricePerLiter(){
        return this.liters > 0 ? this.price / this.liters : 0;
    }

    public int getKmIconResId(){
        return R.drawable.ic_odometer_24dp;
    }

    public int getFuelIconResId() {
        return this.full ? R.drawable.ic_droplet_24dp : R.drawable.ic_droplet_half_24dp;
    }

    public int getPriceIconResId() {
        return R.drawable.ic_euro_price_24dp;
    }
}
