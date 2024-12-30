package com.blogspot.groglogs.maicar.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.blogspot.groglogs.maicar.storage.db.converter.DateConverter;

import java.time.LocalDate;

//todo lombok

@Entity(tableName = "fuel")
@TypeConverters(DateConverter.class)
public class FuelItem {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "km")
    private int km;

    @ColumnInfo(name = "liters")
    private double liters;

    @ColumnInfo(name = "price")
    private double price;

    @ColumnInfo(name = "full")
    private boolean full;

    @ColumnInfo(name = "date")
    private LocalDate date;

    public FuelItem(int km, double liters, double price, boolean full, LocalDate date){
        this.km = km;
        this.liters = liters;
        this.price = price;
        this.full = full;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getKm(){
        return this.km;
    }

    public void setKm(int km) {
        this.km = km;
    }

    public double getLiters() {
        return liters;
    }

    public void setLiters(double liters) {
        this.liters = liters;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
