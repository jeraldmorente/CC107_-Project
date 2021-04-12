package com.cc107.mealhub;

public class DashboardMeal {
    private int mealID, userID;//, likeValue, favValue;
    private String mealName, mealCategory, mealProcedure, mealDate, mealPhoto, userphoto, fullname;


    public DashboardMeal(int mealID, String mealName, String mealCategory, String mealPhoto, String mealProcedure, String mealDate, int userID, String userphoto, String fullname)//, int likeValue, int favValue)
    {
        this.mealID = mealID;
        this.userID = userID;
        this.mealName = mealName;
        this.mealCategory = mealCategory;
        this.mealProcedure = mealProcedure;
        this.mealDate = mealDate;
        this.mealPhoto = mealPhoto;
        this.userphoto = userphoto;
        this.fullname= fullname;
       // this.likeValue = likeValue;
        //this.favValue = favValue;
    }

    public int getMealID() {
        return mealID;
    }

    public int getUserID() {
        return userID;
    }

    public String getMealName() {
        return mealName;
    }

    public String getMealCategory() {
        return mealCategory;
    }

    public String getMealProcedure() {
        return mealProcedure;
    }

    public String getMealDate() {
        return mealDate;
    }

    public String getMealPhoto() {
        return mealPhoto;
    }

    public String getuserPhoto() {
        return userphoto;
    }

    public String getFullname() {
        return fullname;
    }
/*
    public int getLikeValue() {
        return likeValue;
    }

    public int getFavValue() {
        return favValue;
    }

 */
}
