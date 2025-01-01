package com.blogspot.groglogs.maicar.storage.db.repository;

import android.app.Application;

import com.blogspot.groglogs.maicar.model.entity.MaintenanceItem;
import com.blogspot.groglogs.maicar.storage.db.AppDatabase;
import com.blogspot.groglogs.maicar.storage.db.dao.MaintenanceDao;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MaintenanceRepository {

    private final MaintenanceDao maintenanceDao;

    public MaintenanceRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        maintenanceDao = db.maintenanceDao();
    }

    public List<MaintenanceItem> getAllItemsByDateDesc() throws ExecutionException, InterruptedException {
        return AppDatabase.getDatabaseWriteExecutor().submit(() -> maintenanceDao.getAllItemsByDateDesc()).get();
    }

    public long insert(MaintenanceItem entity) throws ExecutionException, InterruptedException {
        return AppDatabase.getDatabaseWriteExecutor().submit(() -> maintenanceDao.insert(entity)).get();
    }

    public void insertAll(List<MaintenanceItem> entities){
        AppDatabase.getDatabaseWriteExecutor().execute(() -> maintenanceDao.bulkInsert(entities));
    }

    public void delete(long id) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> maintenanceDao.delete(id));
    }

    public void update(MaintenanceItem entity) {
        AppDatabase.getDatabaseWriteExecutor().execute(() -> maintenanceDao.update(entity));
    }
}
