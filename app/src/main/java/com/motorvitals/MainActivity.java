package com.motorvitals;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.motorvitals.classes.Element;
import com.motorvitals.classes.ElementList;
import com.motorvitals.classes.Motorcycle;
import com.motorvitals.databinding.ActivityMainBinding;
import com.motorvitals.fragments.MotorcycleFragment;
import com.motorvitals.fragments.ProfileFragment;
import com.motorvitals.fragments.StatusFragment;
import net.bytebuddy.jar.asm.TypeReference;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

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
        File file = new File(getApplicationContext().getFilesDir(), "Motorcycles");
        if (file.exists() && file.canRead()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
//                motorcycles.add(objectMapper.readValue(file, Motorcycle.class));
                ObjectReader objectReader = objectMapper.readerForArrayOf(Motorcycle.class);
                Motorcycle [] array = objectReader.readValue(file);
                motorcycles.addAll(Arrays.asList(array));
                Log.d("mess now", Arrays.toString(array));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        //TODO -> load from backup
        /*ArrayList<ElementList> elementList = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            String name = "list_" + i;
            ArrayList<Element> elements = new ArrayList<>();
            for (int k = 0; k < 8; k++) {
                String str = "element_" + k;
                Element element = new Element(str, false);
                if (k % 2 != 0) {
                    element = new Element(str, true);
                    element.setLastServiceDate(new Date());
                    element.setLastServiceKm(k * 100);
                }
                element.setDescription(name);
                elements.add(element);
            }
            ElementList list = new ElementList(name, elements);
            elementList.add(list);
        }

        for (int i = 0; i < 2; i++){
            String name = "moto_" + i;
            Motorcycle moto = new Motorcycle(elementList, name, 15000);
            moto.setDescription("Descrizione di " + name);
            motorcycles.add(moto);
        }*/
    }

    private void saveData() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File file = new File(getApplicationContext().getFilesDir(), "Motorcycles");

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, motorcycles);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Log.d("mess", line);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}