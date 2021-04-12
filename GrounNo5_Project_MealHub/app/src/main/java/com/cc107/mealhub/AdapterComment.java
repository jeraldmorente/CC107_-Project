package com.cc107.mealhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class AdapterComment extends RecyclerView.Adapter<AdapterComment.CommentViewHolder> {
    private Context mCtx;
    private List<MealComments> mealCommentsList;

    public AdapterComment(Context mCtx, List<MealComments> mealCommentsList) {
        this.mCtx = mCtx;
        this.mealCommentsList = mealCommentsList;
    }

    public AdapterComment(MHRatings mCtx, List<RatingReviews> ratingReviewsList) {
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_comment, parent, false);
        return new AdapterComment.CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, final int position) {
        MealComments mealComments = mealCommentsList.get(position);
        holder.fullname.setText( mealComments.getFullname());
        holder.comments.setText( mealComments.getComments());
        holder.commentDate.setText(mealComments.getCommentDate());
        Glide.with(mCtx)
                .load( mealComments.getUserphoto()).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.userphoto);
    }

    @Override
    public int getItemCount() {
        return mealCommentsList.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        TextView comments, commentDate, fullname;
        ImageView userphoto;
        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userphoto = itemView.findViewById(R.id.com_picture);
            fullname = itemView.findViewById(R.id.com_textuser);
            comments = itemView.findViewById(R.id.view_comment);
            commentDate = itemView.findViewById(R.id.com_textdate);

        }
    }
}
