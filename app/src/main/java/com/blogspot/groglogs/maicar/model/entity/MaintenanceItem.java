package com.blogspot.groglogs.maicar.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.blogspot.groglogs.maicar.storage.db.converter.DateConverter;

import java.time.LocalDate;

import lombok.Data;

@Data
@Entity(tableName = "maintenance")
@TypeConverters(DateConverter.class)
public class MaintenanceItem {

    @PrimaryKey(autoGenerate = true)
    private long id;

    @ColumnInfo(name = "km")
    private final int km;

    @ColumnInfo(name = "price")
    private final double price;

    @ColumnInfo(name = "date")
    private final LocalDate date;

    @ColumnInfo(name = "maintenanceType")
    private final MaintenanceTypeEnum maintenanceType;

    @ColumnInfo(name = "notes")
    private final String notes;
}
