package com.example.tourapplication.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tourapplication.AdminMainActivity;
import com.example.tourapplication.influencer.InfluencerMainActivity;
import com.example.tourapplication.user.UserMainActivity;
import com.example.tourapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mfirebaseFirestore;

    private EditText emailEdt, passwordEdt;
    private ProgressBar log_progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
       getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();

        emailEdt = findViewById(R.id.emailEdt);
        passwordEdt = findViewById(R.id.passwordEdt);
        log_progressBar = findViewById(R.id.log_progressBar);

    }

    public void goHome(View view) {
        closeKeyboard();
        log_progressBar.setVisibility(View.VISIBLE);
        mfirebaseFirestore = FirebaseFirestore.getInstance();


        String email = emailEdt.getText().toString().trim();
        String password = passwordEdt.getText().toString().trim();

        if (email.isEmpty()) {
            log_progressBar.setVisibility(View.INVISIBLE);
            emailEdt.setError("Enter e-mail");
            emailEdt.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            log_progressBar.setVisibility(View.INVISIBLE);
            passwordEdt.setError("Enter e-mail");
            passwordEdt.setError("Enter your password");
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    if (email.equalsIgnoreCase("admin@admin.com") && password.equalsIgnoreCase("admin123")) {
                        Toast.makeText(SignInActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignInActivity.this, AdminMainActivity.class));
                        finish();
                    }else {
                        mfirebaseFirestore.collection("User").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()){
                                    if (task.getResult().get("type").toString().equalsIgnoreCase("0")){
                                        log_progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(SignInActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(SignInActivity.this, UserMainActivity.class));
                                        finish();
                                    }else {
                                        log_progressBar.setVisibility(View.INVISIBLE);
                                        startActivity(new Intent(SignInActivity.this, InfluencerMainActivity.class));
                                        finish();
                                    }
                                }
                            }
                        });

                    }



                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignInActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                log_progressBar.setVisibility(View.GONE);
            }
        });


    }

    public void returnRegister(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
