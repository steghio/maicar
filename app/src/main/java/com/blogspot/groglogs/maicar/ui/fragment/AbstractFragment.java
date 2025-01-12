package com.blogspot.groglogs.maicar.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.blogspot.groglogs.maicar.ui.adapter.AbstractAdapter;
import com.blogspot.groglogs.maicar.ui.fuel.FuelAdapter;
import com.blogspot.groglogs.maicar.ui.maintenance.MaintenanceAdapter;
import com.blogspot.groglogs.maicar.ui.menu.TopMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import lombok.Getter;

public abstract class AbstractFragment extends Fragment {

    @Getter
    protected static AbstractAdapter adapter;

    protected View buildView(LayoutInflater inflater, ViewGroup container, boolean isFuel){
        View view = inflater.inflate(isFuel ? R.layout.fragment_fuel : R.layout.fragment_maintenance, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        adapter = isFuel ? new FuelAdapter(getActivity().getApplication(), recyclerView) : new MaintenanceAdapter(getActivity().getApplication(), recyclerView);
        recyclerView.setAdapter(adapter);

        //divider between items in view
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        loadAllItems();

        TopMenu topMenu = new TopMenu(requireContext(), adapter, isFuel ? R.menu.top_menu_fuel : R.menu.top_menu_maintenance);
        if(!isFuel){
            topMenu.setContextView(view);
        }
        requireActivity().addMenuProvider(topMenu, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        requireActivity().invalidateOptionsMenu();

        return view;
    }

    //ensure floating button is not hidden behind bottom navigation bar
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //make sure the BottomNavigationView is available after the layout is drawn
        view.getViewTreeObserver().addOnPreDrawListener(() -> {
            //only access the BottomNavigationView once the layout has been drawn
            if (getActivity() != null) {
                BottomNavigationView navView = getActivity().findViewById(R.id.nav_view);

                if (navView != null) {
                    //get the height of the BottomNavigationView
                    int navViewHeight = navView.getHeight();

                    FloatingActionButton fab = view.findViewById(R.id.fab);

                    if (fab != null) {
                        //adjust the FAB's position dynamically
                        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) fab.getLayoutParams();
                        params.bottomMargin = navViewHeight + (int) (16 * getResources().getDisplayMetrics().density);//16dp extra spacing
                        fab.setLayoutParams(params);
                    }
                }
            }
            //return true to allow drawing to proceed
            return true;
        });

        FloatingActionButton fab = view.findViewById(R.id.fab);
        if (fab != null) {
            fab.setOnClickListener(view1 -> {
                Toast.makeText(view1.getContext(), "Opening add item dialog", Toast.LENGTH_SHORT).show();

                showInsertDialog();
            });
        }
    }

    public void showInsertDialog() {
        adapter.showInsertDialog(getContext());
    }

    protected void loadAllItems() {
        adapter.loadAllItems();
    }
}
