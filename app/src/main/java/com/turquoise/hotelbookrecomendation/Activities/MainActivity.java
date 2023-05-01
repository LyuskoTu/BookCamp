package com.turquoise.hotelbookrecomendation.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.room.Room;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.turquoise.hotelbookrecomendation.Fragments.FavouriteFrag;
import com.turquoise.hotelbookrecomendation.Fragments.HomeFrag;
import com.turquoise.hotelbookrecomendation.Fragments.Recommendation;
import com.turquoise.hotelbookrecomendation.R;
import com.turquoise.hotelbookrecomendation.Utils.Utilities;
import com.turquoise.hotelbookrecomendation.model.Booking;
import com.turquoise.hotelbookrecomendation.model.Hotel;
import com.turquoise.hotelbookrecomendation.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Serializable {

    private static Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static Booking bookings = new Booking();

    private ImageButton logOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolBar);
        toolbar.setTitle("Hotel App");

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        //LOG OUT
        logOut = findViewById(R.id.logoutButton);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logout button click
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity so that the user cannot go back to it with the back button
            }
        });

        //logOut confirmation dialog

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show exit confirmation dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Exit");
                builder.setMessage("Are you sure you want to exit the app?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish(); // Close the activity
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Dismiss the dialog
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        //log out confirmation dialog


        FloatingActionButton favoriteBtn = findViewById(R.id.searchBtn);
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog();
            }
        });
    }

    private String loadJSONStringFromAsset(String fileName) {
        String jsonString = null;
        try {
            InputStream is = getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    private JSONObject loadHotelsFromJSON() {
        String jsonString = loadJSONStringFromAsset("hotels.json"); // зареждане на JSON файл от assets папката
        if (jsonString != null) {
            try {
                return new JSONObject(jsonString);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void setupViewPager(final ViewPager viewPager) {
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new HomeFrag(), "Начало");
        viewPagerAdapter.addFrag(new Recommendation(), "Препоръчан");
        viewPagerAdapter.addFrag(new FavouriteFrag(), "История");
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    ((Recommendation) viewPagerAdapter.getItem(position)).updateList();
                } else if (position == 2) {
                    ((FavouriteFrag) viewPagerAdapter.getItem(position)).updateList();
                } else {
                    ((HomeFrag) viewPagerAdapter.getItem(position)).updateList();
                }
            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });
        viewPager.setAdapter(viewPagerAdapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

        private void showSearchDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Търсене на хотел");
            builder.setMessage("Въведете името на хотела:");
            final EditText input = new EditText(this);
            builder.setView(input);
            builder.setPositiveButton("Търсене", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String hotelName = input.getText().toString();
                    JSONObject hotels = loadHotelsFromJSON(); // зареждане на данните за хотелите от JSON файл
                    if (hotels != null) {
                        try {
                            JSONArray hotelList = hotels.getJSONArray("hotels");
                            for (int i = 0; i < hotelList.length(); i++) {
                                JSONObject hotel = hotelList.getJSONObject(i);
                                String name = hotel.getString("name");
    //                            int views = hotel.getInt("numReviews");
    //                            String imageUrl = hotel.getString("imageUrl");
    //                            hotel.getDouble("rating");
    //                            hotel.getInt("price");


                                if (name.equalsIgnoreCase(hotelName)) { // търсене на хотела по име
    //                                 показване на данните на хотела в диалоговия прозорец
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setView(R.layout.find_success);
                                    builder.setPositiveButton("OK", null);
                                    builder.show();


                                    return;
                                }
                            }
                            // ако няма хотел, отговарящ на името, показва се съобщение
                            AlertDialog.Builder notFoundBuilder = new AlertDialog.Builder(MainActivity.this);
                            notFoundBuilder.setTitle("Хотелът не е намерен");
                            notFoundBuilder.setMessage("Моля, опитайте отново.");
                            notFoundBuilder.setPositiveButton("OK", null);
                            notFoundBuilder.show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            builder.setNegativeButton("Отказ", null);
            builder.show();
        }


}
