package com.example.btnavbar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.MyViewHolder> {


    RequestQueue requestQueue;
    Context context;
    ArrayList<Events> arrayList;
    DBOpenHelper dbOpenHelper;



    public EventRecyclerAdapter(Context context, ArrayList<Events> arrayList) {

        this.context = context;
        this.arrayList = arrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_events_rowlayout,parent,false);
        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(view.getContext());
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Events events = arrayList.get(position);
        Getlat(events.getSTART(), new VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject res = jsonObject.getJSONObject("response");
                    JSONObject result = res.getJSONObject("result");
                    JSONObject point = result.getJSONObject("point");
                    holder.sx = point.get("x").toString();
                    holder.sy = point.get("y").toString();
                    Log.v("holder.start","");
                    Getlat(events.getDESTINATION(), new VolleyCallback() {
                        @Override
                        public void onSuccess(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject res = jsonObject.getJSONObject("response");
                                JSONObject result = res.getJSONObject("result");
                                JSONObject point = result.getJSONObject("point");
                                holder.ex = point.get("x").toString();
                                holder.ey = point.get("y").toString();
                                Log.v("holder.destination","");
                                GetTotaltime(holder.sx, holder.sy, holder.ex, holder.ey, new VolleyCallback() {
                                    @Override
                                    public void onSuccess(String response2) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response2);
                                            JSONObject result = new JSONObject(jsonObject.getString("result"));
                                            JSONArray path = result.getJSONArray("path");
                                            JSONObject pathobj = path.getJSONObject(0);
                                            JSONObject info = pathobj.getJSONObject("info");
                                            holder.totalTime = info.getString("totalTime");
                                            Log.v("holder.totaltime","");
                                            int intotalTime = Integer.valueOf(holder.totalTime);
                                            int hour=0, minute=0;

                                            if(intotalTime > 60){
                                                hour = intotalTime / 60;
                                                minute = intotalTime % 60;
                                                String shour = Integer.toString(hour);
                                                String sminute = Integer.toString(minute);
                                                holder.calcTime.setText(shour + "시간 " + sminute + "분 소요" );
                                            }
                                            else{
                                                holder.calcTime.setText(holder.totalTime + "분 소요" );
                                            }
//                                            String url = "kakaomap://route?sp="+holder.sy+","+holder.sx+"&ep="+holder.ey+","+holder.ex+"&by=PUBLICTRANSIT";
                                            String url = "nmap://route/public?slat="+holder.sy+"&slng="+holder.sx+"&sname="+events.getSTART()+"&dlat="+holder.ey+"&dlng="+holder.ex+"&dname="+events.getDESTINATION()+"&appname=com.example.btnavbar";
                                            holder.search.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    Log.v("kakaotest",holder.sx);
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                                    context.startActivity(intent);
                                                }
                                            });



                                        } catch (JSONException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                });
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        holder.Event.setText(events.getEVENT());
        holder.DateText.setText(events.getDATE());
        holder.Time.setText(events.getTIME());
        holder.Start.setText(events.getSTART());
        Log.v("startsettext","");
        holder.Destination.setText(events.getDESTINATION());
        Log.v("dessettext","");
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteCalendarEvent(events.getEVENT(), events.getDATE(), events.getTIME());
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView DateText, Event, Time, Start, Destination, calcTime;
        Button delete;
        ImageButton search;
        String sx, sy, ex, ey, totalTime;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            DateText = itemView.findViewById(R.id.eventdate);
            Event = itemView.findViewById(R.id.eventname);
            Time = itemView.findViewById(R.id.eventime);
            Start = itemView.findViewById(R.id.start);
            Destination = itemView.findViewById(R.id.destination);
            calcTime = itemView.findViewById(R.id.calctime);
            delete = itemView.findViewById(R.id.delete);
            search = itemView.findViewById(R.id.search);
        }
    }
    private void deleteCalendarEvent (String event, String date, String time){
        dbOpenHelper = new DBOpenHelper(context);
        SQLiteDatabase database = dbOpenHelper.getWritableDatabase();
        dbOpenHelper.deleteEvent(event, date, time, database);
        dbOpenHelper.close();
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

        URL url2 = null;
        try {
            url2 = new URL(sb.toString());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        StringRequest request = new StringRequest(Request.Method.GET, url2.toString(), new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                Log.v("getlat","");
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
            urlInfo = "https://api.odsay.com/v1/api/searchPubTransPathT?&SX="+sx+"&SY="+sy+"&EX="+ex+"&EY="+ey+"&apiKey="
                    + URLEncoder.encode(apikey, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }


        StringRequest request = new StringRequest(Request.Method.GET, urlInfo, new Response.Listener<String>() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                Log.v("getotaltime","");
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