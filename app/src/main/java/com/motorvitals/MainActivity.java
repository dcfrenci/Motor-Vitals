package com.motorvitals;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.motorvitals.classes.Motorcycle;
import com.motorvitals.classes.NotificationWorker;
import com.motorvitals.classes.User;
import com.motorvitals.databinding.ActivityMainBinding;
import com.motorvitals.fragments.MotorcycleFragment;
import com.motorvitals.fragments.ProfileFragment;
import com.motorvitals.fragments.StatusFragment;

import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    ArrayList<Motorcycle> motorcycles;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());

        loadData();
        notificationSystem();
        //set fragment when the app is opened
        replaceFragment(MotorcycleFragment.newInstance(motorcycles));


        //------------------------ Listeners ------------------------
        activityMainBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.motorcycle_fragment)
                replaceFragment(MotorcycleFragment.newInstance(motorcycles));
            if (item.getItemId() == R.id.status_fragment)
                replaceFragment(StatusFragment.newInstance(motorcycles, user));
            if (item.getItemId() == R.id.profile_fragment)
                replaceFragment(ProfileFragment.newInstance(motorcycles, user));
            return true;
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }

    /**
     * Load the new fragment into the activity
     * @param fragment Fragment to be loaded
     */
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    /**
     * Load all the data of the app.
     */
    private void loadData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                        101
                );
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        101
                );
            }
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(getApplicationContext().getFilesDir(), ContextCompat.getString(getApplicationContext(), R.string.app_motorcycle_file));
            motorcycles = mapper.readValue(file, new TypeReference<ArrayList<Motorcycle>>(){});
        } catch (IOException e) {
            System.err.println("Error while loading the ArrayList of motorcycle number: " + e.getMessage());
            motorcycles = new ArrayList<>();
        }
        try{
            ObjectMapper mapper = new ObjectMapper();
            File fileUser = new File(getApplicationContext().getFilesDir(), ContextCompat.getString(getApplicationContext(), R.string.app_user_file));
            user = mapper.readValue(fileUser, new TypeReference<User>() {});
        } catch (IOException e) {
            System.err.println("Error while loading the user data: " + e.getMessage());
            user = new User();
        }
    }

    /**
     * Save all the data of the app.
     */
    private void saveData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            File file = new File(getApplicationContext().getFilesDir(), ContextCompat.getString(getApplicationContext(), R.string.app_motorcycle_file));
            mapper.writeValue(file, motorcycles);
        } catch (IOException e) {
            System.err.println("Error while saving the ArrayList of motorcycle number: " + e.getMessage());
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            File fileUser = new File(getApplicationContext().getFilesDir(), ContextCompat.getString(getApplicationContext(), R.string.app_user_file));
            mapper.writeValue(fileUser, user);
        } catch (IOException e) {
            System.err.println("Error while saving the user data: " + e.getMessage());
        }
    }

    /**
     * Send periodically notification to the user
     */
    private void notificationSystem() {
        WorkManager.getInstance(getApplicationContext()).cancelAllWork();
        PeriodicWorkRequest notificationRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.DAYS).build();
        WorkManager.getInstance(getApplicationContext()).enqueue(notificationRequest);
    }
}