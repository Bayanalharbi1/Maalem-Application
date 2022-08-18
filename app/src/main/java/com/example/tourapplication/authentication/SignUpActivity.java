package com.example.tourapplication.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.tourapplication.influencer.InfluencerMainActivity;
import com.example.tourapplication.user.UserMainActivity;
import com.example.tourapplication.R;
import com.example.tourapplication.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SignUpActivity extends AppCompatActivity {


    private EditText confirmPasswordEdt, userNameEdt, emailEdt, passwordEdt;
    private ProgressBar reg_progressBar;
    private RadioGroup radiogroup;
    private RadioButton tourist_RadioButton, influencer_RadioButton;
    ImageView userAvatarIv;
    Uri nImageUri;
    private LinearLayout userAvatarlayout;
    private TextInputLayout channelInputLayout;
    private TextInputEditText channel_name;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    StorageTask mUploadTask;
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("UsersImages");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        userNameEdt = findViewById(R.id.userNameEdt);
        emailEdt = findViewById(R.id.emailEdt);
        passwordEdt = findViewById(R.id.passwordEdt);
        confirmPasswordEdt = findViewById(R.id.confirmPasswordEdt);
        reg_progressBar = findViewById(R.id.reg_progressBar);
        tourist_RadioButton = findViewById(R.id.tourist_RadioButton);
        influencer_RadioButton = findViewById(R.id.influencer_RadioButton);
        radiogroup = findViewById(R.id.radiogroup);
        userAvatarIv = findViewById(R.id.userAvatarIv);
        channelInputLayout = findViewById(R.id.channelInputLayout);
        channel_name = findViewById(R.id.channel_name);
        userAvatarlayout = findViewById(R.id.userAvatarlayout);

        influencer_RadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "onCreate: " + influencer_RadioButton.isChecked());
                channelInputLayout.setVisibility(View.VISIBLE);

            }
        });
        tourist_RadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG2", "onCreate: " + influencer_RadioButton.isChecked());
                channelInputLayout.setVisibility(View.GONE);
            }
        });


    }

    public void returnLogin(View view) {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    public void goHomeActivity(int x) {
        if (x == 0 ){
            Intent intent = new Intent(this, UserMainActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this, InfluencerMainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void Register(View view) {
        closeKeyboard();
        String user_name = userNameEdt.getText().toString().trim();
        String email = emailEdt.getText().toString().toLowerCase().trim();
        String password = passwordEdt.getText().toString().trim();
        String confirm_password = confirmPasswordEdt.getText().toString().trim();


        boolean touristrad = tourist_RadioButton.isChecked();
        boolean influencerrad = influencer_RadioButton.isChecked();

        Log.d("tag", ""+!touristrad + !influencerrad);



        if (userAvatarIv.getDrawable() != null) {
            userAvatarlayout.setBackgroundColor(getResources().getColor(R.color.white));

            if (!user_name.isEmpty()) {
                if (!email.isEmpty()&& Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (!password.isEmpty() && password.length() >6) {
                        if (!confirm_password.isEmpty()) {
                            if (!password.equalsIgnoreCase(confirm_password)) {
                                confirmPasswordEdt.setError("Passwords does not match");
                                confirmPasswordEdt.requestFocus();
                                reg_progressBar.setVisibility(View.INVISIBLE);

                            } else {
                                if (touristrad ==true || influencerrad == true) {

                                        reg_progressBar.setVisibility(View.VISIBLE);
                                        if (nImageUri != null) {
                                            StorageReference fileReference = mStorageRef.child(nImageUri.getLastPathSegment());

                                            mUploadTask = fileReference.putFile(nImageUri)
                                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                            Handler handler = new Handler();
                                                            handler.postDelayed(new Runnable() {
                                                                @Override
                                                                public void run() {
                                                                    reg_progressBar.setVisibility(View.INVISIBLE);
                                                                }
                                                            }, 1000);
                                                            Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                                                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                @Override
                                                                public void onSuccess(Uri uri) {
                                                                    String imageUrl = uri.toString();
                                                                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                                                            if (task.isSuccessful()) {
                                                                                if (touristrad) {

                                                                                    User user = new User(user_name, password, email, imageUrl, 0);
                                                                                    firestore.collection("User").document(email).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                        @Override
                                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                                            if (task.isSuccessful()) {
                                                                                                Toast.makeText(SignUpActivity.this, "Register success :)", Toast.LENGTH_SHORT).show();
                                                                                                reg_progressBar.setVisibility(View.INVISIBLE);
                                                                                                goHomeActivity(0);
                                                                                            } else {
                                                                                                Toast.makeText(SignUpActivity.this, "Register Failed :(", Toast.LENGTH_SHORT).show();
                                                                                                reg_progressBar.setVisibility(View.INVISIBLE);

                                                                                            }
                                                                                        }
                                                                                    });
                                                                                } else if (influencerrad) {
                                                                                    if (!channel_name.getText().toString().isEmpty()) {
                                                                                        User user = new User(user_name, password, email, imageUrl, 1, channel_name.getText().toString() , "No location" , 0);
                                                                                        firestore.collection("User").document(email).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                            @Override
                                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                                if (task.isSuccessful()) {
                                                                                                    reg_progressBar.setVisibility(View.INVISIBLE);
                                                                                                    goHomeActivity(1);
                                                                                                } else {
                                                                                                    Toast.makeText(SignUpActivity.this, "Register Failed :(", Toast.LENGTH_SHORT).show();
                                                                                                    reg_progressBar.setVisibility(View.INVISIBLE);


                                                                                                }
                                                                                            }
                                                                                        });


                                                                                    } else {
                                                                                        channel_name.setError("Required!!");
                                                                                        channel_name.requestFocus();
                                                                                        return;
                                                                                    }


                                                                                }


                                                                            }
                                                                        }
                                                                    }).addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            Toast.makeText(SignUpActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                                            reg_progressBar.setVisibility(View.GONE);
                                                                        }
                                                                    });

                                                                    Toast.makeText(SignUpActivity.this, "Register success", Toast.LENGTH_SHORT).show();
                                                                    userAvatarIv.setBackground(getResources().getDrawable(R.color.white));

                                                                }
                                                            });


                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                                        @Override
                                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                                            reg_progressBar.setVisibility(View.VISIBLE);
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(this, "u should upload pic", Toast.LENGTH_SHORT).show();
                                        }




                                } else {

                                    Toast.makeText(this, "Choose account type", Toast.LENGTH_SHORT).show();


                                }

                            }
                        } else {
                            confirmPasswordEdt.setError("Re type your password correctly !!");
                            confirmPasswordEdt.requestFocus();
                            reg_progressBar.setVisibility(View.INVISIBLE);

                        }

                    } else {
                        passwordEdt.setError("The password should contain more than 6 symbols");
                        passwordEdt.requestFocus();
                        reg_progressBar.setVisibility(View.INVISIBLE);
                        return;
                    }

                } else {
                    emailEdt.setError("Please provide valid email");
                    emailEdt.requestFocus();
                    reg_progressBar.setVisibility(View.INVISIBLE);
                    return;
                }
            } else {
                userNameEdt.setError("Empty Field");
                userNameEdt.requestFocus();
                reg_progressBar.setVisibility(View.INVISIBLE);
                return;
            }
        } else {
            userAvatarlayout.setBackgroundColor(getResources().getColor(R.color.red));
            Toast.makeText(this, "Choose your profile picture", Toast.LENGTH_SHORT).show();

        }


    }

    public void UploadImage(View view) {
        openFileChooser();
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            nImageUri = data.getData();
            Picasso.get().load(nImageUri).into(userAvatarIv);
        }
    }


}