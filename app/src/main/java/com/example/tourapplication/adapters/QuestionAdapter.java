package com.example.tourapplication.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tourapplication.Question;
import com.example.tourapplication.R;
import com.example.tourapplication.RecyclerViewInterface;
import com.example.tourapplication.influencer.Posts;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    private ArrayList<Question> qList;
    private Context mContext;

    public QuestionAdapter(RecyclerViewInterface recyclerViewInterface, ArrayList<Question> qList, Context mContext) {
        this.recyclerViewInterface = recyclerViewInterface;
        this.qList = qList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public QuestionAdapter.QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.questions_layout, parent, false);

        return new QuestionViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionAdapter.QuestionViewHolder holder, int position) {
        Question questionPost = qList.get(position);
        holder.qtextView.setText(questionPost.getQuestion());
        holder.anstextview.setText(questionPost.getAnswer());
    }

    @Override
    public int getItemCount() {
        return qList.size();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {
        TextView qtextView, anstextview;

        public QuestionViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            qtextView = itemView.findViewById(R.id.question_title);
            anstextview = itemView.findViewById(R.id.ans_title);
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
