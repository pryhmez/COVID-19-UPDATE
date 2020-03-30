package com.example.covid_19_update;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.widget.ProgressBar;

public class ArticlesWebView extends AppCompatActivity {

    private WebView myWeb;
    private String URL;
    private ProgressBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        myWeb = (WebView)findViewById(R.id.myView);
        loadingBar = findViewById(R.id.progressbar);
        loadingBar.setIndeterminate(true);
        loadingBar.setForegroundGravity(Gravity.CENTER);
        loadingBar.setVisibility(View.VISIBLE);

        Intent intent=getIntent();
        URL = intent.getStringExtra("page");

        myWeb.getSettings().setJavaScriptEnabled(false);
        myWeb.loadUrl(URL);
//        loadingBar.setVisibility(View.GONE);

    }

}

