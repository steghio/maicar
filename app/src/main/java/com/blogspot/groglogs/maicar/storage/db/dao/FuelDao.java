package com.blogspot.groglogs.maicar.storage.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.blogspot.groglogs.maicar.model.entity.FuelItem;

import java.util.List;

//transaction annotation so that subsequent operations are flushed and ui refresh get the latest data from db load
@Dao
public interface FuelDao {

    @Query("SELECT * FROM fuel where id = :id")
    FuelItem findById(Long id);

    //could make it return Long to get the ID of the inserted entity back
    @Transaction
    @Insert
    void insert(FuelItem fuelItem);

    @Transaction
    @Query("DELETE FROM fuel WHERE id = :id")
    void delete(long id);

    @Transaction
    @Query("DELETE FROM fuel")
    void deleteAll();

    @Transaction
    @Update
    void update(FuelItem fuelItem);

    //2 fuel in same day would be sorted by insertion time
    @Transaction
    @Query("SELECT * FROM fuel ORDER BY date ASC")
    List<FuelItem> getAllItemsByDateAsc();
}
