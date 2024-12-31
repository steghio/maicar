package com.blogspot.groglogs.maicar.ui.fuel;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.model.entity.FuelItem;
import com.blogspot.groglogs.maicar.storage.db.AppDatabase;
import com.blogspot.groglogs.maicar.util.StringUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;

public class FuelFragment extends Fragment {

    private RecyclerView recyclerView;
    private FuelAdapter adapter;
    private AppDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fuel, container, false);

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        // Initialize Adapter with sample data
        adapter = new FuelAdapter();
        recyclerView.setAdapter(adapter);

        // Add a DividerItemDecoration
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        db = Room.databaseBuilder(view.getContext(),
                AppDatabase.class, "maicardb").build();

        loadData();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Make sure the BottomNavigationView is available after the layout is drawn
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                // Only access the BottomNavigationView once the layout has been drawn
                if (getActivity() != null) {
                    BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);

                    if (navView != null) {
                        // Get the height of the BottomNavigationView
                        int navViewHeight = navView.getHeight();

                        // Find the FloatingActionButton by ID
                        FloatingActionButton fab = view.findViewById(R.id.fab);

                        if (fab != null) {
                            // Adjust the FAB's position dynamically
                            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) fab.getLayoutParams();
                            params.bottomMargin = navViewHeight + (int) (16 * getResources().getDisplayMetrics().density); // 16dp extra spacing
                            fab.setLayoutParams(params);
                        }
                    }
                }
                // Return true to allow drawing to proceed
                return true;
            }
        });

        // Set an OnClickListener to handle the FAB click action
        FloatingActionButton fab = view.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Action to be performed when the FAB is clicked
                    Toast.makeText(view.getContext(), "FAB clicked!", Toast.LENGTH_SHORT).show();

                    showInsertDialog();
                }
            });
        }
    }

    // Method to show the form dialog to collect user input
    private void showInsertDialog() {
        // Create a custom view for the dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_fuel, null);

        // Get references to the EditTexts in the custom layout
        final EditText editTextKm = dialogView.findViewById(R.id.editTextKm);
        final EditText editTextLiters = dialogView.findViewById(R.id.editTextLiters);
        final EditText editTextPrice = dialogView.findViewById(R.id.editTextPrice);
        final EditText editTextPriceLiter = dialogView.findViewById(R.id.editTextPriceLiter);
        final SwitchMaterial editToggleFull = dialogView.findViewById(R.id.editToggleFull);
        final EditText editTextDate = dialogView.findViewById(R.id.editTextDate);

        //today
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String currentDate = day + "/" + (month + 1) + "/" + year;
        editTextDate.setText(currentDate);

        // Handle DatePickerDialog for the date field
        editTextDate.setOnClickListener(v -> {
            // Show the DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        // Format the selected date
                        String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        editTextDate.setText(formattedDate);
                    },
                    year,
                    month,
                    day
            );
            datePickerDialog.show();
        });

        //todo make error message block ui but not close dialog, use textwatcher or similar and show red field + message instead of toast

        // Set up the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Enter Refuel Details")
                .setView(dialogView) // Set the custom layout in the dialog
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int km = -1;
                        double liters = -1.0;
                        double price = -1.0;
                        double priceLiter = -1.0;
                        boolean full = editToggleFull.isChecked();
                        LocalDate date = null;

                        try {
                            km = Integer.parseInt(editTextKm.getText().toString());
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Please enter a positive value for Km", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            liters = Double.parseDouble(editTextLiters.getText().toString());
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Please enter a positive value for Liters", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            if(!StringUtils.isBlank(editTextPrice.getText().toString())) {
                                price = Double.parseDouble(editTextPrice.getText().toString());
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Please enter a positive value for Price", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            if(!StringUtils.isBlank(editTextPriceLiter.getText().toString())) {
                                priceLiter = Double.parseDouble(editTextPriceLiter.getText().toString());
                            }
                        } catch (NumberFormatException e) {
                            Toast.makeText(getContext(), "Please enter a positive value for Price per Liter", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(price >= 0 && priceLiter >= 0){
                            Toast.makeText(getContext(), "Please enter either Price or Price per Liter, not both", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(priceLiter >= 0){
                            price = liters * priceLiter;
                        }

                        try{
                            String[] dateParts = editTextDate.getText().toString().split("/");
                            date = LocalDate.of(Integer.parseInt(dateParts[2]), Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0]));
                        } catch (Exception e){
                            Toast.makeText(getContext(), "Invalid Date", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        FuelItem i = new FuelItem(km, liters, price, full, date);
                        saveData(i);

                        Toast.makeText(getContext(), "Refuel added", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null) // Do nothing on cancel
                .show();
    }

    private void saveData(FuelItem f){
        new Thread(new Runnable() {
            @Override
            public void run() {
                db.fuelDao().insert(f);

                // Update the UI on the main thread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addItem(f);  // Update the adapter with the new list
                    }
                });
            }
        }).start();
    }

    private void loadData() {
        // Use AsyncTask or background thread to load data from the database
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<FuelItem> items = db.fuelDao().getAllItems();

                // Update the UI on the main thread
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.addItems(items);  // Update the adapter with the new list
                    }
                });
            }
        }).start();
    }
}