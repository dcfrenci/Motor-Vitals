package com.motorvitals;

import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.motorvitals.adapter.MotorcycleRecycleViewAdapter;
import com.motorvitals.classes.Element;
import com.motorvitals.classes.Motorcycle;
import com.motorvitals.databinding.ActivityMainBinding;
import com.motorvitals.fragments.MotorcycleFragment;
import com.motorvitals.fragments.ProfileFragment;
import com.motorvitals.fragments.StatusFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding activityMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_main);
        setContentView(activityMainBinding.getRoot());
        //set fragment when the app is opened
        replaceFragment(new MotorcycleFragment());


//        RecyclerView recyclerView = findViewById(R.id.motorcycleRecyclerView);
//        setUpMotorcycleModels();
//        MotorcycleRecycleViewAdapter adapter = new MotorcycleRecycleViewAdapter(this, motorcycles);
//
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        //------------------------ Listeners ------------------------
        activityMainBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.motorcycle)
                replaceFragment(new MotorcycleFragment());
            if (item.getItemId() == R.id.status)
                replaceFragment(new StatusFragment());
            if (item.getItemId() == R.id.profile)
                replaceFragment(new ProfileFragment());

            return true;
        });
    }

    /**
     *
     * @param fragment Fragment that will be loaded in the fragment container
     */
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }


}