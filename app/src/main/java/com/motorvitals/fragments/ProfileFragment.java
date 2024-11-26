package com.motorvitals.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.motorvitals.R;
import com.motorvitals.classes.Motorcycle;
import com.motorvitals.classes.User;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.*;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private static final String MOTORCYCLE = "motorcycles";
    private static final String USER = "user";
    private ArrayList<Motorcycle> motorcycles;
    private User user;
    private ActivityResultLauncher<Intent> folderPickerLauncher;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @return A new instance of fragment ProfileFragment.
     */
    public static ProfileFragment newInstance(ArrayList<Motorcycle> motorcycle, User user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(MOTORCYCLE, motorcycle);
        args.putParcelable(USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            motorcycles = getArguments().getParcelableArrayList(MOTORCYCLE);
            user = getArguments().getParcelable(USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setUpUser(view);
        loadBackup();

        view.findViewById(R.id.profile_save).setOnClickListener(click -> {
            TextView name = view.findViewById(R.id.profile_user_name);
            if (!name.getText().toString().isEmpty())
                user.setName(name.getText().toString());

            if (updateColor(view, R.id.profile_color_min, R.id.profile_circle_color_min, "min") &&
                    updateColor(view, R.id.profile_color_med, R.id.profile_circle_color_med, "med") &&
                    updateColor(view, R.id.profile_color_max, R.id.profile_circle_color_max, "max")){
                view.findViewById(R.id.profile_save).setVisibility(View.GONE);
                view.findViewById(R.id.profile_edit).setVisibility(View.VISIBLE);

                updateTextViewClickable(view, R.id.profile_user_name, false);
                updateTextViewClickable(view, R.id.profile_color_min, false);
                updateTextViewClickable(view, R.id.profile_color_med, false);
                updateTextViewClickable(view, R.id.profile_color_max, false);
            }
        });

        view.findViewById(R.id.profile_edit).setOnClickListener(click -> {
            view.findViewById(R.id.profile_edit).setVisibility(View.GONE);
            view.findViewById(R.id.profile_save).setVisibility(View.VISIBLE);

            updateTextViewClickable(view, R.id.profile_user_name, true);
            updateTextViewClickable(view, R.id.profile_color_min, true);
            updateTextViewClickable(view, R.id.profile_color_med, true);
            updateTextViewClickable(view, R.id.profile_color_max, true);
        });

        view.findViewById(R.id.profile_backup_button).setOnClickListener(click -> createBackup());

        view.findViewById(R.id.profile_load_backup_button).setOnClickListener(click -> openFolderPicker());
        return view;
    }

    /**
     * Load/Update the fragments with the stored user data if they are present.
     * @param view View
     */
    private void setUpUser(View view) {
        TextView name = view.findViewById(R.id.profile_user_name);
        TextView minColorCode = view.findViewById(R.id.profile_color_min);
        TextView medColorCode = view.findViewById(R.id.profile_color_med);
        TextView maxColorCode = view.findViewById(R.id.profile_color_max);
        ImageView minColorCircle = view.findViewById(R.id.profile_circle_color_min);
        ImageView medColorCircle = view.findViewById(R.id.profile_circle_color_med);
        ImageView maxColorCircle = view.findViewById(R.id.profile_circle_color_max);

        name.setText(user.getName());
        minColorCode.setText(user.getStateColor().get("min") == null ? "" : String.format("#%08X", (user.getStateColor().get("min"))));
        medColorCode.setText(user.getStateColor().get("med") == null ? "" : String.format("#%08X", (user.getStateColor().get("med"))));
        maxColorCode.setText(user.getStateColor().get("max") == null ? "" : String.format("#%08X", (user.getStateColor().get("max"))));
        minColorCircle.setColorFilter(user.getStateColor().get("min") == null ? ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.state_min) : user.getStateColor().get("min"));
        medColorCircle.setColorFilter(user.getStateColor().get("med") == null ? ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.state_med) : user.getStateColor().get("med"));
        maxColorCircle.setColorFilter(user.getStateColor().get("max") == null ? ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.state_max) : user.getStateColor().get("max"));
    }

    /**
     * Check if the String correspond to a color in the correct format.
     * @param colorString Color in string form
     * @return Integer
     */
    private Integer checkIfColor(String colorString) {
        if (colorString == null || !colorString.matches("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$")) {
            return null;
        }
        try {
            return Color.parseColor(colorString);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Given the TextView ID check if the color is correct and in that case update the
     * ImageView ID with that color corresponding to that String type.
     *
     * @param view View
     * @param colorTextId ID of the TextView
     * @param colorImageId ID of the ImageView
     * @param type Type
     * @return boolean
     */
    private boolean updateColor(View view, int colorTextId, int colorImageId, String type) {
        TextView colorTextView = view.findViewById(colorTextId);
        ImageView colorImageView = view.findViewById(colorImageId);
        if (colorTextView.getText().toString().isEmpty()){
            if (type.matches("min")) {
                user.getStateColor().put(type, ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.state_min));
                colorImageView.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.state_min));
            } else if (type.matches("med")) {
                user.getStateColor().put(type, ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.state_med));
                colorImageView.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.state_med));
            } else if (type.matches("max")) {
                user.getStateColor().put(type, ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.state_max));
                colorImageView.setColorFilter(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.state_max));
            }
            return true;
        }
        Integer color = checkIfColor(colorTextView.getText().toString());
        if (color != null) {
            user.getStateColor().put(type, color);
            colorImageView.setColorFilter(color);
            return true;
        } else {
            AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                    .setTitle("Warning")
                    .setMessage("The color set as (" + colorTextView.getText().toString() + ") is not in the correct format")
                    .create();
            alertDialog.show();
            return false;
        }
    }

    /**
     * Activate/Deactivate the EditText with the corresponding ID based on the value of the param active.
     * @param view View
     * @param id ID of the EditText view
     * @param active True (activate) / False (deactivate)
     */
    private void updateTextViewClickable(View view, int id, boolean active) {
        EditText text = view.findViewById(id);
        text.setEnabled(active);
        text.setFocusable(active);
        text.setFocusableInTouchMode(active);
        text.setClickable(active);
    }

    /**
     * Let the user choose a directory
     */
    private void openFolderPicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        folderPickerLauncher.launch(intent);
    }

    /**
     * Load the backup, making sure that the user is sure before doing it
     */
    private void loadBackup() {
        folderPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri folderUri = result.getData().getData();
                        if (folderUri != null) {
                            Objects.requireNonNull(getContext()).getContentResolver().takePersistableUriPermission(
                                    folderUri,
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                            );
                            if (checkFolderFiles(folderUri)) {
                                new AlertDialog.Builder(getContext())
                                        .setCancelable(true)
                                        .setTitle("Are you sure to load this backup ?")
                                        .setMessage("If you confirm the vehicles will be added and the user data will be updated with the backup data")
                                        .setPositiveButton("Confirm", (dialog, which) ->  loadFiles(folderUri))
                                        .setNegativeButton(android.R.string.cancel, (dialog, which) -> {})
                                        .show();
                            } else {
                                Toast.makeText(getContext(), "The selected folder is not a backup of " + ContextCompat.getString(getContext(), R.string.app_name), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );
    }

    /**
     * Check if the given Uri is a directory and contains all the files of the backup
     * @param folderUri Uri of the directory
     * @return true if the Uri is correct, False otherwise
     */
    private boolean checkFolderFiles(Uri folderUri) {
        DocumentFile directory = DocumentFile.fromTreeUri(Objects.requireNonNull(getContext()), folderUri);
        if (directory == null || !directory.isDirectory()) {
            return false;
        }
        Set<String> requiredFiles = new HashSet<>(Arrays.asList(ContextCompat.getString(getContext(), R.string.app_motorcycle_backup_file), ContextCompat.getString(getContext(), R.string.app_user_backup_file)));
        int fileCount = 0;
        for (DocumentFile file : directory.listFiles()) {
            if (file.isFile() && requiredFiles.contains(file.getName())) {
                fileCount++;
            } else {
                return false;
            }
        }
        return fileCount == 2;
    }

    /**
     * Load the files into the app, adding vehicles and restoring the user data
     * @param folderUri Uri of the folder to load
     */
    private void loadFiles(Uri folderUri) {
        DocumentFile directory = DocumentFile.fromTreeUri(Objects.requireNonNull(getContext()), folderUri);
        if (directory == null || !directory.isDirectory()) {
            return;
        }
        for (DocumentFile file : directory.listFiles()) {
            if (file.getName() != null && file.getName().matches(ContextCompat.getString(getContext(), R.string.app_motorcycle_backup_file))) {
                try {
                    InputStream inputStream = getContext().getContentResolver().openInputStream(file.getUri());
                    ObjectMapper mapper = new ObjectMapper();
                    ArrayList<Motorcycle> motorcyclesBackup = mapper.readValue(inputStream, new TypeReference<ArrayList<Motorcycle>>() {});
                    motorcycles.addAll(motorcyclesBackup);
                    inputStream.close();
                } catch (IOException e) {
                    System.err.println("Error while loading the ArrayList of backup motorcycle: " + e.getMessage());
                    Toast.makeText(getContext(), "ERROR: The backup was not loaded successfully", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (file.getName() != null && file.getName().matches(ContextCompat.getString(getContext(), R.string.app_user_backup_file))) {
                try {
                    InputStream inputStream = getContext().getContentResolver().openInputStream(file.getUri());
                    ObjectMapper mapper = new ObjectMapper();
                    user = mapper.readValue(inputStream, new TypeReference<User>() {});
                    inputStream.close();
                } catch (IOException e) {
                    System.err.println("Error while loading the user backup data: " + e.getMessage());
                    Toast.makeText(getContext(), "ERROR: The backup was not loaded successfully", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            Toast.makeText(getContext(), "DONE: The backup was not loaded successfully", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * CreateBackup of the data of the app and save them in the Download directory with the name of the app and the date.
     * When it has finished send a notification.
     */
    private void createBackup() {
        saveData();

        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getContext()), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        try {
            File backupDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    "Motor-Vitals-Backup (" + LocalDate.now() + ")");
            if (backupDir.mkdirs()){
                File fileMotorcycle = new File(getContext().getFilesDir(), ContextCompat.getString(getContext(), R.string.app_motorcycle_file));
                File fileUser = new File(getContext().getFilesDir(), ContextCompat.getString(getContext(), R.string.app_user_file));
                File backupMotorcycle = new File(backupDir, ContextCompat.getString(getContext(), R.string.app_motorcycle_backup_file));
                File backupUser = new File(backupDir, ContextCompat.getString(getContext(), R.string.app_user_backup_file));
                Files.copy(fileMotorcycle.toPath(), backupMotorcycle.toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(fileUser.toPath(), backupUser.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }

        showNotification();
    }

    /**
     * Save all the data in the app local storage.
     */
    private void saveData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(Objects.requireNonNull(getContext()).getFilesDir(), ContextCompat.getString(getContext(), R.string.app_motorcycle_file));
            mapper.writeValue(file, motorcycles);
        } catch (IOException e) {
            System.err.println("Error while saving the ArrayList of motorcycle number: " + e.getMessage());
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            File fileUser = new File(getContext().getFilesDir(), ContextCompat.getString(getContext(), R.string.app_user_file));
            mapper.writeValue(fileUser, user);
        } catch (IOException e) {
            System.err.println("Error while saving the user data: " + e.getMessage());
        }
    }

    /**
     * Send a notification to let the user know that the backup is finished
     */
    private void showNotification() {
        CharSequence name = "Backup Channel";
        String description = "Channel for backup notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("CHANNEL_BACKUP_ID", name, importance);
        channel.setDescription(description);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(Objects.requireNonNull(getContext()), "CHANNEL_BACKUP_ID")
                .setSmallIcon(R.drawable.baseline_backup_24)
                .setContentTitle("Backup " + ContextCompat.getString(getContext(), R.string.app_name))
                .setContentText("The backup was completed successfully")
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
        notificationManager.createNotificationChannel(channel);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()), new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
        notificationManager.notify(1, builder.build());
    }

}