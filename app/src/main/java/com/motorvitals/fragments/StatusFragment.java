package com.motorvitals.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.motorvitals.R;
import com.motorvitals.adapter.StatusRecyclerViewAdapter;
import com.motorvitals.classes.Motorcycle;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatusFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatusFragment extends Fragment {

    // the fragment initialization parameters
    private static final String MOTORCYCLES = "motorcycles";
    private ArrayList<Motorcycle> motorcycles;
    private ArrayList<Motorcycle> motorcyclesWithState;

    public StatusFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param motorcycles ArrayList of motorcycles.
     * @return A new instance of fragment StatusFragment.
     */
    public static StatusFragment newInstance(ArrayList<Motorcycle> motorcycles) {
        StatusFragment fragment = new StatusFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(MOTORCYCLES, motorcycles);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            motorcycles = getArguments().getParcelableArrayList(MOTORCYCLES);
            motorcyclesWithState = motorcycles.stream().filter(Motorcycle::hasElementsWithStatus).collect(Collectors.toCollection(ArrayList::new));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.status_recycler_view);
        StatusRecyclerViewAdapter adapter = new StatusRecyclerViewAdapter(motorcyclesWithState, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        return view;
    }
}