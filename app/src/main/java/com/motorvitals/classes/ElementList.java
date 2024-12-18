package com.motorvitals.classes;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.motorvitals.adapter.MotorcycleDetailElementRecyclerViewAdapter;
import com.motorvitals.adapter.RecyclerViewInterface;
import com.motorvitals.jackson.*;

import java.util.ArrayList;

@JsonSerialize(using = ElementListSerializer.class)
@JsonDeserialize(using = ElementListDeserializer.class)
public class ElementList implements Parcelable {
    String title;
    ArrayList<Element> elements;

    public ElementList(String title) {
        this.title = title;
        this.elements = new ArrayList<>();
    }

    @JsonCreator
    public ElementList(String title, ArrayList<Element> elements) {
        this.title = title;
        this.elements = elements;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public Element getElement(int position) {
        return getElements().get(position);
    }

    public TextView getTitleView(TextView textView) {
        textView.setText(getTitle());
        return textView;
    }

    public RecyclerView getElementsRecyclerView(RecyclerView recyclerView, Fragment fragment, int position, RecyclerViewInterface recyclerViewInterface) {
        MotorcycleDetailElementRecyclerViewAdapter adapter = new MotorcycleDetailElementRecyclerViewAdapter(recyclerViewInterface, getElements(), position);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(fragment.getContext()));
        return recyclerView;
    }

//  ---------------------- Implementation Parcelable ----------------------
    protected ElementList(Parcel in) {
        title = in.readString();
        elements = in.createTypedArrayList(Element.CREATOR);
    }

    public static final Creator<ElementList> CREATOR = new Creator<ElementList>() {
        @Override
        public ElementList createFromParcel(Parcel in) {
            return new ElementList(in);
        }

        @Override
        public ElementList[] newArray(int size) {
            return new ElementList[size];
        }
    };
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeTypedList(elements);
    }

}
