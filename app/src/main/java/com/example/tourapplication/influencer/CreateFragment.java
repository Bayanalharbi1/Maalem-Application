package com.example.tourapplication.influencer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourapplication.R;
import com.example.tourapplication.user.UserMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateFragment extends Fragment  {

     ImageView img_upload;
    private TextView uploadTxt;
    private EditText place_name, place_desc, place_location;
    private Button createPlaceBtn;
    private static final int PICK_IMAGE_REQUEST = 1;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private ProgressBar createPlace_progress;
    StorageTask mUploadTask;
    Uri nImageUri;
    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("UserPosts");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentemail = user.getEmail();


    private String type;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateFragment newInstance(String param1, String param2) {
        CreateFragment fragment = new CreateFragment();
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
        View v = inflater.inflate(R.layout.fragment_create, container, false);

        img_upload = v.findViewById(R.id.img_upload_post);
        place_name = v.findViewById(R.id.place_name);
        place_desc = v.findViewById(R.id.place_desc);
        place_location = v.findViewById(R.id.place_location);
        createPlace_progress = v.findViewById(R.id.createPlace_progress);
        uploadTxt = v.findViewById(R.id.uploadTxt);
        createPlaceBtn = v.findViewById(R.id.createPlaceBtn);



        uploadTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });
        createPlaceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PostPlace();
            }
        });
        return v;

    }

    public void PostPlace() {

        closeKeyboard();
        createPlace_progress.setVisibility(View.VISIBLE);
        String st_place_name = place_name.getText().toString().trim();
        String st_descName = place_desc.getText().toString().trim();
        String st_location = place_location.getText().toString().trim();

        String post_id = UUID.randomUUID().toString();
        Date currentTime =  Calendar.getInstance().getTime();




        firestore.collection("User").document(currentemail).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().get("type").toString().equalsIgnoreCase("1")){

                        String influncer_name = task.getResult().getString("userName");
                        String influncer_img = task.getResult().getString("imageUrl");
                        String channel = task.getResult().getString("channelName");
                        if (nImageUri != null && st_descName != null && st_descName != null && st_location != null) {
                            StorageReference fileReference = mStorageRef.child(nImageUri.getLastPathSegment());
                            mUploadTask = fileReference.putFile(nImageUri)
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
                                                    String imagePostUrl = uri.toString();
                                                    Posts infpost = new Posts(st_place_name, st_descName, imagePostUrl, influncer_name, influncer_img, st_location , channel , currentemail , post_id , currentTime);
                                                    firestore.collection("User").document(currentemail).collection("MyPosts").document(post_id).set(infpost).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                            } else {


                                                            }
                                                        }
                                                    });
                                                    firestore.collection("Post").document(post_id).set(infpost).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Handler handler = new Handler();
                                                                handler.postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {

                                                                        Toast.makeText(getActivity(), "Upload success :)", Toast.LENGTH_SHORT).show();
                                                                        createPlace_progress.setVisibility(View.INVISIBLE);
                                                                        place_name.getText().clear();
                                                                        place_location.getText().clear();
                                                                        place_desc.getText().clear();
                                                                        img_upload.setImageDrawable(getResources().getDrawable(R.drawable.image_gallery));
                                                                        createPlace_progress.setVisibility(View.INVISIBLE);

                                                                    }
                                                                }, 2000);


                                                            } else {
                                                                Toast.makeText(getActivity(), "Upload Failed :(", Toast.LENGTH_SHORT).show();
                                                                createPlace_progress.setVisibility(View.INVISIBLE);

                                                            }
                                                        }
                                                    });


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
                                            createPlace_progress.setVisibility(View.VISIBLE);
                                        }
                                    });
                        } else {
                            createPlace_progress.setVisibility(View.INVISIBLE);
                            Toast.makeText(getActivity(), "Complete Post details...", Toast.LENGTH_SHORT).show();
                        }

                        firestore.collection("User").document(currentemail).update("location",st_location).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });


                    }else {
                        createPlace_progress.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Not Allowed to add posts", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }


    private void closeKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK
                && data != null && data.getData() != null) {
            nImageUri = data.getData();
            Picasso.get().load(nImageUri).into(img_upload);
        }
    }


}
