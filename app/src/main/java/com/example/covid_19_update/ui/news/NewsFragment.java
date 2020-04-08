package com.example.covid_19_update.ui.news;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Movie;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.covid_19_update.Article;
import com.example.covid_19_update.ArticlesAdapter;
import com.example.covid_19_update.ArticlesWebView;
import com.example.covid_19_update.MainActivity;
import com.example.covid_19_update.R;
import com.example.covid_19_update.app.MyApplication;
import com.example.covid_19_update.utils.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class NewsFragment extends Fragment {

    private static final String TAG = NewsFragment.class.getSimpleName();

    // url to fetch shopping items
    private static final String URL = "http://newsapi.org/v2/top-headlines?country=ng&apiKey=06f01ddc91754aecb0b2e0a827f0ff84&q=virus";

    private RecyclerView recyclerView;
    private List<Article> itemsList;
    private ArticlesAdapter mAdapter;
    private ProgressDialog nDialog;
    private Article article;
    private Util utils;
    private LinearLayout noInt;
    private final String FILE = "news";
    private SharedPreferences sharedPreferences;

    ArticlesAdapter.RecyclerViewClickListener listener;

    public NewsFragment() {
        // Required empty public constructor
    }

    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

//    private View.OnClickListener onItemClickListener = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            //TODO: Step 4 of 4: Finally call getTag() on the view.
//            // This viewHolder will have all required values.
//            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) view.getTag();
//            int position = viewHolder.getAdapterPosition();
//            // viewHolder.getItemId();
//            // viewHolder.getItemViewType();
//            // viewHolder.itemView;
//            article = itemsList.get(position);
//            Toast.makeText(getContext(), "You Clicked: " + article.getTitle(), Toast.LENGTH_SHORT).show();
//            Log.d("chosen", article.getTitle().toString());
//        }
//    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        utils = new Util();
        noInt = view.findViewById(R.id.noInternet);
        recyclerView = view.findViewById(R.id.recycler_view);
        itemsList = new ArrayList<>();
//        mAdapter = new ArticlesAdapter(itemsList, getActivity(), listener);

        sharedPreferences = getActivity().getSharedPreferences(FILE, Context.MODE_PRIVATE);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(8), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        listener = (myView, position) -> {
//            Toast.makeText(getActivity().getApplicationContext(), "Position "+position , Toast.LENGTH_LONG).show();
            Log.d("chosen", String.valueOf(((int) position)));
            Intent intent = new Intent(getActivity(), ArticlesWebView.class);
            intent.putExtra("page", itemsList.get(position).getUrl());
            startActivity(intent);
        };
         mAdapter = new ArticlesAdapter(itemsList, getContext(), listener);
//        mAdapter.setOnItemClickListener(onItemClickListener);

        if(utils.isNetworkAvailable(getActivity())){
            noInt.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            fetchStoreItems();
        }else{
            noInt.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(getActivity().getApplicationContext(), "Please turn on your internet connection", Toast.LENGTH_LONG).show();
        }

        return view;
    }

    /**
     * fetching shopping item by making http call
     */
    private void fetchStoreItems() {
        StringRequest request = new StringRequest (Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        nDialog = new ProgressDialog(getActivity()); //Here I get an error: The constructor ProgressDialog(PFragment) is undefined
                        nDialog.setMessage("Loading..");
                        nDialog.setTitle("Checking Network");
                        nDialog.setIndeterminate(false);
                        nDialog.setCancelable(true);
                        nDialog.show();
                        if (response == null) {
                            Toast.makeText(getActivity(), "Couldn't fetch the store items! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("articles");

                            for(int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                JSONObject sourceObj = obj.getJSONObject("source");

                                Article article = new Article(
                                        jsonObject.getString("totalResults"),
                                        sourceObj.getString("name"),
                                        obj.getString("author"),
                                        obj.getString("title"),
                                        obj.getString("description"),
                                        obj.getString("url"),
                                        obj.getString("urlToImage"),
                                        obj.getString("publishedAt"),
                                        obj.getString("content")

                                );
                                Log.d("responses", obj.getString("title").toString());
                                itemsList.add(article);
                            }

                            mAdapter = new ArticlesAdapter(itemsList, getActivity(), listener);
                            recyclerView.setAdapter(mAdapter);
                            nDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally{

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                Gson gson = new Gson();
                                String json = gson.toJson(itemsList);
                                editor.clear();
                                editor.putString( "data", json);
                                editor.apply();
                                Log.d("noti", "data news entered");

                        }

//                        List<Movie> items = new Gson().fromJson(response.toString(), new TypeToken<List<Movie>>() {
//                        }.getType());

//                        itemsList.clear();
//

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(request);

//        MyApplication.getInstance().addToRequestQueue(request);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    /**
     * RecyclerView adapter class to render items
     * This class can go into another separate class, but for simplicity
     */
//    class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {
//        private Context context;
//        private List<Movie> movieList;
//
//        public class MyViewHolder extends RecyclerView.ViewHolder {
//            public TextView name, price;
//            public ImageView thumbnail;
//
//            public MyViewHolder(View view) {
//                super(view);
//                name = view.findViewById(R.id.title);
//                price = view.findViewById(R.id.price);
//                thumbnail = view.findViewById(R.id.thumbnail);
//            }
//        }
//
//
//        public NewsAdapter(Context context, List<Article> movieList) {
//            this.context = context;
//            this.movieList = movieList;
//        }
//
//        @Override
//        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.store_item_row, parent, false);
//
//            return new MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(MyViewHolder holder, final int position) {
//            final Movie movie = movieList.get(position);
//            holder.name.setText(movie.getTitle());
//            holder.price.setText(movie.getPrice());
//
//            Glide.with(context)
//                    .load(movie.getImage())
//                    .into(holder.thumbnail);
//        }
//
//        @Override
//        public int getItemCount() {
//            return movieList.size();
//        }
//    }
}

