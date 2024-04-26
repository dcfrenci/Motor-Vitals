package com.motorvitals.fragments;

import android.os.Bundle;
import android.widget.*;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.motorvitals.R;
import com.motorvitals.classes.Element;
import com.motorvitals.classes.ElementList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;

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
    private ElementList elementList;
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
     * @return A new instance of fragment MotorcycleDetailElementFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        Bundle bundle = getArguments();
        if (bundle != null) {
            elementList = bundle.getParcelable("elementList");
            element = elementList.getElement(elementIndex);
            setUpMotorcycleDetailElementModel();
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

        view.findViewById(R.id.element_today_date_button).setOnClickListener(click -> {
            String date = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            EditText dateTextView = view.findViewById(R.id.element_last_date);
            dateTextView.setText(date);
        });

        view.findViewById(R.id.element_back_button).setOnClickListener(click -> {
            onDestroy();
            getParentFragmentManager().popBackStack();
        });

        view.findViewById(R.id.element_check_save).setOnClickListener(click -> {
            existing = false;
            onDestroy();
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
        elementList.setElement(elementIndex, element);
        dataPassingInterface.passingObject(elementList, elementListIndex);
    }

    private void onSave() throws ParseException {
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

        element.setName(title.getText().toString());
        element.setDescription(description.getText().toString());
        element.setPrice(Double.parseDouble(price.getText().toString()));
        if (element.getState() != notificationSwitch.isChecked()) {
            element.setState(notificationSwitch.isChecked());
            HashMap<String, Integer> map = new HashMap<>();
            map.put("min", Integer.valueOf(minDay.getText().toString()));
            map.put("med", Integer.valueOf(medDay.getText().toString()));
            map.put("max", Integer.valueOf(maxDay.getText().toString()));
            element.setDayInterval(map);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ITALIAN);
            element.setLastServiceDate(formatter.parse(lastDay.getText().toString()));
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
        Switch notificationSwitch = view.findViewById(R.id.element_show_notification_switch);
        TextView minDay = view.findViewById(R.id.element_min_date);
        TextView medDay = view.findViewById(R.id.element_med_date);
        TextView maxDay = view.findViewById(R.id.element_max_date);
        TextView lastDay = view.findViewById(R.id.element_last_date);
        TextView minKm = view.findViewById(R.id.element_min_km);
        TextView medKm = view.findViewById(R.id.element_med_km);
        TextView maxKm = view.findViewById(R.id.element_max_km);
        TextView lastKm = view.findViewById(R.id.element_last_km);
        ImageView saveButton = view.findViewById(R.id.element_check_save);

        title.setText(element.getName());
        description.setText(element.getDescription());
        price.setText(String.valueOf(element.getPrice()));
        notificationSwitch.setChecked(element.getState());
        if (element.getState()) {
            view.findViewById(R.id.notification_container).setVisibility(View.VISIBLE);
        }
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
            lastDay.setText(LocalDate.from(element.getLastServiceDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
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
            lastKm.setText(Integer.toString(element.getCurrentKm() - element.getLastServiceKm()));
        }
        if (existing) {
            saveButton.setVisibility(View.VISIBLE);
        }
    }

    public void setDataPassingInterface(DataPassingInterface dataPassingInterface, Integer elementListIndex, Integer elementIndex, Boolean elementNew) {
        this.dataPassingInterface = dataPassingInterface;
        this.elementListIndex = elementListIndex;
        this.elementIndex = elementIndex;
        this.existing = elementNew;
    }
}