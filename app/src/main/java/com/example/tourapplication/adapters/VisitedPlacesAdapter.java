package com.example.tourapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourapplication.R;
import com.example.tourapplication.influencer.Posts;

import java.util.ArrayList;

public class VisitedPlacesAdapter extends RecyclerView.Adapter<VisitedPlacesAdapter.VisitedViewHolder> {
    private ArrayList<Posts> visitedList;
    private Context mContext;

    public VisitedPlacesAdapter(ArrayList<Posts> visitedList, Context mContext) {
        this.visitedList = visitedList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public VisitedPlacesAdapter.VisitedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.visitedplaces_item, parent, false);


        return new VisitedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitedPlacesAdapter.VisitedViewHolder holder, int position) {
        Posts visitedPost = visitedList.get(position);
        holder.visitedplaces_txt.setText(visitedPost.getLocation());
    }

    @Override
    public int getItemCount() {
        return visitedList.size();
    }

    public class VisitedViewHolder extends RecyclerView.ViewHolder {

        TextView visitedplaces_txt;
        public VisitedViewHolder(@NonNull View itemView) {
            super(itemView);
            visitedplaces_txt = itemView.findViewById(R.id.visitedplaces_txt);


        }
    }
}
