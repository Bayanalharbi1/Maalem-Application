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
import com.example.tourapplication.RecyclerViewInterface;
import com.example.tourapplication.user.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchInfluencerAdapter extends RecyclerView.Adapter<SearchInfluencerAdapter.SearchInfluencerViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private ArrayList<User> mInfList;
    private Context mContext;

    public SearchInfluencerAdapter(ArrayList<User> infList, Context mContext, RecyclerViewInterface recyclerViewInterface) {
        this.mInfList = infList;
        this.mContext = mContext;
        this.recyclerViewInterface = recyclerViewInterface;
    }


    @NonNull
    @Override
    public SearchInfluencerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.influencer_item, parent, false);


        return new SearchInfluencerViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchInfluencerViewHolder holder, int position) {
        User inf_usernPost = mInfList.get(position);

        String ImageUrl = null;
        ImageUrl = inf_usernPost.getImageUrl();
        Picasso.get().load(ImageUrl).into(holder.img_user_side);

        holder.name_user_side.setText(inf_usernPost.getChannelName());
        holder.location_txt_user_side.setText(inf_usernPost.getLocation());

    }

    @Override
    public int getItemCount() {
        return mInfList.size();
    }

    public void filterList(ArrayList<User> filteredList) {
        mInfList = filteredList;
        notifyDataSetChanged();
    }

    public static class SearchInfluencerViewHolder extends RecyclerView.ViewHolder {
        ImageView img_user_side;
        TextView name_user_side, location_txt_user_side;

        public SearchInfluencerViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            this.img_user_side = this.itemView.findViewById(R.id.img_user_side);
            this.name_user_side = this.itemView.findViewById(R.id.name_user_side);
            this.location_txt_user_side = this.itemView.findViewById(R.id.location_txt_user_side);

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
