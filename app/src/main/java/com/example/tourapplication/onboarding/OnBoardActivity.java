package com.example.tourapplication.onboarding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.tourapplication.R;
import com.example.tourapplication.adapters.OnBoardAdapter;
import com.example.tourapplication.authentication.SignInActivity;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

public class OnBoardActivity extends AppCompatActivity {
    private ViewPager viewPager;
    FrameLayout vb;
    private OnBoardAdapter adapter;
    Button next;
    DotsIndicator tab_indicator;
    int x = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);

        getSupportActionBar().hide();

        tab_indicator = findViewById(R.id.tab_indicator);
        viewPager = findViewById(R.id.view_pager);
        vb = findViewById(R.id.vb);
        next = findViewById(R.id.next);

        adapter = new OnBoardAdapter(getSupportFragmentManager());



        viewPager.setAdapter(adapter);
        tab_indicator.setViewPager(viewPager);

        viewPager.setCurrentItem(0);
        adapter.setTimer(viewPager, 2, x);
        viewPager.setOffscreenPageLimit(1);

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                x = position;

                if (position == 3) {
                    adapter.stopTimer();


                } else {


                }

            }


            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                goLogin();
            }
        });




    }

    @Override
    protected void onRestart() {
        viewPager.setCurrentItem(x);
        adapter.setTimer(viewPager, 3, x);
        super.onRestart();

    }

    @Override
    public void onBackPressed() {
        viewPager.setCurrentItem(x);
        adapter.setTimer(viewPager, 3, x);
        super.onBackPressed();
    }


    public void goLogin() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }


}
