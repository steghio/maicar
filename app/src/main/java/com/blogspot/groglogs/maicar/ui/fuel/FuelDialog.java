package com.blogspot.groglogs.maicar.ui.fuel;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.model.entity.FuelItem;
import com.blogspot.groglogs.maicar.model.view.FuelViewItem;
import com.blogspot.groglogs.maicar.util.DateUtils;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class FuelDialog {

    private final View dialogView;
    private long id;
    private int position;
    private final EditText editTextKm;
    private final EditText editTextLiters;
    private final EditText editTextPrice;
    private final EditText editTextPriceLiter;
    private final SwitchMaterial editToggleFull;
    private final EditText editTextDate;
    private final FuelAdapter fuelAdapter;

    public FuelDialog(Context context, FuelAdapter fuelAdapter){
        this.id = -1;
        this.position = -1;

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        this.dialogView = layoutInflater.inflate(R.layout.dialog_fuel, null);

        this.editTextKm = dialogView.findViewById(R.id.editTextKm);
        this.editTextLiters = dialogView.findViewById(R.id.editTextLiters);
        this.editTextPrice = dialogView.findViewById(R.id.editTextPrice);
        this.editTextPriceLiter = dialogView.findViewById(R.id.editTextPriceLiter);
        this.editToggleFull = dialogView.findViewById(R.id.editToggleFull);
        this.editTextDate = dialogView.findViewById(R.id.editTextDate);

        this.fuelAdapter = fuelAdapter;
    }

    public void fillDialog(Context context, FuelViewItem f, int position){
        this.id = f.getId();
        this.position = position;

        f.setPricePerLiter(-1.0);
        this.editTextPriceLiter.setText("");

        this.editTextKm.setText(String.valueOf(f.getKm()));
        this.editTextLiters.setText(String.valueOf(f.getLiters()));
        this.editTextPrice.setText(String.valueOf(f.getPrice()));
        this.editToggleFull.setChecked(f.isFull());

        this.addDatePicker(context, f.getDate());
    }

    public int getKm(){
        return Integer.parseInt(this.editTextKm.getText().toString());
    }

    public double getLiters(){
        return Double.parseDouble(this.editTextLiters.getText().toString());
    }

    public double getPrice(){
        return Double.parseDouble(this.editTextPrice.getText().toString());
    }

    public double getPriceLiter(){
        return Double.parseDouble(this.editTextPriceLiter.getText().toString());
    }

    public LocalDate getDate(){
        return DateUtils.fromString(this.editTextDate.getText().toString());
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

    public View.OnClickListener getSubmitButtonWithValidation(AlertDialog d, boolean isUpdate){
        return v -> {
            int km = -1;
            double liters = -1.0;
            double price = -1.0;
            double priceLiter = -1.0;
            boolean full = this.editToggleFull.isChecked();
            LocalDate date = null;

            boolean isError = false;

            try {
                km = this.getKm();
            } catch (NumberFormatException e) {
                this.editTextKm.setError("Please enter a positive value for Km");
                isError = true;
            }

            try {
                liters = this.getLiters();
            } catch (NumberFormatException e) {
                this.editTextLiters.setError("Please enter a positive value for Liters");
                isError = true;
            }

            try {
                price = this.getPrice();
            } catch (NumberFormatException e) {
                //ignore, is handled later
            }

            try{
                priceLiter = this.getPriceLiter();
            } catch (NumberFormatException e) {
                //ignore, is handled later
            }

            if((price >= 0 && priceLiter >= 0) || (price < 0 && priceLiter < 0)){
                this.editTextPrice.setError("Please enter a positive value for either Price or Price per Liter, not both");
                this.editTextPriceLiter.setError("Please enter a positive value for either Price or Price per Liter, not both");
                isError = true;
            }

            if(!isError && priceLiter >= 0){
                price = liters * priceLiter;
            }

            try{
                date = this.getDate();
            } catch (Exception e){
                this.editTextDate.setError("Invalid Date");
                isError = true;
            }

            if(!isError){
                if(isUpdate){
                    if(this.id == -1){
                        Toast.makeText(d.getContext(), "Error update row with no ID!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        FuelItem i = new FuelItem(km, liters, price, full, date);
                        i.setId(this.id);
                        fuelAdapter.updateEntity(i, this.position);

                        Toast.makeText(d.getContext(), "Refuel updated", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    FuelItem i = new FuelItem(km, liters, price, full, date);
                    fuelAdapter.saveEntityAndRefreshView(i);

                    Toast.makeText(d.getContext(), "Refuel added", Toast.LENGTH_SHORT).show();
                }

                d.dismiss();
            }
        };
    }
}
