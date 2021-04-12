package com.cc107.mealhub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    public static final String NAME = "NAME";
    public static final String USERNAME = "USERNAME";
    public static final String EMAIL = "EMAIL";
    public static final String GENDER = "GENDER";
    public static final String JOINDATE = "JOINDATE";
    public static final String USERID = "USERID";
    public static final String PHOTO = "PHOTO";

    public SessionManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createSession(String fullname, String email, String username, String gender, String joinDate, String userID, String userphoto){
        editor.putBoolean(LOGIN, true);
        editor.putString(NAME, fullname);
        editor.putString(EMAIL, email);
        editor.putString(USERNAME, username);
        editor.putString(GENDER, gender);
        editor.putString(JOINDATE, joinDate);
        editor.putString(USERID, userID);
        editor.putString(PHOTO, userphoto);
        editor.apply();
    }

    public boolean isLoggin(){
        return sharedPreferences.getBoolean(LOGIN, false);
    }

    public void checkLogin(){
        if (!this.isLoggin()){
            Intent i = new Intent(context, MHLogin.class);
            context.startActivity(i);
            ((MHDashboard) context).finish();
        }
    }

    public HashMap<String, String> getUserDetail(){

        HashMap<String, String> user = new HashMap<>();
        user.put(NAME, sharedPreferences.getString(NAME, null));
        user.put(EMAIL, sharedPreferences.getString(EMAIL, null));
        user.put(GENDER, sharedPreferences.getString(GENDER, null));
        user.put(JOINDATE, sharedPreferences.getString(JOINDATE, null));
        user.put(USERNAME, sharedPreferences.getString(USERNAME, null));
        user.put(USERID, sharedPreferences.getString(USERID, null));
        user.put(PHOTO, sharedPreferences.getString(PHOTO, null));
        return user;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, MHLogin.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(i);
        ((MHDashboard) context).finish();
    }
}