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

public class InfluencerPostsAdapter extends RecyclerView.Adapter<InfluencerPostsAdapter.InfleuncerPostsViewHolder> {
    @NonNull
    private Context mContext;
    private ArrayList<Posts> post;
    private final RecyclerViewInterface recyclerViewInterface;



    public InfluencerPostsAdapter(Context context, ArrayList<Posts> mPost, RecyclerViewInterface recyclerViewInterface) {
        mContext = context;
        post = mPost;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @Override
    public InfluencerPostsAdapter.InfleuncerPostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.influencer_content_item, parent, false);


        return new InfleuncerPostsViewHolder(v, recyclerViewInterface);
    }



    @Override
    public void onBindViewHolder(@NonNull InfluencerPostsAdapter.InfleuncerPostsViewHolder holder, int position) {
        Posts newPost = post.get(position);

        String ImageUrl = null;
        ImageUrl = newPost.getImageposturl();
        Picasso.get().load(ImageUrl).into(holder.news_img);
        holder.place_name_tv.setText(newPost.getPostName());

    }

    @Override
    public int getItemCount() {
        return post.size();
    }


    public void filterList(ArrayList<Posts> filteredList) {
        post = filteredList;
        notifyDataSetChanged();
    }

    public static class InfleuncerPostsViewHolder extends RecyclerView.ViewHolder {
        ImageView news_img , delete_post;
        TextView place_name_tv;

        public InfleuncerPostsViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);

            news_img = itemView.findViewById(R.id.single_img);
            delete_post = itemView.findViewById(R.id.delete_post);
            place_name_tv = itemView.findViewById(R.id.place_name_mainitem);

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
        }
    }
}
