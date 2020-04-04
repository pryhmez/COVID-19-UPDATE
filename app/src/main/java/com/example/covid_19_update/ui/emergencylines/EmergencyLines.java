package com.example.covid_19_update.ui.emergencylines;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.covid_19_update.Hotlines;
import com.example.covid_19_update.MainActivity;
import com.example.covid_19_update.R;
import com.example.covid_19_update.ui.share.ShareFragment;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;

public class EmergencyLines extends Fragment {

    private EmergencyLinesViewModel mViewModel;
    private Spinner statesSpinner;
    private ListView listView;
    private Hotlines hotlines;
    private String [] f;
    private Button share;
    private static final int CALL_PERMISSION_CODE = 100;

    public static EmergencyLines newInstance() {
        return new EmergencyLines();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.emergency_lines_fragment, container, false);

        hotlines = new Hotlines();
        statesSpinner = root.findViewById(R.id.statesSpinner);
        listView = root.findViewById(R.id.numberslist);
        share = root.findViewById(R.id.share_lines);
        initSpinner();

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.nav_share);

            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(EmergencyLinesViewModel.class);
        // TODO: Use the ViewModel
    }

    private void initSpinner() {

//        Locale[] locales = Locale.getAvailableLocales();
//        String [] locales = hotlines.getStates();

        ArrayList<String> states = new ArrayList<String>();
        for (String locale : hotlines.getStates()) {
            String country = locale;
            if (country.trim().length() > 0 && !states.contains(country)) {
                states.add(country);
            }
        }
        Collections.sort(states);
        for (String country : states) {
            Log.d("country", country);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, states);
        // set the view for the Drop down list
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // set the ArrayAdapter to the spinner
        statesSpinner.setAdapter(dataAdapter);
        statesSpinner.setSelection(0);

        statesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String stateSelect = parent.getItemAtPosition(position).toString();
                int index = 0;
                for(int i = 0; i < hotlines.getStates().length; i++) {
                   String myState = hotlines.getStates()[i];
                    if (stateSelect.matches(myState)) {
                        index = i;
                    }
                }
//                fetchStoreItems();
                f = hotlines.getNumbers()[index];
//                Log.d("deArray", Arrays.toString(f));

                listView.setVisibility(View.VISIBLE);
                ArrayAdapter<String> numAdapter = new ArrayAdapter<String>(getActivity(),
                        R.layout.number_layout, f);
//                numAdapter.setDropDownViewResource(android.R.layout.simple_gallery_item);
                listView.setAdapter(numAdapter);
                listView.setSelection(0);


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(checkPermission(
                        Manifest.permission.CALL_PHONE,
                        CALL_PERMISSION_CODE)){
                    String d = f[i];
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:"+d));
                        startActivity(callIntent);
                    } catch (ActivityNotFoundException activityException) {
                        Log.e("Calling a Phone Number", "Call failed", activityException);
                    }
                }else {
                    requestPermission( Manifest.permission.CALL_PHONE, CALL_PERMISSION_CODE);
                }

            }
        });
    }


    // Function to check and request permission.
    public boolean checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(getActivity(), permission)
                == PackageManager.PERMISSION_DENIED) {

            return false;
        }
        else {
            return true;
        }
    }

    public  void requestPermission(String permission, int requestCode) {
        // Requesting the permission
        ActivityCompat.requestPermissions(getActivity(),
                new String[] { permission },
                requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == CALL_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(),
                        "Call Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(getActivity(),
                        "Call Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == CALL_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(),
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(getActivity(),
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

}
