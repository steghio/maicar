package com.blogspot.groglogs.maicar.ui.fuel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.model.FuelViewItem;
import com.blogspot.groglogs.maicar.model.entity.FuelItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FuelAdapter extends RecyclerView.Adapter<FuelViewHolder> {

    private DecimalFormat df4 = new DecimalFormat("#.####");
    private DecimalFormat df1 = new DecimalFormat("#.#");
    private List<FuelViewItem> items;
    //key = item position, value = mpg for item (if full tank)
    private Map<Integer,Double> mpgMap;

    public FuelAdapter() {
        this.items = new ArrayList<>();
        this.mpgMap = new HashMap<>();
    }

    @NonNull
    @Override
    public FuelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_fuel, parent, false);
        return new FuelViewHolder(view);
    }

    //todo use strings with placeholders
    @Override
    public void onBindViewHolder(@NonNull FuelViewHolder holder, int position) {
        FuelViewItem item = items.get(position);
        holder.kmIconImageView.setImageResource(item.getKmIconResId());
        holder.kmTextView.setText(item.getKm() + " km");
        holder.literIconImageView.setImageResource(item.getFuelIconResId());
        holder.literTextView.setText(item.getLiters() + " L");
        holder.priceIconImageView.setImageResource(item.getPriceIconResId());
        holder.priceTextView.setText(item.getPrice() + " €");
        holder.priceLiterTextView.setText("€/L " + df4.format(item.getPricePerLiter()));
        holder.dateTextView.setText(item.getDate().toString());

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

            if(prevMpg == null || Math.abs(prevMpg - mpg) == 0.0){
                holder.mpgIconImageView.setImageResource(R.drawable.ic_chart_flat_24dp);
            }
            else if(prevMpg - mpg < 0.1){
                holder.mpgIconImageView.setImageResource(R.drawable.ic_chart_up_24dp);
            }
            else{
                holder.mpgIconImageView.setImageResource(R.drawable.ic_chart_down_24dp);
            }

            holder.mpgTextView.setText(df1.format(mpg));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItems(List<FuelItem> items) {
        for(FuelItem f : items){
            addItem(f);
        }
    }

    public void addItem(FuelItem f) {
        //todo add mapper
        items.add(new FuelViewItem(f.getId(), f.getKm(), f.getLiters(), f.getPrice(), f.isFull(), f.getDate()));
        //todo refresh UI correctly eg if item inserted in middle of list and not top
        //todo also refresh correctly after insert should appear on top
        notifyItemInserted(items.size() - 1);
    }
}
