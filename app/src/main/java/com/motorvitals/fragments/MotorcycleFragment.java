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
import com.motorvitals.MainActivity;
import com.motorvitals.R;
import com.motorvitals.adapter.MotorcycleRecycleViewAdapter;
import com.motorvitals.adapter.RecyclerViewInterface;
import com.motorvitals.classes.Element;
import com.motorvitals.classes.ElementList;
import com.motorvitals.classes.Motorcycle;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MotorcycleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MotorcycleFragment extends Fragment implements RecyclerViewInterface, DataPassingInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static String mParam1;
    private static String mParam2;
    private View view;
    private ArrayList<Motorcycle> motorcycles = new ArrayList<>();

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            motorcycles = getArguments().getParcelableArrayList("motorcycles");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_motorcycle, container, false);
        // Load the recycler view

        RecyclerView recyclerView = view.findViewById(R.id.motorcycleRecyclerView);
        MotorcycleRecycleViewAdapter adapter = new MotorcycleRecycleViewAdapter(this, this, motorcycles);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        view.findViewById(R.id.floating_button_motorcycle).setOnClickListener(click -> {
            onCardClick(RecyclerView.NO_POSITION, RecyclerView.NO_POSITION);
        });

        view.findViewById(R.id.motorcycleRecyclerView).setOnLongClickListener(click -> {
            view.findViewById(R.id.cardMotorcycleCancel).setVisibility(View.VISIBLE);
            return true;
        });
        return view;
    }

    @Override
    public void onCardClick(int position, int positionElement) {
        MotorcycleDetailFragment fragment = new MotorcycleDetailFragment();
        Bundle bundle = new Bundle();
        if (position == RecyclerView.NO_POSITION) {
            motorcycles.add(new Motorcycle());
            position = motorcycles.size() - 1;
        }
        bundle.putParcelable("motorcycle", motorcycles.get(position));
        fragment.setArguments(bundle);
        fragment.setDataPassingInterface(this, position, position);
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void passingObject(Object object, int position) {
        if (object instanceof Motorcycle) {
            motorcycles.set(position, (Motorcycle) object);
        }
    }
}