package com.turquoise.hotelbookrecomendation.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.turquoise.hotelbookrecomendation.Activities.CampInfo;
import com.turquoise.hotelbookrecomendation.R;
import com.turquoise.hotelbookrecomendation.model.Hotel;
import com.turquoise.hotelbookrecomendation.model.CampsResult;

import java.util.List;

public class CampAdapter extends RecyclerView.Adapter<CampAdapter.HotelViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private View view;
    private HotelViewHolder hotelViewHolder;
    private List<Hotel> hotels;
    private CampsResult campsResult = new CampsResult();

    public CampAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setHotels(List<Hotel> lists) {
        this.hotels = lists;
        this.campsResult.setHotels(hotels);
        CampsResult campsResult = new CampsResult();
        campsResult.setHotels(hotels);
        storeUpdates(campsResult);
        notifyDataSetChanged();
    }

    public void storeUpdates(CampsResult campsResult) {
        SharedPreferences.Editor spe = context.getSharedPreferences("hotel", Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        spe.putString("data", gson.toJson(campsResult));
        spe.apply();

    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = inflater.inflate(R.layout.hotelcard, parent, false);
        hotelViewHolder = new HotelViewHolder(view);

        return hotelViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Picasso
                .with(context)
                .load(Uri.parse(hotels.get(position).getImageUrl()))
                .into(holder.hotelImage);

        holder.hotelRatings.setText(hotels.get(position).getRatings());
        holder.tags.setText(hotels.get(position).getTags());
        holder.hotelName.setText(hotels.get(position).getName());
        holder.hotelViews.setText(hotels.get(position).getVisits() + "\nViews");
        holder.bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vis = Integer.valueOf(hotels.get(position).getVisits());
                hotels.get(position).setVisits(String.valueOf(++vis));
                setHotels(hotels);
                Intent i = new Intent(context, CampInfo.class);
                i.putExtra("hotels", campsResult);
                i.putExtra("pos", position);
                i.putExtra("data", hotels.get(position));
                context.startActivity(i);

            }
        });

    }


    @Override
    public int getItemCount() {
        return hotels.size();
    }

    class HotelViewHolder extends RecyclerView.ViewHolder {

        ImageView hotelImage;
        TextView hotelRatings, hotelName, hotelViews;

        TextView tags;
        Button bookButton;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            hotelImage = itemView.findViewById(R.id.hotelImage);
            hotelRatings = itemView.findViewById(R.id.ratings);
            bookButton = itemView.findViewById(R.id.hotelBookButton);
            tags = itemView.findViewById(R.id.tagsList);
            hotelName = itemView.findViewById(R.id.hotelName);
            hotelViews = itemView.findViewById(R.id.hotelCardViews);


        }
    }


}
