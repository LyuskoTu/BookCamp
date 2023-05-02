package com.turquoise.hotelbookrecomendation.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.turquoise.hotelbookrecomendation.Adapters.RecommendationAdapter;
import com.turquoise.hotelbookrecomendation.Fragments.Recommendation;
import com.turquoise.hotelbookrecomendation.R;
import com.turquoise.hotelbookrecomendation.model.Booking;
import com.turquoise.hotelbookrecomendation.model.Hotel;
import com.turquoise.hotelbookrecomendation.model.CampsResult;
import com.turquoise.hotelbookrecomendation.model.UserCamp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CampInfo extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView hotelImage;
    private TextView hotelDesc, views, visits, completed;
    private ImageButton btnBack;
    private Button save;
    private RecommendationAdapter.HotelViewHolder hotelViewHolder;
    Hotel hotel;
    int pos;
    CampsResult campsResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_info);

        toolbar = findViewById(R.id.toolbarInfo);
        hotelImage = findViewById(R.id.hotelImage);
        hotelDesc = findViewById(R.id.hotelDesc);
        save = findViewById(R.id.confirmBooking);
        btnBack =findViewById(R.id.btnBack);
        views = findViewById(R.id.views);
        visits = findViewById(R.id.draftText);
        completed = findViewById(R.id.completedText);

        Button button = findViewById(R.id.btnContacts);

        Button mapButton = findViewById(R.id.mapButton);

        // Create a Uri object from the Google Maps URL

        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a Uri object from the Google Maps URL
                Uri gmmIntentUri = Uri.parse("geo:42.3652,-71.0621");

                // Create an Intent object and set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);

                // Set the package name of the Google Maps app
                mapIntent.setPackage("com.google.android.apps.maps");

                // Start the Google Maps app
                startActivity(mapIntent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CampInfo.this);
                builder.setView(R.layout.contacts_popup);
                builder.setPositiveButton("OK", null);
                builder.show();

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        hotel = (Hotel) getIntent().getExtras().getSerializable("data");


        campsResult = new Gson().fromJson(getHotels(), CampsResult.class);
        pos = getIntent().getExtras().getInt("pos");

        toolbar.setTitle(hotel.getName());

        setSupportActionBar(toolbar);
        Picasso
                .with(CampInfo.this)
                .load(Uri.parse(hotel.getImageUrl()))
                .into(hotelImage);
        hotelDesc.setText(hotel.getDescription());

        views.setText(campsResult.getHotels().get(pos).getVisits() + " views");
        visits.setText(campsResult.getHotels().get(pos).getDraftBookings() + " drafts");
        completed.setText(campsResult.getHotels().get(pos).getCompletedBookings() + " booked");


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setBooking(true);
                finish();

            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setBooking(false);
//                MainActivity.updatec(1);
                Log.d("ononon", "onClick: ");
                finish();

            }
        });
    }

    public String getHotels() {
        SharedPreferences sp = getSharedPreferences("hotel", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        if (sp.contains("data")) {
            return sp.getString("data", null);
        } else {
            return null;
        }

    }

    private void setBooking(Boolean complete) {


        UserCamp hotel1 = new UserCamp();
        hotel1.setName(hotel.getName());
        hotel1.setCompleted(complete);
        hotel1.setTags(hotel.getTags());

        Booking booking = new Booking();

        List<UserCamp> userCamps = MainActivity.bookings.getUserHotels();
        userCamps.add(hotel1);

        MainActivity.bookings.setUserHotels(userCamps);

        Set<String> s = new HashSet<>();
        for (UserCamp userCamp : MainActivity.bookings.getUserHotels()) {
            if (userCamp.getCompleted()) {
                for (String ss : userCamp.getTags().split("\n")) {
                    ss = ss.replace("null", "");
                    s.add(ss);
                }

            }
        }
        String sa = "";

        for (String sss : s) {
            sa += sss;
            Recommendation.tagSet.add(sss);
        }

        if (complete) {
            int c = Integer.valueOf(campsResult.getHotels().get(pos).getCompletedBookings());
            campsResult.getHotels().get(pos).setCompletedBookings(String.valueOf(c + 1));
            storeUpdates(campsResult);
        } else {
            int c = Integer.valueOf(campsResult.getHotels().get(pos).getDraftBookings());
            campsResult.getHotels().get(pos).setDraftBookings(String.valueOf(c + 1));
            storeUpdates(campsResult);
        }

    }

    public void storeUpdates(CampsResult campsResult) {
        SharedPreferences.Editor spe = getSharedPreferences("hotel", Context.MODE_PRIVATE).edit();
        String save = new Gson().toJson(campsResult);
        spe.putString("data", save);
        spe.apply();


    }

    public void showContactsPopUp(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.contacts_popup, null);

        PopupWindow popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }
    public void onCloseButtonClick(View view) {
        onBackPressed();
    }

}
