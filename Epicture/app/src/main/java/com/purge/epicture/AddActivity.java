package com.purge.epicture;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    Button buttonPickImage;
    Button buttonUpload;
    private EditText title;
    private EditText description;

    ImageView imageThumbnails;
    String imgfinal;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceStat) {
        super.onCreate(savedInstanceStat);
        overridePendingTransition(0, 0);
        setContentView(R.layout.acitivity_add);


        buttonPickImage = findViewById(R.id.pickImageButton);
        imageThumbnails = findViewById(R.id.imageThumbnails);
        title = findViewById(R.id.titleInput);
        description = findViewById(R.id.descriptionInput);
        buttonUpload = findViewById(R.id.uploadButton);

        buttonPickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (imgfinal != null) {
                    uploadRequest();
                }
                else {
                    Toast.makeText(AddActivity.this, "Please, select a image from the gallery", Toast.LENGTH_LONG).show();
                }
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.ic_home:
                        Intent intent1 = new Intent(AddActivity.this, HomeActivity.class);
                        startActivity(intent1);
                        break;
                    case R.id.ic_search:
                        Intent intent2 = new Intent(AddActivity.this, SearchActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.ic_add:
                        break;
                    case R.id.ic_profile_person:
                        Intent intent4 = new Intent(AddActivity.this, ProfileActivity.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });
    }



    // upload image with volley


    private void uploadRequest() {

        String UPLOAD_IMAGE_URL = "https://api.imgur.com/3/image";
        JSONObject body = new JSONObject();
        try {
            body.put("image", imgfinal);
            body.put("title", title.getText().toString());
            body.put("description", description.getText().toString());
        } catch (JSONException e) {
            return;
        }
        final String bodyFinal = body.toString();

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, UPLOAD_IMAGE_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(AddActivity.this, "Image upload success", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getMessage() != null) {
                    Log.d("error message", error.getMessage());
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

            @Override
            public byte[] getBody() {
                return bodyFinal.getBytes(StandardCharsets.UTF_8);
            }
        };
        Volley.newRequestQueue(AddActivity.this).add(stringRequest);
        Log.d("Request", stringRequest.toString());
    }



    // Pick image from gallery


    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }



    // ImageUri to Bitmap


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        try {
            if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
                final Uri imageUri = data.getData();
                imageThumbnails.setImageURI(imageUri);
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                String encodedImage = encodeImage(selectedImage);
                imgfinal = encodedImage;
                System.out.println("Encoded" + encodedImage);
            }
        } catch (Exception e) {
            System.out.println("Error catch in AddAcitivty ImageUri to bitmap");
        }
    }



    // Encode Bitmap in base64


    private String encodeImage(Bitmap bm)
    {
        String encImage = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG,50,baos);
            byte[] b = baos.toByteArray();
            encImage = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return encImage;
    }
}