package com.example.tourapplication.user;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tourapplication.AddPhoto;
import com.example.tourapplication.R;
import com.example.tourapplication.RealPathUtil;
import com.example.tourapplication.ReturnResult;
import com.example.tourapplication.ReturnResultApiInterface;
import com.example.tourapplication.UploadApis;
import com.example.tourapplication.databinding.FragmentUploadImgBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UploadImgFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UploadImgFragment extends Fragment {

    FragmentUploadImgBinding binding ;

    String path;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    String currentemail = firebaseAuth.getCurrentUser().getEmail();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UploadImgFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UploadImgFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UploadImgFragment newInstance(String param1, String param2) {
        UploadImgFragment fragment = new UploadImgFragment();
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


        binding = FragmentUploadImgBinding.inflate(inflater,container, false);




        binding.uploadImageTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open camera
                if (ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 10);
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
        });



        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 10 && resultCode == RESULT_OK) {

            Uri uri = data.getData();
            Context context = getActivity();
            path = RealPathUtil.getRealPath(context, uri);

            Bitmap bitmap = BitmapFactory.decodeFile(path);
            binding.uploadedImage.setImageBitmap(bitmap);
            binding.progressUpload.setVisibility(View.VISIBLE);
            ImageUpload(currentemail);
            ShowResultDialog();


        }

    }

    private void ImageUpload(String email) {

        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://165.232.90.241/api/").addConverterFactory(GsonConverterFactory.create()).build();


        File file = new File(path);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        RequestBody req_email = RequestBody.create(MediaType.parse("multipart/form-data"), email);
        UploadApis uploadApis = retrofit.create(UploadApis.class);


        Call<AddPhoto> call = uploadApis.uploadImage(body, req_email);

        call.enqueue(new Callback<AddPhoto>() {
            @Override
            public void onResponse(Call<AddPhoto> call, Response<AddPhoto> response) {
                if (response.isSuccessful()) {
                    if (response.body().getCode().toString().equalsIgnoreCase("200")) {
                        Toast.makeText(getActivity(), R.string.up_suc, Toast.LENGTH_SHORT).show();
                        //  progressDialog.dismiss();

                    } else {
                        Toast.makeText(getActivity(), R.string.up_fail, Toast.LENGTH_SHORT).show();
                        //  progressDialog.dismiss();

                    }
                }
            }

            @Override
            public void onFailure(Call<AddPhoto> call, Throwable t) {
                Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();


                Log.d("TAG", "failedresponse: " + t.toString());

            }
        });


    }

    private void ShowResultDialog() {

        AlertDialog Result_Dialog = new AlertDialog.Builder(getActivity()).create();
        Result_Dialog.setTitle(getString(R.string.accident_alert));
        final List<ReturnResult>[] arrayList = new List[]{new ArrayList<ReturnResult>()};


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://165.232.90.241/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ReturnResultApiInterface returnResultApiInterface = retrofit.create(ReturnResultApiInterface.class);


        Call<List<List<ReturnResult>>> call = returnResultApiInterface.getResultList();


        final Handler handler2 = new Handler(Looper.getMainLooper());
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {

                call.enqueue(new Callback<List<List<ReturnResult>>>() {
                    @Override
                    public void onResponse(Call<List<List<ReturnResult>>> call, Response<List<List<ReturnResult>>> response) {
                        if (response.isSuccessful()) {
                            List<ReturnResult> list = response.body().get(0);
                            LinkedList<String> list1 = new LinkedList<String>();

                            for (ReturnResult d : list) {
                                if (d.getEmail() != null && d.getEmail().equalsIgnoreCase(currentemail)) {
                                    if (d.getResult() != null) {
                                        list1.add(d.getResult());
                                        list1.remove("");

                                    }
                                }

                            }
                            Log.d("listonResponse", "onResponse: " + list1);
                            if (!list1.getLast().trim().isEmpty()){
                                binding.uploadImageTv.setText(list1.getLast().trim());
                                Toast.makeText(getActivity(), R.string.api_redirect_sucess, Toast.LENGTH_SHORT).show();


                                final Handler handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        binding.progressUpload.setVisibility(View.GONE);
                                        Bundle bundle = new Bundle();



                                        bundle.putString("matched_result",list1.getLast().trim());

                                        UserHomeFragment fragobj = new UserHomeFragment();
                                        fragobj.setArguments(bundle);

                                        getParentFragmentManager().beginTransaction().replace(R.id.container,
                                                fragobj).commit();


                                    }
                                }, 3000);

                            }else {
                                Toast.makeText(getActivity(), R.string.api_failed, Toast.LENGTH_SHORT).show();
                            }













                        } else {
                            Toast.makeText(getActivity(), R.string.api_failed_2, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<List<List<ReturnResult>>> call, Throwable t) {
                        Toast.makeText(getActivity(), t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        // Result_Dialog.setMessage(t.getLocalizedMessage());
                        Log.d("fail", "onFailure: " + t.getLocalizedMessage());
                    }
                });


            }
        }, 6000);




    }


}