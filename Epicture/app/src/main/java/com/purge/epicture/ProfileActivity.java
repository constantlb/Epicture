package com.purge.epicture;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.purge.epicture.ui.main.SectionsPagerAdapter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceStat) {
        super.onCreate(savedInstanceStat);
        overridePendingTransition(0, 0);
        setContentView(R.layout.acitivity_profile);


        // Navigation between pages (navbar config)

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_home:
                        Intent intent1 = new Intent(ProfileActivity.this, HomeActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.ic_search:
                        Intent intent2 = new Intent(ProfileActivity.this, SearchActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.ic_add:
                        Intent intent3 = new Intent(ProfileActivity.this, AddActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.ic_profile_person:
                        break;
                }
                return false;
            }
        });

        // Set texts

        TextView PseudoTittle = findViewById(R.id.pseudo);
        TextView ReputationText = findViewById(R.id.reputation);
        TextView rp = findViewById(R.id.rp);
        ImageView profileView = findViewById(R.id.imgViewProfile);
        ImageView backgroundView = findViewById(R.id.imgBackgroundProfile);

        PseudoTittle.setText(imgur_info.username);
        ReputationText.setText(imgur_info.reputation);
        rp.setText(imgur_info.reputationPoints);

        if (imgur_info.urlAvat != null) {
            Log.d("imgAvatar link", imgur_info.urlAvat);
            Picasso.get().load(imgur_info.urlAvat).into(profileView);
        }
        if (imgur_info.urlCover != null) {
            Log.d("imgCover link", imgur_info.urlCover);
            Picasso.get().load(imgur_info.urlCover).into(backgroundView);
        }
    }
}
