package com.motorvitals.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.motorvitals.R;
import com.motorvitals.classes.Element;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MotorcycleDetailElementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MotorcycleDetailElementFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Element element;

    public MotorcycleDetailElementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MotorcycleDetailElementFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MotorcycleDetailElementFragment newInstance(String param1, String param2) {
        MotorcycleDetailElementFragment fragment = new MotorcycleDetailElementFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_motorcycle_detail_element, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            element = bundle.getParcelable("element");
            setUpMotorcycleDetailElementModel(view);
        }
        Switch showNotification = view.findViewById(R.id.element_show_notification_switch);
        showNotification.setOnClickListener(click -> {
            element.setState(!element.getState());
            showNotification.setChecked(element.getState());
            if (element.getState()) {
                view.findViewById(R.id.notification_container).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.notification_container).setVisibility(View.GONE);
            }
        });
        return view;
    }

    private void setUpMotorcycleDetailElementModel(View view) {
        TextView title = view.findViewById(R.id.element_title);
        TextView description = view.findViewById(R.id.element_description);
        TextView price = view.findViewById(R.id.element_price);
        Switch notificationSwitch = view.findViewById(R.id.element_show_notification_switch);
        TextView minDay = view.findViewById(R.id.element_min_date);
        TextView medDay = view.findViewById(R.id.element_med_date);
        TextView maxDay = view.findViewById(R.id.element_max_date);
        TextView lastDay = view.findViewById(R.id.element_last_date);
        TextView minKm = view.findViewById(R.id.element_min_km);
        TextView medKm = view.findViewById(R.id.element_med_km);
        TextView maxKm = view.findViewById(R.id.element_max_km);
        TextView lastKm = view.findViewById(R.id.element_last_km);

        title.setText(element.getName());
        description.setText(element.getDescription());
        price.setText(String.valueOf(element.getPrice()));
        notificationSwitch.setChecked(element.getState());
        if (element.getDayInterval().get("min") != null) {
            minDay.setText(element.getDayInterval().get("min").toString());
        }
        if (element.getDayInterval().get("med") != null) {
            medDay.setText(element.getDayInterval().get("med").toString());
        }
        if (element.getDayInterval().get("max") != null) {
            maxDay.setText(element.getDayInterval().get("max").toString());
        }
        if (element.getLastServiceDate() != null) {
            lastDay.setText(element.getLastServiceDate().toString());
        }
        if (element.getKmInterval().get("min") != null) {
            minKm.setText(element.getKmInterval().get("min").toString());
        }
        if (element.getKmInterval().get("med") != null) {
            medKm.setText(element.getKmInterval().get("med").toString());
        }
        if (element.getKmInterval().get("max") != null) {
            maxKm.setText(element.getKmInterval().get("max").toString());
        }
        if (element.getLastServiceKm() != null) {
            lastKm.setText(element.getLastServiceKm().toString());
        }
    }
}