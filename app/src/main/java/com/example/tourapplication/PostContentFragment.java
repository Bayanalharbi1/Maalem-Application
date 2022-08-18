package com.example.tourapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.tourapplication.adapters.CommentAdapter;
import com.example.tourapplication.databinding.FragmentPostContentBinding;
import com.example.tourapplication.influencer.InfluencerProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PostContentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PostContentFragment extends Fragment {

    FragmentPostContentBinding binding;
    private boolean isclicked = false;
    private boolean iscommentimgclicked = false;


    String postID ;


    GridLayoutManager layoutManager ;

    private CommentAdapter commentAdapter;
    private ArrayList<Comment> commentArrayList;
    private FirebaseFirestore mFirebaseFirestore;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PostContentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PostContentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PostContentFragment newInstance(String param1, String param2) {
        PostContentFragment fragment = new PostContentFragment();
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
        binding = FragmentPostContentBinding.inflate(inflater, container, false);
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        binding.imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), R.string.like_feature, Toast.LENGTH_SHORT).show();
            }
        });     binding.imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), R.string.feature_unavailable, Toast.LENGTH_SHORT).show();
            }
        });


        String influencerName = getArguments().getString("influencerName");
        String imageposturl = getArguments().getString("imageposturl");
        String postName = getArguments().getString("postName");
        String influncerimg = getArguments().getString("influncerimg");
        String postDesc = getArguments().getString("postDesc");
       String channel = getArguments().getString("channel");
        postID = getArguments().getString("doc_id");
        String visited_profile_id = getArguments().getString("visited_profile_id");

        mFirebaseFirestore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
           if (task.isSuccessful()){
               if (!task.getResult().get("type").toString().equalsIgnoreCase("0")){
                   binding.visitProfiletv.setVisibility(View.GONE);
               }
           }
            }
        });

        mFirebaseFirestore.collection("User").document(visited_profile_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    Picasso.get().load(task.getResult().getString("imageUrl")).into(binding.influencerPostPic);
                    binding.influencerPostName.setText(task.getResult().getString("userName"));
                    binding.influencerChannelName.setText(task.getResult().getString("channelName"));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        Picasso.get().load(imageposturl).into(binding.userPostImg);

        binding.userPostName.setText(postName);

        binding.userpostDesc.setText(postDesc);


        initRecycler();
        RetrieveNewsData();

        binding.visitProfiletv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();

                bundle.putString("recommend", visited_profile_id);


                InfluencerProfileFragment fragobj = new InfluencerProfileFragment();
                fragobj.setArguments(bundle);

                getParentFragmentManager().beginTransaction().replace(R.id.container,
                        fragobj).addToBackStack(null).commit();

            }
        });
        binding.commentimgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!iscommentimgclicked){
                    binding.commentfieldlayout.setVisibility(View.VISIBLE);
                    binding.commenttxt.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                    imm.showSoftInput(binding.commenttxt, InputMethodManager.SHOW_IMPLICIT);
                    iscommentimgclicked = true;
                }else {
                    binding.commentfieldlayout.setVisibility(View.INVISIBLE);
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(binding.commenttxt.getWindowToken(), 0);
                    iscommentimgclicked = false;
                }
            }
        });


        binding.addComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();
            }
        });

        binding.commentlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isclicked){

                    binding.commentsRv.setVisibility(View.VISIBLE);
                    binding.commentsCountTv.setVisibility(View.VISIBLE);

                    isclicked =true;
                }else {

                    binding.commentsRv.setVisibility(View.GONE);
                    binding.commentsCountTv.setVisibility(View.GONE);
                    isclicked =false;


                }
            }
        });


        return binding.getRoot();
    }

    private void initRecycler() {
        commentArrayList = new ArrayList<Comment>();
        layoutManager = new GridLayoutManager(getActivity(), 1);
        binding.commentsRv.setLayoutManager(layoutManager);
        binding.commentsRv.setHasFixedSize(true);


        commentAdapter = new CommentAdapter(commentArrayList, getActivity().getApplicationContext());
        binding.commentsRv.setAdapter(commentAdapter);
    }

    private void RetrieveNewsData() {

        mFirebaseFirestore.collection("Post").document(postID).collection("Comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Log.d("fireStore Error", error.getMessage().toString());

                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {

                        commentArrayList.add(documentChange.getDocument().
                                toObject(Comment.class));
                        binding.commentsCountTv.setText(String.valueOf(commentArrayList.size()));
                        Log.d("commentArrayList", "onEvent: "+commentArrayList.size());
                        Log.d("commentArrayList", "onEvent: "+documentChange.getDocument().getData());


                    }

                    commentAdapter.notifyDataSetChanged();


                }


            }
        });

    }

    private void addComment() {
        String comment = binding.commenttxt.getText().toString().trim();

        DocumentReference currentUser = FirebaseFirestore.getInstance().collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        DocumentReference currentPost = FirebaseFirestore.getInstance().collection("Post").document(postID);


        if (!comment.isEmpty()){

            currentUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){

                        Comment comment1 = new Comment( task.getResult().getString("userName") ,comment ,task.getResult().getString("imageUrl"));


                        currentPost.collection("Comments").document().set(comment1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                            Toast.makeText(getActivity(), R.string.comment_added, Toast.LENGTH_SHORT).show();
                            binding.commenttxt.getText().clear();
                            binding.commentfieldlayout.setVisibility(View.INVISIBLE);

                        }
                    });

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getActivity(), "Error !"+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });




        }else {
            Toast.makeText(getActivity(), R.string.comment_empty, Toast.LENGTH_SHORT).show();
        }
    }
}