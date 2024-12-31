package com.blogspot.groglogs.maicar.storage.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.blogspot.groglogs.maicar.model.entity.FuelItem;
import com.blogspot.groglogs.maicar.storage.db.converter.DateConverter;
import com.blogspot.groglogs.maicar.storage.db.dao.FuelDao;

@Database(entities = {FuelItem.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FuelDao fuelDao();
}