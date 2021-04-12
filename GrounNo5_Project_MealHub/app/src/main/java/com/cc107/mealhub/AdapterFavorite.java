package com.cc107.mealhub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdapterFavorite extends RecyclerView.Adapter<AdapterFavorite.FavoriteHolder> {
    private Context mCtx;
    private List<UsersFavorite> usersFavoriteList;
    onClickInterface onClickInterface;
    SessionManager sessionManager;
    String getID;
    private static String URL_ADDFAV ="https://jdrael.000webhostapp.com/MealHub/addfav.php";

    public AdapterFavorite(Context mCtx, List<UsersFavorite> usersFavoriteList, onClickInterface onClickInterface) {
        this.mCtx = mCtx;
        this.usersFavoriteList = usersFavoriteList;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public AdapterFavorite.FavoriteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_favorite, parent, false);
        return new AdapterFavorite.FavoriteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterFavorite.FavoriteHolder holder, final int position) {
        UsersFavorite usersFavorite = usersFavoriteList.get(position);
        holder.mealName.setText(usersFavorite.getMealName());
        holder.mealDate.setText(usersFavorite.getMealDate());
        holder.fullname.setText(usersFavorite.getFullname());
        Glide.with(mCtx)
                .load(usersFavorite.getMealPhoto()).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.mealPhoto);

        Glide.with(mCtx)
                .load(usersFavorite.getUserphoto()).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.userphoto);

        String favValue = usersFavorite.getFavValue();

        holder.addfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                sessionManager = new SessionManager( mCtx);
                sessionManager.checkLogin();
                HashMap<String, String> user = sessionManager.getUserDetail();
                getID = user.get(sessionManager.USERID);

                final int userID = Integer.parseInt(getID);
                final String mealID = usersFavoriteList.get(position).getMealID();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADDFAV,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try{
                                    JSONObject jsonObject = new JSONObject(response);
                                    String success = jsonObject.getString("success");
                                    String message = jsonObject.getString("message");
                                    if(success.equals("1")){
                                        Toast.makeText(mCtx, message, Toast.LENGTH_SHORT).show();
                                    }
                                    if(message.equals("Meal added to favorite."))
                                        holder.addfav.setImageResource(R.drawable.ic_fav_on);

                                    else{
                                        holder.addfav.setImageResource(R.drawable.ic_fav_off);
                                    }

                                    Intent refreshT = new Intent(mCtx, MHFavorite.class);
                                    mCtx.startActivity(refreshT);
                                    ((Activity)mCtx).finish();
                                }
                                catch(JSONException e){
                                    e.printStackTrace();
                                    Toast.makeText(mCtx, "Failed to add on favorite this meal!\n"+ e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mCtx, "Failed to add on favorite this meal!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("mealID", String.valueOf(mealID));
                        params.put("userID", String.valueOf(userID));
                        return params;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                requestQueue.add(stringRequest);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersFavoriteList.size();
    }

    class FavoriteHolder extends RecyclerView.ViewHolder {
        TextView mealName, mealDate, fullname;
        ImageView mealPhoto, userphoto, addfav;
        LinearLayout linearLayout;

        public FavoriteHolder(View itemView) {
            super(itemView);
            mealPhoto = itemView.findViewById(R.id.fav_listImage);
            mealName = itemView.findViewById(R.id.fav_textTitle);
            mealDate = itemView.findViewById(R.id.fav_textdate);
            userphoto = itemView.findViewById(R.id.fav_picture);
            fullname = itemView.findViewById(R.id.fav_textuser);
            linearLayout = itemView.findViewById(R.id.linear_fav);
            addfav = itemView.findViewById(R.id.fav_heart);
        }
    }
}
