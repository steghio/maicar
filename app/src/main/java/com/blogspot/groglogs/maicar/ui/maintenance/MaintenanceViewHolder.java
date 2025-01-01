package com.blogspot.groglogs.maicar.ui.maintenance;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.groglogs.maicar.R;

import lombok.Getter;

@Getter
public class MaintenanceViewHolder extends RecyclerView.ViewHolder {
    private final ImageView kmIconImageView;
    private final TextView kmTextView;
    private final ImageView typeIconImageView;
    private final TextView typeTextView;
    private final ImageView priceIconImageView;
    private final TextView priceTextView;
    private final TextView notesTextView;
    private final TextView dateTextView;
    private final ImageButton editButton;
    private final ImageButton deleteButton;

    //todo gridlayout column weights for proper item positioning
    public MaintenanceViewHolder(@NonNull View itemView) {
        super(itemView);
        this.kmIconImageView = itemView.findViewById(R.id.kmIconImageView);
        this.kmTextView = itemView.findViewById(R.id.kmTextView);
        this.typeIconImageView = itemView.findViewById(R.id.typeIconImageView);
        this.typeTextView = itemView.findViewById(R.id.typeTextView);
        this.priceIconImageView = itemView.findViewById(R.id.priceIconImageView);
        this.priceTextView = itemView.findViewById(R.id.priceTextView);
        this.notesTextView = itemView.findViewById(R.id.notesTextView);
        this.dateTextView = itemView.findViewById(R.id.dateTextView);
        this.editButton = itemView.findViewById(R.id.editButton);
        this.deleteButton = itemView.findViewById(R.id.deleteButton);
    }
}
