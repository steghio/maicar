package com.blogspot.groglogs.maicar.storage.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.blogspot.groglogs.maicar.model.entity.FuelItem;

import java.util.List;

//todo use repository everywhere to perform db operations
@Dao
public interface FuelDao {

    @Insert
    void insert(FuelItem fuelItem);

    @Transaction
    default void bulkInsert(List<FuelItem> fuelItems){
        insertAll(fuelItems);
    }

    @Insert
    void insertAll(List<FuelItem> fuelItems);

    @Query("DELETE FROM fuel WHERE id = :id")
    void delete(long id);

    @Update
    void update(FuelItem fuelItem);

    //todo what if 2 fuel in same day?
    //todo order DESC but mpg calc uses asc...
    @Query("SELECT * FROM fuel ORDER BY date ASC")
    List<FuelItem> getAllItemsSortedDesc();
}
