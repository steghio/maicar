package com.blogspot.groglogs.maicar.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.groglogs.maicar.model.entity.FuelItem;
import com.blogspot.groglogs.maicar.model.entity.MaintenanceItem;
import com.blogspot.groglogs.maicar.model.view.FuelViewItem;
import com.blogspot.groglogs.maicar.model.view.MaintenanceViewItem;
import com.blogspot.groglogs.maicar.ui.fuel.FuelAdapter;
import com.blogspot.groglogs.maicar.ui.fuel.FuelFragment;
import com.blogspot.groglogs.maicar.ui.maintenance.MaintenanceAdapter;
import com.blogspot.groglogs.maicar.ui.maintenance.MaintenanceFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadDocumentActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> readDocumentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        readDocumentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Uri fileUri = result.getData().getData();

                        if (fileUri != null) {
                            readFromFile(fileUri);
                        }
                    }
                    else {
                        Toast.makeText(ReadDocumentActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
                    }

                    //finish the current activity and return to the previous screen
                    finish();
                }
        );

        readDocument();
    }

    private void readDocument() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("text/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        readDocumentLauncher.launch(intent);
    }

    private void readFromFile(Uri fileUri) {

        String activityType = getIntent().getStringExtra(CreateDocumentActivity.TYPE);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(fileUri)))) {

            if(FuelAdapter.ACTIVITY_TYPE.equals(activityType)){
                readFuelItems(reader);
            }
            else if(MaintenanceAdapter.ACTIVITY_TYPE.equals(activityType)){
                readMaintenanceItems(reader);
            }
            else{
                Toast.makeText(this, "Neither Fuel nor Maintenance activity source!", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, "Failed to read the file", Toast.LENGTH_SHORT).show();
        }
    }

    private void readFuelItems(BufferedReader reader) throws IOException {

        FuelFragment.getAdapter().deleteAllItems();

        String line;

        while ((line = reader.readLine()) != null) {

            FuelViewItem item = FuelViewItem.fromCsv(line);

            FuelItem f = new FuelItem(item.getKm(), item.getLiters(), item.getPrice(), item.isFull(), item.getDate());

            FuelFragment.getAdapter().saveEntity(f);
        }

        Toast.makeText(this, "Fuel items imported successfully", Toast.LENGTH_SHORT).show();

        FuelFragment.getAdapter().loadAllItems();
    }

    private void readMaintenanceItems(BufferedReader reader) throws IOException {

        MaintenanceFragment.getAdapter().deleteAllItems();

        String line;

        while ((line = reader.readLine()) != null) {

            MaintenanceViewItem item = MaintenanceViewItem.fromCsv(line);

            MaintenanceItem f = new MaintenanceItem(item.getKm(), item.getPrice(), item.getDate(), item.getMaintenanceType(), item.getNotes());

            MaintenanceFragment.getAdapter().saveEntity(f);
        }

        Toast.makeText(this, "Maintenance items imported successfully", Toast.LENGTH_SHORT).show();

        MaintenanceFragment.getAdapter().loadAllItems();
    }
}
