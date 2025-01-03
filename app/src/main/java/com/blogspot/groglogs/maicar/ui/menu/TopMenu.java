package com.blogspot.groglogs.maicar.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.activity.CreateDocumentActivity;
import com.blogspot.groglogs.maicar.activity.ReadDocumentActivity;
import com.blogspot.groglogs.maicar.ui.fuel.FuelAdapter;

import java.util.ArrayList;

import lombok.AllArgsConstructor;

//todo action search/filter
@AllArgsConstructor
public class TopMenu implements MenuProvider {

    private Context context;
    private FuelAdapter fuelAdapter;

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
        Intent intent = new Intent(context, ReadDocumentActivity.class);
        context.startActivity(intent);
    }

    private void exportToFile(){
        Intent intent = new Intent(context, CreateDocumentActivity.class);
        intent.putParcelableArrayListExtra(CreateDocumentActivity.FUEL_DATA, (ArrayList<? extends Parcelable>) fuelAdapter.getItems());
        context.startActivity(intent);
    }
}
