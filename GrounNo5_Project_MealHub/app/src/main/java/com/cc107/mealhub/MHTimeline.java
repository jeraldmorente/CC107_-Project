package com.cc107.mealhub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MHTimeline extends AppCompatActivity {
    private static final String GETMEAL = "https://jdrael.000webhostapp.com/MealHub/getUsersMeal.php?setUser=";
    private static final String COUNTMEAL = "https://jdrael.000webhostapp.com/MealHub/countUsersMeal.php?setUser=";
    private static final String COUNTBRK = "https://jdrael.000webhostapp.com/MealHub/countBreakfast.php?setUser=";
    private static final String COUNTLNCH = "https://jdrael.000webhostapp.com/MealHub/countLunch.php?setUser=";
    private static final String COUNTDINNER = "https://jdrael.000webhostapp.com/MealHub/countDinner.php?setUser=";
    private RecyclerView timelineRecycler;
    AdapterTimeline adapter;
    private onClickInterface onclickInterface;
    List<UsersTimeline> usersTimelineList;
    String getID, setUser;
    SessionManager sessionManager;
    TextView loggedFullname, countotalMeal, countBreakfast, countLunch, countDinner;

    ImageView loggedProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealhub_timeline);
        sessionManager = new SessionManager( this);

        onclickInterface = new onClickInterface() {
            @Override
            public void setClick(int abc) {
            }
        };

        countotalMeal = findViewById(R.id.timeline_count);
        countBreakfast = findViewById(R.id.breakfast_count);
        countLunch = findViewById(R.id.lunch_count);
        countDinner = findViewById(R.id.dinner_count);
        loggedProfile = findViewById(R.id.timeline_picture);
        usersTimelineList = new ArrayList<>();
        timelineRecycler = findViewById(R.id.timeline_recycler);
        timelineRecycler.setHasFixedSize(true);
        timelineRecycler.setLayoutManager(new LinearLayoutManager(this));
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        String mName = user.get(sessionManager.NAME);
        String mEmail = user.get(sessionManager.EMAIL);
        String mPhoto = user.get(sessionManager.PHOTO);
        getID = user.get(sessionManager.USERID);

        if (mPhoto != null){
            Glide.with(this)
                    .load(mPhoto).error(R.mipmap.ic_launcher)
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(loggedProfile);
        }

        else if(!mPhoto.isEmpty()){
            String default_photo = "https://jdrael.000webhostapp.com/MealHub/photo/mh_default.png";
            Glide.with(this)
                    .load(default_photo).error(R.mipmap.ic_launcher)
                    .apply(RequestOptions.circleCropTransform())
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(loggedProfile);
        }
        getMealCount();
        getBreakfastCount();
        getLunchCount();
        getDinnerCount();
        getUsersTimeline();
    }

    private void getMealCount() {
        setUser = getID;
        ContentValues values = new ContentValues();
        values.put("1", setUser);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, COUNTMEAL+setUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashMeal = new JSONArray(response);
                            JSONObject mealObject = dashMeal.getJSONObject(0);
                            int totalMealData = mealObject.getInt("totalMeal");
                            String totalMeal = String.valueOf(totalMealData);
                            countotalMeal.setText("You shared "+totalMeal+" meals.");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHTimeline.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getBreakfastCount() {
        setUser = getID;
        ContentValues values = new ContentValues();
        values.put("1", setUser);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, COUNTBRK+setUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashMeal = new JSONArray(response);
                            JSONObject mealObject = dashMeal.getJSONObject(0);
                            int totalMealData = mealObject.getInt("totalBreakfast");
                            String totalMeal = String.valueOf(totalMealData);
                            countBreakfast.setText("Breakfast ("+totalMeal+")");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHTimeline.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getLunchCount() {
        setUser = getID;
        ContentValues values = new ContentValues();
        values.put("1", setUser);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, COUNTLNCH+setUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashMeal = new JSONArray(response);
                            JSONObject mealObject = dashMeal.getJSONObject(0);
                            int totalMealData = mealObject.getInt("totalLunch");
                            String totalMeal = String.valueOf(totalMealData);
                            countLunch.setText("Lunch ("+totalMeal+")");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHTimeline.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getDinnerCount() {
        setUser = getID;
        ContentValues values = new ContentValues();
        values.put("1", setUser);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, COUNTDINNER+setUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashMeal = new JSONArray(response);
                            JSONObject mealObject = dashMeal.getJSONObject(0);
                            int totalMealData = mealObject.getInt("totalDinner");
                            String totalMeal = String.valueOf(totalMealData);
                            countDinner.setText("Dinner ("+totalMeal+")");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHTimeline.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getUsersTimeline() {
        setUser = getID;
        ContentValues values = new ContentValues();
        values.put("1", setUser);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETMEAL+setUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashMeal = new JSONArray(response);
                            for(int i=0; i<dashMeal.length();i++){
                                JSONObject mealObject = dashMeal.getJSONObject(i);
                                String userphoto = mealObject.getString("userphoto");
                                String fullname = mealObject.getString("fullname");
                                String mealDate = mealObject.getString("mealDate");
                                String mealName = mealObject.getString("mealName");
                                String mealPhoto = mealObject.getString("mealPhoto");
                                String mealID = mealObject.getString("mealID");
                                String mealCategory = mealObject.getString("mealCategory");
                                String mealProcedure = mealObject.getString("mealProcedure");
                                UsersTimeline usersTimeline = new UsersTimeline(userphoto, fullname, mealDate, mealName, mealPhoto, mealID, mealCategory, mealProcedure);
                                usersTimelineList.add(usersTimeline);
                            }
                            adapter = new AdapterTimeline(MHTimeline.this, usersTimelineList, onclickInterface);
                            timelineRecycler.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHTimeline.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onBackPressed(){
                Intent b = new Intent(MHTimeline.this, MHDashboard.class );
                startActivity(b);
                finish();
    }
}
