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

public class Frag1_posts extends Fragment {

    private RecyclerView recyclerView;
    private List<MyObject> imgurImages = new ArrayList<>();
    public AdapterPosts adapter;
    View view;

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_frag1_posts, container, false);

        final Context ctx = this.getContext();

        postsRequest(new imgur_info.VolleyCallback() {
            @Override
            public void onSuccessResponse(JSONObject result) {
                addImagesPosts();
                recyclerView = view.findViewById(R.id.recyclerViewPosts);
                recyclerView.setLayoutManager(new LinearLayoutManager(ctx));
                adapter = new AdapterPosts(imgurImages);
                recyclerView.setAdapter(adapter);
                adapter.setOnItemClickListener(new AdapterPosts.OnItemClickListener() {
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


    private void postsRequest(final imgur_info.VolleyCallback cb) {
        RequestQueue queue2 = Volley.newRequestQueue(this.getContext());
        String urlImages = "https://api.imgur.com/3/account/me/images";

        JsonObjectRequest postsProfile = new JsonObjectRequest(Request.Method.GET, urlImages, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("account images", response.toString());
                    imgur_info.jsonArrayImagesPosts = response.getJSONArray("data");
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

        queue2.add(postsProfile);
    }


    private void addImagesPosts() {
        int i = 0;
        while (i <= 200) {
            try {
                String objTitle = imgur_info.jsonArrayImagesPosts.getJSONObject(i).getString("title");
                String objLink = imgur_info.jsonArrayImagesPosts.getJSONObject(i).getString("link");
                String objID = imgur_info.jsonArrayImagesPosts.getJSONObject(i).getString("id");

                imgur_info.imageIDPosts.add(objID);

                if (objTitle != "null") {
                    imgurImages.add(new MyObject(objTitle, objLink));
                } else {
                    imgurImages.add(new MyObject("", objLink));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            i += 1;
        }
    }


    private void favRequest(int position) {

        String theID = imgur_info.imageIDPosts.get(position);
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
