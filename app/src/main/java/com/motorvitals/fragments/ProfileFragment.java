package com.motorvitals.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.motorvitals.R;
import com.motorvitals.classes.User;
import org.w3c.dom.Text;

import java.time.LocalTime;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String USER = "user";
    private User user;

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
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(User user) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putParcelable(USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            user = getArguments().getParcelable(USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        setUpUser(view);

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

        view.findViewById(R.id.profile_backup_button).setOnClickListener(click -> {

        });
        return view;
    }

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
        medColorCircle.setColorFilter(user.getStateColor().get("med") == null ? ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.state_med) : user.getStateColor().get("max"));
        maxColorCircle.setColorFilter(user.getStateColor().get("max") == null ? ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.state_max) : user.getStateColor().get("max"));
    }

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

    private boolean updateColor(View view, int colorTextId, int colorImageId, String type) {
        TextView colorTextView = view.findViewById(colorTextId);
        if (colorTextView.getText().toString().isEmpty())
            return true;
        ImageView colorImageView = view.findViewById(colorImageId);
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

    private void updateTextViewClickable(View view, int id, boolean active) {
        EditText text = view.findViewById(id);
        text.setEnabled(active);
        text.setFocusable(active);
        text.setFocusableInTouchMode(active);
        text.setClickable(active);
    }
}