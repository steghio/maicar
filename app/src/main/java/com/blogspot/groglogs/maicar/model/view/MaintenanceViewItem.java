package com.blogspot.groglogs.maicar.model.view;

import android.os.Parcel;
import android.os.Parcelable;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.activity.CreateDocumentActivity;
import com.blogspot.groglogs.maicar.model.entity.MaintenanceTypeEnum;
import com.blogspot.groglogs.maicar.util.DateUtils;
import com.blogspot.groglogs.maicar.util.StringUtils;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class MaintenanceViewItem extends AbstractViewItem {

    //todo maybe better way than this for csv null values
    private static final String NULL_PLACEHOLDER = "#null#";

    private Long id;
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

    public MaintenanceTypeEnum getMaintenanceType(){
        return this.maintenanceType == null ? MaintenanceTypeEnum.OTHER : this.maintenanceType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(km);
        dest.writeDouble(price);
        dest.writeLong(date.toEpochDay());
        dest.writeString(maintenanceType.name());
        dest.writeString(notes);
    }

    protected MaintenanceViewItem(Parcel in) {
        id = in.readLong();
        km = in.readInt();
        price = in.readDouble();
        date = LocalDate.ofEpochDay(in.readLong());
        maintenanceType = MaintenanceTypeEnum.valueOf(in.readString());
        notes = in.readString();
    }

    public static final Parcelable.Creator<MaintenanceViewItem> CREATOR = new Parcelable.Creator<>() {
        @Override
        public MaintenanceViewItem createFromParcel(Parcel source) {
            return new MaintenanceViewItem(source);
        }

        @Override
        public MaintenanceViewItem[] newArray(int size) {
            return new MaintenanceViewItem[size];
        }
    };

    public String toCsv(){
        return id + CreateDocumentActivity.CSV_SEPARATOR +
                km + CreateDocumentActivity.CSV_SEPARATOR +
                price + CreateDocumentActivity.CSV_SEPARATOR +
                date.toString() + CreateDocumentActivity.CSV_SEPARATOR +
                maintenanceType.name() + CreateDocumentActivity.CSV_SEPARATOR +
                (StringUtils.isBlank(notes) ? NULL_PLACEHOLDER : notes) + "\n";
    }

    public static MaintenanceViewItem fromCsv(String csv){
        String[] split = csv.split(CreateDocumentActivity.CSV_SEPARATOR);

        return new MaintenanceViewItem(null,
                Integer.parseInt(split[1]),
                Double.parseDouble(split[2]),
                DateUtils.fromString(split[3]),
                MaintenanceTypeEnum.valueOf(split[4]),
                NULL_PLACEHOLDER.equals(split[5]) ? "" : split[5]);
    }
}
