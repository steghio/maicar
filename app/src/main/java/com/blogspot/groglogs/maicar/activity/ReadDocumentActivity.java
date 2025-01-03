package com.blogspot.groglogs.maicar.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.groglogs.maicar.model.entity.FuelItem;
import com.blogspot.groglogs.maicar.model.view.FuelViewItem;
import com.blogspot.groglogs.maicar.ui.fuel.FuelFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ReadDocumentActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> readDocumentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register the ActivityResultLauncher using OpenDocument contract
        readDocumentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Uri fileUri = result.getData().getData();
                        if (fileUri != null) {
                            // Process the selected CSV file
                            readFromFile(fileUri);
                        }
                    }
                    else {
                        // Inform the user if no file was selected
                        Toast.makeText(ReadDocumentActivity.this, "No file selected", Toast.LENGTH_SHORT).show();
                    }

                    // Finish the current activity and return to the previous screen
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
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(fileUri)))) {
            String line;

            while ((line = reader.readLine()) != null) {

                FuelViewItem item = FuelViewItem.fromCsv(line);

                //todo if we have id, update, otherwise save
                FuelItem f = new FuelItem(item.getKm(), item.getLiters(), item.getPrice(), item.isFull(), item.getDate());

                FuelFragment.getFuelAdapter().saveEntity(f);
            }

            Toast.makeText(this, "File read successfully", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Failed to read the file", Toast.LENGTH_SHORT).show();
        }
    }
}
