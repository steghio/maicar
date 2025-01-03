package com.blogspot.groglogs.maicar.model.view;

import android.os.Parcel;
import android.os.Parcelable;

import com.blogspot.groglogs.maicar.R;
import com.blogspot.groglogs.maicar.activity.CreateDocumentActivity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class FuelViewItem extends AbstractViewItem {

    private Long id;
    private int km;
    private double liters;
    private double price;
    private double pricePerLiter;
    private boolean full;
    private LocalDate date;

    public FuelViewItem(Long id, int km, double liters, double price, boolean full, LocalDate date){
        this.id = id;
        this.km = km;
        this.liters = liters;
        this.price = price;
        this.full = full;
        this.date = date;
    }

    public double getPricePerLiter(){
        return this.liters > 0 ? this.price / this.liters : 0;
    }

    public int getKmIconResId(){
        return R.drawable.ic_odometer_24dp;
    }

    public int getFuelIconResId() {
        return this.full ? R.drawable.ic_droplet_24dp : R.drawable.ic_droplet_half_24dp;
    }

    public int getPriceIconResId() {
        return R.drawable.ic_euro_price_24dp;
    }

    // Parcelable implementation
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeInt(km);
        dest.writeDouble(liters);
        dest.writeDouble(price);
        dest.writeDouble(pricePerLiter);
        dest.writeBoolean(full);
        dest.writeLong(date.toEpochDay());
    }

    protected FuelViewItem(Parcel in) {
        id = in.readLong();
        km = in.readInt();
        liters = in.readDouble();
        price = in.readDouble();
        pricePerLiter = in.readDouble();
        full = in.readBoolean();
        date = LocalDate.ofEpochDay(in.readLong());
    }

    public static final Parcelable.Creator<FuelViewItem> CREATOR = new Parcelable.Creator<FuelViewItem>() {
        @Override
        public FuelViewItem createFromParcel(Parcel source) {
            return new FuelViewItem(source);
        }

        @Override
        public FuelViewItem[] newArray(int size) {
            return new FuelViewItem[size];
        }
    };

    public String toCsv(){
        return id + CreateDocumentActivity.CSV_SEPARATOR +
                km + CreateDocumentActivity.CSV_SEPARATOR +
                liters + CreateDocumentActivity.CSV_SEPARATOR +
                price + CreateDocumentActivity.CSV_SEPARATOR +
                pricePerLiter + CreateDocumentActivity.CSV_SEPARATOR +
                full + CreateDocumentActivity.CSV_SEPARATOR +
                date.toEpochDay();
    }

    public static FuelViewItem fromCsv(String csv){
        String[] split = csv.split(CreateDocumentActivity.CSV_SEPARATOR);

        FuelViewItem i = new FuelViewItem(null,
                Integer.parseInt(split[1]),
                Double.parseDouble(split[2]),
                Double.parseDouble(split[3]),
                Boolean.parseBoolean(split[5]),
                LocalDate.ofEpochDay(Long.parseLong(split[6]))
        );

        if(i.getLiters() > 0) {
            i.setPricePerLiter(i.getPrice() / i.getLiters());
        }

        return i;
    }
}
