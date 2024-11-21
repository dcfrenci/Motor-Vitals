package com.motorvitals.fragments;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.motorvitals.R;
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

    // the fragment initialization parameters
    private static final String MOTORCYCLE = "motorcycle";
    private static final String MOTORCYCLE_INDEX = "motorcycleIndex";
    private static final String MOTORCYCLE_EXISTING = "motorcycleExisting";
    private View view;
    private DataPassingInterface dataPassingInterface;
    private Motorcycle motorcycle;
    private Integer motorcycleIndex;
    private Boolean motorcycleExisting;
    private ActivityResultLauncher<Intent> imagePickerLauncher;

    public MotorcycleDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param motorcycle Motorcycle.
     * @param motorcycleIndex Index of the motorcycle.
     * @param motorcycleExisting True if Motorcycle is new.
     * @return A new instance of fragment MotorcycleDetailFragment.
     */
    public static MotorcycleDetailFragment newInstance(Motorcycle motorcycle, Integer motorcycleIndex, Boolean motorcycleExisting) {
        MotorcycleDetailFragment fragment = new MotorcycleDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOTORCYCLE, motorcycle);
        args.putInt(MOTORCYCLE_INDEX, motorcycleIndex);
        args.putBoolean(MOTORCYCLE_EXISTING, motorcycleExisting);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            motorcycle = getArguments().getParcelable(MOTORCYCLE);
            motorcycleIndex = getArguments().getInt(MOTORCYCLE_INDEX);
            motorcycleExisting = getArguments().getBoolean(MOTORCYCLE_EXISTING);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_motorcycle_detail, container, false);
        setUpMotorcycleDetailModels();
        initImagePickerLauncher();

        view.findViewById(R.id.floating_detail_button_motorcycle).setOnClickListener(click -> {
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(container.getContext());
            View dialogView = LayoutInflater.from(container.getContext()).inflate(R.layout.motorcycle_detail_list_menu, container, false);
            bottomSheetDialog.setContentView(dialogView);
            bottomSheetDialog.show();

            TextView listName = dialogView.findViewById(R.id.list_menu_edit_text);
            Button addButton = dialogView.findViewById(R.id.list_menu_add_button);

            addButton.setOnClickListener(addList -> {
                if (listName.getText().toString().isEmpty()) {
                    listName.setError("Please type the list name");
                } else {
                    onSave();
                    ElementList elementList = new ElementList(listName.getText().toString());
                    passingObject(elementList, RecyclerView.NO_POSITION, RecyclerView.NO_POSITION);
                    bottomSheetDialog.dismiss();
                }
            });
        });

        view.findViewById(R.id.detail_motorcycle_image_view).setOnClickListener(click -> {
            openImagePicker();
        });

        view.findViewById(R.id.detail_motorcycle_back_button).setOnClickListener(click -> {
            getParentFragmentManager().popBackStack();
        });

        view.findViewById(R.id.detail_motorcycle_check_save).setOnClickListener(click -> {
            motorcycleExisting = false;
            view.findViewById(R.id.detail_motorcycle_check_save).setVisibility(View.GONE);
            view.findViewById(R.id.floating_detail_button_motorcycle).setVisibility(View.VISIBLE);
            view.findViewById(R.id.floating_detail_image_motorcycle).setVisibility(View.VISIBLE);
        });
        return view;
    }

    private void initImagePickerLauncher() {
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri selectedImageUri = result.getData().getData();
                        if (selectedImageUri != null) {
                            ImageView imageView = view.findViewById(R.id.detail_motorcycle_image_view);
                            imageView.setImageURI(selectedImageUri);
                            motorcycle.setPhoto(getRealPathFromURI(selectedImageUri));
                        }
                    }
                }
        );
    }

    private String getRealPathFromURI(Uri uri) {
        String path = null;
        // Se l'URI è un URI di tipo "content://"
        if (uri.getScheme().equals("content")) {
            // Controlla se il contenuto è nel MediaStore (galleria immagini)
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                path = cursor.getString(columnIndex);
                cursor.close();
            }
        }
        // Se l'URI è un URI di tipo "file://"
        else if (uri.getScheme().equals("file")) {
            path = uri.getPath();
        }
        return path;
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        imagePickerLauncher.launch(intent);
    }

    private void setUpMotorcycleDetailModels() {
        ImageView motorcycleImage = view.findViewById(R.id.detail_motorcycle_image_view);
        TextView title = view.findViewById(R.id.detail_motorcycle_title_view);
        TextView description = view.findViewById(R.id.motorcycle_description_text_view);
        TextView km = view.findViewById(R.id.motorcycle_km_text_view);

        motorcycleImage.setImageURI(motorcycle.getPhotoUri());
        title.setText(motorcycle.getName());
        description.setText(motorcycle.getDescription());
        km.setText(motorcycle.getKm() == 0 ? "" : motorcycle.getKm().toString());
        if (motorcycleExisting) {
            view.findViewById(R.id.detail_motorcycle_check_save).setVisibility(View.VISIBLE);
            view.findViewById(R.id.floating_detail_button_motorcycle).setVisibility(View.GONE);
            view.findViewById(R.id.floating_detail_image_motorcycle).setVisibility(View.GONE);
        }

        RecyclerView recyclerView = view.findViewById(R.id.motorcycle_detail_recycler_view);
        MotorcycleDetailRecyclerViewAdapter adapter = new MotorcycleDetailRecyclerViewAdapter(this, this, motorcycle.getElementList());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void onSave() {
        TextView title = view.findViewById(R.id.detail_motorcycle_title_view);
        TextView description = view.findViewById(R.id.motorcycle_description_text_view);
        TextView km = view.findViewById(R.id.motorcycle_km_text_view);

        motorcycle.setName(title.getText().toString());
        motorcycle.setDescription(description.getText().toString());
        motorcycle.setKm(Integer.valueOf(km.getText().toString().isEmpty() ? "0" : km.getText().toString()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (motorcycleExisting) {
            return;
        }
        onSave();
        dataPassingInterface.passingObject(motorcycle, RecyclerView.NO_POSITION, motorcycleIndex);
    }

    @Override
    public void onCardClick(int position, int positionElement) {
        Element element = positionElement == RecyclerView.NO_POSITION ? new Element(motorcycle.getKm()) : motorcycle.getElementList().get(position).getElement(positionElement);
        Boolean existing = positionElement == RecyclerView.NO_POSITION;
        MotorcycleDetailElementFragment fragment = MotorcycleDetailElementFragment.newInstance(element, position, positionElement, existing);
        fragment.setDataPassingInterface(this);

        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onCardDelete(int listIndex, int index) {
        if (listIndex <= motorcycle.getElementList().size()) {
            if (index <= motorcycle.getElementList().get(listIndex).getElements().size()) {
                motorcycle.getElementList().get(listIndex).getElements().remove(index);
                setUpMotorcycleDetailModels();
            }
        }
    }

    @Override
    public void passingObject(Object object, int listIndex, int index) {
        if (object instanceof Element) {
            if (index == RecyclerView.NO_POSITION) {
                //New element
                motorcycle.getElementList().get(listIndex).getElements().add((Element) object);
            } else {
                //Update element
                motorcycle.getElementList().get(listIndex).getElements().set(index, (Element) object);
            }
        }
        if (object instanceof ElementList) {
            if (index == RecyclerView.NO_POSITION) {
                //New ElementList
                motorcycle.getElementList().add((ElementList) object);
                setUpMotorcycleDetailModels();
            }
        }
    }

    public void setDataPassingInterface(DataPassingInterface dataPassingInterface) {
        this.dataPassingInterface = dataPassingInterface;
    }
}