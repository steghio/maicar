package com.blogspot.groglogs.maicar.ui.fuel;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.ui.viewholder.AbstractViewHolder;

import lombok.Getter;

@Getter
public class FuelViewHolder extends AbstractViewHolder {
    private final ImageView literIconImageView;
    private final TextView literTextView;
    private final TextView priceLiterTextView;
    private final ImageView mpgIconImageView;
    private final TextView mpgTextView;

    public FuelViewHolder(@NonNull View itemView) {
        super(itemView);
        this.literIconImageView = itemView.findViewById(R.id.literIconImageView);
        this.literTextView = itemView.findViewById(R.id.literTextView);
        this.priceLiterTextView = itemView.findViewById(R.id.priceLiterTextView);
        this.mpgIconImageView = itemView.findViewById(R.id.mpgIconImageView);
        this.mpgTextView = itemView.findViewById(R.id.mpgTextView);
    }
}
