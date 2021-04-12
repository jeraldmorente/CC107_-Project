package com.cc107.mealhub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;

public class MHProfile extends AppCompatActivity {
    private TextView fullname,
                     email,
                     username,
                     gender,
                     joindate;
    SessionManager sessionManager;
    CircleImageView user_photo;
    private Button button_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealhub_profile);
        sessionManager = new SessionManager( this);

        fullname = findViewById(R.id.profile_fullname);
        email = findViewById(R.id.profile_email);
        username = findViewById(R.id.profile_username);
        gender = findViewById(R.id.profile_gender);
        joindate = findViewById(R.id.profile_join);
        button_update = findViewById(R.id.button_edit);
        user_photo = findViewById(R.id.picture);

        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        String mName = user.get(sessionManager.NAME);
        String mEmail = user.get(sessionManager.EMAIL);
        String mUsername = user.get(sessionManager.USERNAME);
        String mGender = user.get(sessionManager.GENDER);
        String mJoindate = user.get(sessionManager.JOINDATE);
        String mPhoto = user.get(sessionManager.PHOTO);

        if (mPhoto != null){
            Glide.with(this)
                    .load(mPhoto).error(R.mipmap.ic_launcher)
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(user_photo);
        }
        else if(!mPhoto.isEmpty()){
            String default_photo = "https://jdrael.000webhostapp.com/MealHub/photo/mh_default.png";
            Glide.with(this)
                    .load(default_photo).error(R.mipmap.ic_launcher)
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(user_photo);
        }

        fullname.setText(mName);
        email.setText(mEmail);
        username.setText(mUsername);
        gender.setText(mGender);
        joindate.setText(mJoindate);
        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent edit_profile = new Intent(MHProfile.this, MHProfileUpdate.class);
                startActivity(edit_profile);
            }
        });
    }

    public void onBackPressed(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Go back to Dashboard?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent b= new Intent(MHProfile.this, MHDashboard.class );
                startActivity(b);
                finish();
            }
        });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    }



