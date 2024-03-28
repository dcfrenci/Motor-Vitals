package com.motorvitals.classes;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

public class Element implements Parcelable {
    private String name;
    private String description;
    private Image photo;
    private double price;
    private Boolean state;
    private HashMap<String, Integer> dayInterval;
    private Date lastServiceDate;
    private HashMap<String, Integer> kmInterval;
    private Integer lastServiceKm;
    private Integer currentKm;

    public Element(String name, Boolean state) {
        this.name = name;
        this.state = state;
        this.dayInterval = basicMap();
        this.kmInterval = basicMap();
        this.lastServiceDate = new Date();
    }

    public Element() {
        this.name = "";
        this.state = false;
        this.dayInterval = basicMap();
        this.kmInterval = basicMap();
        this.lastServiceDate = new Date();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Image getPhoto() {
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Boolean getState() {
        return state;
    }

    public void setState(Boolean state) {
        this.state = state;
    }

    public Integer getCurrentKm() {
        return currentKm;
    }

    public void setCurrentKm(Integer currentKm) {
        this.currentKm = currentKm;
    }

    public HashMap<String, Integer> getDayInterval() {
        return dayInterval;
    }

    public void setDayInterval(HashMap<String, Integer> dayInterval) {
        this.dayInterval = dayInterval;
    }

    public Date getLastServiceDate() {
        if (lastServiceDate == null) {
            return new Date();
        }
        return lastServiceDate;
    }

    public void setLastServiceDate(Date lastServiceDate) {
        this.lastServiceDate = lastServiceDate;
    }

    public HashMap<String, Integer> getKmInterval() {
        return kmInterval;
    }

    public void setKmInterval(HashMap<String, Integer> kmInterval) {
        this.kmInterval = kmInterval;
    }

    public Integer getLastServiceKm() {
        if (lastServiceKm == null) {
            return 0;
        }
        return lastServiceKm;
    }

    public void setLastServiceKm(Integer lastServiceKm) {
        this.lastServiceKm = lastServiceKm;
    }

    public TextView getTextView(TextView textView) {
        textView.setText(getName());
        return textView;
    }

    public ImageView getImageView(ImageView imageView) {
        return imageView;
    }

    public TextView getDaysInterval(TextView textView) {
        String text = "Last service: " + getNumberDays() + " d";
        textView.setText(text);
        return textView;
    }

    public int getNumberDays() {
        Period period = Period.between(getLastServiceDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), LocalDate.now());
        return period.getDays();
    }

    public TextView getKmInterval(TextView textView) {
        String text = "Last service: " + getLastServiceKm() + " Km";
        textView.setText(text);
        return textView;
    }

    public int getNumberKm() {
        return getLastServiceKm();
    }

    private HashMap<String, Integer> basicMap() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("min", 0);
        map.put("med", 100);
        map.put("max", 200);
        return map;
    }

//  ---------------------- Implementation Parcelable ----------------------
    protected Element(Parcel in) {
        name = in.readString();
        description = in.readString();
        price = in.readDouble();
        byte tmpState = in.readByte();
        state = tmpState == 0 ? null : tmpState == 1;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeByte((byte) (state == null ? 0 : state ? 1 : 2));
    }

    public static final Creator<Element> CREATOR = new Creator<Element>() {
        @Override
        public Element createFromParcel(Parcel in) {
            return new Element(in);
        }

        @Override
        public Element[] newArray(int size) {
            return new Element[size];
        }
    };
}
