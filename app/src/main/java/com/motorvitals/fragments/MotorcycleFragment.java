package com.motorvitals.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.motorvitals.R;
import com.motorvitals.adapter.MotorcycleRecycleViewAdapter;
import com.motorvitals.adapter.RecyclerViewInterface;
import com.motorvitals.classes.Motorcycle;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MotorcycleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MotorcycleFragment extends Fragment implements RecyclerViewInterface, DataPassingInterface {
    // the fragment initialization parameters
    private static final String MOTORCYCLES = "motorcycles";
    private View view;
    private ArrayList<Motorcycle> motorcycles;

    public MotorcycleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param motorcycles ArrayList of motorcycles.
     * @return A new instance of fragment MotorcycleFragment.
     */
    public static MotorcycleFragment newInstance(ArrayList<Motorcycle> motorcycles) {
        MotorcycleFragment fragment = new MotorcycleFragment();
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_motorcycle, container, false);
        setUpMotorcycleModels();

        view.findViewById(R.id.floating_button_motorcycle).setOnClickListener(click -> onCardClick(RecyclerView.NO_POSITION, RecyclerView.NO_POSITION));
        return view;
    }

    private void setUpMotorcycleModels() {
        RecyclerView recyclerView = view.findViewById(R.id.motorcycleRecyclerView);
        MotorcycleRecycleViewAdapter adapter = new MotorcycleRecycleViewAdapter(this, this, motorcycles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onCardClick(int position, int positionElement) {
        Motorcycle motorcycle = position == RecyclerView.NO_POSITION ? new Motorcycle() : motorcycles.get(position);
        Boolean motorcycleExisting = position == RecyclerView.NO_POSITION;
        MotorcycleDetailFragment fragment = MotorcycleDetailFragment.newInstance(motorcycle, position, motorcycleExisting);
        fragment.setDataPassingInterface(this);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onCardDelete(int listIndex, int index) {
        if (index <= motorcycles.size()) {
            motorcycles.remove(index);
            setUpMotorcycleModels();
        }
    }

    @Override
    public void passingObject(Object object, int listIndex, int index) {
        if (object instanceof Motorcycle) {
            if (index == RecyclerView.NO_POSITION) {
                motorcycles.add((Motorcycle) object);
            } else {
                motorcycles.set(index, (Motorcycle) object);
            }
        }
    }
}