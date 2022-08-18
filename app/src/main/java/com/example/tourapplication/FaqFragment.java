package com.example.tourapplication;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tourapplication.adapters.InfluencerPostsAdapter;
import com.example.tourapplication.adapters.QuestionAdapter;
import com.example.tourapplication.adapters.SearchInfluencerAdapter2;
import com.example.tourapplication.databinding.FragmentFaqBinding;
import com.example.tourapplication.influencer.Posts;
import com.example.tourapplication.user.User;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FaqFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaqFragment extends Fragment implements RecyclerViewInterface {

 private   FragmentFaqBinding binding ;

 private QuestionAdapter questionAdapter ;
 private ArrayList<Question> questionArrayList ;
 private FirebaseFirestore mFirebaseFirestore ;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FaqFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FaqFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FaqFragment newInstance(String param1, String param2) {
        FaqFragment fragment = new FaqFragment();
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
        binding = FragmentFaqBinding.inflate(inflater , container , false);

        mFirebaseFirestore = FirebaseFirestore.getInstance();

        initRecycler();
        RetrieveNewsData();


        return binding.getRoot();

    }

    private void initRecycler() {
        questionArrayList = new ArrayList<Question>();
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        layoutManager2.setReverseLayout(false);
        binding.faqRv.setLayoutManager(layoutManager2);
        binding.faqRv.setHasFixedSize(true);

        questionAdapter = new QuestionAdapter( this,questionArrayList , getActivity().getApplicationContext());
        binding.faqRv.setAdapter(questionAdapter);
    }
    public void RetrieveNewsData() {
        // retrieve  recomendded inf Lists
        mFirebaseFirestore.collection("Question").whereNotEqualTo("answer", "null").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Log.d("fireStore Error", error.getMessage().toString());

                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {

                        questionArrayList.add(documentChange.getDocument().
                                toObject(Question.class));


                    }
                    questionAdapter.notifyDataSetChanged();
                }


            }
        });


    }

    @Override
    public void onItemClick(Integer position) {

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