package com.cc107.mealhub;

public class RatingReviews {
    private int rateID, userID, mealID;
    private String mealRate;
    private String rateReview, rateDate, fullname, userphoto;

    public RatingReviews(int rateID, int userID, int mealID, String mealRate, String rateReview, String rateDate, String fullname, String userphoto) {
        this.rateID = rateID;
        this.userID = userID;
        this.mealID = mealID;
        this.mealRate = mealRate;
        this.rateReview = rateReview;
        this.rateDate = rateDate;
        this.fullname = fullname;
        this.userphoto = userphoto;
    }

    public int getRateID() {
        return rateID;
    }

    public int getUserID() {
        return userID;
    }

    public int getMealID() {
        return mealID;
    }

    public String getMealRate() {
        return mealRate;
    }

    public String getRateReview() {
        return rateReview;
    }

    public String getRateDate() {
        return rateDate;
    }

    public String getFullname() {
        return fullname;
    }

    public String getUserphoto() {
        return userphoto;
    }
}
