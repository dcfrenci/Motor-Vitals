package com.motorvitals.classes;

import android.media.Image;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Element {
    private String name;
    private String description;
    private Image photo;
    private double price;
    private Boolean state;
    private HashMap<String, Integer> dayInterval;
    private Date lastServiceDate;
    private HashMap<String, Integer> kmInterval;
    private Date lastServiceKm;

    public Element(String name, Boolean state) {
        this.name = name;
        this.state = state;
        this.dayInterval = basicMap();
        this.kmInterval = basicMap();
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

    public HashMap<String, Integer> getDayInterval() {
        return dayInterval;
    }

    public void setDayInterval(HashMap<String, Integer> dayInterval) {
        this.dayInterval = dayInterval;
    }

    public Date getLastServiceDate() {
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

    public Date getLastServiceKm() {
        return lastServiceKm;
    }

    public void setLastServiceKm(Date lastServiceKm) {
        this.lastServiceKm = lastServiceKm;
    }

    public TextView getTextView(TextView textView) {
        textView.setText(getName());
        return textView;
    }

    public ImageView getImageView(ImageView imageView) {
        return imageView;
    }

    private HashMap<String, Integer> basicMap() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put("min", 0);
        map.put("med", 100);
        map.put("max", 200);
        return map;
    }
}
