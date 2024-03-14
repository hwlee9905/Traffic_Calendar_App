package com.example.btnavbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Timecalc extends AppCompatActivity {
    String start;
    String destination;
    String sx, sy, ex, ey, totalTime;
    RequestQueue requestQueue;
    public Timecalc(RequestQueue requestQueue, String start, String destination) {
        this.start = start;
        this.destination = destination;
        this.requestQueue = requestQueue;
    }
    public String getSx() {
        return sx;
    }

    public void setSx(String sx) {
        this.sx = sx;
    }

    public String getSy() {
        return sy;
    }

    public void setSy(String sy) {
        this.sy = sy;
    }

    public String getEx() {
        return ex;
    }

    public void setEx(String ex) {
        this.ex = ex;
    }

    public String getEy() {
        return ey;
    }

    public void setEy(String ey) {
        this.ey = ey;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public RequestQueue getRequestQueue() {
        return requestQueue;
    }

    public void setRequestQueue(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public void Getlat(String address, final VolleyCallback callback) {


        String apikey = "C8FF2F7C-CF6D-3E55-9BDE-303E30C2AA1F";
        String searchType = "road";

        String epsg = "epsg:4326";

        StringBuilder sb = new StringBuilder("https://api.vworld.kr/req/address");
        sb.append("?service=address");
        sb.append("&request=getCoord");
        sb.append("&format=json");
        sb.append("&crs=" + epsg);
        sb.append("&key=" + apikey);
        sb.append("&type=" + searchType);
        sb.append("&address=" + URLEncoder.encode(address, StandardCharsets.UTF_8));
        Log.v("address", address);

        URL url = null;
        try {
            url = new URL(sb.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        StringRequest request = new StringRequest(Request.Method.GET, url.toString(), new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                return params;
            }

        };


        request.setShouldCache(false);
        requestQueue.add(request);
    }
    public void GetTotaltime(String sx, String sy, String ex, String ey, final VolleyCallback callback) {


        String urlInfo; //https://api.odsay.com/v1/api/searchPubTransPathT?SX=126.9027279&SY=37.5349277&EX=126.9145430&EY=37.5499421&apiKey=LJ7FHvNk8f6VYY/Ud+zz+jsIFkXOnB/uNaolZGyWMVQ

        String apikey = "C4UNy+miMRMaCyjltq6yJ4TNF+sLpp0Fw+4C8QedD/U";

        try {
            urlInfo = "https://api.odsay.com/v1/api/searchPubTransPathT?SX="+sx+"&SY="+sy+"&EX="+ex+"&EY="+ey+"&apiKey="
                    + URLEncoder.encode(apikey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


        StringRequest request = new StringRequest(Request.Method.GET, urlInfo, new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };


        request.setShouldCache(false);
        requestQueue.add(request);
    }

}
