package com.motorvitals.fragments;

import android.os.Bundle;
import android.widget.*;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.motorvitals.R;
import com.motorvitals.classes.Element;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MotorcycleDetailElementFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MotorcycleDetailElementFragment extends Fragment {

    // the fragment initialization parameters
    private static final String ELEMENT = "element";
    private static final String LIST_INDEX = "listIndex";
    private static final String ELEMENT_INDEX = "elementIndex";
    private static final String EXISTING = "existing";
    private View view;
    private DataPassingInterface dataPassingInterface;
    private Element element;
    private Integer elementListIndex;
    private Integer elementIndex;
    private Boolean existing;

    public MotorcycleDetailElementFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param element Element
     * @param elementListIndex Index of the list containing the element.
     * @param elementIndex Index of the element in the list.
     * @param existing True if Element is new.
     * @return A new instance of fragment MotorcycleDetailElementFragment.
     */
    public static MotorcycleDetailElementFragment newInstance(Element element, Integer elementListIndex, Integer elementIndex, Boolean existing) {
        MotorcycleDetailElementFragment fragment = new MotorcycleDetailElementFragment();
        Bundle args = new Bundle();
        args.putParcelable(ELEMENT, element);
        args.putInt(LIST_INDEX, elementListIndex);
        args.putInt(ELEMENT_INDEX, elementIndex);
        args.putBoolean(EXISTING, existing);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            element = getArguments().getParcelable(ELEMENT);
            elementListIndex = getArguments().getInt(LIST_INDEX);
            elementIndex = getArguments().getInt(ELEMENT_INDEX);
            existing = getArguments().getBoolean(EXISTING);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_motorcycle_detail_element, container, false);
        setUpMotorcycleDetailElementModel();

        SwitchMaterial showNotification = view.findViewById(R.id.element_show_notification_switch);
        showNotification.setOnClickListener(click -> {
            element.setState(!element.getState());
            showNotification.setChecked(element.getState());
            if (element.getState()) {
                view.findViewById(R.id.notification_container).setVisibility(View.VISIBLE);
            } else {
                view.findViewById(R.id.notification_container).setVisibility(View.GONE);
            }
        });

        view.findViewById(R.id.element_today_date_button).setOnClickListener(click -> {
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            EditText dateTextView = view.findViewById(R.id.element_last_date);
            dateTextView.setText(date);
        });

        view.findViewById(R.id.element_back_button).setOnClickListener(click -> {
            getParentFragmentManager().popBackStack();
        });

        view.findViewById(R.id.element_check_save).setOnClickListener(click -> {
            existing = false;
            getParentFragmentManager().popBackStack();
        });
        return view;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (existing) {
            return;
        }
        try {
            onSave();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        dataPassingInterface.passingObject(element, elementListIndex, elementIndex);
    }

    private void onSave() throws ParseException {
        TextView title = view.findViewById(R.id.element_title);
        TextView description = view.findViewById(R.id.element_description);
        TextView price = view.findViewById(R.id.element_price);
        TextView minDay = view.findViewById(R.id.element_min_date);
        TextView medDay = view.findViewById(R.id.element_med_date);
        TextView maxDay = view.findViewById(R.id.element_max_date);
        TextView lastDay = view.findViewById(R.id.element_last_date);
        TextView minKm = view.findViewById(R.id.element_min_km);
        TextView medKm = view.findViewById(R.id.element_med_km);
        TextView maxKm = view.findViewById(R.id.element_max_km);
        TextView lastKm = view.findViewById(R.id.element_last_km);
        SwitchMaterial notificationSwitch = view.findViewById(R.id.element_show_notification_switch);

        element.setName(title.getText().toString());
        element.setDescription(description.getText().toString());
        element.setPrice(Double.parseDouble(Objects.equals(price.getText().toString(), "") ? "0" : price.getText().toString()));
        if (notificationSwitch.isChecked()) {
            element.setState(notificationSwitch.isChecked());
            HashMap<String, Integer> map = new HashMap<>();
            map.put("min", Integer.valueOf(minDay.getText().toString()));
            map.put("med", Integer.valueOf(medDay.getText().toString()));
            map.put("max", Integer.valueOf(maxDay.getText().toString()));
            element.setDayInterval(map);
            LocalDate formatter = LocalDate.parse(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            element.setLastServiceDate(formatter);
            map.put("min", Integer.valueOf(minKm.getText().toString()));
            map.put("med", Integer.valueOf(medKm.getText().toString()));
            map.put("max", Integer.valueOf(maxKm.getText().toString()));
            element.setKmInterval(map);
            element.setLastServiceKm(Integer.parseInt(lastKm.getText().toString()));
        }
    }

    private void setUpMotorcycleDetailElementModel() {
        TextView title = view.findViewById(R.id.element_title);
        TextView description = view.findViewById(R.id.element_description);
        TextView price = view.findViewById(R.id.element_price);
        TextView minDay = view.findViewById(R.id.element_min_date);
        TextView medDay = view.findViewById(R.id.element_med_date);
        TextView maxDay = view.findViewById(R.id.element_max_date);
        TextView lastDay = view.findViewById(R.id.element_last_date);
        TextView minKm = view.findViewById(R.id.element_min_km);
        TextView medKm = view.findViewById(R.id.element_med_km);
        TextView maxKm = view.findViewById(R.id.element_max_km);
        TextView lastKm = view.findViewById(R.id.element_last_km);
        ImageView saveButton = view.findViewById(R.id.element_check_save);
        SwitchMaterial notificationSwitch = view.findViewById(R.id.element_show_notification_switch);

        title.setText(element.getName());
        description.setText(element.getDescription());
        price.setText(element.getPrice() == 0 ? "" : String.valueOf(element.getPrice()));
        notificationSwitch.setChecked(element.getState());
        if (element.getState()) {
            view.findViewById(R.id.notification_container).setVisibility(View.VISIBLE);
        }
        if (element.getDayInterval().get("min") != null) {
            minDay.setText(String.format(Locale.getDefault(), "%d", element.getDayInterval().get("min")));
        }
        if (element.getDayInterval().get("med") != null) {
            medDay.setText(String.format(Locale.getDefault(), "%d", element.getDayInterval().get("med")));
        }
        if (element.getDayInterval().get("max") != null) {
            maxDay.setText(String.format(Locale.getDefault(), "%d", element.getDayInterval().get("max")));
        }
        if (element.getLastServiceDate() != null) {
            lastDay.setText(element.getLastServiceDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        if (element.getKmInterval().get("min") != null) {
            minKm.setText(String.format(Locale.getDefault(), "%d", element.getKmInterval().get("min")));
        }
        if (element.getKmInterval().get("med") != null) {
            medKm.setText(String.format(Locale.getDefault(), "%d", element.getKmInterval().get("med")));
        }
        if (element.getKmInterval().get("max") != null) {
            maxKm.setText(String.format(Locale.getDefault(), "%d", element.getKmInterval().get("max")));
        }
        if (element.getLastServiceKm() != null) {
            lastKm.setText(String.format(Locale.getDefault(), "%d", element.getCurrentKm() - element.getLastServiceKm()));
        }
        if (existing) {
            saveButton.setVisibility(View.VISIBLE);
        }
    }

    public void setDataPassingInterface(DataPassingInterface dataPassingInterface) {
        this.dataPassingInterface = dataPassingInterface;
    }
}