package com.motorvitals.classes;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.motorvitals.adapter.StatusElementRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Motorcycle implements Motor, Parcelable {
    private ArrayList<ElementList> elementList;
    private String name;
    private Integer km;
    private String description;
    private Image photo;

    public Motorcycle() {
        this.name = "";
        this.km = 0;
        this.elementList = new ArrayList<>();
    }
    public Motorcycle(String name, Integer km) {
        this.name = name;
        this.km = km;
    }

    public Motorcycle(ArrayList<ElementList> elementList, String name, Integer km) {
        this.elementList = elementList;
        this.name = name;
        setKm(km);
    }

    public ArrayList<ElementList> getElementList() {
        return elementList;
    }

    public void setElementList(ArrayList<ElementList> elementList) {
        this.elementList = elementList;
    }

    public void setOneElementList(ElementList elementList, Integer index) {
        ArrayList<ElementList> arrayList = getElementList();
        arrayList.set(index, elementList);
        setElementList(arrayList);
    }

    public void addOneElementList(ElementList elementList) {
        ArrayList<ElementList> arrayList = getElementList();
        arrayList.add(elementList);
        setElementList(arrayList);
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
        getElementList().forEach(list -> list.getElements().forEach(elem -> elem.setCurrentKm(km)));
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

    public RecyclerView getElementsWithStatusRecyclerView(RecyclerView recyclerView, Fragment fragment, Integer motorcyclePosition) {
        ArrayList<Element> filtered = new ArrayList<>();
        for (ElementList list : getElementList()) {
            filtered.addAll(getElementsWithState(list));
        }
        StatusElementRecyclerViewAdapter adapter = new StatusElementRecyclerViewAdapter(fragment, filtered, getElementList(), motorcyclePosition);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragment.getContext()));
        return recyclerView;
    }

    @Override
    public ArrayList<Element> getElementsWithState(ElementList arrayList) {
        return arrayList.getElements().stream().filter(Element::getState).collect(Collectors.toCollection(ArrayList::new));
    }

//  ---------------------- Implementation Parcelable ----------------------
    protected Motorcycle(Parcel in) {
        elementList = in.createTypedArrayList(ElementList.CREATOR);
        name = in.readString();
        if (in.readByte() == 0) {
            km = null;
        } else {
            km = in.readInt();
        }
        description = in.readString();
    }

    public static final Creator<Motorcycle> CREATOR = new Creator<Motorcycle>() {
        @Override
        public Motorcycle createFromParcel(Parcel in) {
            return new Motorcycle(in);
        }

        @Override
        public Motorcycle[] newArray(int size) {
            return new Motorcycle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeTypedList(elementList);
        dest.writeString(name);
        if (km == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(km);
        }
        dest.writeString(description);
    }
}
