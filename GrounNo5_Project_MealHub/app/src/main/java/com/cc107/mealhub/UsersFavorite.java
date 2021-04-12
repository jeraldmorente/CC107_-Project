package com.cc107.mealhub;

public class UsersFavorite {
    private String userphoto, fullname, mealDate, mealName, mealPhoto, favValue, mealID;

    public UsersFavorite(String userphoto, String fullname, String mealDate, String mealName, String mealPhoto, String favValue, String mealID) {
        this.userphoto = userphoto;
        this.fullname = fullname;
        this.mealDate = mealDate;
        this.mealName = mealName;
        this.mealPhoto = mealPhoto;
        this.favValue = favValue;
        this.mealID = mealID;

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

    public String getFavValue() {
        return favValue;
    }

    public String getMealID() {
        return mealID;
    }
}
