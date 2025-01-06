package com.blogspot.groglogs.maicar.ui.maintenance;

import android.graphics.text.LineBreaker;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.ui.viewholder.AbstractViewHolder;

import lombok.Getter;

@Getter
public class MaintenanceViewHolder extends AbstractViewHolder {
    private final ImageView typeIconImageView;
    private final TextView typeTextView;
    private final TextView notesTextView;

    public MaintenanceViewHolder(@NonNull View itemView) {
        super(itemView);
        this.typeIconImageView = itemView.findViewById(R.id.typeIconImageView);
        this.typeTextView = itemView.findViewById(R.id.typeTextView);
        this.notesTextView = itemView.findViewById(R.id.notesTextView);
        //todo make notes break in multiline text properly
        this.notesTextView.setSingleLine(false);
        this.notesTextView.setBreakStrategy(LineBreaker.BREAK_STRATEGY_HIGH_QUALITY);
        this.notesTextView.setEllipsize(null);
        this.notesTextView.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
    }
}
