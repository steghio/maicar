package com.blogspot.groglogs.maicar.storage.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.blogspot.groglogs.maicar.model.entity.FuelItem;
import com.blogspot.groglogs.maicar.model.entity.MaintenanceItem;
import com.blogspot.groglogs.maicar.storage.db.converter.DateConverter;
import com.blogspot.groglogs.maicar.storage.db.dao.FuelDao;
import com.blogspot.groglogs.maicar.storage.db.dao.MaintenanceDao;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.Getter;

@Database(entities = {FuelItem.class, MaintenanceItem.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static volatile AppDatabase INSTANCE;

    @Getter
    private static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(4);

    public abstract FuelDao fuelDao();

    public abstract MaintenanceDao maintenanceDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "maicardb"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }

}