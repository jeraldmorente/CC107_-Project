package com.cc107.mealhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MHRatings extends AppCompatActivity {
    String data_mealName, data_mealDate, data_mealDescription, data_fullname, data_mealCategory,
            data_mealPhoto, data_userphoto;
    String getID, set_meal;
    double sumRate;
    int data_mealID, data_userID;
    int star1, star2, star3, star4, star5, starSum, prog1, prog2, prog3, prog4, prog5;
    SessionManager sessionManager;
    final Handler handler = new Handler();

    private static String URL_ADDRATE ="https://jdrael.000webhostapp.com/MealHub/addRateReview.php";
    private static final String GETREVIEW = "https://jdrael.000webhostapp.com/MealHub/getRateReview.php?setMeal=";
    private static String GETTOTALRATER ="https://jdrael.000webhostapp.com/MealHub/countTotalRater.php?setMeal=";
    private static String GETTOTALNUM1 ="https://jdrael.000webhostapp.com/MealHub/count1Rate.php?setMeal=";
    private static String GETTOTALNUM2 ="https://jdrael.000webhostapp.com/MealHub/count2Rate.php?setMeal=";
    private static String GETTOTALNUM3 ="https://jdrael.000webhostapp.com/MealHub/count3Rate.php?setMeal=";
    private static String GETTOTALNUM4 ="https://jdrael.000webhostapp.com/MealHub/count4Rate.php?setMeal=";
    private static String GETTOTALNUM5 ="https://jdrael.000webhostapp.com/MealHub/count5Rate.php?setMeal=";
    private static String GETSUMRATE ="https://jdrael.000webhostapp.com/MealHub/countSumRate.php?setMeal=";

    ////////
    TextView mealName, userFullName, totalMealRate, totalRater, totalRates,
            total5, total4, total3, total2, total1 ;
    ImageView mealPhoto, userphoto;
    ProgressBar progress01, progress02, progress03, progress04, progress05;
    RatingBar totalRate, setRate;
    EditText rateReviewbox;
    Button submittRate;
    List<RatingReviews> ratingReviewsList;
    private RecyclerView rateRecycler;
    AdapterRatings adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealhub_ratings);
        sessionManager = new SessionManager( this);
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(sessionManager.USERID);

        ///////INITIALIZE////////////
        mealName = findViewById(R.id.rate_mealname);
        userFullName = findViewById(R.id.rate_mealUser);
        totalMealRate = findViewById(R.id.totalRate);
        totalRater = findViewById(R.id.rater);
        totalRates = findViewById(R.id.total_rater);
        mealPhoto = findViewById(R.id.rate_mealimage);
        userphoto = findViewById(R.id.rate_userphoto);
        progress01 = findViewById(R.id.ratebar1);
        progress02 = findViewById(R.id.ratebar2);
        progress03 = findViewById(R.id.ratebar3);
        progress04 = findViewById(R.id.ratebar4);
        progress05 = findViewById(R.id.ratebar5);
        totalRate = findViewById(R.id.totalRate_bar);

        total1 = findViewById(R.id.total_num1);
        total2 = findViewById(R.id.total_num2);
        total3 = findViewById(R.id.total_num3);
        total4 = findViewById(R.id.total_num4);
        total5 = findViewById(R.id.total_num5);

        setRate = findViewById(R.id.meal_ratebar);
        submittRate = findViewById(R.id.button_rate);
        rateReviewbox= findViewById(R.id.review_box);

        ratingReviewsList = new ArrayList<>();
        rateRecycler = findViewById(R.id.rate_recycler);
        rateRecycler.setHasFixedSize(true);
        rateRecycler.setLayoutManager(new LinearLayoutManager(this));

        ////INTENT DATA////////////////////////////////////
        Intent intent = getIntent();
        data_mealID = intent.getIntExtra("mealID", 0);
        data_mealName = intent.getStringExtra("mealName");
        data_mealCategory = intent.getStringExtra("mealCategory");
        data_mealPhoto = intent.getStringExtra("mealPhoto");
        data_mealDescription = intent.getStringExtra("mealProcedure");
        data_mealDate = intent.getStringExtra("mealDate");
        data_userID = intent.getIntExtra("userID",0);
        data_userphoto = intent.getStringExtra("userphoto");
        data_fullname = intent.getStringExtra("fullname");

        ///////////SET INTENT DATA////////////////////////////////

        Glide.with(this)
                .load(data_mealPhoto).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(mealPhoto);
        Glide.with(this)
                .load(data_userphoto).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(userphoto);
        userFullName.setText("by "+data_fullname);
        mealName.setText(data_mealName);

        generateRatings();
        /////////////////////////ADDING RATE
        submittRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float mealRate = setRate.getRating();
                String rateReview = rateReviewbox.getText().toString();
                if(mealRate==0){
                    Toast.makeText(MHRatings.this, "Your rate has not been set!", Toast.LENGTH_SHORT).show();
                }
                else if(rateReview.isEmpty()){
                    Toast.makeText(MHRatings.this, "Please write your review!", Toast.LENGTH_SHORT).show();
                }
                else {
                    addRateReview();
                }
            }
        });
    }

    public void generateRatings(){
        getTotal1();
        getTotal2();
        getTotal3();
        getTotal4();
        getTotal5();
        getTotalRater();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ratingReviewsList.clear();
                getRateReview();
                getSumRates();

            }
        }, 4000);
    }

    private void addRateReview()  {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding your Review");
        progressDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final float mealRate = setRate.getRating();
        final String rateReview = this.rateReviewbox.getText().toString();
        final String userID = getID;
        final int mealID = data_mealID;
        final String rateDate = sdf.format(new Date());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADDRATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                ratingReviewsList.clear();
                                getRateReview();
                                rateReviewbox.setText("");
                                generateRatings();
                                progressDialog.dismiss();
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                            Toast.makeText(MHRatings.this, "You have already rated this meal!\n", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHRatings.this, "You have already rated this meal!\n", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("rateReview", rateReview);
                params.put("mealID", String.valueOf(mealID));
                params.put("mealRate", String.valueOf(mealRate));
                params.put("rateDate", rateDate);
                params.put("userID", userID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getRateReview() {
        set_meal = String.valueOf(data_mealID);
        ContentValues values = new ContentValues();
        values.put("1", set_meal);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETREVIEW+set_meal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashMeal = new JSONArray(response);
                            for(int i=0; i<dashMeal.length();i++){
                                JSONObject mealObject = dashMeal.getJSONObject(i);
                                int rateID = mealObject.getInt("rateID");
                                int userID = mealObject.getInt("userID");
                                int mealID = mealObject.getInt("mealID");
                                String mealRate = mealObject.getString("mealRate");
                                String rateReview = mealObject.getString("rateReview");
                                String rateDate = mealObject.getString("rateDate");
                                String fullname = mealObject.getString("fullname");
                                String userphoto = mealObject.getString("userphoto");
                                RatingReviews ratingReviews = new RatingReviews (rateID, userID, mealID, mealRate, rateReview, rateDate, fullname, userphoto);
                                ratingReviewsList.add(ratingReviews);
                            }
                            adapter = new AdapterRatings(MHRatings.this, ratingReviewsList);
                            adapter.notifyDataSetChanged();
                            rateRecycler.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHRatings.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }

                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getTotalRater() {
        set_meal = String.valueOf(data_mealID);
        ContentValues values = new ContentValues();
        values.put("1", set_meal);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETTOTALRATER+set_meal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashMeal = new JSONArray(response);
                                JSONObject mealObject = dashMeal.getJSONObject(0);
                                int mealRater = mealObject.getInt("mealRater");
                                starSum = mealRater;
                                totalRater.setText(String.valueOf(mealRater)+ " ratings");
                                totalRates.setText(String.valueOf(mealRater));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHRatings.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getTotal1() {
        set_meal = String.valueOf(data_mealID);
        ContentValues values = new ContentValues();
        values.put("1", set_meal);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETTOTALNUM1+set_meal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashMeal = new JSONArray(response);
                            JSONObject mealObject = dashMeal.getJSONObject(0);
                            int meal1Rate = mealObject.getInt("meal1Rate");
                            star1 = meal1Rate;
                            total1.setText(String.valueOf(meal1Rate));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHRatings.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getTotal2() {
        set_meal = String.valueOf(data_mealID);
        ContentValues values = new ContentValues();
        values.put("1", set_meal);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETTOTALNUM2+set_meal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashMeal = new JSONArray(response);
                            JSONObject mealObject = dashMeal.getJSONObject(0);
                            int meal2Rate = mealObject.getInt("meal2Rate");
                            star2 = meal2Rate;
                            total2.setText(String.valueOf(meal2Rate));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHRatings.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getTotal3() {
        set_meal = String.valueOf(data_mealID);
        ContentValues values = new ContentValues();
        values.put("1", set_meal);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETTOTALNUM3+set_meal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashMeal = new JSONArray(response);
                            JSONObject mealObject = dashMeal.getJSONObject(0);
                            int meal3Rate = mealObject.getInt("meal3Rate");
                            star3 = meal3Rate;
                            total3.setText(String.valueOf(meal3Rate));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHRatings.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getTotal4() {
        set_meal = String.valueOf(data_mealID);
        ContentValues values = new ContentValues();
        values.put("1", set_meal);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETTOTALNUM4+set_meal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashMeal = new JSONArray(response);
                            JSONObject mealObject = dashMeal.getJSONObject(0);
                            int meal4Rate = mealObject.getInt("meal4Rate");
                            star4 = meal4Rate;
                            total4.setText(String.valueOf(meal4Rate));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHRatings.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getTotal5() {
        set_meal = String.valueOf(data_mealID);
        ContentValues values = new ContentValues();
        values.put("1", set_meal);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETTOTALNUM5+set_meal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashMeal = new JSONArray(response);
                            JSONObject mealObject = dashMeal.getJSONObject(0);
                            int meal5Rate = mealObject.getInt("meal5Rate");
                            star5 = meal5Rate;
                            total5.setText(String.valueOf(meal5Rate));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHRatings.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getSumRates() {
        set_meal = String.valueOf(data_mealID);
        ContentValues values = new ContentValues();
        values.put("1", set_meal);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETSUMRATE+set_meal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashMeal = new JSONArray(response);
                            JSONObject mealObject = dashMeal.getJSONObject(0);
                            double data_sumRate = Double.valueOf(mealObject.getDouble("sumRate"));
                            sumRate = data_sumRate;

                            prog1 = (100/starSum)*star1;
                            prog2 = (100/starSum)*star2;
                            prog3 = (100/starSum)*star3;
                            prog4 = (100/starSum)*star4;
                            prog5 = (100/starSum)*star5;

                            progress01.setProgress(prog1);
                            progress02.setProgress(prog2);
                            progress03.setProgress(prog3);
                            progress04.setProgress(prog4);
                            progress05.setProgress(prog5);

                            NumberFormat df = new DecimalFormat("#0.0");
                            double averageRate = sumRate/starSum;
                            Double ave = Double.valueOf(averageRate);
                            totalRate.setRating(ave.floatValue());
                            totalMealRate.setText(String.valueOf(df.format(averageRate)));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHRatings.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

}


