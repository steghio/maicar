package com.blogspot.groglogs.maicar.storage.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.blogspot.groglogs.maicar.model.entity.MaintenanceItem;

import java.util.List;

@Dao
public interface MaintenanceDao {

    @Insert
    long insert(MaintenanceItem maintenanceItem);

    @Transaction
    default void bulkInsert(List<MaintenanceItem> maintenanceItems){
        insertAll(maintenanceItems);
    }

    @Insert
    void insertAll(List<MaintenanceItem> maintenanceItems);

    @Query("DELETE FROM maintenance WHERE id = :id")
    void delete(long id);

    @Update
    void update(MaintenanceItem maintenanceItem);

    @Query("SELECT * FROM maintenance ORDER BY date DESC")
    List<MaintenanceItem> getAllItemsByDateDesc();
}
