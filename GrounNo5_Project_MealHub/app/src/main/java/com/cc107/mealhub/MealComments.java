package com.cc107.mealhub;

public class MealComments {
    private String commentID, comments, commentDate, mealID,  userphoto, fullname;

    public MealComments(String commentID, String comments, String commentDate, String mealID, String userphoto, String fullname) {
        this.commentID = commentID;
        this.comments = comments;
        this.commentDate = commentDate;
        this.mealID = mealID;
        this.userphoto = userphoto;
        this.fullname = fullname;
    }

    public String getCommentID() {
        return commentID;
    }

    public String getComments() {
        return comments;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public String getMealID() {
        return mealID;
    }

    public String getUserphoto() {
        return userphoto;
    }

    public String getFullname() {
        return fullname;
    }
}
