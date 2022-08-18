package com.example.tourapplication;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tourapplication.adapters.FollowerAdapter;
import com.example.tourapplication.databinding.FragmentManageFollowingBinding;
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
 * Use the {@link ManageFollowingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ManageFollowingFragment extends Fragment {
    FragmentManageFollowingBinding binding;
    private FollowerAdapter followerAdapter ;
    private ArrayList<Followers> followed_userlist;


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

    public ManageFollowingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ManageFollowingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ManageFollowingFragment newInstance(String param1, String param2) {
        ManageFollowingFragment fragment = new ManageFollowingFragment();
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

        binding = FragmentManageFollowingBinding.inflate(inflater,container, false);

//
        initRecycler();
        RetrieveNewsData();


        return binding.getRoot();

    }



    private void initRecycler() {

        followed_userlist = new ArrayList<Followers>();
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        layoutManager2.setReverseLayout(false);
        binding.managefollowingRecycler.setLayoutManager(layoutManager2);
        binding.managefollowingRecycler.setHasFixedSize(true);

        followerAdapter = new FollowerAdapter(followed_userlist, getActivity().getApplicationContext());
        binding.managefollowingRecycler.setAdapter(followerAdapter);
    }
    private void RetrieveNewsData() {

        infProfiledetails.collection("Follower").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Log.d("fireStore Error", error.getMessage().toString());

                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {

                        followed_userlist.add(documentChange.getDocument().
                                toObject(Followers.class));



                    }
                    Log.d("followers_list2", "onEvent: "+followed_userlist);

                    if (!followed_userlist.isEmpty()){
                        binding.managefolloingTv.setVisibility(View.GONE);
                    }else {
                        binding.managefolloingTv.setVisibility(View.VISIBLE);

                    }


                    followerAdapter.notifyDataSetChanged();



                }


            }
        });
    }

}