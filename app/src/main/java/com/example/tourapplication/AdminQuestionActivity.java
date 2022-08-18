package com.example.tourapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tourapplication.adapters.QuestionAdapter;
import com.example.tourapplication.adapters.RequestsAdapter;
import com.example.tourapplication.authentication.SignInActivity;
import com.example.tourapplication.databinding.ActivityAdminMainBinding;
import com.example.tourapplication.databinding.ActivityAdminQuestionBinding;
import com.example.tourapplication.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminQuestionActivity extends AppCompatActivity implements RecyclerViewInterface {
    private ActivityAdminQuestionBinding binding;

    ArrayList<Question> questionArrayList;
    QuestionAdapter questionAdapter;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminQuestionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle(R.string.answerQ);


        binding.questionRecycler.setLayoutManager(new LinearLayoutManager(this));

        binding.questionRecycler.setHasFixedSize(true);

        firestore = FirebaseFirestore.getInstance();
        questionArrayList = new ArrayList<Question>();

        questionAdapter = new QuestionAdapter(this, questionArrayList, this);
        binding.questionRecycler.setAdapter(questionAdapter);
        RetrieveDataFirestore();

    }

    private void RetrieveDataFirestore() {

        firestore.collection("Question").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {

                    Log.d("Fire Store Error", error.getMessage());
                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {

                        questionArrayList.add(documentChange.getDocument().toObject(Question.class));

                    }
                    questionAdapter.notifyDataSetChanged();


                }

            }
        });


    }

    @Override
    public void onItemClick(Integer position) {

        // Toast.makeText(this, questionArrayList.get(position).getQid(), Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setTitle(R.string.set_answer);
        final EditText question_edittxt = new EditText(this);
        question_edittxt.setText(questionArrayList.get(position).getAnswer());

        question_edittxt.setInputType(InputType.TYPE_CLASS_TEXT);
        builder1.setView(question_edittxt);

        builder1.setPositiveButton(
                R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        firestore.collection("Question").document(questionArrayList.get(position).getQid()).update("answer", question_edittxt.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                questionArrayList.get(position).setAnswer(question_edittxt.getText().toString().trim());
                                questionAdapter.notifyDataSetChanged();

                            }

                        });
                        dialog.cancel();


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
    public void onItemClickfiltered(int position) {

    }

    @Override
    public void onDeleteClick(int postion) {

    }

    @Override
    public void onItemLongClick(int position) {

    }
}