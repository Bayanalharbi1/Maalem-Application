package com.example.tourapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourapplication.R;
import com.example.tourapplication.RecyclerViewInterface;
import com.example.tourapplication.influencer.Posts;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MainPostsAdaptar extends RecyclerView.Adapter<MainPostsAdaptar.MainViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private ArrayList<Posts> mainList;
    private Context mContext;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();


    public MainPostsAdaptar(ArrayList<Posts> mainList, Context mContext, RecyclerViewInterface recyclerViewInterface) {
        this.mainList = mainList;
        this.mContext = mContext;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.influencer_content_item, parent, false);


        return new MainViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        Posts mainPost = mainList.get(position);
        holder.place_name_mainiten.setText(mainPost.getPostName());
        String ImageUrl = null;
        ImageUrl = mainPost.getImageposturl();
        Picasso.get().load(ImageUrl).into(holder.single_img);
        firestore.collection("User").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().get("type").toString().equalsIgnoreCase("0")) {

                        holder.delete_post.setVisibility(View.GONE);

                    } else {
                        holder.delete_post.setVisibility(View.VISIBLE);

                    }
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mainList.size();
    }


    public static class MainViewHolder extends RecyclerView.ViewHolder {
        ImageView single_img, delete_post;
        TextView place_name_mainiten;

        public MainViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            single_img = itemView.findViewById(R.id.single_img);
            place_name_mainiten = itemView.findViewById(R.id.place_name_mainitem);
            delete_post = itemView.findViewById(R.id.delete_post);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemLongClick(pos);
                        }
                    }
                    return true;
                }
            });

            delete_post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onDeleteClick(pos);
                        }
                    }
                }
            });

        }
    }
}
