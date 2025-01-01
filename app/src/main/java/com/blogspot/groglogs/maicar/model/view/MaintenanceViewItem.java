package com.blogspot.groglogs.maicar.model.view;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.model.entity.MaintenanceTypeEnum;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MaintenanceViewItem {

    private long id;
    private int km;
    private double price;
    private LocalDate date;
    private MaintenanceTypeEnum maintenanceType;
    private String notes;

    public int getKmIconResId(){
        return R.drawable.ic_odometer_24dp;
    }

    public int getPriceIconResId() {
        return R.drawable.ic_euro_price_24dp;
    }

    public int getTypeIconResId(){
        return MaintenanceTypeEnum.getEnumIcon(this.maintenanceType);
    }

    public String getTypeText(){
        return this.maintenanceType.toString();
    }
}
