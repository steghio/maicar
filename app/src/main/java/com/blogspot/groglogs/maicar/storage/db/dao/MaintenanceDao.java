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

    @Query("SELECT * FROM maintenance WHERE id = :id")
    MaintenanceItem findById(Long id);

    @Transaction
    @Insert
    long insert(MaintenanceItem maintenanceItem);

    @Transaction
    @Query("DELETE FROM maintenance WHERE id = :id")
    void delete(long id);

    @Transaction
    @Query("DELETE FROM maintenance")
    void deleteAll();

    @Transaction
    @Update
    void update(MaintenanceItem maintenanceItem);

    @Transaction
    @Query("SELECT * FROM maintenance ORDER BY date DESC")
    List<MaintenanceItem> getAllItemsByDateDesc();
}
