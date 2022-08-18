package com.example.tourapplication.user;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tourapplication.adapters.FollowingAdapter;
import com.example.tourapplication.databinding.FragmentNotifivationBinding;
import com.example.tourapplication.influencer.Posts;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {

    FragmentNotifivationBinding binding;
    private FollowingAdapter followingAdapter;
    private ArrayList<Posts> following_userlist;
    private ArrayList<Object> following_email;
    private ArrayList<Object> following_date;


    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String currentemail = user.getEmail();
    private FirebaseFirestore mFirebaseFirestore = FirebaseFirestore.getInstance();
    DocumentReference infProfiledetails = mFirebaseFirestore.collection("User").document(currentemail);
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotifivationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        binding = FragmentNotifivationBinding.inflate(inflater, container, false);

        initRecycler();
        RetrieveNewsData();

        return binding.getRoot();
    }

    private void initRecycler() {

        following_userlist = new ArrayList<Posts>();
        following_email = new ArrayList<Object>();
        following_date = new ArrayList<Object>();
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        layoutManager2.setReverseLayout(false);
        binding.followingRv.setLayoutManager(layoutManager2);
        binding.followingRv.setHasFixedSize(true);

        followingAdapter = new FollowingAdapter(following_userlist, getActivity().getApplicationContext());
        binding.followingRv.setAdapter(followingAdapter);
    }

    private void RetrieveNewsData() {


        infProfiledetails.collection("Following").whereEqualTo("followedUser", true).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Log.d("fireStore Error", error.getMessage().toString());

                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {



                        following_email.add(documentChange.getDocument().get("email"));
                        following_date.add(documentChange.getDocument().get("timestamp"));

                        Log.d("following_list", "onEvent: "+ following_email);




                    }
                }

                for (int x = 0 ; x< following_email.size();x++){
                    mFirebaseFirestore.collection("Post").whereEqualTo("email",following_email.get(x)).whereGreaterThan("timestamp",following_date.get(x)).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if (error != null) {

                                Log.d("fireStore Error", error.getMessage().toString());

                                return;
                            }
                            for (DocumentChange documentChange : value.getDocumentChanges()) {
                                if (documentChange.getType() == DocumentChange.Type.ADDED) {

                                    following_userlist.add(documentChange.getDocument().
                                            toObject(Posts.class));



                                }
                                Log.d("following_list2", "onEvent: "+following_userlist);


                                followingAdapter.notifyDataSetChanged();


                            }


                        }
                    });


                }


            }
        });


    }


}
