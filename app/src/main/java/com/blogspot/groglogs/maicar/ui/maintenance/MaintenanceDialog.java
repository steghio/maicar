package com.blogspot.groglogs.maicar.ui.maintenance;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.model.entity.MaintenanceItem;
import com.blogspot.groglogs.maicar.model.entity.MaintenanceTypeEnum;
import com.blogspot.groglogs.maicar.model.view.MaintenanceViewItem;
import com.blogspot.groglogs.maicar.util.DateUtils;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class MaintenanceDialog {
    private View dialogView;
    private long id;
    private int position;
    private EditText editTextKm;
    private EditText editTextPrice;
    private Spinner editTextType;
    private EditText editTextDate;
    private EditText editTextNotes;
    private MaintenanceAdapter maintenanceAdapter;

    public MaintenanceDialog(Context context, MaintenanceAdapter maintenanceAdapter){
        this.id = -1;
        this.position = -1;

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //todo do not pass null for root
        this.dialogView = layoutInflater.inflate(R.layout.dialog_maintenance, null);

        this.editTextKm = dialogView.findViewById(R.id.editTextKm);
        this.editTextPrice = dialogView.findViewById(R.id.editTextPrice);
        this.editTextType = dialogView.findViewById(R.id.editTextType);
        this.editTextNotes = dialogView.findViewById(R.id.editTextNotes);
        this.editTextDate = dialogView.findViewById(R.id.editTextDate);

        this.maintenanceAdapter = maintenanceAdapter;
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

    public int getKm(){
        return Integer.parseInt(this.editTextKm.getText().toString());
    }

    public double getPrice(){
        return Double.parseDouble(this.editTextPrice.getText().toString());
    }

    public LocalDate getDate(){
        String[] dateParts = this.editTextDate.getText().toString().split("-");
        return LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
    }

    public String getNotes(){
        return this.editTextNotes.getText().toString();
    }

    //todo we display a label but work with values so adapt everything to work properly
    public MaintenanceTypeEnum getMaintenanceType(){
        MaintenanceTypeEnum maintenanceTypeEnum = MaintenanceTypeEnum.OTHER;

        try{
            maintenanceTypeEnum = MaintenanceTypeEnum.valueOf(this.editTextType.getSelectedItem().toString());
        } catch (IllegalArgumentException e) {
            //ignore
        }

        return maintenanceTypeEnum;
    }

    /**
     * month in date picker is 0-based so we need to handle +/- 1 from LocalDate
     * @param context
     * @param date default date to display
     */
    public void addDatePicker(Context context, LocalDate date){
        this.editTextDate.setText(date.toString());

        this.editTextDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    context,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        this.editTextDate.setText(DateUtils.stringFrom(selectedYear, selectedMonth + 1, selectedDay));
                    },
                    date.getYear(),
                    date.getMonthValue() - 1,
                    date.getDayOfMonth()
            );
            datePickerDialog.show();
        });
    }

    public void addTypeDropdown(Context context){
        // Get enum values and convert them to a String array
        MaintenanceTypeEnum[] enumValues = MaintenanceTypeEnum.values();
        String[] dropdownItems = new String[enumValues.length];
        for (int i = 0; i < enumValues.length; i++) {
            dropdownItems[i] = enumValues[i].toString();
        }

        // Set up the Spinner with an ArrayAdapter
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
                        maintenanceAdapter.updateEntity(i, this.position);

                        Toast.makeText(d.getContext(), "Maintenance updated", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    MaintenanceItem i = new MaintenanceItem(km, price, date, maintenanceType, notes);
                    maintenanceAdapter.saveEntity(i);

                    Toast.makeText(d.getContext(), "Maintenance added", Toast.LENGTH_SHORT).show();
                }

                d.dismiss();
            }
        };
    }
}
