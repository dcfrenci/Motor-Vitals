package com.motorvitals.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.motorvitals.R;
import com.motorvitals.adapter.MotorcycleDetailElementRecyclerViewAdapter;
import com.motorvitals.adapter.MotorcycleDetailRecyclerViewAdapter;
import com.motorvitals.adapter.RecyclerViewInterface;
import com.motorvitals.classes.Element;
import com.motorvitals.classes.ElementList;
import com.motorvitals.classes.Motorcycle;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MotorcycleDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MotorcycleDetailFragment extends Fragment implements RecyclerViewInterface, DataPassingInterface {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View view;
    private Motorcycle motorcycle;

    public MotorcycleDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MotorcycleDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MotorcycleDetailFragment newInstance(String param1, String param2) {
        MotorcycleDetailFragment fragment = new MotorcycleDetailFragment();
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
        }

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                getParentFragmentManager().popBackStack();
            }
        };

        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_motorcycle_detail, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            motorcycle = bundle.getParcelable("motorcycle");
            setUpMotorcycleDetailModels(/*view*/);
        }
        return view;
    }
    private void setUpMotorcycleDetailModels(/*View view*/) {
        ImageView motorcycleImage = view.findViewById(R.id.detail_motorcycle_image_view);
        TextView title = view.findViewById(R.id.motorcycle_title_text_view);
        TextView description = view.findViewById(R.id.motorcycle_description_text_view);

        title.setText(motorcycle.getName());
        description.setText(motorcycle.getDescription());

        RecyclerView recyclerView = view.findViewById(R.id.motorcycle_detail_recycler_view);
        MotorcycleDetailRecyclerViewAdapter adapter = new MotorcycleDetailRecyclerViewAdapter(this, this, motorcycle.getElementList());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onCardClick(int position, int positionElement) {
        MotorcycleDetailElementFragment fragment = new MotorcycleDetailElementFragment();
        Bundle bundle = new Bundle();
        ElementList element = motorcycle.getElementList().get(position);
        bundle.putParcelable("elementList", element);
        fragment.setArguments(bundle);
        fragment.setDataPassingInterface(this, position, positionElement);
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void passingObject(Object object, int position) {
        if (object instanceof ElementList) {
            Log.d("messPrima", motorcycle.getElementList().get(position).getTitle());
            motorcycle.setOneElementList((ElementList) object, position);
            Log.d("messDopo", motorcycle.getElementList().get(position).getTitle());
            Log.d("mess", "salvando?");
            setUpMotorcycleDetailModels();
        }
    }
}