package com.example.tourapplication.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.tourapplication.R;
import com.example.tourapplication.view.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserMainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private FrameLayout container;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private String currentemail = user.getEmail();
    private DocumentReference ref1 = firestore.collection("User").document(currentemail);
    private Fragment selectedFragment = null;
    private TextView title_tv ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        HidePostIcon();
        getSupportActionBar().hide();
        title_tv = findViewById(R.id.title_tv);



        PostIconClick();

        container = findViewById(R.id.container);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);






        bottomNavigationView.setBackground(null);

        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);


        if (savedInstanceState== null){
            ref1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.getResult().exists()){
                        if (task.getResult().get("type").toString().equalsIgnoreCase("0")){

                            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                                    new UserHomeFragment()).commit();
                        }else
                        {
                            getSupportFragmentManager().beginTransaction().replace(R.id.container,
                                    new ProfileFragment()).commit();

                        }
                    }
                }
            });

        }



    }



    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.navigation_profile:
                            selectedFragment = new ProfileFragment();

                            break;
                        case R.id.navigation_home:
                            selectedFragment = new UserHomeFragment();

                            break;
                            case R.id.navigation_search:
                            selectedFragment = new SearchFragment();

                                break;
                            case R.id.navigation_Uploadimg:
                            selectedFragment = new UploadImgFragment();

                                break;
                            case R.id.navigation_notification:
                            selectedFragment = new NotificationFragment();

                                break;



                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.container,
                            selectedFragment).commit();

                    return true;
                }
            };

    private void HidePostIcon(){
        ref1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
           if (task.getResult().exists()){
               if (task.getResult().get("type").toString().equalsIgnoreCase("0")){

                   title_tv.setText(R.string.app_name);

               }else
               {


               }
           }
            }
        });

    }
    private void PostIconClick(){
        ref1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
           if (task.getResult().exists()){
               if (task.getResult().get("type").toString().equalsIgnoreCase("0")){
                   bottomNavigationView.setVisibility(View.VISIBLE);



               }else {


               }
           }
            }
        });
    }


}