package com.cc107.mealhub;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class AdapterRatings extends RecyclerView.Adapter<AdapterRatings.RatingsViewHolder>  {
    private Context mCtx;
    private List<RatingReviews> ratingReviewsList;

    public AdapterRatings(Context mCtx, List<RatingReviews> ratingReviewsList) {
        this.mCtx = mCtx;
        this.ratingReviewsList = ratingReviewsList;
    }

    @NonNull
    @Override
    public AdapterRatings.RatingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_rater, parent, false);
        return new AdapterRatings.RatingsViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull AdapterRatings.RatingsViewHolder holder, int position) {
        RatingReviews ratingReviews = ratingReviewsList.get(position);
        holder.fullname.setText( ratingReviews.getFullname());
        holder.rateReview.setText( ratingReviews.getRateReview());
        holder.rateDate.setText(ratingReviews.getRateDate());
        String mealRatedata = ratingReviews.getMealRate();
        holder.mealRate.setRating(Float.parseFloat(String.valueOf(mealRatedata)));
        Glide.with(mCtx)
                .load( ratingReviews.getUserphoto()).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.userphoto);
    }

    @Override
    public int getItemCount() {
        return ratingReviewsList.size();
    }


    public class RatingsViewHolder extends RecyclerView.ViewHolder{
        TextView rateReview, rateDate, fullname;
        RatingBar mealRate;
        ImageView userphoto;
        public RatingsViewHolder(@NonNull View itemView) {
            super(itemView);
            userphoto = itemView.findViewById(R.id.rate_picture);
            fullname = itemView.findViewById(R.id.rate_textuser);
            rateReview = itemView.findViewById(R.id.view_review);
            rateDate = itemView.findViewById(R.id.rate_textdate);
            mealRate = itemView.findViewById(R.id.rating_listbar);
        }
    }
}

