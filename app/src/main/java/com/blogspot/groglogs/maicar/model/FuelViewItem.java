package com.blogspot.groglogs.maicar.model;

import com.blogspot.groglogs.maicar.R;

import java.time.LocalDate;

//todo lombok
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
        this.pricePerLiter = this.price / this.liters;
        this.full = full;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public int getKmIconResId(){
        return R.drawable.ic_odometer_24dp;
    }

    public int getKm(){
        return this.km;
    }

    public int getFuelIconResId() {
        return this.full ? R.drawable.ic_droplet_24dp : R.drawable.ic_droplet_half_24dp;
    }

    public double getLiters() {
        return liters;
    }

    public int getPriceIconResId() {
        return R.drawable.ic_euro_price_24dp;
    }

    public double getPrice() {
        return price;
    }

    public double getPricePerLiter() {
        return pricePerLiter;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
