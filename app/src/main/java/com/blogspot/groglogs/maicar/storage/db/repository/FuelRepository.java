package com.blogspot.groglogs.maicar.storage.db.repository;

import android.app.Application;

import com.blogspot.groglogs.maicar.model.entity.FuelItem;
import com.blogspot.groglogs.maicar.storage.db.AppDatabase;
import com.blogspot.groglogs.maicar.storage.db.dao.FuelDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class FuelRepository {

    private final FuelDao fuelDao;

    public FuelRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        fuelDao = db.fuelDao();
    }

    public FuelItem findById(Long id) throws ExecutionException, InterruptedException {
        return AppDatabase.getDatabaseWriteExecutor().submit(() -> fuelDao.findById(id)).get();
    }

    public List<FuelItem> getAllItemsByDateAsc() throws ExecutionException, InterruptedException {
        return AppDatabase.getDatabaseWriteExecutor().submit(fuelDao::getAllItemsByDateAsc).get();
    }

    public void insert(FuelItem entity) throws ExecutionException, InterruptedException {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> fuelDao.insert(entity));
    }

    public void delete(long id) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> fuelDao.delete(id));
    }

    public void deleteAll() {
        AppDatabase.getDatabaseWriteExecutor().execute(fuelDao::deleteAll);
    }

    public void update(FuelItem entity) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> fuelDao.update(entity));
    }
}
