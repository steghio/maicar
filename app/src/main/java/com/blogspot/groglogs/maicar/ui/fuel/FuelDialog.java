package com.blogspot.groglogs.maicar.ui.fuel;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.model.view.FuelViewItem;
import com.blogspot.groglogs.maicar.util.DateUtils;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class FuelDialog {

    private View dialogView;
    private EditText editTextKm;
    private EditText editTextLiters;
    private EditText editTextPrice;
    private EditText editTextPriceLiter;
    private SwitchMaterial editToggleFull;
    private EditText editTextDate;

    public FuelDialog(Context context){
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        // Create a custom view for the dialog
        //todo do not pass null for root
        this.dialogView = layoutInflater.inflate(R.layout.dialog_fuel, null);

        // Get references to the EditTexts in the custom layout
        this.editTextKm = dialogView.findViewById(R.id.editTextKm);
        this.editTextLiters = dialogView.findViewById(R.id.editTextLiters);
        this.editTextPrice = dialogView.findViewById(R.id.editTextPrice);
        this.editTextPriceLiter = dialogView.findViewById(R.id.editTextPriceLiter);
        this.editToggleFull = dialogView.findViewById(R.id.editToggleFull);
        this.editTextDate = dialogView.findViewById(R.id.editTextDate);
    }

    public int getKm(){
        return Integer.parseInt(this.editTextKm.getText().toString());
    }

    /**
     *
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

    public void showUpdateDialog(Context context, FuelViewItem f) {
        // Inflate the custom layout
        LayoutInflater inflater = LayoutInflater.from(context);
        // Create a custom view for the dialog
        View dialogView = inflater.inflate(R.layout.dialog_fuel, null);

        // Get references to the input fields
        final EditText editTextKm = dialogView.findViewById(R.id.editTextKm);
        final EditText editTextLiters = dialogView.findViewById(R.id.editTextLiters);
        final EditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);
        final SwitchMaterial editToggleFull = dialogView.findViewById(R.id.editToggleFull);
        final EditText editTextDate = dialogView.findViewById(R.id.editTextDate);

        // Pre-fill the fields with data
        editTextKm.setText(String.valueOf(f.getKm()));
        editTextLiters.setText(String.valueOf(f.getLiters()));
        editTextPrice.setText(String.valueOf(f.getPrice()));
        editToggleFull.setChecked(f.isFull());
        //todo use proper date format with / and add the datepicker also here
        editTextDate.setText(f.getDate().toString());

        // Create and show the AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit Details")
                .setView(dialogView)
                .setPositiveButton("Update", (dialog, which) -> {
                    // Get updated data from the fields
                    //todo get the updated values

                    //todo save the updated data
                    Toast.makeText(context, "UPDATED DATA SAVED", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
