package com.turquoise.hotelbookrecomendation.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.turquoise.hotelbookrecomendation.Fragments.FavouriteFrag;
import com.turquoise.hotelbookrecomendation.Fragments.HomeFrag;
import com.turquoise.hotelbookrecomendation.Fragments.Recommendations;
import com.turquoise.hotelbookrecomendation.R;
import com.turquoise.hotelbookrecomendation.model.Booking;

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


        FloatingActionButton favoriteBtn = findViewById(R.id.searchBtn);
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog();
            }
        });
    }


    private void setupViewPager(final ViewPager viewPager) {
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new HomeFrag(), "Начало");
        viewPagerAdapter.addFrag(new Recommendations(), "Препоръчан");
        viewPagerAdapter.addFrag(new FavouriteFrag(), "История");
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    ((Recommendations) viewPagerAdapter.getItem(position)).updateList();
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
                // Тук може да добавите логика за търсене на хотела
            }
        });
        builder.setNegativeButton("Отказ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

}
