package com.blogspot.groglogs.maicar.ui.fuel;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.groglogs.maicar.R;

import lombok.Getter;

@Getter
public class FuelViewHolder extends RecyclerView.ViewHolder {
    private final ImageView kmIconImageView;
    private final TextView kmTextView;
    private final ImageView literIconImageView;
    private final TextView literTextView;
    private final ImageView priceIconImageView;
    private final TextView priceTextView;
    private final TextView priceLiterTextView;
    private final TextView dateTextView;
    private final ImageView mpgIconImageView;
    private final TextView mpgTextView;
    private final ImageButton editButton;
    private final ImageButton deleteButton;

    //todo gridlayout column weights for proper item spacing and positioning
    public FuelViewHolder(@NonNull View itemView) {
        super(itemView);
        this.kmIconImageView = itemView.findViewById(R.id.kmIconImageView);
        this.kmTextView = itemView.findViewById(R.id.kmTextView);
        this.literIconImageView = itemView.findViewById(R.id.literIconImageView);
        this.literTextView = itemView.findViewById(R.id.literTextView);
        this.priceIconImageView = itemView.findViewById(R.id.priceIconImageView);
        this.priceTextView = itemView.findViewById(R.id.priceTextView);
        this.priceLiterTextView = itemView.findViewById(R.id.priceLiterTextView);
        this.dateTextView = itemView.findViewById(R.id.dateTextView);
        this.mpgIconImageView = itemView.findViewById(R.id.mpgIconImageView);
        this.mpgTextView = itemView.findViewById(R.id.mpgTextView);
        this.editButton = itemView.findViewById(R.id.editButton);
        this.deleteButton = itemView.findViewById(R.id.deleteButton);
    }
}
