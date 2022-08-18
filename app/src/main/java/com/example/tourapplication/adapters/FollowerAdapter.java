package com.example.tourapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourapplication.Followers;
import com.example.tourapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FollowerAdapter extends RecyclerView.Adapter<FollowerAdapter.FollowerViewHolder>{
    private ArrayList<Followers> followerlist;
    private Context mContext;


    public FollowerAdapter(ArrayList<Followers> followerlist, Context mContext){
        this.followerlist = followerlist;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public FollowerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.follower_item, parent, false);


        return new FollowerAdapter.FollowerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowerViewHolder holder, int position) {
        Followers user = followerlist.get(position);
        holder.follower_name.setText(user.getFollower_name());
        String ImageUrl = null;
        ImageUrl = user.getFollower_image();
        Picasso.get().load(ImageUrl).into(holder.follower_img);
        
        if (!user.isFollowedUser()){
            holder.followtxt_state.setText(R.string.unfollow_state);
            
        }else {
            holder.followtxt_state.setText(R.string.followed);

        }

    }

    @Override
    public int getItemCount() {
        return followerlist.size();
    }

    public class FollowerViewHolder extends RecyclerView.ViewHolder {

        ImageView follower_img;
        TextView follower_name ,followtxt_state ;

        public FollowerViewHolder(@NonNull View itemView) {
            super(itemView);

            follower_img = itemView.findViewById(R.id.follower_img);
            follower_name = itemView.findViewById(R.id.follower_name);
            followtxt_state = itemView.findViewById(R.id.followtxt_state);
        }
    }
}
