package com.motorvitals;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.motorvitals.classes.Element;
import com.motorvitals.classes.ElementList;
import com.motorvitals.classes.Motorcycle;
import com.motorvitals.databinding.ActivityMainBinding;
import com.motorvitals.fragments.MotorcycleFragment;
import com.motorvitals.fragments.ProfileFragment;
import com.motorvitals.fragments.StatusFragment;
import net.bytebuddy.jar.asm.TypeReference;

import java.io.*;
import java.util.*;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;
    ArrayList<Motorcycle> motorcycles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        loadData();
        //set fragment when the app is opened
        replaceFragment(new MotorcycleFragment(), motorcycles);


        //------------------------ Listeners ------------------------
        activityMainBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.motorcycle_fragment)
                replaceFragment(new MotorcycleFragment(), motorcycles);
            if (item.getItemId() == R.id.status_fragment)
                replaceFragment(new StatusFragment(), motorcycles);
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

    /**
     *
     * @param fragment Fragment that will be loaded
     * @param motorcycles ArrayList that will be sent into the bundle
     */
    public void replaceFragment(Fragment fragment, ArrayList<Motorcycle> motorcycles) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("motorcycles", motorcycles);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }

    private void loadData() {
        motorcycles = new ArrayList<>();
        /*File file = new File(getApplicationContext().getFilesDir(), "Motorcycles");
        if (file.exists() && file.canRead()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                motorcycles = new ArrayList<>(Arrays.asList(objectMapper.readValue(file, Motorcycle[].class)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }*/

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
    }

    private void saveData() {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(getApplicationContext().getFilesDir(), "Motorcycles");
        try {
            ObjectWriter objectWriter = objectMapper.writerFor(Motorcycle.class);
            SequenceWriter sequenceWriter = objectWriter.writeValuesAsArray(file);
            for (Motorcycle motorcycle : motorcycles) {
                sequenceWriter.write(motorcycle);
            }
            sequenceWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}