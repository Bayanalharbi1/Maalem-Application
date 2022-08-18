package com.example.tourapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourapplication.R;
import com.example.tourapplication.influencer.Posts;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.FollowingViewHolder> {
    private ArrayList<Posts> followinglist;
    private Context mContext;

    public FollowingAdapter( ArrayList<Posts> followinglist, Context mContext){

        this.followinglist = followinglist;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public FollowingAdapter.FollowingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.following_item, parent, false);


        return new FollowingAdapter.FollowingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowingAdapter.FollowingViewHolder holder, int position) {
        Posts user = followinglist.get(position);
        holder.following_name.setText(user.getChannel());
        holder.following_location.setText(user.getLocation());
        String ImageUrl = null;
        ImageUrl = user.getInfluncerimg();
        Picasso.get().load(ImageUrl).into(holder.following_img);
    }

    @Override
    public int getItemCount() {
        return followinglist.size();
    }

    public class FollowingViewHolder extends RecyclerView.ViewHolder {
        ImageView following_img;
        TextView following_name ,following_location ;

        public FollowingViewHolder(@NonNull View itemView) {
            super(itemView);

            following_img = itemView.findViewById(R.id.following_user_side_img);
            following_name = itemView.findViewById(R.id.following_user_side);
            following_location = itemView.findViewById(R.id.following_location_user_side);

        }
    }
}
