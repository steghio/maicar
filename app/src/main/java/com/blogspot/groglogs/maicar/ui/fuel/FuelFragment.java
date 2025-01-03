package com.blogspot.groglogs.maicar.ui.fuel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.ui.menu.TopMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class FuelFragment extends Fragment {
    private static FuelAdapter fuelAdapter;

    //todo check if we can do better
    public static FuelAdapter getFuelAdapter(){
        return fuelAdapter;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fuel, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        fuelAdapter = new FuelAdapter(getActivity().getApplication());
        recyclerView.setAdapter(fuelAdapter);

        //divider between items in view
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        loadData();

        requireActivity().addMenuProvider(new TopMenu(requireContext(), fuelAdapter), getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        requireActivity().invalidateOptionsMenu();

        return view;
    }

    //ensure floating button is not hidden behind bottom navigation bar
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

        FloatingActionButton fab = view.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //todo remove this debug
                    Toast.makeText(view.getContext(), "FAB clicked!", Toast.LENGTH_SHORT).show();

                    showInsertDialog();
                }
            });
        }
    }

    private void showInsertDialog() {
        fuelAdapter.showInsertDialog(getContext());
    }

    private void loadData() {
        fuelAdapter.loadAllItems();
    }
}