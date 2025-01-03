package com.blogspot.groglogs.maicar.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.activity.CreateDocumentActivity;

import lombok.AllArgsConstructor;

//todo action search/filter
@AllArgsConstructor
public class TopMenu implements MenuProvider {

    private Context context;

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.top_menu, menu);
    }

    @Override
    public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();

        if (id == R.id.action_import) {
            Toast.makeText(context, "MENU IMPORT", Toast.LENGTH_SHORT).show();
            importFromFile();
            return true;
        } else if (id == R.id.action_export) {
            Toast.makeText(context, "MENU EXPORT", Toast.LENGTH_SHORT).show();
            exportToFile();
            return true;
        }
        return false;
    }

    private void importFromFile(){
        //todo activity to import instead
        Intent intent = new Intent(context, CreateDocumentActivity.class);
        context.startActivity(intent);
    }

    private void exportToFile(){
        Intent intent = new Intent(context, CreateDocumentActivity.class);
        context.startActivity(intent);
    }
}
