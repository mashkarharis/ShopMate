package com.huawei.shopmate.model;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huawei.shopmate.R;

import java.util.ArrayList;

public class SiteAdapter extends RecyclerView.Adapter {
    ArrayList sitedatalist;
    public SiteAdapter(ArrayList sitedatalist) {
        this.sitedatalist=sitedatalist;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.site_card, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        MyViewHolder viewhold=(MyViewHolder) viewHolder;
        ArrayList data= (ArrayList) sitedatalist.get(i);
        //Random rnd = new Random();
        int currentColor = Color.WHITE;
        viewhold.parent.setBackgroundColor(currentColor);
        viewhold.name.setText("NAME: "+String.valueOf(data.get(0)));
        viewhold.address.setText("ADDRESS: "+String.valueOf(data.get(1)));
        viewhold.phone.setText("PHONE: "+String.valueOf(data.get(2)));
        viewhold.distance.setText("DISTANCE: "+String.valueOf(data.get(3))+" M");
        viewhold.rating.setText(String.valueOf("RATINGS: "+data.get(6))+" / 5.0 ");

    }


    @Override
    public int getItemCount() {
        return sitedatalist.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,address,phone,distance,rating;
        LinearLayout parent;
        public MyViewHolder(View itemView) {
            super(itemView);
            parent = itemView.findViewById(R.id.parent);
            name = itemView.findViewById(R.id.name);
            address = itemView.findViewById(R.id.address);
            phone = itemView.findViewById(R.id.phone);
            distance = itemView.findViewById(R.id.distance);
            rating = itemView.findViewById(R.id.rating);
        }
    }
}
