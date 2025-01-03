package com.blogspot.groglogs.maicar.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class CreateDocumentActivity extends AppCompatActivity {
    private ActivityResultLauncher<Intent> createDocumentLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Register the ActivityResultLauncher using OpenDocument contract
        createDocumentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<androidx.activity.result.ActivityResult>() {
                    @Override
                    public void onActivityResult(androidx.activity.result.ActivityResult result) {
                        if (result.getResultCode() == RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null) {
                                Uri uri = data.getData();
                                // Once the user selects a file, write content to it
                                if (uri != null) {
                                    writeToFile(uri);
                                }
                            }
                        }
                        // Finish the current activity and return to the previous screen

                        finish();
                    }
                });

        createDocument();
    }

    private void createDocument() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("text/csv");
        intent.putExtra(Intent.EXTRA_TITLE, "maicar_export_" + LocalDateTime.now().toString() + ".csv");

        // Launch the document creation flow
        createDocumentLauncher.launch(intent);
    }

    private void writeToFile(Uri uri) {
        try {
            OutputStream outputStream = getContentResolver().openOutputStream(uri);
            if (outputStream != null) {
                //todo write the actual file
                String text = "asd;lol";
                outputStream.write(text.getBytes());
                outputStream.close();
            }
        } catch (IOException e) {
            //todo handle
        }
    }
}
