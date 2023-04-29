package com.turquoise.hotelbookrecomendation.Activities;

import android.content.DialogInterface;
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
import com.turquoise.hotelbookrecomendation.database.CampDatabase;
import com.turquoise.hotelbookrecomendation.model.Booking;
import com.turquoise.hotelbookrecomendation.model.User;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Serializable {

    private static Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static Booking bookings=new Booking();



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

        //HERE ADD MOCK USERS
        CampDatabase userDatabase = Room.databaseBuilder(getApplicationContext(),
                        CampDatabase.class, "user_database")
                .fallbackToDestructiveMigration()
                .build();

        List<User> users = new ArrayList<>();
        users.add(new User("johnnyy","John","Doe","pass"));

        new Thread(() -> {
            userDatabase.userDao().insertUser(users.get(0));
            Log.d("TAG","INSERTED!!!");
        }).start();



        FloatingActionButton favoriteBtn = findViewById(R.id.favoriteBtn);
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
        viewPagerAdapter.addFrag(new Recommendation(), "Препоръчан");
        viewPagerAdapter.addFrag(new FavouriteFrag(),"Запазени");
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
