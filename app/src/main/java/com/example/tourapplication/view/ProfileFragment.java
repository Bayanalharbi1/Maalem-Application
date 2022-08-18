package com.example.tourapplication.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourapplication.ManageFollowingFragment;
import com.example.tourapplication.R;
import com.example.tourapplication.authentication.SignInActivity;
import com.example.tourapplication.user.SettingsFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private String currentemail = user.getEmail();
    private TextView  aac_type, username;
    private LinearLayout logout;
    private DocumentReference  ref1 = firestore.collection("User").document(currentemail);;





    RelativeLayout personalinfoLayout, settingslayout, managefolloing_layout;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    CircularImageView authorAvatarIv;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment



        authorAvatarIv = v.findViewById(R.id.authorAvatarIv);
        personalinfoLayout = v.findViewById(R.id.personalinfoLayout);
        settingslayout = v.findViewById(R.id.settingslayout);
        managefolloing_layout = v.findViewById(R.id.managefolloing_layout);
        aac_type = v.findViewById(R.id.aac_type);
        username = v.findViewById(R.id.username);
        logout = v.findViewById(R.id.logout);


        ref1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().get("type").toString().equalsIgnoreCase("0")) {
                        managefolloing_layout.setVisibility(View.GONE);
                    } else {
                        managefolloing_layout.setVisibility(View.VISIBLE);


                    }
                }
            }
        });




        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), SignInActivity.class));
                Toast.makeText(getActivity(), R.string.logout_success, Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        });

        personalinfoLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().get("type").toString().equals("0")) {
                                getParentFragmentManager().beginTransaction().replace(R.id.container, new ProfileDetailsFragment()).addToBackStack(null).commit();
                            } else {
                                getParentFragmentManager().beginTransaction().replace(R.id.inf_container, new ProfileDetailsFragment()).addToBackStack(null).commit();

                            }
                        }
                    }
                });

            }
        });
        settingslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ref1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().get("type").toString().equals("0")) {
                                getParentFragmentManager().beginTransaction().replace(R.id.container, new SettingsFragment() ,"settings").addToBackStack(null).commit();
                            } else {
                                getParentFragmentManager().beginTransaction().replace(R.id.inf_container, new SettingsFragment() , "settings").addToBackStack(null).commit();

                            }
                        }
                    }
                });
            }
        });

        managefolloing_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getParentFragmentManager().beginTransaction().replace(R.id.inf_container, new ManageFollowingFragment()).addToBackStack(null).commit();

            }
        });

        ref1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {

                    if (task.getResult().getString("imageUrl") != null) {
                        if (task.getResult().get("type").toString().equals("0")) {
                            aac_type.setText("Tourist Account");
                        } else {
                            aac_type.setText("Influencer Account");

                        }
                        username.setText(task.getResult().getString("userName"));
                        Picasso.get().load(task.getResult().getString("imageUrl")).into(authorAvatarIv);

                    } else {
                      //  Toast.makeText(getActivity(), "Your image is not found", Toast.LENGTH_SHORT).show();
                        authorAvatarIv.setImageDrawable(getResources().getDrawable(R.drawable.ic_person));                    }

                }
            }
        });

        ref1.
                collection("Posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    }
                });


        return v;
    }


}