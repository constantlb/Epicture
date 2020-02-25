package com.purge.epicture;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<MyObject> imgurImages = new ArrayList<>();
    public AdapterSearch adapter;
    EditText input;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceStat) {
        super.onCreate(savedInstanceStat);
        overridePendingTransition(0, 0);
        setContentView(R.layout.acitivity_search);

        input = findViewById(R.id.textInput);
        input.addTextChangedListener(textWatcher);




        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.ic_home:
                        Intent intent1 = new Intent(SearchActivity.this, HomeActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.ic_search:
                        break;
                    case R.id.ic_add:
                        Intent intent3 = new Intent(SearchActivity.this, AddActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.ic_profile_person:
                        Intent intent4 = new Intent(SearchActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            imgur_info.Fsearch = imgur_info.search + s;
        }
        @Override
        public void afterTextChanged(Editable s) {
            imgur_info.jsonArrayImagesSearch = new JSONArray();
            imgur_info.imageIDSearch = new ArrayList<>();
            imgurImages = new ArrayList<>();

            Log.d("link", imgur_info.Fsearch);
            try {
                Thread.sleep(1000);
            }
            catch(InterruptedException e)
            {
            }
            imageRequest(new imgur_info.VolleyCallback() {
                @Override
                public void onSuccessResponse(JSONObject result) {
                    addImages();
                    recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                    adapter = new AdapterSearch(imgurImages);
                    recyclerView.setAdapter(adapter);

                    adapter.setOnItemClickListener(new AdapterSearch.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                        }

                        @Override
                        public void ontLikeClick(int position) {
                            favRequest(position);
                        }
                    });
                }
            });

        }
    };

    private void imageRequest(final imgur_info.VolleyCallback cb) {

        JsonObjectRequest images = new JsonObjectRequest(Request.Method.GET, imgur_info.Fsearch, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Result", response.toString());
                    imgur_info.jsonArrayImagesSearch = response.getJSONArray("data");
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
        Volley.newRequestQueue(SearchActivity.this).add(images);
    }


    private void addImages() {
        int i = 0;
        while (i <= 200) {
            try {
                if (imgur_info.jsonArrayImagesSearch.getJSONObject(i).has("images")) {
                    JSONArray arrayImages = imgur_info.jsonArrayImagesSearch.getJSONObject(i).getJSONArray("images");

                    Boolean animated = arrayImages.getJSONObject(0).getBoolean("animated");
                    if (animated == false) {
                        String id = arrayImages.getJSONObject(0).getString("id");
                        Log.d("imageid", id);
                        imgur_info.imageIDSearch.add(id);
                        String title = arrayImages.getJSONObject(0).getString("title");
                        String link = arrayImages.getJSONObject(0).getString("link");
                        if (title != "null") {
                            imgurImages.add(new MyObject(title, link));
                        } else {
                            imgurImages.add(new MyObject("", link));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            i += 1;
        }
    }

    private void favRequest(int position) {

        String theID = imgur_info.imageIDSearch.get(position);
        String urlFav = "https://api.imgur.com/3/image/" + theID + "/favorite";
        Log.d("URLFAV", urlFav);


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, urlFav, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null) {
                    Log.d("error request", error.getMessage());
                }
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + imgur_info.access_token);
                return headers;
            }
        };
        Volley.newRequestQueue(SearchActivity.this).add(stringRequest);
        Log.d("Request favorites", stringRequest.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }
}
