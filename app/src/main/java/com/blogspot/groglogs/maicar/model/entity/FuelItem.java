package com.blogspot.groglogs.maicar.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.blogspot.groglogs.maicar.storage.db.converter.DateConverter;

import java.time.LocalDate;

import lombok.Data;

@Data
@Entity(tableName = "fuel")
@TypeConverters(DateConverter.class)
public class FuelItem {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "km")
    private final int km;

    @ColumnInfo(name = "liters")
    private final double liters;

    @ColumnInfo(name = "price")
    private final double price;

    @ColumnInfo(name = "full")
    private final boolean full;

    @ColumnInfo(name = "date")
    private final LocalDate date;
}
