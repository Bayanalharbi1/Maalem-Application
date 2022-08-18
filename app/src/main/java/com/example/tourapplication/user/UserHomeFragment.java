package com.example.tourapplication.user;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tourapplication.PostContentFragment;
import com.example.tourapplication.R;
import com.example.tourapplication.RecyclerViewInterface;
import com.example.tourapplication.adapters.InfluencerPostsAdapter;
import com.example.tourapplication.influencer.Posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserHomeFragment extends Fragment implements RecyclerViewInterface {

    RecyclerView postsRv;
    private EditText searh_places ;
    private ImageView search_icon;

    private InfluencerPostsAdapter influencerPostsAdapter;
    private ArrayList<Posts> postsArrayList , filteredList;
    private FirebaseFirestore mFirebaseFirestore;
    StaggeredGridLayoutManager layoutManager;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserHomeFragment newInstance(String param1, String param2) {
        UserHomeFragment fragment = new UserHomeFragment();
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
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        postsRv = v.findViewById(R.id.postsRv);
        searh_places = v.findViewById(R.id.searh_places);
        search_icon = v.findViewById(R.id.search_icon);

        search_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter(searh_places.getText().toString());
            }
        });




        mFirebaseFirestore = FirebaseFirestore.getInstance();

        initRecycler();
        RetrieveNewsData();

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                searh_places.addTextChangedListener(new TextWatcher() {
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

            }
        }, 2000);



        //todo :    search depend on result from camera


        if (getArguments()!= null) {
            String filter = getArguments().getString("matched_result");
            searh_places.setText(filter);
          //  Toast.makeText(getActivity(), getArguments().getString("matched_result"), Toast.LENGTH_SHORT).show();

         //   filter(filter);
        }

        return v;
    }

    private void filter(String text) {

        filteredList  = new ArrayList<>();

        for (Posts item : postsArrayList) {
            if (item.getPostName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        influencerPostsAdapter.filterList(filteredList);
    }

    private void initRecycler() {
        postsArrayList = new ArrayList<Posts>();
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        postsRv.setLayoutManager(layoutManager);
        postsRv.setHasFixedSize(true);
        influencerPostsAdapter = new InfluencerPostsAdapter(getActivity().getApplicationContext(), postsArrayList, this);
        postsRv.setAdapter(influencerPostsAdapter);
    }

    private void RetrieveNewsData() {

        mFirebaseFirestore.collection("Post").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Log.d("fireStore Error", error.getMessage().toString());

                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {

                        postsArrayList.add(documentChange.getDocument().
                                toObject(Posts.class));


                    }
                    Log.d("following_list2", "onEvent: "+postsArrayList);


                    influencerPostsAdapter.notifyDataSetChanged();


                }


            }
        });

    }

    @Override
    public void onItemClick(Integer position) {
        Bundle bundle = new Bundle();
      //  searh_places.getText().clear();

if (filteredList != null){


    mFirebaseFirestore.collection("User").document(filteredList.get(position).getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                bundle.putString("influncerimg", task.getResult().getString("imageUrl"));


                bundle.putString("influencerName", filteredList.get(position).getInfluencerName());
                bundle.putString("imageposturl", filteredList.get(position).getImageposturl());
                bundle.putString("postName", filteredList.get(position).getPostName());
                bundle.putString("postDesc", filteredList.get(position).getPostDesc());
                bundle.putString("channel", filteredList.get(position).getChannel());
                bundle.putString("doc_id", filteredList.get(position).getPostId());
                bundle.putString("visited_profile_id", filteredList.get(position).getEmail());
                bundle.putString("channelName", filteredList.get(position).getChannel());


                Log.d("doc_id", "onComplete: " + filteredList.get(position).getPostId());

//                initRecycler();
//                RetrieveNewsData();
          searh_places.getText().clear();
//                filteredList.clear();



                PostContentFragment fragobj = new PostContentFragment();
                fragobj.setArguments(bundle);

                getParentFragmentManager().beginTransaction().replace(R.id.container,
                        fragobj).addToBackStack(null).commit();

            } else {
                Toast.makeText(getActivity(), R.string.firestore_fail, Toast.LENGTH_SHORT).show();

            }
        }
    });

}else {
    mFirebaseFirestore.collection("User").document(postsArrayList.get(position).getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()) {
                bundle.putString("influncerimg", task.getResult().getString("imageUrl"));


                bundle.putString("influencerName", postsArrayList.get(position).getInfluencerName());
                bundle.putString("imageposturl", postsArrayList.get(position).getImageposturl());
                bundle.putString("postName", postsArrayList.get(position).getPostName());
                bundle.putString("postDesc", postsArrayList.get(position).getPostDesc());
                bundle.putString("channel", postsArrayList.get(position).getChannel());
                bundle.putString("doc_id", postsArrayList.get(position).getPostId());
                bundle.putString("visited_profile_id", postsArrayList.get(position).getEmail());
                bundle.putString("channelName", postsArrayList.get(position).getChannel());


                Log.d("doc_id", "onComplete: " + postsArrayList.get(position).getPostId());



                PostContentFragment fragobj = new PostContentFragment();
                fragobj.setArguments(bundle);

                getParentFragmentManager().beginTransaction().replace(R.id.container,
                        fragobj).addToBackStack(null).commit();

            } else {
                Toast.makeText(getActivity(), R.string.firestore_fail, Toast.LENGTH_SHORT).show();

            }
        }
    });

}









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
}