package com.blogspot.groglogs.maicar.ui.fuel;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.model.entity.MaintenanceTypeEnum;
import com.blogspot.groglogs.maicar.model.view.FuelViewItem;
import com.blogspot.groglogs.maicar.model.entity.FuelItem;
import com.blogspot.groglogs.maicar.storage.db.repository.FuelRepository;
import com.blogspot.groglogs.maicar.ui.adapter.AbstractAdapter;
import com.blogspot.groglogs.maicar.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import lombok.Getter;

public class FuelAdapter extends AbstractAdapter<FuelViewHolder> {

    public static final String ACTIVITY_TYPE = "FUEL";

    @Getter
    private List<FuelViewItem> items;

    //todo if possible to track better structure to modify only what necessary on data changes to display correct mpg comparison
    //key = item position, value = mpg for item (if full tank)
    private Map<Integer,Double> mpgMap;

    private final FuelRepository fuelRepository;

    public FuelAdapter(Application application, RecyclerView recyclerView) {
        this.items = new ArrayList<>();
        this.mpgMap = new HashMap<>();
        this.fuelRepository = new FuelRepository(application);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public FuelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fuel, parent, false);

        return new FuelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FuelViewHolder holder, int position) {
        FuelViewItem item = items.get(position);
        holder.getKmIconImageView().setImageResource(item.getKmIconResId());
        holder.getKmTextView().setText(recyclerView.getContext().getString(R.string.display_km, StringUtils.formatIntegerWithThousandSeparator(item.getKm())));
        holder.getLiterIconImageView().setImageResource(item.getFuelIconResId());
        holder.getLiterTextView().setText(recyclerView.getContext().getString(R.string.display_liters, item.getLiters()));
        holder.getPriceIconImageView().setImageResource(item.getPriceIconResId());
        holder.getPriceTextView().setText(recyclerView.getContext().getString(R.string.display_price4dec, item.getPrice()));
        holder.getPriceLiterTextView().setText(recyclerView.getContext().getString(R.string.display_priceL4dec, item.getPricePerLiter()));
        holder.getDateTextView().setText(item.getDate().toString());

        //previous item in sorted desc list
        int idx = items.size() - 1 - position - 1;

        Double prevMpg = null;

        while(idx > 0){
            prevMpg = mpgMap.get(idx);

            if(prevMpg != null){
                break;
            }

            idx--;
        }

        Double mpg = mpgMap.get(items.size() - 1 - position);

        if(position < items.size() && mpg != null) {
            int img;

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
            holder.getMpgTextView().setText(StringUtils.decimal2String1Precision(mpg));
        }

        holder.getEditButton().setOnClickListener(view -> {
                Toast.makeText(view.getContext(), "EDIT ID: " + item.getId() + " - POS: " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();

                showUpdateDialog(view.getContext(), item, holder.getAdapterPosition());
            });

        holder.getDeleteButton().setOnClickListener(view -> {
                Toast.makeText(view.getContext(), "DELETE ID: " + item.getId() + " - POS: " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();

                deleteItem(item);
            });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    //load asc but display desc
    public void loadAllItems(){
        this.items = new ArrayList<>();
        this.mpgMap = new HashMap<>();

        this.recyclerView.post(this::notifyDataSetChanged);

        List<FuelItem> entities = loadAndCalculateMpg();

        for(int i = entities.size() - 1; i >= 0; i--){
            addEntity(entities.get(i));
        }
    }

    private List<FuelItem> loadAndCalculateMpg(){
        List<FuelItem> entities;

        try {
            entities = fuelRepository.getAllItemsByDateAsc();
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(recyclerView.getContext(), "Error loading items", Toast.LENGTH_SHORT).show();

            throw new RuntimeException(e);
        }

        for(int i = 0; i < entities.size(); i++) {
            FuelItem item = entities.get(i);

            double totLiters = item.getLiters();
            int currKm = item.getKm();
            int prevKmFull = 0;
            int idx = i - 1;
            FuelItem prevItem;

            while (idx > 0) {
                prevItem = entities.get(idx);

                if (prevItem.isFull()) {
                    prevKmFull = prevItem.getKm();
                    break;
                }

                totLiters += prevItem.getLiters();
                idx--;
            }

            if (i > 0 && item.isFull()) {
                double mpg = 100 * totLiters / (currKm - prevKmFull);
                mpgMap.put(i, mpg);
            }
        }

        return entities;
    }

    public void deleteItem(FuelViewItem item){
        fuelRepository.delete(item.getId());

        //mpg comparison is between adjacent elements, indexed by position
        //we need to recalc all the mpg map for all positions when structure changes
        loadAllItems();
    }

    public void deleteAllItems(){
        fuelRepository.deleteAll();
        items.clear();
        recyclerView.post(this::notifyDataSetChanged);
    }

    public void saveEntity(Object entity) {
        try {
            fuelRepository.insert((FuelItem) entity);
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(recyclerView.getContext(), "Error saving item", Toast.LENGTH_SHORT).show();

            throw new RuntimeException(e);
        }
    }

    public void saveEntityAndRefreshView(Object entity) {
        saveEntity(entity);
        loadAllItems();
    }

    public void updateEntity(Object o, int position){
        boolean isFullRefresh = false;

        FuelItem entity = (FuelItem) o;

        try{
            FuelItem old = fuelRepository.findById(entity.getId());
            isFullRefresh = old != null && (
                    !old.getDate().isEqual(entity.getDate()) ||
                            old.getKm() != entity.getKm() ||
                            old.getLiters() != entity.getLiters() ||
                            old.isFull() != entity.isFull()
            );
        } catch (ExecutionException | InterruptedException e) {
            //ignore, bad luck on refresh
        }

        fuelRepository.update(entity);

        //if anything used for mpg calc has been changed, reload, otherwise simple refresh
        if(isFullRefresh){
            loadAllItems();
        }
        else{
            items.set(position, new FuelViewItem(entity.getId(), entity.getKm(), entity.getLiters(), entity.getPrice(), entity.isFull(), entity.getDate()));
            recyclerView.post(() -> notifyItemChanged(position));
        }
    }

    public void addEntity(FuelItem entity) {
        items.add(new FuelViewItem(entity.getId(), entity.getKm(), entity.getLiters(), entity.getPrice(), entity.isFull(), entity.getDate()));
        recyclerView.post(() -> notifyItemInserted(items.size() - 1));
    }

    public void showInsertDialog(Context context) {
        FuelDialog fuelDialog = new FuelDialog(context, this);

        fuelDialog.addDatePicker(context, LocalDate.now());

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Enter Refuel Details")
                .setView(fuelDialog.getDialogView())
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            final AlertDialog d = (AlertDialog) dialogInterface;
            d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(fuelDialog.getSubmitButtonWithValidation(d, false));
        });

        dialog.show();
    }

    public void showUpdateDialog(Context context, FuelViewItem f, int position) {
        FuelDialog fuelDialog = new FuelDialog(context, this);

        fuelDialog.fillDialog(context, f, position);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Edit Refuel Details")
                .setView(fuelDialog.getDialogView())
                .setPositiveButton("Update", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            final AlertDialog d = (AlertDialog) dialogInterface;
            d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(fuelDialog.getSubmitButtonWithValidation(d, true));
        });

        dialog.show();
    }

    public String getActivityType(){
        return ACTIVITY_TYPE;
    }

    public void filter(MaintenanceTypeEnum position){}
}
