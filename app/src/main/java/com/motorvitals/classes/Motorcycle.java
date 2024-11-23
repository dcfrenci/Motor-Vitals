package com.motorvitals.classes;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.motorvitals.adapter.StatusElementRecyclerViewAdapter;
import com.motorvitals.jackson.MotorcycleDeserializer;
import com.motorvitals.jackson.MotorcycleSerializer;

import java.io.File;
import java.util.ArrayList;
import java.util.stream.Collectors;

@JsonSerialize(using = MotorcycleSerializer.class)
@JsonDeserialize(using = MotorcycleDeserializer.class)
public class Motorcycle implements Motor, Parcelable {
    private String name;
    private Integer km;
    private String description;
    private String photo;
    private ArrayList<ElementList> elementList;

    public Motorcycle() {
        this.name = "";
        this.km = 0;
        this.elementList = new ArrayList<>();
    }
    public Motorcycle(String name, Integer km) {
        this.name = name;
        this.km = km;
    }

    public Motorcycle(ArrayList<ElementList> elementList, String name, String description, Integer km, String photo) {
        this.elementList = elementList;
        this.name = name;
        this.description = description;
        this.photo = photo;
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

    public Uri getPhotoUri() {
        File file = new File(getPhoto());
        return Uri.fromFile(file);
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
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
        if (getPhoto() != null && getPhotoUri() != null)
            imageView.setImageURI(getPhotoUri());
        return imageView;
    }

    public boolean hasElementsWithStatus() {
        if (getElementList().isEmpty()) {
            return false;
        }
        for (ElementList elementList : getElementList()){
            if (elementList.getElements().stream().noneMatch(Element::getState)) {
                return false;
            }
        }
        return true;
    }

    public RecyclerView getElementsWithStatusRecyclerView(RecyclerView recyclerView, Fragment fragment, Integer motorcyclePosition, User user) {
        ArrayList<Element> filtered = new ArrayList<>();
        for (ElementList list : getElementList()) {
            filtered.addAll(getElementsWithState(list));
        }
        StatusElementRecyclerViewAdapter adapter = new StatusElementRecyclerViewAdapter(fragment, filtered, getElementList(), motorcyclePosition, user);
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
