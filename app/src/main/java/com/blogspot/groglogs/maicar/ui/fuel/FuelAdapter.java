package com.blogspot.groglogs.maicar.ui.fuel;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.model.view.FuelViewItem;
import com.blogspot.groglogs.maicar.model.entity.FuelItem;
import com.blogspot.groglogs.maicar.storage.db.repository.FuelRepository;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
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

    public void updateEntity(FuelItem entity){
        fuelRepository.update(entity);
        //todo pass correct position notifyItemChanged(POSITION);
    }

    public void addEntity(FuelItem entity) {
        //todo add mapper
        items.add(new FuelViewItem(entity.getId(), entity.getKm(), entity.getLiters(), entity.getPrice(), entity.isFull(), entity.getDate()));
        //todo refresh UI correctly eg if item inserted in middle of list and not top
        //!!!!IMPORTANT MUST refresh otherwise id from DB is NOT returned in UI!!!!
        //todo also refresh correctly after insert should appear on top
        notifyItemInserted(items.size() - 1);
    }

    public void showInsertDialog(Context context) {
        FuelDialog fuelDialog = new FuelDialog(context, this);

        fuelDialog.addDatePicker(context, LocalDate.now());

        //prepare popup input
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Enter Refuel Details")
                .setView(fuelDialog.getDialogView())
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        //make it so the save button does validation and shows errors until correct, then it saves and closes
        dialog.setOnShowListener(dialogInterface -> {
            final AlertDialog d = (AlertDialog) dialogInterface;
            d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(fuelDialog.getSubmitButtonWithValidation(d, false));
        });

        dialog.show();
    }

    public void showUpdateDialog(Context context, FuelViewItem f) {
        FuelDialog fuelDialog = new FuelDialog(context, this);

        //todo when open dialog for update price should be kept and priceLiter should be removed so validation passes even when inverted
        fuelDialog.fillDialog(context, f);

        //prepare popup input
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Enter Refuel Details")
                .setView(fuelDialog.getDialogView())
                .setPositiveButton("Update", null)
                .setNegativeButton("Cancel", null)
                .create();

        //make it so the save button does validation and shows errors until correct, then it saves and closes
        dialog.setOnShowListener(dialogInterface -> {
            final AlertDialog d = (AlertDialog) dialogInterface;
            d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(fuelDialog.getSubmitButtonWithValidation(d, true));
        });

        dialog.show();
    }
}
