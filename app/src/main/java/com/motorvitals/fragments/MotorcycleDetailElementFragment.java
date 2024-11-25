package com.motorvitals.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.motorvitals.R;
import com.motorvitals.classes.Element;

import java.time.LocalDate;
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
    private ActivityResultLauncher<Intent> imagePickerLauncher;

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
        initImagePickerLauncher();

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
            if (checkIfSave()) {
                getParentFragmentManager().popBackStack();
            }
        });

        view.findViewById(R.id.element_check_save).setOnClickListener(click -> {
            existing = false;
            getParentFragmentManager().popBackStack();
        });

        view.findViewById(R.id.element_image_view).setOnClickListener(click -> openImagePicker());
        return view;
    }
    @Override
    public void onDestroy() {
        if (existing) {
            return;
        }
        try {
            onSave();
        } catch (IllegalArgumentException e){
            return;
        }
        super.onDestroy();
        dataPassingInterface.passingObject(element, elementListIndex, elementIndex);
    }

    /**
     * Check if saving is possible and in that case save all the fragment data.
     */
    private void onSave() {
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

        //Check before saving
        if (!checkIfSave())
            return;

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
            element.setLastServiceDate(LocalDate.parse(lastDay.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            map.put("min", Integer.valueOf(minKm.getText().toString()));
            map.put("med", Integer.valueOf(medKm.getText().toString()));
            map.put("max", Integer.valueOf(maxKm.getText().toString()));
            element.setKmInterval(map);
            element.setLastServiceKm(Integer.parseInt(lastKm.getText().toString()));
        }
    }

    /**
     * Check if saving is possible.
     * @return True is the value of km is correct.
     */
    private boolean checkIfSave() {
        TextView lastKm = view.findViewById(R.id.element_last_km);
        if (Integer.parseInt(lastKm.getText().toString()) > element.getCurrentKm()) {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext()).setTitle("Warning").setMessage("The value set as last Km (" + Integer.parseInt(lastKm.getText().toString()) + ") is more than the actual Km of the vehicle (" + element.getCurrentKm() + ").").create();
            alertDialog.show();
            return false;
        }
        return true;
    }

    /**
     * Load/Update the fragments with the data of the element.
     */
    private void setUpMotorcycleDetailElementModel() {
        TextView title = view.findViewById(R.id.element_title);
        TextView description = view.findViewById(R.id.element_description);
        ImageView elementImage = view.findViewById(R.id.element_image_view);
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

        if (element.getPhoto() != null)
            elementImage.setImageURI(element.getPhotoUri());
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

    /**
     * Load in the fragment the motorcycle image if it exists.
     */
    private void initImagePickerLauncher() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            ImageView imageView = view.findViewById(R.id.element_image_view);
                            imageView.setImageURI(selectedImageUri);
                            element.setPhoto(getRealPathFromURI(selectedImageUri));
                        }
                    }
                }
        );
    }

    /**
     * Returns the Path from the uri passed.
     * @param uri Uri
     * @return The path string or null if it doesn't exist.
     */
    private String getRealPathFromURI(Uri uri) {
        String path = null;
        if (uri.getScheme().equals("content")) {
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = Objects.requireNonNull(getContext()).getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(columnIndex);
                cursor.close();
            }
        }
        else if (uri.getScheme().equals("file")) {
            path = uri.getPath();
        }
        return path;
    }

    /**
     * Let the user choose an image.
     */
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    public void setDataPassingInterface(DataPassingInterface dataPassingInterface) {
        this.dataPassingInterface = dataPassingInterface;
    }
}