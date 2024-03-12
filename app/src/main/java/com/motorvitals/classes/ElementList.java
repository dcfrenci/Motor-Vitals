package com.motorvitals.classes;

import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.motorvitals.adapter.MotorcycleDetailElementRecyclerViewAdapter;

import java.util.ArrayList;

public class ElementList {
    String title;
    ArrayList<Element> elements;

    public ElementList(String title) {
        this.title = title;
        this.elements = new ArrayList<>();
    }

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

    public TextView getTitleView(TextView textView) {
        textView.setText(getTitle());
        return textView;
    }

    public RecyclerView getElementsRecyclerView(RecyclerView recyclerView) {
        MotorcycleDetailElementRecyclerViewAdapter adapter = new MotorcycleDetailElementRecyclerViewAdapter(getElements());
        recyclerView.setAdapter(adapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return recyclerView;
    }
}
