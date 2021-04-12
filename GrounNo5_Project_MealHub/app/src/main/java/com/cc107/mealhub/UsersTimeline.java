package com.cc107.mealhub;

public class UsersTimeline {
    private String userphoto, fullname, mealDate, mealName, mealPhoto, mealID, mealCategory, mealProcedure;

    public UsersTimeline(String userphoto, String fullname, String mealDate, String mealName, String mealPhoto, String mealID, String mealCategory, String mealProcedure) {
        this.userphoto = userphoto;
        this.fullname = fullname;
        this.mealDate = mealDate;
        this.mealName = mealName;
        this.mealPhoto = mealPhoto;
        this.mealCategory = mealCategory;
        this.mealID = mealID;
        this.mealProcedure = mealProcedure;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public String getFullname() {
        return fullname;
    }

    public String getMealDate() {
        return mealDate;
    }

    public String getMealName() {
        return mealName;
    }

    public String getMealPhoto() {
        return mealPhoto;
    }

    public String getMealID() {
        return mealID;
    }

    public String getMealCategory() {
        return mealCategory;
    }

    public String getMealProcedure() {
        return mealProcedure;
    }
}
