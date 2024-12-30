package com.blogspot.groglogs.maicar.storage.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.blogspot.groglogs.maicar.model.entity.FuelItem;

import java.util.List;

@Dao
public interface FuelDao {

    @Insert
    void insert(FuelItem fuelItem);

    //todo what if 2 fuel in same day?
    //todo order DESC but mpg calc uses asc...
    @Query("SELECT * FROM fuel ORDER BY date ASC")
    List<FuelItem> getAllItems();
}
