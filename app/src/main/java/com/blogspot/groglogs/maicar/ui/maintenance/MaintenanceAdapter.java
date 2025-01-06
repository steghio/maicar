package com.blogspot.groglogs.maicar.ui.maintenance;

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
import com.blogspot.groglogs.maicar.model.entity.MaintenanceItem;
import com.blogspot.groglogs.maicar.model.view.MaintenanceViewItem;
import com.blogspot.groglogs.maicar.storage.db.repository.MaintenanceRepository;
import com.blogspot.groglogs.maicar.ui.adapter.AbstractAdapter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import lombok.Getter;

//todo add filtering on maintenanceType logic
public class MaintenanceAdapter extends AbstractAdapter<MaintenanceViewHolder> {

    public static final String ACTIVITY_TYPE = "MAINTENANCE";

    @Getter
    private List<MaintenanceViewItem> items;

    private final MaintenanceRepository maintenanceRepository;

    public MaintenanceAdapter(Application application, RecyclerView recyclerView) {
        this.items = new ArrayList<>();
        this.maintenanceRepository = new MaintenanceRepository(application);
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public MaintenanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_maintenance, parent, false);

        return new MaintenanceViewHolder(view);
    }

    //todo use strings with placeholders
    @Override
    public void onBindViewHolder(@NonNull MaintenanceViewHolder holder, int position) {
        MaintenanceViewItem item = items.get(position);
        holder.getKmIconImageView().setImageResource(item.getKmIconResId());
        holder.getKmTextView().setText(item.getKm() + " km");
        holder.getTypeIconImageView().setImageResource(item.getTypeIconResId());
        holder.getTypeTextView().setText(item.getTypeText());
        holder.getPriceIconImageView().setImageResource(item.getPriceIconResId());
        holder.getPriceTextView().setText(item.getPrice() + " €");
        holder.getNotesTextView().setText(item.getNotes());
        holder.getDateTextView().setText(item.getDate().toString());

        ImageButton editButton = holder.getEditButton();

        if(editButton != null){
            editButton.setOnClickListener(view -> {
                Toast.makeText(view.getContext(), "EDIT ID: " + item.getId() + " - POS: " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();

                showUpdateDialog(view.getContext(), item, holder.getAdapterPosition());
            });
        }

        ImageButton deleteButton = holder.getDeleteButton();

        if(deleteButton != null){
            deleteButton.setOnClickListener(view -> {
                Toast.makeText(view.getContext(), "DELETE ID: " + item.getId() + " - POS: " + holder.getAdapterPosition(), Toast.LENGTH_SHORT).show();

                deleteItem(item, holder.getAdapterPosition());
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void loadAllItems(){
        this.items.clear();

        this.recyclerView.post(this::notifyDataSetChanged);

        List<MaintenanceItem> entities;

        try {
            entities = maintenanceRepository.getAllItemsByDateDesc();
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(recyclerView.getContext(), "Error loading items", Toast.LENGTH_SHORT).show();

            throw new RuntimeException(e);
        }

        for(MaintenanceItem i : entities){
            addEntity(i);
        }
    }

    public void deleteItem(MaintenanceViewItem item, int position){
        maintenanceRepository.delete(item.getId());
        items.remove(position);
        recyclerView.post(() -> notifyItemRemoved(position));
    }

    public void deleteAllItems(){
        maintenanceRepository.deleteAll();
        items.clear();
        recyclerView.post(this::notifyDataSetChanged);
    }

    public void saveEntity(Object entity) {
        try {
            maintenanceRepository.insert((MaintenanceItem) entity);
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(recyclerView.getContext(), "Error saving item", Toast.LENGTH_SHORT).show();

            throw new RuntimeException(e);
        }
    }

    public void saveEntityAndRefreshView(MaintenanceItem entity) {
        saveEntity(entity);
        loadAllItems();
    }

    public void updateEntity(MaintenanceItem entity, int position){
        boolean isFullRefresh = false;

        try{
            MaintenanceItem old = maintenanceRepository.findById(entity.getId());
            isFullRefresh = old != null && !old.getDate().isEqual(entity.getDate());
        } catch (ExecutionException | InterruptedException e) {
            //ignore, bad luck on refresh
        }

        maintenanceRepository.update(entity);

        //if date changed, need to refresh the whole view to insert in proper place
        if(isFullRefresh){
            loadAllItems();
        }
        else {
            items.set(position, new MaintenanceViewItem(entity.getId(), entity.getKm(), entity.getPrice(), entity.getDate(), entity.getMaintenanceType(), entity.getNotes()));
            recyclerView.post(() -> notifyItemChanged(position));
        }
    }

    public void addEntity(MaintenanceItem entity) {
        items.add(new MaintenanceViewItem(entity.getId(), entity.getKm(), entity.getPrice(), entity.getDate(), entity.getMaintenanceType(), entity.getNotes()));
        recyclerView.post(() -> notifyItemInserted(items.size() - 1));
    }

    public void showInsertDialog(Context context) {
        MaintenanceDialog maintenanceDialog = new MaintenanceDialog(context, this);

        maintenanceDialog.addDatePicker(context, LocalDate.now());
        maintenanceDialog.addTypeDropdown(context);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Enter Maintenance Details")
                .setView(maintenanceDialog.getDialogView())
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            final AlertDialog d = (AlertDialog) dialogInterface;
            d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(maintenanceDialog.getSubmitButtonWithValidation(d, false));
        });

        dialog.show();
    }

    public void showUpdateDialog(Context context, MaintenanceViewItem m, int position) {
        MaintenanceDialog maintenanceDialog = new MaintenanceDialog(context, this);

        maintenanceDialog.addTypeDropdown(context);
        maintenanceDialog.fillDialog(context, m, position);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("Edit Maintenance Details")
                .setView(maintenanceDialog.getDialogView())
                .setPositiveButton("Update", null)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(dialogInterface -> {
            final AlertDialog d = (AlertDialog) dialogInterface;
            d.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(maintenanceDialog.getSubmitButtonWithValidation(d, true));
        });

        dialog.show();
    }

    public String getActivityType(){
        return ACTIVITY_TYPE;
    }
}
