package com.example.tourapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tourapplication.adapters.RequestsAdapter;
import com.example.tourapplication.authentication.SignInActivity;
import com.example.tourapplication.databinding.ActivityAdminMainBinding;
import com.example.tourapplication.user.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminMainActivity extends AppCompatActivity {
    private ActivityAdminMainBinding binding;
    private RecyclerView accept_requests_recycler;
    ArrayList<User> requestUserArrayList;
    RequestsAdapter requestsAdapter;
    ProgressBar AcceptReqsprogressBar;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       // setSupportActionBar(binding.adminToolbar);
        getSupportActionBar().setTitle(R.string.adcontrolpanel);


        accept_requests_recycler = findViewById(R.id.accept_requests_recycler);
        AcceptReqsprogressBar = findViewById(R.id.AcceptReqsprogressBar);
        AcceptReqsprogressBar.setVisibility(View.VISIBLE);
        accept_requests_recycler.setLayoutManager(new LinearLayoutManager(this));

        accept_requests_recycler.setHasFixedSize(true);
        List<String> keys = new ArrayList<>();

        firestore = FirebaseFirestore.getInstance();
        requestUserArrayList = new ArrayList<User>();

        requestsAdapter = new RequestsAdapter(this, requestUserArrayList, keys, firestore);
        accept_requests_recycler.setAdapter(requestsAdapter);
        RetrieveDataFirestore();
    }

    private void RetrieveDataFirestore() {

        firestore.collection("User").whereNotEqualTo("type",0).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    if (AcceptReqsprogressBar.isShown()) {
                        AcceptReqsprogressBar.setVisibility(View.GONE);
                    }
                    Log.d("Fire Store Error", error.getMessage());
                    return;
                }
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    if (documentChange.getType() == DocumentChange.Type.ADDED) {

                        requestUserArrayList.add(documentChange.getDocument().toObject(User.class));

                    }
                    requestsAdapter.notifyDataSetChanged();

                    if (AcceptReqsprogressBar.isShown()) {
                        AcceptReqsprogressBar.setVisibility(View.GONE);
                    }

                }

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.adminlogout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                Toast.makeText(this, R.string.logout_success, Toast.LENGTH_SHORT).show();
                break;
                case R.id.adminquestions:
                    startActivity(new Intent(this, AdminQuestionActivity.class));
                break;

        }

        return super.onOptionsItemSelected(item);
    }
}