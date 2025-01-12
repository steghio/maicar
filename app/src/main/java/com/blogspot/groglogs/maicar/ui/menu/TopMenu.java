package com.blogspot.groglogs.maicar.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.MenuProvider;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.activity.CreateDocumentActivity;
import com.blogspot.groglogs.maicar.activity.ReadDocumentActivity;
import com.blogspot.groglogs.maicar.model.entity.MaintenanceTypeEnum;
import com.blogspot.groglogs.maicar.ui.adapter.AbstractAdapter;

import java.util.ArrayList;

import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class TopMenu implements MenuProvider {

    private final Context context;
    @SuppressWarnings("rawtypes")
    private final AbstractAdapter adapter;
    private final int menuResId;
    @Setter
    private View contextView;

    @Override
    public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(menuResId, menu);
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
        } else if (id == R.id.action_search) {
            Toast.makeText(context, "MENU SEARCH", Toast.LENGTH_SHORT).show();
            filterMaintenanceItems();
            return true;
        }
        return false;
    }

    private void filterMaintenanceItems(){
        Spinner filterSpinner = contextView.findViewById(R.id.filterSpinner);

        //add "all" filter to reset it in first position, then handle items by position shifted by 1
        MaintenanceTypeEnum[] enumValues = MaintenanceTypeEnum.values();
        String[] dropdownItems = new String[enumValues.length + 1];
        dropdownItems[0] = "All";
        for (int i = 0; i < enumValues.length; i++) {
            dropdownItems[i + 1] = enumValues[i].toString();
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, dropdownItems);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(spinnerAdapter);

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter.filter(position > 0 ? enumValues[position - 1] : null);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        filterSpinner.setVisibility(View.VISIBLE);
    }

    private void importFromFile(){
        Intent intent = new Intent(context, ReadDocumentActivity.class);
        intent.putExtra(CreateDocumentActivity.TYPE, adapter.getActivityType());
        context.startActivity(intent);
    }

    @SuppressWarnings("unchecked")
    private void exportToFile(){
        Intent intent = new Intent(context, CreateDocumentActivity.class);
        intent.putParcelableArrayListExtra(CreateDocumentActivity.DATA, (ArrayList<? extends Parcelable>) adapter.getItems());
        intent.putExtra(CreateDocumentActivity.TYPE, adapter.getActivityType());
        context.startActivity(intent);
    }
}
