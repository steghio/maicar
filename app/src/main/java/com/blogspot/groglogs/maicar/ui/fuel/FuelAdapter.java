package com.blogspot.groglogs.maicar.ui.fuel;

import android.app.AlertDialog;
import android.app.Application;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.model.view.FuelViewItem;
import com.blogspot.groglogs.maicar.model.entity.FuelItem;
import com.blogspot.groglogs.maicar.storage.db.repository.FuelRepository;
import com.blogspot.groglogs.maicar.util.DateUtils;
import com.blogspot.groglogs.maicar.util.StringUtils;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class FuelAdapter extends RecyclerView.Adapter<FuelViewHolder> {
    private DecimalFormat df4 = new DecimalFormat("#.####");
    private DecimalFormat df1 = new DecimalFormat("#.#");

    private List<FuelViewItem> items;
    //key = item position, value = mpg for item (if full tank)
    private Map<Integer,Double> mpgMap;

    private FuelRepository fuelRepository;

    public FuelAdapter(Application application) {
        this.items = new ArrayList<>();
        this.mpgMap = new HashMap<>();
        this.fuelRepository = new FuelRepository(application);
    }

    @NonNull
    @Override
    public FuelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fuel, parent, false);

        return new FuelViewHolder(view);
    }

    //todo use strings with placeholders
    //todo mpg calc logic in load call
    @Override
    public void onBindViewHolder(@NonNull FuelViewHolder holder, int position) {
        FuelViewItem item = items.get(position);
        holder.getKmIconImageView().setImageResource(item.getKmIconResId());
        holder.getKmTextView().setText(item.getKm() + " km");
        holder.getLiterIconImageView().setImageResource(item.getFuelIconResId());
        holder.getLiterTextView().setText(item.getLiters() + " L");
        holder.getPriceIconImageView().setImageResource(item.getPriceIconResId());
        holder.getPriceTextView().setText(item.getPrice() + " €");
        holder.getPriceLiterTextView().setText("€/L " + df4.format(item.getPricePerLiter()));
        holder.getDateTextView().setText(item.getDate().toString());

        double totLiters = item.getLiters();
        int currKm = item.getKm();
        int prevKmFull = 0;
        int idx = position - 1;
        FuelViewItem prevItem;

        while(idx > 0){
            prevItem = items.get(idx);

            if(prevItem.isFull()){
                prevKmFull = prevItem.getKm();
                break;
            }

            totLiters += prevItem.getLiters();
            idx--;
        }

        if(position > 0 && item.isFull()) {
            double mpg = 100 * totLiters / (currKm - prevKmFull);
            mpgMap.put(position, mpg);
            Double prevMpg = mpgMap.get(idx);

            int img = -1;

            if(prevMpg == null || Math.abs(prevMpg - mpg) == 0.0){
                img = R.drawable.ic_chart_flat_24dp;
            }
            else if(prevMpg - mpg < 0.1){
                img = R.drawable.ic_chart_up_24dp;
            }
            else{
                img = R.drawable.ic_chart_down_24dp;
            }

            holder.getMpgIconImageView().setImageResource(img);
            holder.getMpgTextView().setText(df1.format(mpg));
        }

        ImageButton editButton = holder.getEditButton();

        if(editButton != null){
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Action to be performed when the FAB is clicked
                    Toast.makeText(view.getContext(), "EDIT ID: " + item.getId() + " - POS: " + position, Toast.LENGTH_SHORT).show();

                    showUpdateDialog(view.getContext(), item);
                }
            });
        }

        ImageButton deleteButton = holder.getDeleteButton();

        if(deleteButton != null){
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Action to be performed when the FAB is clicked
                    Toast.makeText(view.getContext(), "DELETE ID: " + item.getId() + " - POS: " + position, Toast.LENGTH_SHORT).show();

                    deleteItem(item, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void loadAllItems(){
        List<FuelItem> entities = new ArrayList<>();

        //todo handle properly
        try {
            entities = fuelRepository.getAllItemsSortedDesc();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        addEntities(entities);
    }

    public void deleteItem(FuelViewItem item, int position){
        fuelRepository.delete(item.getId());

        items.remove(position);

        notifyItemRemoved(position);
    }

    public void addEntities(List<FuelItem> entities) {
        for(FuelItem e : entities){
            addEntity(e);
        }
    }

    public void saveEntity(FuelItem entity) {
        fuelRepository.insert(entity);
        addEntity(entity);
    }

    public void addEntity(FuelItem entity) {
        //todo add mapper
        items.add(new FuelViewItem(entity.getId(), entity.getKm(), entity.getLiters(), entity.getPrice(), entity.isFull(), entity.getDate()));
        //todo refresh UI correctly eg if item inserted in middle of list and not top
        //!!!!IMPORTANT MUST refresh otherwise id from DB is NOT returned in UI!!!!
        //todo also refresh correctly after insert should appear on top
        notifyItemInserted(items.size() - 1);
    }

    //todo make error message block ui but not close dialog, use textwatcher or similar and show red field + message instead of toast
    public void showInsertDialog(Context context) {

        FuelDialog fuelDialog = new FuelDialog(context);

        fuelDialog.addDatePicker(context, LocalDate.now());

        // Set up the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Enter Refuel Details")
                .setView(fuelDialog.getDialogView()) // Set the custom layout in the dialog
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int km = -1;
                        double liters = -1.0;
                        double price = -1.0;
                        double priceLiter = -1.0;
                        boolean full = fuelDialog.getEditToggleFull().isChecked();
                        LocalDate date = null;

                        try {
                            km = fuelDialog.getKm();
                        } catch (NumberFormatException e) {
                            Toast.makeText(context, "Please enter a positive value for Km", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            liters = Double.parseDouble(fuelDialog.getEditTextLiters().getText().toString());
                        } catch (NumberFormatException e) {
                            Toast.makeText(context, "Please enter a positive value for Liters", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            if(!StringUtils.isBlank(fuelDialog.getEditTextPrice().getText().toString())) {
                                price = Double.parseDouble(fuelDialog.getEditTextPrice().getText().toString());
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(context, "Please enter a positive value for Price", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            if(!StringUtils.isBlank(fuelDialog.getEditTextPriceLiter().getText().toString())) {
                                priceLiter = Double.parseDouble(fuelDialog.getEditTextPriceLiter().getText().toString());
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(context, "Please enter a positive value for Price per Liter", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(price >= 0 && priceLiter >= 0){
                            Toast.makeText(context, "Please enter either Price or Price per Liter, not both", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(priceLiter >= 0){
                            price = liters * priceLiter;
                        }

                        try{
                            String[] dateParts = fuelDialog.getEditTextDate().getText().toString().split("-");
                            date = LocalDate.of(Integer.parseInt(dateParts[0]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[2]));
                        } catch (Exception e){
                            Toast.makeText(context, "Invalid Date", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        FuelItem i = new FuelItem(km, liters, price, full, date);
                        saveEntity(i);

                        Toast.makeText(context, "Refuel added", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null) // Do nothing on cancel
                .show();
    }

    //todo use same form and checks and all from fragment, just change prefill logic and confirm button text
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
