package com.cc107.mealhub;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MHDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TextView text_fullname,
                     text_email,
                     text_welcome;
    private CircleImageView user_photo;
    SessionManager sessionManager;
    private static final String GETMEAL = "https://jdrael.000webhostapp.com/MealHub/getMealDash.php";
    private static final String TEST2 = "https://jdrael.000webhostapp.com/MealHub/test.php";
    private String mealhub_category;
    private RecyclerView dashRecycler;
    AdapterDashboard adapter;
    private onClickInterface onclickInterface;
    List<DashboardMeal> dashboardMealList;
    String getID;
    String setID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealhub_dashboard);

        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >=0 && timeOfDay <9){
            mealhub_category = "Breakfast";
        }
        else if (timeOfDay >=9 && timeOfDay <14){
            mealhub_category = "Lunch";
        }
        else if (timeOfDay >=14 && timeOfDay <21){
            mealhub_category = "Dinner";
        }
        else if (timeOfDay >=21 && timeOfDay <24){
            mealhub_category = "Dinner";
        }
        text_welcome = findViewById(R.id.meal_categ);
        text_welcome.setText("Here are our recomended "+mealhub_category+ " for you!");
        sessionManager = new SessionManager( this);
        drawerLayout = findViewById(R.id.nav_drawer);
        onclickInterface = new onClickInterface() {
            @Override
            public void setClick(int abc) {

            }
        };
        dashboardMealList = new ArrayList<>();
        dashRecycler = findViewById(R.id.dash_recycler);
        dashRecycler.setHasFixedSize(true);
        dashRecycler.setLayoutManager(new LinearLayoutManager(this));

        getDashboardMeal();
        ////////////////////////

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView mNavigationView = findViewById(R.id.nav_view);
        text_fullname = mNavigationView.getHeaderView(0).findViewById(R.id.user_fullname);
        text_email = mNavigationView.getHeaderView(0).findViewById(R.id.user_email);
        user_photo = mNavigationView.getHeaderView(0).findViewById(R.id.profile_image);

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
        text_fullname.setText(mName);
        text_email.setText(mEmail);
        if (mNavigationView != null){
            mNavigationView.setNavigationItemSelectedListener(this);
        }
    }

    private void getDashboardMeal() {
        String GETMEAL_URL = null;
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);
        if (timeOfDay >=0 && timeOfDay <9){
            GETMEAL_URL = "https://jdrael.000webhostapp.com/MealHub/getMealDashBreakfast.php";
        }
        else if (timeOfDay >=9 && timeOfDay <14){
            GETMEAL_URL = "https://jdrael.000webhostapp.com/MealHub/getMealDashLunch.php";
        }
        else if (timeOfDay >=14 && timeOfDay <21){
            GETMEAL_URL = "https://jdrael.000webhostapp.com/MealHub/getMealDashDinner.php";
        }
        else if (timeOfDay >=21 && timeOfDay <24){
            GETMEAL_URL = "https://jdrael.000webhostapp.com/MealHub/getMealDashDinner.php";
        }


        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETMEAL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashMeal = new JSONArray(response);
                            for(int i=0; i<dashMeal.length();i++){
                                JSONObject mealObject = dashMeal.getJSONObject(i);
                                int mealID = mealObject.getInt("mealID");
                                String mealName = mealObject.getString("mealName");
                                String mealCategory = mealObject.getString("mealCategory");
                                String mealProcedure = mealObject.getString("mealProcedure");
                                int userID = mealObject.getInt("userID");
                                String mealDate = mealObject.getString("mealDate");
                                String mealPhoto = mealObject.getString("mealPhoto");
                                String userphoto = mealObject.getString("userphoto");
                                String fullname = mealObject.getString("fullname");
                                //int likeValue = mealObject.getInt("likeValue");
                                //int favValue = mealObject.getInt("favValue");
                                DashboardMeal dashboardMeal = new DashboardMeal(mealID, mealName, mealCategory, mealPhoto, mealProcedure, mealDate, userID, userphoto, fullname);//, likeValue, favValue);
                                dashboardMealList.add(dashboardMeal);
                            }
                            adapter = new AdapterDashboard(MHDashboard.this, dashboardMealList, onclickInterface);
                            dashRecycler.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHDashboard.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
       /* {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("r_userID", r_userID);
                params.put("r_category", r_category);
                return params;
            }
        }

        */
        ;
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id = item.getItemId();
        if(id == R.id.nav_logout){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("Are you sure you want to Log out?");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    sessionManager.logout();
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
        else if(id == R.id.nav_profile){
            Intent mh_profile = new Intent(MHDashboard.this, MHProfile.class);
            startActivity(mh_profile);
        }
        else if(id == R.id.nav_sharemeal){
            Intent mh_share = new Intent(MHDashboard.this, MHShare.class);
            startActivity(mh_share);
        }

        else if(id == R.id.nav_timeline){
            Intent mh_timeline = new Intent(MHDashboard.this, MHTimeline.class);
            startActivity(mh_timeline);
        }

        else if(id == R.id.nav_favorites){
            Intent mh_fav = new Intent(MHDashboard.this, MHFavorite.class);
            startActivity(mh_fav);
        }

        else if(id == R.id.nav_about){
            Intent mh_about = new Intent(MHDashboard.this, MHAbout.class);
            startActivity(mh_about);
        }

        else if(id == R.id.nav_dev){
            Intent mh_about = new Intent(MHDashboard.this, MHDevelopers.class);
            startActivity(mh_about);
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return  true;
    }

    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to Log out?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                sessionManager.logout();

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