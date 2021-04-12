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

public class MHFavorite extends AppCompatActivity {
    private static final String GET_FAV = "https://jdrael.000webhostapp.com/MealHub/getFavorites.php?setUser=";
    private static final String COUNTFAV = "https://jdrael.000webhostapp.com/MealHub/countFav.php?setUser=";
    private RecyclerView favRecycler;
    AdapterFavorite adapter;

    private onClickInterface onclickInterface;
    List<UsersFavorite> usersFavoriteList;
    String getID, setUser;
    SessionManager sessionManager;
    TextView loggedFullname, counFav;

    ImageView loggedProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealhub_favorite);
        sessionManager = new SessionManager( this);

        onclickInterface = new onClickInterface() {
            @Override
            public void setClick(int abc) {
            }
        };

        loggedFullname =findViewById(R.id.fav_fullname);
        counFav = findViewById(R.id.fav_count);
        loggedProfile = findViewById(R.id.fav_picture);
        usersFavoriteList = new ArrayList<>();
        favRecycler = findViewById(R.id.favorite_recycler);
        favRecycler.setHasFixedSize(true);
        favRecycler.setLayoutManager(new LinearLayoutManager(this));
        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        String mName = user.get(sessionManager.NAME);
        String mEmail = user.get(sessionManager.EMAIL);
        String mPhoto = user.get(sessionManager.PHOTO);
        getID = user.get(sessionManager.USERID);

        loggedFullname.setText(mName);
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
        getFavCount();
        getFavorites();
    }

    private void getFavCount() {
        setUser = getID;
        ContentValues values = new ContentValues();
        values.put("1", setUser);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, COUNTFAV+setUser,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashMeal = new JSONArray(response);
                            JSONObject mealObject = dashMeal.getJSONObject(0);
                            int totalFav = mealObject.getInt("totalFav");
                            String fav = String.valueOf(totalFav);
                            counFav.setText("You have added "+ fav +" meals on your collection.");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHFavorite.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getFavorites() {
        setUser = getID;
        ContentValues values = new ContentValues();
        values.put("1", setUser);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GET_FAV+setUser,
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
                                String favValue = mealObject.getString("favValue");
                                String mealID = mealObject.getString("mealID");

                                UsersFavorite usersFavorite = new UsersFavorite(userphoto, fullname, mealDate, mealName, mealPhoto, favValue, mealID);
                                usersFavoriteList.add(usersFavorite);
                            }
                            adapter = new AdapterFavorite(MHFavorite.this, usersFavoriteList, onclickInterface);
                            favRecycler.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHFavorite.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onBackPressed(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Go back to Dashboard?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent b = new Intent(MHFavorite.this, MHDashboard.class );
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
