package com.example.tourapplication.user;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tourapplication.FaqFragment;
import com.example.tourapplication.PrivacyFragment;
import com.example.tourapplication.Question;
import com.example.tourapplication.R;
import com.example.tourapplication.authentication.SignInActivity;
import com.example.tourapplication.databinding.FragmentSearchBinding;
import com.example.tourapplication.databinding.FragmentSettingsBinding;
import com.example.tourapplication.influencer.CreateFragment;
import com.example.tourapplication.view.ProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mfirebaseFirestore;

    private boolean islanguagee = false;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH1 = "switch1";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        mfirebaseFirestore = FirebaseFirestore.getInstance();

        binding.deleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setTitle(R.string.delete_title);

                builder1.setMessage(R.string.delete_msg);
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mfirebaseFirestore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).delete();
                                dialog.cancel();
                                startActivity(new Intent(getActivity(), SignInActivity.class));
                                FirebaseAuth.getInstance().getCurrentUser().delete();
                                getActivity().finish();

                            }
                        });

                builder1.setNegativeButton(
                        R.string.no,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
        binding.askQLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder askDailog = new AlertDialog.Builder(getActivity());
                askDailog.setTitle(R.string.ask_quetion);
                final EditText question_edittxt = new EditText(getActivity());
                question_edittxt.setInputType(InputType.TYPE_CLASS_TEXT);
                askDailog.setView(question_edittxt);
                String uniqueID = UUID.randomUUID().toString();



                askDailog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mfirebaseFirestore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if (task.isSuccessful()) {
                                    Question question = new Question(task.getResult().getString("mUserName"), question_edittxt.getText().toString().trim(), "null", task.getResult().getString("mImageUrl") , uniqueID);
                                    mfirebaseFirestore.collection("Question").document(uniqueID).set(question).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getActivity(), R.string.question_ad, Toast.LENGTH_SHORT).show();
                                            dialogInterface.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });



                    }
                });




                askDailog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //     dialogInterface.cancel();
                        dialogInterface.dismiss();
                    }
                });

                askDailog.show();

            }
        });


        binding.privacyClickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mfirebaseFirestore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().get("type").toString().equalsIgnoreCase("1")) {
                                getParentFragmentManager().beginTransaction().replace(R.id.inf_container, new PrivacyFragment()).addToBackStack(null).commit();


                            } else {
                                getParentFragmentManager().beginTransaction().replace(R.id.container, new PrivacyFragment()).addToBackStack(null).commit();

                            }
                        }
                    }
                });


            }
        });

        binding.faqLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mfirebaseFirestore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().get("type").toString().equalsIgnoreCase("0")) {
                                getParentFragmentManager().beginTransaction().replace(R.id.container, new FaqFragment()).addToBackStack(null).commit();


                            } else {
                                getParentFragmentManager().beginTransaction().replace(R.id.inf_container, new FaqFragment()).addToBackStack(null).commit();

                            }
                        }
                    }
                });

            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        islanguagee = sharedPreferences.getBoolean(SWITCH1, false);

        Log.d("language", "onView: " + islanguagee);
        if (islanguagee == false) {
            setLocale(getActivity(), "en");

        } else {
            setLocale(getActivity(), "ar");

        }

        binding.languageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!islanguagee) {
                    setLocale(getActivity(), "ar");
                    islanguagee = true;
                    saveData(islanguagee);


                } else {
                    setLocale(getActivity(), "en");
                    islanguagee = false;
                    saveData(islanguagee);


                }

                Log.d("language", "onClick: " + islanguagee);

            }
        });
        return binding.getRoot();
    }


    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public void saveData(Boolean clicked) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        editor.putBoolean(SWITCH1, clicked);

        editor.apply();

        Toast.makeText(getActivity(), R.string.lang_success, Toast.LENGTH_SHORT).show();
        mfirebaseFirestore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().get("type").toString().equalsIgnoreCase("0")){
                        getParentFragmentManager().beginTransaction().replace(R.id.container, new ProfileFragment()).commit();

                    }else {
                        getParentFragmentManager().beginTransaction().replace(R.id.inf_container, new ProfileFragment()).commit();

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), R.string.lang_fail, Toast.LENGTH_SHORT).show();
            }
        });
    }
}