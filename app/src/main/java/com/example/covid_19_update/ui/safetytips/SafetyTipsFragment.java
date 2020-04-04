package com.example.covid_19_update.ui.safetytips;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.covid_19_update.R;

import static android.widget.Toast.*;

public class SafetyTipsFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private WebView myWeb;
    private ProgressBar loadingBar;
    private String URL = "https://www.who.int/emergencies/diseases/novel-coronavirus-2019/advice-for-public";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.activity_web_view, container, false);
        final TextView textView = root.findViewById(R.id.text_slideshow);
//        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Safety Is Key", Toast.LENGTH_LONG);
//        toast.setGravity(Gravity.CENTER,0,0);
//        // Finally, show the toast
//        toast.show();
//        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        myWeb = (WebView)root.findViewById(R.id.myView);
        loadingBar = root.findViewById(R.id.progressbar);
        loadingBar.setIndeterminate(true);
        loadingBar.setForegroundGravity(Gravity.CENTER);
        loadingBar.setVisibility(View.VISIBLE);

        myWeb.getSettings().setJavaScriptEnabled(true);
        myWeb.loadUrl(URL);
        return root;
    }
}
