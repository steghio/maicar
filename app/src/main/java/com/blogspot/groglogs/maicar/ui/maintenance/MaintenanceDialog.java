package com.blogspot.groglogs.maicar.ui.maintenance;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.model.entity.MaintenanceItem;
import com.blogspot.groglogs.maicar.model.entity.MaintenanceTypeEnum;
import com.blogspot.groglogs.maicar.model.view.MaintenanceViewItem;
import com.blogspot.groglogs.maicar.ui.dialog.AbstractDialog;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class MaintenanceDialog extends AbstractDialog {
    private final Spinner editTextType;
    private final EditText editTextNotes;

    public MaintenanceDialog(Context context, MaintenanceAdapter maintenanceAdapter){
        super(context, maintenanceAdapter, R.layout.dialog_maintenance);

        this.editTextType = dialogView.findViewById(R.id.editTextType);
        this.editTextNotes = dialogView.findViewById(R.id.editTextNotes);
    }

    public void fillDialog(Context context, MaintenanceViewItem f, int position){
        this.id = f.getId();
        this.position = position;

        this.editTextKm.setText(String.valueOf(f.getKm()));
        this.editTextPrice.setText(String.valueOf(f.getPrice()));
        this.editTextType.setSelection(f.getMaintenanceType().getSelection());
        this.editTextNotes.setText(String.valueOf(f.getNotes()));
        this.editTextPrice.setText(String.valueOf(f.getPrice()));

        this.addDatePicker(context, f.getDate());
    }

    public String getNotes(){
        return this.editTextNotes.getText().toString();
    }

    public MaintenanceTypeEnum getMaintenanceType(){
        return MaintenanceTypeEnum.getValueFromLabel(this.editTextType.getSelectedItem().toString());
    }

    public void addTypeDropdown(Context context){
        MaintenanceTypeEnum[] enumValues = MaintenanceTypeEnum.values();
        String[] dropdownItems = new String[enumValues.length];
        for (int i = 0; i < enumValues.length; i++) {
            dropdownItems[i] = enumValues[i].toString();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, dropdownItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = this.dialogView.findViewById(R.id.editTextType);
        spinner.setAdapter(adapter);
    }

    public View.OnClickListener getSubmitButtonWithValidation(AlertDialog d, boolean isUpdate){
        return v -> {
            int km = -1;
            double price = 0;
            LocalDate date = null;
            MaintenanceTypeEnum maintenanceType = this.getMaintenanceType();
            String notes = "";

            boolean isError = false;

            try {
                km = this.getKm();
            } catch (NumberFormatException e) {
                this.editTextKm.setError("Please enter a positive value for Km");
                isError = true;
            }

            try {
                price = this.getPrice();
            } catch (NumberFormatException e) {
                this.editTextPrice.setError("Please enter a valid price");
                isError = true;
            }

            try{
                date = this.getDate();
            } catch (Exception e){
                this.editTextDate.setError("Invalid Date");
                isError = true;
            }

            try{
                notes = this.getNotes();
            } catch (Exception e){
                this.editTextNotes.setError("Invalid Notes");
                isError = true;
            }

            if(!isError){
                if(isUpdate){
                    if(this.id == -1){
                        Toast.makeText(d.getContext(), "Error update row with no ID!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        MaintenanceItem i = new MaintenanceItem(km, price, date, maintenanceType, notes);
                        i.setId(this.id);
                        adapter.updateEntity(i, this.position);

                        Toast.makeText(d.getContext(), "Maintenance updated", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    MaintenanceItem i = new MaintenanceItem(km, price, date, maintenanceType, notes);
                    adapter.saveEntityAndRefreshView(i);

                    Toast.makeText(d.getContext(), "Maintenance added", Toast.LENGTH_SHORT).show();
                }

                d.dismiss();
            }
        };
    }
}
