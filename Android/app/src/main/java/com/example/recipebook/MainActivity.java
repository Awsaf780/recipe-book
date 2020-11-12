package com.example.recipebook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements RecipeAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String EXTRA_URL = "imageUrl";
    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_RATING = "rating";
    public static final String EXTRA_REVIEWS = "reviews";
    public static final String EXTRA_INGREDIENTS = "ingredients";
    public static final String EXTRA_DIRECTIONS = "directions";

    private int totalItemCount;
    private int firstVisibleItem;
    private int visibleItemCount;
    private int previousTotal;
    private LinearLayoutManager layoutManager = new LinearLayoutManager(this);


    private RecyclerView mRecyclerView;
    private JsonObjectRequest request;
    private RecipeAdapter mRecipeAdapter;
    private ArrayList<Recipe> mRecipeList;
    private ArrayList<Recipe> sRecipeList;

    private RequestQueue mRequestQueue;
    private int page = 1;
    private boolean load = true;

    private SwipeRefreshLayout swipeRefreshLayout;
//    static String keyword = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_ACTION_BAR);

        setContentView(R.layout.activity_main);

        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(layoutManager);

        mRecipeList = new ArrayList<>();
        sRecipeList = new ArrayList<>();


        mRequestQueue = Volley.newRequestQueue(this);

        parseJSON();
    }

    private void parseJSON() {
        String url = "https://recipebook327.herokuapp.com/api/recipes/list?ordering=-reviews&page="+ page;
        request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject result = jsonArray.getJSONObject(i);

                                String name = result.getString("name");
                                double rating = result.getDouble("rating");
                                String imageUrl = result.getString("url");
                                int reviews = result.getInt("reviews");
                                String ingredients = result.getString("ingredients");
                                String directions = result.getString("directions");


                                mRecipeList.add(new Recipe(imageUrl, name, rating, reviews, ingredients, directions));

                            }

                            mRecipeAdapter = new RecipeAdapter(MainActivity.this, mRecipeList);
                            mRecyclerView.setAdapter(mRecipeAdapter);
                            mRecipeAdapter.setOnItemClickListener(MainActivity.this);

                            swipeRefreshLayout.setRefreshing(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
        pagination();
    }

    private void pagination() {

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(dy>0){
                    firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                    totalItemCount = layoutManager.getItemCount();
                    visibleItemCount = layoutManager.getChildCount();

                    if (load){
                        if (totalItemCount > previousTotal){
                            previousTotal = totalItemCount;
                            page++;
                            load=false;
                        }

                    }
                    if (!load && (firstVisibleItem+visibleItemCount)>= totalItemCount){
                        getNext(page);
                        load = true;
                    }
                }


            }
        });
    }

    private void getNext(int page) {
        String url = "https://recipebook327.herokuapp.com/api/recipes/list?ordering=-reviews&page="+ page;
        System.out.println(url);
        request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject result = jsonArray.getJSONObject(i);

                                String name = result.getString("name");
                                double rating = result.getDouble("rating");
                                String imageUrl = result.getString("url");
                                int reviews = result.getInt("reviews");
                                String ingredients = result.getString("ingredients");
                                String directions = result.getString("directions");


                                mRecipeList.add(new Recipe(imageUrl, name, rating, reviews, ingredients, directions));

                            }

                            mRecipeAdapter = new RecipeAdapter(MainActivity.this, mRecipeList);
                            mRecyclerView.setAdapter(mRecipeAdapter);
                            mRecipeAdapter.setOnItemClickListener(MainActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);

    }

    private void searchJSON(String keyword) {
        swipeRefreshLayout.setRefreshing(true);
//        keyword = keyword;

        String url = "https://recipebook327.herokuapp.com/api/recipes/list?search=" + keyword;
        request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");

                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject result = jsonArray.getJSONObject(i);

                                String name = result.getString("name");
                                double rating = result.getDouble("rating");
                                String imageUrl = result.getString("url");
                                int reviews = result.getInt("reviews");
                                String ingredients = result.getString("ingredients");
                                String directions = result.getString("directions");


                                sRecipeList.add(new Recipe(imageUrl, name, rating, reviews, ingredients, directions));

                            }

                            mRecipeAdapter = new RecipeAdapter(MainActivity.this, sRecipeList);
                            mRecyclerView.setAdapter(mRecipeAdapter);
                            mRecipeAdapter.setOnItemClickListener(MainActivity.this);

                            swipeRefreshLayout.setRefreshing(false);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mRequestQueue.add(request);
        pagination();
    }

    @Override
    public void onItemClick(int position) {
        Intent detailIntent = new Intent(this, DetailActivity.class);

        Recipe clickedRecipe = new Recipe();
        if (sRecipeList.size() > 0){
            clickedRecipe = sRecipeList.get(position);
        }
        else{
            clickedRecipe = mRecipeList.get(position);
        }
//        Recipe clickedRecipe = mRecipeList.get(position);

        detailIntent.putExtra(EXTRA_URL, clickedRecipe.getmImageUrl());
        detailIntent.putExtra(EXTRA_NAME, clickedRecipe.getmName());
        detailIntent.putExtra(EXTRA_RATING, clickedRecipe.getmRating());
        detailIntent.putExtra(EXTRA_REVIEWS, clickedRecipe.getReviews());
        detailIntent.putExtra(EXTRA_INGREDIENTS, clickedRecipe.getIngredients());
        detailIntent.putExtra(EXTRA_DIRECTIONS, clickedRecipe.getDirections());


        startActivity(detailIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search Recipe...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query.length() > 2){
//                    keyword = query;
                    searchJSON(query);
                    // change this with custom search
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newtext) {
                parseJSON();
                return false;
            }
        });

        searchMenuItem.getIcon().setVisible(false, false);

        return true;
    }

    @Override
    public void onRefresh() {
        parseJSON();

//        searchJSON(keyword);
    }


    private void onLoadingSwipeRefresh(final String keyword){
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                searchJSON(keyword);
            }
        });
    }
}