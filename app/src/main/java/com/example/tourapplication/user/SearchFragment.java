package com.example.tourapplication.user;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tourapplication.R;
import com.example.tourapplication.RecyclerViewInterface;
import com.example.tourapplication.RecyclerViewInterface2;
import com.example.tourapplication.adapters.SearchInfluencerAdapter2;
import com.example.tourapplication.adapters.SearchInfluencerAdapter;
import com.example.tourapplication.databinding.FragmentSearchBinding;
import com.example.tourapplication.influencer.InfluencerProfileFragment;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment implements RecyclerViewInterface , RecyclerViewInterface2 {

    private SearchInfluencerAdapter searchInfluencerAdapter;

    private SearchInfluencerAdapter2 searchInfluencerAdapter2;
    private ArrayList<User> user_infArraylist, fav_inflist;


    private FirebaseFirestore mFirebaseFirestore;

    private FragmentSearchBinding binding;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        mFirebaseFirestore = FirebaseFirestore.getInstance();



        binding.searhV.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        return binding.getRoot();
    }


    private void filter(String text) {
        ArrayList<User> filteredList = new ArrayList<>();
        ArrayList<User> filteredList2 = new ArrayList<>();

        for (User item : user_infArraylist) {
            if (item.getChannelName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        for (User item : fav_inflist) {
            if (item.getChannelName().toLowerCase().contains(text.toLowerCase())) {
                filteredList2.add(item);
            }
        }


        searchInfluencerAdapter.filterList(filteredList);
        searchInfluencerAdapter2.filterList(filteredList2);
    }

    private void initRecycler() {
        user_infArraylist = new ArrayList<User>();
        fav_inflist = new ArrayList<User>();

        GridLayoutManager layoutManager1 = new GridLayoutManager(getActivity(), 3);
        binding.recommendedInfRecycler.setLayoutManager(layoutManager1);
        binding.recommendedInfRecycler.setHasFixedSize(true);

        searchInfluencerAdapter = new SearchInfluencerAdapter(user_infArraylist, getActivity().getApplicationContext(), this);
        binding.recommendedInfRecycler.setAdapter(searchInfluencerAdapter);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true);
        layoutManager2.setReverseLayout(false);
        binding.favInfRecycler.setLayoutManager(layoutManager2);
        binding.favInfRecycler.setHasFixedSize(true);

        searchInfluencerAdapter2 = new SearchInfluencerAdapter2(fav_inflist, getActivity().getApplicationContext(), this);
        binding.favInfRecycler.setAdapter(searchInfluencerAdapter2);


    }


    public void RetrieveNewsData() {
        // retrieve  recomendded inf Lists
        mFirebaseFirestore.collection("User").whereEqualTo("type", 1).whereLessThan("follower", 10).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Log.d("fireStore Error", error.getMessage().toString());

                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {

                        user_infArraylist.add(documentChange.getDocument().
                                toObject(User.class));


                    }
                    searchInfluencerAdapter.notifyDataSetChanged();
                }


            }
        });


    }

    public void RetrieveData2() {
       // retrieve fav inf places

        mFirebaseFirestore.collection("User").whereEqualTo("type", 1).whereGreaterThan("follower", 9).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Log.d("fireStore Error", error.getMessage().toString());

                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {


                        fav_inflist.add(documentChange.getDocument().
                                toObject(User.class));


                    }
                    searchInfluencerAdapter2.notifyDataSetChanged();
                }


            }
        });

    }


    @Override
    public void onItemClick(Integer position) {

        Bundle bundle = new Bundle();

        bundle.putString("recommend", user_infArraylist.get(position).getEmail());


        InfluencerProfileFragment fragobj = new InfluencerProfileFragment();
        fragobj.setArguments(bundle);

        getParentFragmentManager().beginTransaction().replace(R.id.container,
                fragobj).addToBackStack(null).commit();


    }

    @Override
    public void onItemClickfiltered(int position) {

    }

    @Override
    public void onDeleteClick(int postion) {

    }

    @Override
    public void onItemLongClick(int position) {

    }

    @Override
    public void onStart() {
        super.onStart();

        initRecycler();
        RetrieveNewsData();
        RetrieveData2();

    }

    @Override
    public void onItemClick2(int position) {
        Bundle bundle = new Bundle();

        bundle.putString("recommend", fav_inflist.get(position).getEmail());


        InfluencerProfileFragment fragobj = new InfluencerProfileFragment();
        fragobj.setArguments(bundle);

        getParentFragmentManager().beginTransaction().replace(R.id.container,
                fragobj).addToBackStack(null).commit();

    }
}
