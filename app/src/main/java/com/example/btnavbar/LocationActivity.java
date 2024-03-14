package com.example.btnavbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class LocationActivity extends AppCompatActivity {
    public RequestQueue requestQueue;
    public RecyclerView locationRV;
    public ArrayList<Locations> locationArrayList;
    public LocationRecyclerAdapter locationRecyclerAdapter;
    public TextInputEditText searchLocation;
    public ImageButton searchbutton;
    public static Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_location_layout);
        context = this;
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        }
        locationRV = findViewById(R.id.LocationRV);
        searchLocation = findViewById(R.id.searchLocation);
        searchbutton = findViewById(R.id.searchButton);
        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLocationInfo(searchLocation.getText().toString());
            }
        });
        locationArrayList = new ArrayList<>();
        locationRecyclerAdapter = new LocationRecyclerAdapter(this.getApplicationContext(),locationArrayList);
        locationRV.setAdapter(locationRecyclerAdapter);
        locationRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getLocationInfo(String Keyword){
        String apikey = "C8FF2F7C-CF6D-3E55-9BDE-303E30C2AA1F";
        String place = "PLACE";

        String epsg = "epsg:4326";

        StringBuilder sb = new StringBuilder("https://api.vworld.kr/req/search");
        sb.append("?service=search");
        sb.append("&request=search");
        sb.append("&format=json");
        sb.append("&crs=" + epsg);
        sb.append("&key=" + apikey);
        sb.append("&type=" + place);
        sb.append("&query=" + URLEncoder.encode(Keyword, StandardCharsets.UTF_8));

        URL url = null;
        try {
            url = new URL(sb.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url.toString(), null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                locationArrayList.clear();

                try {


                    JSONObject locationObj = response.optJSONObject("response");


                    JSONObject resultObj = locationObj.getJSONObject("result");
                    JSONObject recordObj = locationObj.getJSONObject("record");
                    String currentObj = recordObj.getString("current");
                    int current = Integer.valueOf(currentObj);
                    JSONArray itemsArray = resultObj.getJSONArray("items");


                    for(int i=0; i<current; i++){

                        JSONObject itemsObj = itemsArray.getJSONObject(i);
                        String locationName = itemsObj.getString("title");
                        JSONObject addressObj = itemsObj.getJSONObject("address");
                        String locationAddress = addressObj.getString("road");
                        if(locationAddress == null || locationAddress.isEmpty()){
                            continue;
                        }
                        else{
                            locationArrayList.add(new Locations(locationName,locationAddress));
                        }


                    }
                    locationRecyclerAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
//                    throw new RuntimeException(e);
                    e.printStackTrace();
                    Toast.makeText(context.getApplicationContext(), "No Result", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if( error instanceof NetworkError) {
                    Toast.makeText(context.getApplicationContext(), "No Result", Toast.LENGTH_SHORT).show();
                    //handle your network error here.
                } else if( error instanceof ServerError) {
                    Toast.makeText(context.getApplicationContext(), "No Result", Toast.LENGTH_SHORT).show();
                    //handle if server error occurs with 5** status code
                } else if( error instanceof AuthFailureError) {
                    Toast.makeText(context.getApplicationContext(), "No Result", Toast.LENGTH_SHORT).show();
                    //handle if authFailure occurs.This is generally because of invalid credentials
                } else if( error instanceof ParseError) {
                    Toast.makeText(context.getApplicationContext(), "No Result", Toast.LENGTH_SHORT).show();
                    //handle if the volley is unable to parse the response data.
                } else if( error instanceof NoConnectionError) {
                    Toast.makeText(context.getApplicationContext(), "No Result", Toast.LENGTH_SHORT).show();
                    //handle if no connection is occurred
                } else if( error instanceof TimeoutError) {
                    Toast.makeText(context.getApplicationContext(), "No Result", Toast.LENGTH_SHORT).show();
                    //handle if socket time out is occurred.
                }
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

}
