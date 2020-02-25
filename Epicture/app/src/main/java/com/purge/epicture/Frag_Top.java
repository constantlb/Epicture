package com.purge.epicture;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Frag_Top extends Fragment {

    private RecyclerView recyclerView;
    private List<MyObject> imgurImages = new ArrayList<>();
    public AdapterTop adapter;
    View view;

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_frag_top, container, false);

        final Context ctx = this.getContext();

        imageRequest(new imgur_info.VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                addImages();
                recyclerView = view.findViewById(R.id.recyclerViewTop);
                recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
                adapter = new AdapterTop(imgurImages);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new AdapterTop.OnItemClickListener() {
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
        return view;
    }


    private void imageRequest(final imgur_info.VolleyCallback cb) {
        String urlImages = "https://api.imgur.com/3/gallery/top/top/";
        Log.d("URL account", urlImages);

        JsonObjectRequest images = new JsonObjectRequest(Request.Method.GET, urlImages, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Result", response.toString());
                    imgur_info.jsonArrayImagesTop = response.getJSONArray("data");
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
        Volley.newRequestQueue(this.getContext()).add(images);
    }


    private void addImages() {
        int i = 0;
        while (i <= 200) {
            try {
                if (imgur_info.jsonArrayImagesTop.getJSONObject(i).has("images")) {
                    JSONArray arrayImages = imgur_info.jsonArrayImagesTop.getJSONObject(i).getJSONArray("images");

                    Boolean animated = arrayImages.getJSONObject(0).getBoolean("animated");
                    if (animated == false) {
                        String id = arrayImages.getJSONObject(0).getString("id");
                        Log.d("imageid", id);
                        imgur_info.imageIDTop.add(id);
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

        String theID = imgur_info.imageIDTop.get(position);
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
        Volley.newRequestQueue(this.getContext()).add(stringRequest);
        Log.d("Request favorites", stringRequest.toString());
    }

}
