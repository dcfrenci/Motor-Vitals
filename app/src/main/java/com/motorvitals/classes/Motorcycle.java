package com.motorvitals.classes;

import android.content.Context;
import android.media.Image;
import android.widget.ImageView;
import android.widget.TextView;
import org.w3c.dom.Text;

import java.util.List;
import java.util.stream.Collectors;

public class Motorcycle implements Motor{
    private List<Element> elementList;
    private String name;
    private Integer km;
    private String description;
    private Image photo;

    public Motorcycle(String name, Integer km) {
        this.name = name;
        this.km = km;
    }

    public Motorcycle(List<Element> elementList, String name, Integer km) {
        this.elementList = elementList;
        this.name = name;
        this.km = km;
    }

    public List<Element> getElementList() {
        return elementList;
    }

    public void setElementList(List<Element> elementList) {
        this.elementList = elementList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getKm() {
        return km;
    }

    public void setKm(Integer km) {
        this.km = km;
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

    public TextView getNameView(TextView textView) {
        textView.setText(getName());
        return textView;
    }

    public TextView getDescriptionView(TextView textView) {
        textView.setText(getDescription());
        return textView;
    }

    public ImageView getPhotoView(ImageView imageView) {
        //need to swap photo with the new one
        return imageView;
    }
    @Override
    public List<Element> filterWithState(List<Element> list) {
        return list.stream().filter(Element::getState).collect(Collectors.toList());
    }
}
