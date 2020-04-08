package com.example.covid_19_update.NotificationServices;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid_19_update.Article;
import com.example.covid_19_update.MainActivity;
import com.example.covid_19_update.R;
import com.example.covid_19_update.Stats;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;
import static com.android.volley.VolleyLog.TAG;

public class NotiServices {

    private Context mContext;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private ArrayList<Stats> newItemsList = new ArrayList<>();
    private ArrayList<Stats> sharedList = new ArrayList<>();
    private ArrayList<Article> newsList = new ArrayList<>();
    private static final String URL = "https://api.covid19api.com/summary";
    public static final String NOTIFICATION_CHANNELCOUNTRY_ID = "10001";
    public static final String NOTIFICATION_CHANNELGLOBE_ID = "10011";
    private final String STATS = "statistics";


    public NotiServices(Context context) {
        mContext = context;
    }

    /**
     * Create and push the notification
     */
    public void createNotification() {
        /**Creates an explicit intent for an Activity in your app**/
        fetchStoreItems();
       }


    private void fetchStoreItems() {
        StringRequest request = new StringRequest (Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject globe = jsonObject.getJSONObject("Global");
                            JSONArray jsonArray = jsonObject.getJSONArray("Countries");
                            Log.d("countss", jsonArray.toString());

                            newItemsList.clear();
                            newItemsList.add(new Stats(
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
                                if(obj.getString("Country").matches("Nigeria")) {

                                    Stats stats = new Stats(
                                            obj.getString("NewConfirmed"),
                                            obj.getString("TotalConfirmed"),
                                            obj.getString("NewDeaths"),
                                            obj.getString("TotalDeaths"),
                                            obj.getString("NewRecovered"),
                                            obj.getString("TotalRecovered"),
                                            obj.getString("Country")
                                    );

                                    newItemsList.add(stats);
                                    break;
                                }
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            SharedPreferences sharedPreferences = mContext.getSharedPreferences(STATS, Context.MODE_PRIVATE);
                            SharedPreferences newssharedPreferences = mContext.getSharedPreferences("news", Context.MODE_PRIVATE);
                            Gson gson = new Gson();
                            String json = sharedPreferences.getString("data", null);
                            Type type = new TypeToken<ArrayList<Stats>>() {}.getType();
                            sharedList = gson.fromJson(json, type);
                            if(sharedList != null && newItemsList != null){
                                if(!sharedList.get(0).getNewDeaths().matches(newItemsList.get(0).getNewDeaths())
                                        || !sharedList.get(0).getNewRecovered().matches(newItemsList.get(0).getNewRecovered())
                                        || !sharedList.get(0).getNewConfirmed().matches(newItemsList.get(0).getNewConfirmed())
                                ) {
                                        notifi("Global New cases update ",
                                                "new deaths " + sharedList.get(0).getNewDeaths() + "\nnew recovered cases " + sharedList.get(0).getNewRecovered() + "\nnew confirmed cases " + sharedList.get(0).getNewConfirmed(),
                                                0, NOTIFICATION_CHANNELGLOBE_ID);
                                    Log.d("notifylist", newItemsList.get(0).getCountry());

                                }
                                if(!sharedList.get(1).getNewDeaths().matches(newItemsList.get(1).getNewDeaths())
                                        || !sharedList.get(1).getNewRecovered().matches(newItemsList.get(1).getNewRecovered())
                                        || !sharedList.get(1).getNewConfirmed().matches(newItemsList.get(1).getNewConfirmed())
                                ) {
                                    notifi("New cases update in Nigeria " ,
                                            "new deaths " + sharedList.get(1).getNewDeaths() + "\nnew recovered cases " + sharedList.get(1).getNewRecovered() + "\nnew confirmed cases " + sharedList.get(1).getNewConfirmed(),
                                            1, NOTIFICATION_CHANNELCOUNTRY_ID);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.clear();
                                        editor.apply();
                                    Log.d("notifylist", sharedList.get(1).getCountry() );

                                }

                                //retrieve news
                                Random num = new Random();
                                String newsjson = newssharedPreferences.getString("data", null);
                                Type type2 = new TypeToken<ArrayList<Article>>() {}.getType();
                                newsList = gson.fromJson(newsjson, type2);
                                if(newsList.size() != 0){
                                    notifi("NEWS UPDATE", newsList.get(num.nextInt(newsList.size())).getTitle(), 2, "10101");

                                }

                            }
                       }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "notiError: " + error.getMessage());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(request);

//        MyApplication.getInstance().addToRequestQueue(request);
    }

    public void notifi (String title, String message, int id, String channelId) {
        Intent resultIntent = new Intent(mContext, MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(mContext,
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder = new NotificationCompat.Builder(mContext);
        mBuilder.setSmallIcon(R.drawable.virus);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        mNotificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(channelId);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(id /* Request Code */, mBuilder.build());

    }

}