package com.blogspot.groglogs.maicar.ui.fuel;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.groglogs.maicar.R;

//todo lombok and access via getter instead of field
public class FuelViewHolder extends RecyclerView.ViewHolder {
    ImageView kmIconImageView;
    TextView kmTextView;
    ImageView literIconImageView;
    TextView literTextView;
    ImageView priceIconImageView;
    TextView priceTextView;
    TextView priceLiterTextView;
    TextView dateTextView;
    ImageView mpgIconImageView;
    TextView mpgTextView;
    ImageButton editButton;
    ImageButton deleteButton;

    //todo gridlayout column weights for proper item positioning
    public FuelViewHolder(@NonNull View itemView) {
        super(itemView);
        kmIconImageView = itemView.findViewById(R.id.kmIconImageView);
        kmTextView = itemView.findViewById(R.id.kmTextView);
        literIconImageView = itemView.findViewById(R.id.literIconImageView);
        literTextView = itemView.findViewById(R.id.literTextView);
        priceIconImageView = itemView.findViewById(R.id.priceIconImageView);
        priceTextView = itemView.findViewById(R.id.priceTextView);
        priceLiterTextView = itemView.findViewById(R.id.priceLiterTextView);
        dateTextView = itemView.findViewById(R.id.dateTextView);
        mpgIconImageView = itemView.findViewById(R.id.mpgIconImageView);
        mpgTextView = itemView.findViewById(R.id.mpgTextView);
        editButton = itemView.findViewById(R.id.editButton);
        deleteButton = itemView.findViewById(R.id.deleteButton);
    }
}
