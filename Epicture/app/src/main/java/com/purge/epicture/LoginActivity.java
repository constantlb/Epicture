package com.purge.epicture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private WebView webView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button = findViewById(R.id.button_login);
        webView = findViewById(R.id.webV);
        webView.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebView();
            }
        });
    }

    public void openWebView () {
        if (webView.getVisibility()==View.GONE) {
            webView.setVisibility(View.VISIBLE);
        } else
            webView.setVisibility(View.GONE);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient(this));
        webView.loadUrl("https://api.imgur.com/oauth2/authorize?client_id=dfb5dfa43ee3073&response_type=token&state=APPLICATION_STATE");
    }

    public static class MyWebViewClient extends WebViewClient {

        Context context1;

        public MyWebViewClient(Context context) {
            this.context1 = context;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            String real_url = url.replace("&", "=");
            Log.d("my url :", real_url);
            String[] parts = real_url.split("=");

            if (url.contains("username")) {
                ArrayList<String> profile = new ArrayList<>();
                for(Integer a = 0; a < parts.length; a++) {
                    profile.add(parts[a]);
                }
                imgur_info.access_token = profile.get(2);
                imgur_info.refresh_token = profile.get(8);
                imgur_info.username = profile.get(10);
                Intent intent = new Intent(context1, HomeActivity.class);
                context1.startActivity(intent);
            }
            if (imgur_info.access_token != null) {
                Log.d("access token :", imgur_info.access_token);
            }
            else {
                Log.d("acces token", "null");
            }
      }
    }

        @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        else {
            webView.setVisibility(View.GONE);
        }
        return true;
    }
}