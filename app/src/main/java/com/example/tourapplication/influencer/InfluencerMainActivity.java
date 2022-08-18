package com.example.tourapplication.influencer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.tourapplication.R;
import com.example.tourapplication.view.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InfluencerMainActivity extends AppCompatActivity {

    BottomNavigationView infbottomNavigationView;
    private Fragment selectedFragment = null;
    private TextView welcome_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_influencer);

        welcome_tv = findViewById(R.id.welcome_tv);
        infbottomNavigationView = findViewById(R.id.infbottomNavigationView);
        infbottomNavigationView.setBackground(null);

//        Animation a = AnimationUtils.loadAnimation(this, R.anim.fade_out);
//        Animation b = AnimationUtils.loadAnimation(this, R.anim.fade_in);
//        a.reset();
//        welcome_tv.startAnimation(a);
//        infbottomNavigationView.startAnimation(b);
//        infbottomNavigationView.getMenu().getItem(0).setEnabled(false);
//        infbottomNavigationView.getMenu().getItem(1).setEnabled(false);



                if (savedInstanceState == null) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.inf_container,
                            new CreateFragment()).commit();
                }



//
//        final Handler handler = new Handler(Looper.getMainLooper());
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                welcome_tv.clearAnimation();
//                infbottomNavigationView.setVisibility(View.VISIBLE);
//                infbottomNavigationView.getMenu().getItem(0).setEnabled(true);
//                infbottomNavigationView.getMenu().getItem(1).setEnabled(true);
//                welcome_tv.setVisibility(View.GONE);

//
//            }
//        }, 3000);

        infbottomNavigationView.setOnNavigationItemSelectedListener(navListener);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.influemcer_create:
                            selectedFragment = new CreateFragment();
                            break;
                        case R.id.influencer_profile:
                            selectedFragment = new ProfileFragment();

                            break;

                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.inf_container,
                            selectedFragment).commit();

                    return true;
                }
            };


}