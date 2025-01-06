package com.blogspot.groglogs.maicar.storage.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.blogspot.groglogs.maicar.model.entity.FuelItem;

import java.util.List;

@Dao
public interface FuelDao {

    @Insert
    long insert(FuelItem fuelItem);

    @Query("DELETE FROM fuel WHERE id = :id")
    void delete(long id);

    @Query("DELETE FROM fuel")
    void deleteAll();

    @Update
    void update(FuelItem fuelItem);

    //todo what if 2 fuel in same day?
    @Query("SELECT * FROM fuel ORDER BY date ASC")
    List<FuelItem> getAllItemsByDateAsc();
}
