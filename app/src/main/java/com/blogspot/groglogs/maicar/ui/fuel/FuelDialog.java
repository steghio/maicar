package com.blogspot.groglogs.maicar.ui.fuel;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.model.entity.FuelItem;
import com.blogspot.groglogs.maicar.model.view.FuelViewItem;
import com.blogspot.groglogs.maicar.ui.dialog.AbstractDialog;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.time.LocalDate;

import lombok.Getter;

@Getter
public class FuelDialog extends AbstractDialog {

    private final EditText editTextLiters;
    private final EditText editTextPriceLiter;
    private final SwitchMaterial editToggleFull;

    public FuelDialog(Context context, FuelAdapter fuelAdapter){
        super(context, fuelAdapter, R.layout.dialog_fuel);

        this.editTextLiters = dialogView.findViewById(R.id.editTextLiters);
        this.editTextPriceLiter = dialogView.findViewById(R.id.editTextPriceLiter);
        this.editToggleFull = dialogView.findViewById(R.id.editToggleFull);
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

    public double getLiters(){
        return Double.parseDouble(this.editTextLiters.getText().toString());
    }

    public double getPriceLiter(){
        return Double.parseDouble(this.editTextPriceLiter.getText().toString());
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
                        adapter.updateEntity(i, this.position);

                        Toast.makeText(d.getContext(), "Refuel updated", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    FuelItem i = new FuelItem(km, liters, price, full, date);
                    adapter.saveEntityAndRefreshView(i);

                    Toast.makeText(d.getContext(), "Refuel added", Toast.LENGTH_SHORT).show();
                }

                d.dismiss();
            }
        };
    }
}
