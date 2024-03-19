package com.motorvitals;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.motorvitals.databinding.ActivityMainBinding;
import com.motorvitals.fragments.MotorcycleFragment;
import com.motorvitals.fragments.ProfileFragment;
import com.motorvitals.fragments.StatusFragment;

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


        //------------------------ Listeners ------------------------
        activityMainBinding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.motorcycle_fragment)
                replaceFragment(new MotorcycleFragment());
            if (item.getItemId() == R.id.status_fragment)
                replaceFragment(new StatusFragment());
            if (item.getItemId() == R.id.profile_fragment)
                replaceFragment(new ProfileFragment());

            return true;
        });
    }

    /**
     *
     * @param fragment Fragment that will be loaded in the fragment container
     */
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }


}