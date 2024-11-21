package com.motorvitals;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.motorvitals.classes.Motorcycle;
import com.motorvitals.classes.NotificationWorker;
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
                replaceFragment(StatusFragment.newInstance(motorcycles));
            if (item.getItemId() == R.id.profile_fragment)
                replaceFragment(new ProfileFragment());
            return true;
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveData();
    }

    /**
     *
     * @param fragment Fragment that will be loaded
     */
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void loadData() {
        /*
        //Populate motorcycles array-list
        motorcycles = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Motorcycle moto1 = new Motorcycle();
            moto1.setName("moto" + i);
            moto1.setDescription("description moto" + i);
            moto1.setKm(15000);
            ArrayList<ElementList> elementLists = new ArrayList<>();
            for (int t = 0; t < 3; t++) {
                ElementList elementList = new ElementList("list" + t);
                for (int k = 0; k < 3; k++) {
                    Element element = new Element();
                    element.setName("element" + t + k);
                    element.setDescription("description element" + t + k);
                    if (k % 2 == 0) {
                        element.setState(true);
                    }
                    elementList.getElements().add(element);
                }
                elementLists.add(elementList);
            }
            moto1.setElementList(elementLists);
            motorcycles.add(moto1);
        }
*/
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(getApplicationContext().getFilesDir(), "Motorcycles");
            motorcycles = mapper.readValue(file, new TypeReference<ArrayList<Motorcycle>>(){});
        } catch (IOException e) {
            System.err.println("Error while loading the ArrayList of motorcycle number: " + e.getMessage());
            motorcycles = new ArrayList<>();
        }
    }

    private void saveData() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            File file = new File(getApplicationContext().getFilesDir(), "Motorcycles");
            mapper.writeValue(file, motorcycles);
        } catch (IOException e) {
            System.err.println("Error while saving the ArrayList of motorcycle number: " + e.getMessage());
        }
    }

    private void notificationSystem() {
        WorkManager.getInstance(getApplicationContext()).cancelAllWork();
        PeriodicWorkRequest notificationRequest = new PeriodicWorkRequest.Builder(NotificationWorker.class, 15, TimeUnit.MINUTES).build();
        WorkManager.getInstance(getApplicationContext()).enqueue(notificationRequest);
    }
}