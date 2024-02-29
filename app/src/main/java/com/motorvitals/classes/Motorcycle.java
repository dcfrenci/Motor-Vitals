package com.motorvitals.classes;

import android.media.Image;

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

    @Override
    public List<Element> filterWithState(List<Element> list) {
        return list.stream().filter(Element::getState).collect(Collectors.toList());
    }
}
