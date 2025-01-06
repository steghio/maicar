package com.blogspot.groglogs.maicar.ui.viewholder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blogspot.groglogs.maicar.R;

import lombok.Getter;

@Getter
public class AbstractViewHolder extends RecyclerView.ViewHolder {
    protected final ImageView kmIconImageView;
    protected final TextView kmTextView;
    protected final ImageView priceIconImageView;
    protected final TextView priceTextView;
    protected final TextView dateTextView;
    protected final ImageButton editButton;
    protected final ImageButton deleteButton;

    public AbstractViewHolder(@NonNull View itemView) {
        super(itemView);
        this.kmIconImageView = itemView.findViewById(R.id.kmIconImageView);
        this.kmTextView = itemView.findViewById(R.id.kmTextView);
        this.priceIconImageView = itemView.findViewById(R.id.priceIconImageView);
        this.priceTextView = itemView.findViewById(R.id.priceTextView);
        this.dateTextView = itemView.findViewById(R.id.dateTextView);
        this.editButton = itemView.findViewById(R.id.editButton);
        this.deleteButton = itemView.findViewById(R.id.deleteButton);
    }
}
