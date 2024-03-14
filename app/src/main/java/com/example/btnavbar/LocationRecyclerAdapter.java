package com.example.btnavbar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LocationRecyclerAdapter extends RecyclerView.Adapter<LocationRecyclerAdapter.MyViewHolder>{
    Context context;
    ArrayList<Locations> arrayList;
    long start;
    long destination;
    public LocationRecyclerAdapter(Context context, ArrayList<Locations> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }
    @NonNull
    @Override
    public LocationRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_location_rowlayout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationRecyclerAdapter.MyViewHolder holder, int position) {
        Locations locations = arrayList.get(position);
        holder.LocationName.setText(locations.getLocation());
        holder.LocationAddress.setText(locations.getAddress());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomCalendarView.start.setText(holder.LocationAddress.getText());
                ((LocationActivity)LocationActivity.context).finish();


            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView LocationName;
        private TextView LocationAddress;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            LocationName = itemView.findViewById(R.id.locationName);
            LocationAddress = itemView.findViewById(R.id.locationAddress);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }


}
