package com.motorvitals.fragments;

import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.motorvitals.R;
import com.motorvitals.adapter.MotorcycleRecycleViewAdapter;
import com.motorvitals.classes.Element;
import com.motorvitals.classes.Motorcycle;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MotorcycleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MotorcycleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    /*private static final String mParam1;
    private static final String mParam2;*/
    private ArrayList<Motorcycle> motorcycles = new ArrayList<>();
    private RecyclerView recyclerView;

    public MotorcycleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MotorcycleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MotorcycleFragment newInstance(String param1, String param2) {
        MotorcycleFragment fragment = new MotorcycleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    /*if (getArguments() != null) {
        mParam1 = getArguments().getString(ARG_PARAM1);
        mParam2 = getArguments().getString(ARG_PARAM2);
    }*/

        setUpMotorcycleModels();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_motorcycle, container, false);
        // Load the recycler view
        recyclerView = view.findViewById(R.id.motorcycleRecyclerView);
        MotorcycleRecycleViewAdapter adapter = new MotorcycleRecycleViewAdapter(this, motorcycles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }


    private void setUpMotorcycleModels() {
        //load motorcycle from saved data
        List<Element> elementList = new ArrayList<>();

        for (int i = 0; i < 15; i++){
            String name = "moto_" + i;
            Motorcycle moto = new Motorcycle(elementList, name, 15000);
            moto.setDescription("Descrizione di " + name);
            motorcycles.add(moto);
        }
        //set up motorcycle arraylist

        //android:src="@tools:sample/avatars"
    }
}