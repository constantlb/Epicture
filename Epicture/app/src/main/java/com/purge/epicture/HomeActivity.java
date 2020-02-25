package com.purge.epicture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.purge.epicture.filters.param.main.SectionsPagerAdapterFilter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<MyObject> imgurImages = new ArrayList<>();
    public AdapterHome adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        setContentView(R.layout.activity_home);

        SectionsPagerAdapterFilter sectionsPagerAdapter = new SectionsPagerAdapterFilter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pagerFilters);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabsFilters);
        tabs.setupWithViewPager(viewPager);

        accountbaseRequest(new imgur_info.VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

            }
        });

        coverRequest(new imgur_info.VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

            }
        });
        avatarRequest(new imgur_info.VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {

            }
        });


        // Page Manager

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_home:
                        Intent intent1 = new Intent(HomeActivity.this, HomeActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.ic_search:
                        Intent intent2 = new Intent(HomeActivity.this, SearchActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.ic_add:
                        Intent intent3 = new Intent(HomeActivity.this, AddActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.ic_profile_person:
                        Intent intent4 = new Intent(HomeActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });
    }

    private void accountbaseRequest(final imgur_info.VolleyCallback cb) {
        String url = "https://api.imgur.com/3/account/" + imgur_info.username;

        JsonObjectRequest accountbase = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    imgur_info.reputation = response.getJSONObject("data").getString("reputation_name");
                    imgur_info.reputationPoints = response.getJSONObject("data").getString("reputation");
                    cb.onSuccessResponse(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Client-ID " + imgur_info.Client_ID);
                return headers;
            }
        };
        Volley.newRequestQueue(HomeActivity.this).add(accountbase);
    }

    private void avatarRequest(final imgur_info.VolleyCallback cb) {
        String urlAvatar = "https://api.imgur.com/3/account/" + imgur_info.username + "/avatar";

        JsonObjectRequest avatarpicture = new JsonObjectRequest(Request.Method.GET, urlAvatar, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Result", response.toString());
                    imgur_info.urlAvat = response.getJSONObject("data").getString("avatar");
                    cb.onSuccessResponse(response);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer " + imgur_info.access_token);
                return headers;
            }
        };
        Volley.newRequestQueue(HomeActivity.this).add(avatarpicture);
    }


    private void coverRequest(final imgur_info.VolleyCallback cb) {
        String urlCover = "https://api.imgur.com/3/account/" + imgur_info.username + "/cover";

        JsonObjectRequest coverpicture = new JsonObjectRequest(Request.Method.GET, urlCover, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Result cover", response.toString());
                    imgur_info.urlCover = response.getJSONObject("data").getString("cover");
                    cb.onSuccessResponse(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Authorization", "Bearer " + imgur_info.access_token);
                return headers;
            }
        };
        Volley.newRequestQueue(HomeActivity.this).add(coverpicture);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }
}

