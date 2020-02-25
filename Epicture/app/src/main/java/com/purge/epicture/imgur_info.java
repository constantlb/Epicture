package com.purge.epicture;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class imgur_info {

    public static String Client_ID = "dfb5dfa43ee3073";
    public static String access_token = "";
    public static String refresh_token = "";
    public static String username = "";
    public static String reputation = "";
    public static String reputationPoints = "";
    public static String urlAvat = "";
    public static String urlCover = "";
    public static Bitmap imgAvatar = null;
    public static JSONArray jsonArrayImages = new JSONArray();
    public static JSONArray jsonArrayImagesPosts = new JSONArray();
    public static JSONArray jsonArrayImagesFavorites = new JSONArray();
    public static JSONArray jsonArrayImagesSearch = new JSONArray();
    public static List<String> imageID = new ArrayList<>();
    public static List<String> imageIDPosts = new ArrayList<>();
    public static List<String> imageIDFavorites = new ArrayList<>();
    public static List<String> imageIDSearch = new ArrayList<>();
    public static String search = "https://api.imgur.com/3/gallery/search/?q=";
    public static String Fsearch = "";
    public static JSONArray jsonArrayImagesRandom = new JSONArray();
    public static JSONArray jsonArrayImagesTop = new JSONArray();
    public static JSONArray jsonArrayImagesHot = new JSONArray();
    public static List<String> imageIDRandom = new ArrayList<>();
    public static List<String> imageIDTop = new ArrayList<>();
    public static List<String> imageIDHot = new ArrayList<>();


    public interface VolleyCallback {
        void onSuccessResponse(JSONObject result);
    }
}

class MyObject {
    public String text;
    public String imageUrl;

    public MyObject(String text, String imageUrl) {
        this.text = text;
        this.imageUrl = imageUrl;
    }
}
