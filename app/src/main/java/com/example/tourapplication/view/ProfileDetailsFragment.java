package com.example.tourapplication.view;

import static android.app.Activity.RESULT_OK;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourapplication.PostContentFragment;
import com.example.tourapplication.R;
import com.example.tourapplication.influencer.InfluencerProfileFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileDetailsFragment extends Fragment {
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private String currentemail = user.getEmail();


    private DocumentReference reference1 = firestore.collection("User").document(currentemail);


    TextView profile_username, profile_email, profile_about, go_influencer_profile, profile_password , change_profileimg;
    EditText profile_update_about , profile_update_name , oldPass ,newPass;
    LinearLayout bio_layout, about_layout , userName_layout , nameEdit_layout , email_layout , passwordEdt_layout ,passwordEdt_layout2;
    Button update_bioBtn, dismiss_bioBtn , dismiss_nameBtn , update_nameBtn ,updatePassBtn,cancelUpdate;
    ImageView profile_editimg;
    private Uri mImageUri;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
    private StorageTask mUploadTask;

    Map<String, Object> map = new HashMap<>();


    DocumentReference userprofilenode = FirebaseFirestore.getInstance().collection("User").document(currentemail);


    private ProgressBar progress_photo ;


    private static final int PICK_IMAGE_REQUEST = 1;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileDetailsFragment newInstance(String param1, String param2) {
        ProfileDetailsFragment fragment = new ProfileDetailsFragment();
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
        View v = inflater.inflate(R.layout.fragment_profile_details, container, false);
        profile_username = v.findViewById(R.id.profile_username);
        profile_email = v.findViewById(R.id.profile_email);
        profile_password = v.findViewById(R.id.profile_password);
        profile_editimg = v.findViewById(R.id.profile_editimg);
        go_influencer_profile = v.findViewById(R.id.go_influencer_profile);
        profile_about = v.findViewById(R.id.profile_about);
        profile_update_about = v.findViewById(R.id.profile_update_about);
        bio_layout = v.findViewById(R.id.bio_layout);
        update_bioBtn = v.findViewById(R.id.update_bioBtn);
        about_layout = v.findViewById(R.id.about_layout);
        dismiss_bioBtn = v.findViewById(R.id.dismiss_bioBtn);
        change_profileimg = v.findViewById(R.id.change_profileimg);
        progress_photo = v.findViewById(R.id.progress_photo);
        userName_layout = v.findViewById(R.id.userName_layout);
        nameEdit_layout = v.findViewById(R.id.nameEdit_layout);
        dismiss_nameBtn = v.findViewById(R.id.dismiss_nameBtn);
        update_nameBtn = v.findViewById(R.id.update_nameBtn);
        profile_update_name = v.findViewById(R.id.profile_update_name);
        email_layout = v.findViewById(R.id.email_layout);
        passwordEdt_layout = v.findViewById(R.id.passwordEdt_layout);
        passwordEdt_layout2 = v.findViewById(R.id.passwordEdt_layout2);
        cancelUpdate = v.findViewById(R.id.cancelUpdate);
        updatePassBtn = v.findViewById(R.id.updatePassBtn);
        oldPass = v.findViewById(R.id.oldPass);
        newPass = v.findViewById(R.id.newPass);

        updatePassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdatePassword();
            }
        });

        passwordEdt_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordEdt_layout2.setVisibility(View.VISIBLE);
            }
        });
        cancelUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordEdt_layout2.setVisibility(View.GONE);
            }
        });



        email_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), R.string.email_edit_fail, Toast.LENGTH_SHORT).show();
            }
        });

        change_profileimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });


        about_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bio_layout.setVisibility(View.VISIBLE);


            }
        });
        userName_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameEdit_layout.setVisibility(View.VISIBLE);

            }
        });
        dismiss_nameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameEdit_layout.setVisibility(View.GONE);
            }
        });


        dismiss_bioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bio_layout.setVisibility(View.GONE);


            }
        });


        firestore.collection("User").document(currentemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String bio = task.getResult().getString("about");

                    profile_about.setText(bio);

                }
            }
        });
        update_nameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = profile_update_name.getText().toString();
                if (!TextUtils.isEmpty(profile_update_name.getText().toString())) {
                    firestore.collection("User").document(currentemail).update("userName", newName).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), R.string.update_succ, Toast.LENGTH_SHORT).show();
                                nameEdit_layout.setVisibility(View.GONE);
                                profile_update_name.getText().clear();
                                firestore.collection("User").document(currentemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            String bio = task.getResult().getString("userName");

                                            profile_username.setText(bio);

                                        }
                                    }
                                });

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), R.string.empty_field, Toast.LENGTH_SHORT).show();
                }

            }
        });
        update_bioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bio = profile_update_about.getText().toString();
                if (!TextUtils.isEmpty(profile_update_about.getText().toString())) {
                    firestore.collection("User").document(currentemail).update("about", bio).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getActivity(), R.string.update_succ , Toast.LENGTH_SHORT).show();
                                bio_layout.setVisibility(View.GONE);
                                profile_update_about.getText().clear();
                                firestore.collection("User").document(currentemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            String bio = task.getResult().getString("about");

                                            profile_about.setText(bio);

                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(getActivity(), R.string.update_fail, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), R.string.empty_bio, Toast.LENGTH_SHORT).show();
                }

            }
        });


        go_influencer_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();

                bundle.putString("recommend",currentemail);

                InfluencerProfileFragment fragobj = new InfluencerProfileFragment();
                fragobj.setArguments(bundle);

                getParentFragmentManager().beginTransaction().replace(R.id.inf_container,
                        fragobj).addToBackStack(null).commit();

            }
        });


        reference1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    String nameResult = task.getResult().getString("userName");
                    String emailResult = task.getResult().getString("email");
                    String passwordResult = task.getResult().getString("password");
                    String img = task.getResult().getString("imageUrl");
                    if (task.getResult().get("type").toString().equalsIgnoreCase("1")) {

                        go_influencer_profile.setVisibility(View.VISIBLE);

                    }
                    profile_username.setText(nameResult);
                    profile_email.setText(emailResult);
                    profile_password.setText(passwordResult);
                    Picasso.get().load(img).into(profile_editimg);

                }
            }
        });


        return v;
    }

    private void UpdatePassword() {

        String email = user.getEmail();
        String old_Pass = oldPass.getText().toString().trim();
        String new_Pass = newPass.getText().toString().trim();
        AuthCredential credential = EmailAuthProvider
                .getCredential(email, old_Pass);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(new_Pass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        userprofilenode.update("password",new_Pass);
                                        oldPass.getText().clear();
                                        newPass.getText().clear();
                                        Toast.makeText(getActivity(), "Password Changed ", Toast.LENGTH_SHORT).show();
                                        Log.d("pas", "Password updated");
                                        profile_password.setText(new_Pass);
                                        passwordEdt_layout2.setVisibility(View.GONE);

                                    } else {
                                        Toast.makeText(getActivity(), "Error password not Changed", Toast.LENGTH_SHORT).show();
                                        Log.d("pas", "Error password not updated");
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(getActivity(), "Error auth failed", Toast.LENGTH_SHORT).show();
                            Log.d("pas", "Error auth failed");


                        }
                    }
                });
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
            mImageUri = data.getData();
            Picasso.get().load(mImageUri).into(profile_editimg);
            progress_photo.setVisibility(View.VISIBLE);


            uploadFile();
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContext().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                }
                            }, 500);
                            Uri downloadUrl = taskSnapshot.getUploadSessionUri();
                            fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String ImageUrl = uri.toString();
                                    map.put("imageUrl", ImageUrl);
                                    firestore.collection("User").document(currentemail).update(map);
                                    Toast.makeText(getActivity(), R.string.success_image, Toast.LENGTH_SHORT).show();
                                    progress_photo.setVisibility(View.GONE);


                                }
                            });


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            progress_photo.setVisibility(View.VISIBLE);

                        }
                    });
        } else {
//            Toast.makeText(this, "No file selected For Image", Toast.LENGTH_SHORT).show();
        }
    }

}
