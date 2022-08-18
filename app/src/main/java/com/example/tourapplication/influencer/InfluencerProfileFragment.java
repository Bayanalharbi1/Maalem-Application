package com.example.tourapplication.influencer;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourapplication.Followers;
import com.example.tourapplication.adapters.MainPostsAdaptar;
import com.example.tourapplication.PostContentFragment;
import com.example.tourapplication.R;
import com.example.tourapplication.RecyclerViewInterface;
import com.example.tourapplication.adapters.VisitedPlacesAdapter;
import com.example.tourapplication.authentication.SignInActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InfluencerProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfluencerProfileFragment extends Fragment implements RecyclerViewInterface {
    private MainPostsAdaptar mainPostsAdaptar;
    private ArrayList<Posts> postssArrayList, visitedArraylist;
    private static final int PICK_IMAGE_REQUEST = 1;
    Uri nImageUri;
    private Dialog dialog;
//



    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String currentemail = user.getEmail();
    private FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference infProfiledetails = mFirebaseFirestore.collection("User").document(currentemail);

    ImageView inf_profileimg;
    TextView inf_profilename, location_txt, inf_abouttxt, follower_count, inf_channelprofile;
    Button followbtn;
    private VisitedPlacesAdapter visitedPlacesAdapter;
    RecyclerView profilePostRv, visitedRv;

    private boolean isfollowed = false;

    String email;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    public InfluencerProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfluencerProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfluencerProfileFragment newInstance(String param1, String param2) {
        InfluencerProfileFragment fragment = new InfluencerProfileFragment();
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

    @SuppressLint("LongLogTag")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_influencer_profile, container, false);


        profilePostRv = v.findViewById(R.id.profilePostRv);
        visitedRv = v.findViewById(R.id.visitedRv);
        inf_profileimg = v.findViewById(R.id.inf_profileimg);
        inf_profilename = v.findViewById(R.id.inf_profilename);
        location_txt = v.findViewById(R.id.location_txt);
        inf_abouttxt = v.findViewById(R.id.inf_abouttxt);
        followbtn = v.findViewById(R.id.followbtn);
        follower_count = v.findViewById(R.id.follower_count);
        inf_channelprofile = v.findViewById(R.id.inf_channelprofile);
        dialog = new Dialog(getActivity());

       // UpdatePost();


        followbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Follow();


            }
        });


        infProfiledetails.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().get("type").toString().equalsIgnoreCase("1")) {
                        // open inf account from influencer type
                        initRecycler();
                        RetrieveNewsData();
                        followbtn.setEnabled(false);
                        followbtn.setText("Personal account");

                    } else {
                        //open inf account from Tourist type
                        email = getArguments().getString("recommend");

                        initRecycler();
                        RetrieveNewsData2();
                        followbtn.setEnabled(true);


                    }
                }
            }
        });


        return v;

    }


    private void Follow() {
        Date currentTime = Calendar.getInstance().getTime();
        String followed_id = getArguments().getString("recommend");
        String x = getArguments().getString("recommend");

        DocumentReference infProfiledetails2 = mFirebaseFirestore.collection("User").document(x);

        if (!isfollowed) {
            followbtn.setBackgroundColor(getResources().getColor(R.color.darkgray));
            followbtn.setText(R.string.unfollow);
            isfollowed = true;

            mFirebaseFirestore.collection("User").document(currentemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    Followers user = new Followers(followed_id, true);
                    Followers userx = new Followers(currentemail, true, task.getResult().getString("mUserName"), task.getResult().getString("mImageUrl"), currentTime);
                    infProfiledetails.collection("Following").document(followed_id).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Influncer Followed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    infProfiledetails2.collection("Follower").document(currentemail).set(userx).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            infProfiledetails2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        long x = (long) task.getResult().get("follower");
                                        x++;
                                        infProfiledetails2.update("follower", x);

                                    }
                                }
                            });
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            followbtn.setBackgroundColor(getResources().getColor(R.color.main_color));
            followbtn.setText(R.string.follow_txt);
            isfollowed = false;
            mFirebaseFirestore.collection("User").document(currentemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    Followers user2 = new Followers(followed_id, false);
                    Followers ussery = new Followers(currentemail, false, task.getResult().getString("mUserName"), task.getResult().getString("mImageUrl"), currentTime);

                    infProfiledetails.collection("Following").document(followed_id).set(user2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), "Influncer Unfollowed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                    infProfiledetails2.collection("Follower").document(currentemail).set(ussery).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            infProfiledetails2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        long x = (long) task.getResult().get("follower");
                                        x--;
                                        infProfiledetails2.update("follower", x);

                                    }
                                }
                            });
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }

    }

    private void checkFollowed() {


        String followed_id = getArguments().getString("recommend");


        infProfiledetails.collection("Following").document(followed_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getBoolean("followedUser") != null) {
                        if (!task.getResult().getBoolean("followedUser")) {
                            followbtn.setBackgroundColor(getResources().getColor(R.color.main_color));
                            followbtn.setText(R.string.follow_txt);
                            isfollowed = false;


                        } else {
                            followbtn.setBackgroundColor(getResources().getColor(R.color.darkgray));
                            followbtn.setText(R.string.unfollow);
                            isfollowed = true;

                        }
                    } else {

                    }
                }
            }
        });
    }

    private void initRecycler() {
        postssArrayList = new ArrayList<Posts>();

        StaggeredGridLayoutManager layoutManager1 = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        profilePostRv.setLayoutManager(layoutManager1);
        profilePostRv.setHasFixedSize(true);
        mainPostsAdaptar = new MainPostsAdaptar(postssArrayList, getActivity().getApplicationContext(), this);
        profilePostRv.setAdapter(mainPostsAdaptar);

        visitedArraylist = new ArrayList<Posts>();
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        layoutManager2.setReverseLayout(false);
        visitedRv.setLayoutManager(layoutManager2);
        visitedRv.setHasFixedSize(true);

        visitedPlacesAdapter = new VisitedPlacesAdapter(visitedArraylist, getActivity().getApplicationContext());
        visitedRv.setAdapter(visitedPlacesAdapter);


    }

    @Override
    public void onStart() {
        super.onStart();
        checkFollowed();
    }

    public void RetrieveNewsData() {

        infProfiledetails.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Picasso.get().load(task.getResult().getString("imageUrl")).into(inf_profileimg);
                    inf_profilename.setText(task.getResult().getString("userName"));
                    location_txt.setText(task.getResult().getString("location"));
                    follower_count.setText(String.valueOf(task.getResult().get("follower")));
                    inf_channelprofile.setText(String.valueOf(task.getResult().get("channelName")));


                    if (task.getResult().getString("about") != null) {
                        inf_abouttxt.setText(task.getResult().getString("about"));
                    } else {
                        inf_abouttxt.setText(" No bio yet ");
                    }


                    //todo : Dont forget to upload influencer description inf_abouttxt
                }
            }
        });


        infProfiledetails.collection("MyPosts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Log.d("fireStore Error", error.getMessage().toString());

                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {

                        postssArrayList.add(documentChange.getDocument().
                                toObject(Posts.class));

                        visitedArraylist.add(documentChange.getDocument().
                                toObject(Posts.class));


                    }

                    mainPostsAdaptar.notifyDataSetChanged();
                    visitedPlacesAdapter.notifyDataSetChanged();


                }


            }
        });

    }


    public void RetrieveNewsData2() {

        DocumentReference deliveredeDoc = mFirebaseFirestore.collection("User").document(email);


        deliveredeDoc.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Picasso.get().load(task.getResult().getString("imageUrl")).into(inf_profileimg);
                    inf_profilename.setText(task.getResult().getString("userName"));
                    location_txt.setText(task.getResult().getString("location"));
                    inf_channelprofile.setText(String.valueOf(task.getResult().getString("channelName")));

                    if (task.getResult().getString("about") != null) {
                        inf_abouttxt.setText(task.getResult().getString("about"));
                    } else {
                        inf_abouttxt.setText(" No bio yet ");
                    }


                }
            }
        });
        deliveredeDoc.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {


                follower_count.setText(value.get("follower").toString());


            }
        });

        deliveredeDoc.collection("Follower").whereEqualTo("followedUser", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count = 0;
                            for (DocumentSnapshot document : task.getResult()) {
                                count++;
                            }
                            follower_count.setText(String.valueOf(count));
                        } else {
                            Log.d("follower count", "Error getting followers count ", task.getException());
                        }
                    }
                });


        deliveredeDoc.collection("MyPosts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Log.d("fireStore Error", error.getMessage().toString());

                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {

                        postssArrayList.add(documentChange.getDocument().
                                toObject(Posts.class));

                        visitedArraylist.add(documentChange.getDocument().
                                toObject(Posts.class));


                    }

                    mainPostsAdaptar.notifyDataSetChanged();
                    visitedPlacesAdapter.notifyDataSetChanged();


                }


            }
        });

    }


    @Override
    public void onItemClick(Integer position) {



        Bundle bundle = new Bundle();


        mFirebaseFirestore.collection("User").document(postssArrayList.get(position).getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    bundle.putString("influncerimg", task.getResult().getString("imageUrl"));


                    bundle.putString("influencerName", postssArrayList.get(position).getInfluencerName());
                    bundle.putString("imageposturl", postssArrayList.get(position).getImageposturl());
                    bundle.putString("postName", postssArrayList.get(position).getPostName());
                    bundle.putString("postDesc", postssArrayList.get(position).getPostDesc());
                    bundle.putString("channel", postssArrayList.get(position).getChannel());
                    bundle.putString("doc_id", postssArrayList.get(position).getPostId());
                    bundle.putString("visited_profile_id", postssArrayList.get(position).getEmail());
                    bundle.putString("channelName", postssArrayList.get(position).getChannel());


                    Log.d("doc_id", "onComplete: " + postssArrayList.get(position).getPostId());


                    PostContentFragment fragobj = new PostContentFragment();
                    fragobj.setArguments(bundle);

                    mFirebaseFirestore.collection("User").document(currentemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()){
                                if (task.getResult().get("type").toString().equalsIgnoreCase("0")){

                                    getParentFragmentManager().beginTransaction().replace(R.id.container,
                                            fragobj).addToBackStack(null).commit();
                                }else {
                                    getParentFragmentManager().beginTransaction().replace(R.id.inf_container,
                                            fragobj).addToBackStack(null).commit();
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Error , couldnt retrieve data", Toast.LENGTH_SHORT).show();

                        }
                    });





                } else {
                    Toast.makeText(getActivity(), "Error , couldnt retrieve data", Toast.LENGTH_SHORT).show();

                }
            }
        });


    }

    @Override
    public void onItemClickfiltered(int position) {

    }

    @Override
    public void onDeleteClick(int position) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
        builder1.setTitle(R.string.delete_title);

        builder1.setMessage(R.string.delete_post);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mFirebaseFirestore.collection("Post").document(postssArrayList.get(position).getPostId()).delete();
                        mFirebaseFirestore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("MyPosts").document(postssArrayList.get(position).getPostId()).delete();
                        postssArrayList.remove(position);
                        mainPostsAdaptar.notifyItemRemoved(position);

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

    @Override
    public void onItemLongClick(int position) {

    //    UpdatePass();
        mFirebaseFirestore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
           if (task.isSuccessful()){
               if (!task.getResult().get("type").toString().equalsIgnoreCase("0")){
                   dialog.show();


               }else {
                   Toast.makeText(getActivity(), "This is " + postssArrayList.get(position).getPostName(), Toast.LENGTH_SHORT).show();

               }
           }
            }
        });


        dialog.setContentView(R.layout.update_post_dialog);
        dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.worm_dot_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        Button updatePostBtn = dialog.findViewById(R.id.update_createPlaceBtn);
        Button cancelUpdate = dialog.findViewById(R.id.update_cancelPlaceBtn);


        EditText update_place_name = dialog.findViewById(R.id.update_place_name);
        EditText update_place_desc = dialog.findViewById(R.id.update_place_desc);
        EditText update_place_location = dialog.findViewById(R.id.update_place_location);
        ProgressBar update_createPlace_progress = dialog.findViewById(R.id.update_createPlace_progress);



            mFirebaseFirestore.collection("Post").document(postssArrayList.get(position).getPostId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        update_place_name.setText(task.getResult().getString("postName"));
                        update_place_desc.setText(task.getResult().getString("postDesc"));
                        update_place_location.setText(task.getResult().getString("location"));
                    }
                }
            });

        updatePostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_createPlace_progress.setVisibility(View.VISIBLE);

                mFirebaseFirestore.collection("Post").document(postssArrayList.get(position).getPostId()).update("postName" , update_place_name.getText().toString().trim());
                mFirebaseFirestore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("MyPosts").document(postssArrayList.get(position).getPostId()).update("postName" , update_place_name.getText().toString().trim());

                mFirebaseFirestore.collection("Post").document(postssArrayList.get(position).getPostId()).update("postDesc" , update_place_desc.getText().toString().trim());
                mFirebaseFirestore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("MyPosts").document(postssArrayList.get(position).getPostId()).update("postDesc" , update_place_desc.getText().toString().trim());

                mFirebaseFirestore.collection("Post").document(postssArrayList.get(position).getPostId()).update("location" , update_place_location.getText().toString().trim());
                mFirebaseFirestore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).collection("MyPosts").document(postssArrayList.get(position).getPostId()).update("location" , update_place_location.getText().toString().trim());
                update_createPlace_progress.setVisibility(View.GONE);
                postssArrayList.get(position).setPostName(update_place_name.getText().toString().trim());
                mainPostsAdaptar.notifyDataSetChanged();
                dialog.dismiss();



            }
        });
        cancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



    }

}