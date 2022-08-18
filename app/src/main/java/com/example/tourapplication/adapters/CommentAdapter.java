package com.example.tourapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourapplication.Comment;
import com.example.tourapplication.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private ArrayList<Comment> commentList;
    private Context mContext;
    public CommentAdapter(ArrayList<Comment> commentList, Context mContext) {
        this.commentList = commentList;
        this.mContext = mContext;

    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);


        return new CommentAdapter.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment user_comment = commentList.get(position);

        String ImageUrl = null;
        ImageUrl = user_comment.getCommentProfilePic();
        Picasso.get().load(ImageUrl).into(holder.img_user_comment);

        holder.cooment_txt.setText(user_comment.getCommentText());
        holder.comment_UserName.setText(user_comment.getCommentusername());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView img_user_comment;
        TextView cooment_txt , comment_UserName;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            img_user_comment = itemView.findViewById(R.id.img_user_comment);
            cooment_txt = itemView.findViewById(R.id.cooment_txt);
            comment_UserName = itemView.findViewById(R.id.comment_UserName);
        }
    }
}
