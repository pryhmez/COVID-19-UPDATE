package com.example.covid_19_update.ui.statistics;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid_19_update.NotificationServices.NotificationServices;
import com.example.covid_19_update.R;
import com.example.covid_19_update.Stats;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class StatisticsFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    TextView country, totalConfirmed, totalDeaths, totalRecovered, newConfirmed, newDeaths, newRecovered;
    String countrySelect, totalConfirmedCases, totalDeathCases, totalRecoveredCases, newConfirmedCases, newDeathCases, newRecoveredCases;
    private static final String URL = "https://api.covid19api.com/summary";
    private ProgressDialog nDialog;
    private Spinner countrys;
    private List<Stats> itemsList;
    private List<Stats> shared = new ArrayList<>();
    private final String STATS = "statistics";
    private SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_statistics, container, false);

        itemsList = new ArrayList<>();
        countrys = root.findViewById(R.id.spinner1);
        nDialog = new ProgressDialog(getActivity()); //Here I get an error: The constructor ProgressDialog(PFragment) is undefined

        fetchStoreItems();

        sharedPreferences = getActivity().getSharedPreferences(STATS, Context.MODE_PRIVATE);

        country = root.findViewById(R.id.country);
         totalConfirmed = root.findViewById(R.id.totalconfirmed);
         totalDeaths = root.findViewById(R.id.totaldeaths);
         totalRecovered = root.findViewById(R.id.totalrecovered);
         newConfirmed = root.findViewById(R.id.newconfirmed);
         newDeaths = root.findViewById(R.id.newdeaths);
         newRecovered = root.findViewById(R.id.newrecovered);
//         countrySelect = "Nigeria";


        nDialog.setMessage("Loading..");
        nDialog.setTitle("Checking Network");
        nDialog.setIndeterminate(false);
        nDialog.setCancelable(true);
        nDialog.show();



//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                country.setText(countrySelect);
//                fetchStoreItem();
//
//            }
//        });
        return root;

    }

    private void initSpinner() {

//        Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        int j = 0;
        int count = 0;
        for (Stats stats : itemsList) {
            String country = stats.getCountry();

            if (country.trim().length() > 0 && !countries.contains(country)) {
                j++;
                if(country.matches("Nigeria")){
                    count = j-1;
                }
                countries.add(country);
            }
        }
//        Collections.sort(countries);
//        for (String country : countries) {
//            Log.d("country", country);
//        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, countries);
        // set the view for the Drop down list
        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // set the ArrayAdapter to the spinner
        countrys.setAdapter(dataAdapter);
        countrys.setSelection(count);
        if(sharedPreferences.getString("data", null) == null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            Gson gson = new Gson();
            String json = gson.toJson(shared);
            editor.clear();
            editor.putString( "data", json);
            editor.apply();
            Log.d("noti", "data entered");
        }

        countrys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countrySelect = parent.getItemAtPosition(position).toString();
                if (countrySelect.matches("United States")) {
                    countrySelect = "United States of America";
                }
                fetchStoreItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        System.out.println("# countries found: " + countries.size());

    }


    private void fetchStoreItem() {


                        try {

                            for(int i = 0; i < itemsList.size(); i++) {
                                Stats s = itemsList.get(i);

                                String count = s.getCountry();
                                if(count.matches(countrySelect) ) {
                                    Log.d("matches", count + countrySelect);
                                       newConfirmedCases = s.getNewConfirmed();
                                       totalConfirmedCases = s.getTotalConfirmed();
                                       newDeathCases = s.getNewDeaths();
                                       totalDeathCases = s.getTotalDeaths();
                                       newRecoveredCases = s.getNewRecovered();
                                       totalRecoveredCases = s.getTotalRecovered();
                                       break;
                                }else {
                                    newConfirmedCases= "0";
                                    totalConfirmedCases = "0";
                                    newDeathCases = "0";
                                    totalDeathCases = "0";
                                    newRecoveredCases = "0";
                                    totalRecoveredCases = "0";
                                }
                            }
                            setTexts();
                            nDialog.dismiss();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }



    }

    private void setTexts() {
        country.setText(countrySelect);
        newConfirmed.setText(newConfirmedCases);
        totalConfirmed.setText(totalConfirmedCases);
        newDeaths.setText(newDeathCases);
        totalDeaths.setText(totalDeathCases);
        newRecovered.setText(newRecoveredCases);
        totalRecovered.setText(totalRecoveredCases);
    }

    private void fetchStoreItems() {
        StringRequest request = new StringRequest (Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response == null) {
                            Toast.makeText(getActivity(), "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject globe = jsonObject.getJSONObject("Global");
                            JSONArray jsonArray = jsonObject.getJSONArray("Countries");
                            Log.d("countss", jsonArray.toString());

                            itemsList.add(new Stats(
                                    globe.getString("NewConfirmed"),
                                    globe.getString("TotalConfirmed"),
                                    globe.getString("NewDeaths"),
                                    globe.getString("TotalDeaths"),
                                    globe.getString("NewRecovered"),
                                    globe.getString("TotalRecovered"),
                                    "Global"
                            ));
                            shared.add(new Stats(
                                    globe.getString("NewConfirmed"),
                                    globe.getString("TotalConfirmed"),
                                    globe.getString("NewDeaths"),
                                    globe.getString("TotalDeaths"),
                                    globe.getString("NewRecovered"),
                                    globe.getString("TotalRecovered"),
                                    "Global"
                            ));

                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);

                                Stats stats = new Stats(
                                        obj.getString("NewConfirmed"),
                                        obj.getString("TotalConfirmed"),
                                        obj.getString("NewDeaths"),
                                        obj.getString("TotalDeaths"),
                                        obj.getString("NewRecovered"),
                                        obj.getString("TotalRecovered"),
                                        obj.getString("Country")
                                );

                                if(obj.getString("Country").matches("Nigeria")) {
                                    shared.add(stats);
                                }
                                itemsList.add(stats);
                            }
                            nDialog.dismiss();
                            initSpinner();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
//                nDialog.dismiss();
                Toast.makeText(getActivity(), "Error: " + "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

//        MyApplication.getInstance().addToRequestQueue(request);
    }

}
