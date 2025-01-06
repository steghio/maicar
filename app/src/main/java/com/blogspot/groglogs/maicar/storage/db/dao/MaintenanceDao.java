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

    @Insert
    long insert(MaintenanceItem maintenanceItem);

    @Query("DELETE FROM maintenance WHERE id = :id")
    void delete(long id);

    @Query("DELETE FROM maintenance")
    void deleteAll();

    @Update
    void update(MaintenanceItem maintenanceItem);

    @Query("SELECT * FROM maintenance ORDER BY date DESC")
    List<MaintenanceItem> getAllItemsByDateDesc();
}
