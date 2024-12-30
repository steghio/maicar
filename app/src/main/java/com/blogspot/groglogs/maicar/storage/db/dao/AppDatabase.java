package com.blogspot.groglogs.maicar.storage.db.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.blogspot.groglogs.maicar.model.entity.FuelItem;
import com.blogspot.groglogs.maicar.storage.db.converter.DateConverter;

@Database(entities = {FuelItem.class}, version = 1)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FuelDao fuelDao();
}