package com.cc107.mealhub;
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


public class AdapterDashboard extends RecyclerView.Adapter<AdapterDashboard.DashBoardMealHolder> {
    private Context mCtx;
    private List<DashboardMeal> dashboardMealList;
    SessionManager sessionManager;
    String getID;
    onClickInterface onClickInterface;
    private static String URL_ADDLIKE ="https://jdrael.000webhostapp.com/MealHub/addLike.php";
    private static String URL_ADDFAV ="https://jdrael.000webhostapp.com/MealHub/addfav.php";


    public AdapterDashboard(Context mCtx, List<DashboardMeal> dashboardMealList, onClickInterface onClickInterface) {
        this.mCtx = mCtx;
        this.dashboardMealList = dashboardMealList;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public DashBoardMealHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_dashboard, parent, false);
        return new DashBoardMealHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DashBoardMealHolder holder, final int position) {
        DashboardMeal dashboardMeal = dashboardMealList.get(position);
        holder.mealName.setText(dashboardMeal.getMealName());
        holder.mealDate.setText(dashboardMeal.getMealDate());
        holder.mealDescription.setText(dashboardMeal.getMealProcedure());
        holder.fullname.setText(dashboardMeal.getFullname());

       /* int likeValue = dashboardMeal.getLikeValue();
        if(likeValue==1)
            holder.addlike.setImageResource(R.drawable.ic_like_on);
        else{
            holder.addlike.setImageResource(R.drawable.ic_like_off);
        }

        int favValue = dashboardMeal.getFavValue();
        if(favValue==1)
            holder.addfav.setImageResource(R.drawable.ic_fav_on);
        else{
            holder.addfav.setImageResource(R.drawable.ic_fav_off);
        }

        */

        Glide.with(mCtx)
                .load(dashboardMeal.getMealPhoto()).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.mealPhoto);

        Glide.with(mCtx)
                .load(dashboardMeal.getuserPhoto()).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.userphoto);

        holder.commentview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                Intent intent = new Intent(mCtx, MHMealView.class);
                intent.putExtra("mealID", dashboardMealList.get(position).getMealID());
                intent.putExtra("mealName", dashboardMealList.get(position).getMealName());
                intent.putExtra("mealCategory", dashboardMealList.get(position).getMealCategory());
                intent.putExtra("mealPhoto", dashboardMealList.get(position).getMealPhoto());
                intent.putExtra("mealProcedure", dashboardMealList.get(position).getMealProcedure());
                intent.putExtra("mealDate", dashboardMealList.get(position).getMealDate());
                intent.putExtra("userID", dashboardMealList.get(position).getUserID());
                intent.putExtra("userphoto", dashboardMealList.get(position).getuserPhoto());
                intent.putExtra("fullname", dashboardMealList.get(position).getFullname());
                mCtx.startActivity(intent);
            }
        });


        holder.dashrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                Intent intent = new Intent(mCtx, MHRatings.class);
                intent.putExtra("mealID", dashboardMealList.get(position).getMealID());
                intent.putExtra("mealName", dashboardMealList.get(position).getMealName());
                intent.putExtra("mealCategory", dashboardMealList.get(position).getMealCategory());
                intent.putExtra("mealPhoto", dashboardMealList.get(position).getMealPhoto());
                intent.putExtra("mealProcedure", dashboardMealList.get(position).getMealProcedure());
                intent.putExtra("mealDate", dashboardMealList.get(position).getMealDate());
                intent.putExtra("userID", dashboardMealList.get(position).getUserID());
                intent.putExtra("userphoto", dashboardMealList.get(position).getuserPhoto());
                intent.putExtra("fullname", dashboardMealList.get(position).getFullname());
                mCtx.startActivity(intent);
            }
        });

        holder.addlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                sessionManager = new SessionManager( mCtx);
                sessionManager.checkLogin();
                HashMap<String, String> user = sessionManager.getUserDetail();
                getID = user.get(sessionManager.USERID);

                final int userID = Integer.parseInt(getID);
                final int mealID = dashboardMealList.get(position).getMealID();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADDLIKE,
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
                                    if(message.equals("You liked this meal."))
                                        holder.addlike.setImageResource(R.drawable.ic_like_on);
                                    else{
                                        holder.addlike.setImageResource(R.drawable.ic_like_off);
                                    }
                                }
                                catch(JSONException e){
                                    e.printStackTrace();
                                    Toast.makeText(mCtx, "Failed to like this meal!\n"+ e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(mCtx, "Failed to like this meal!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
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

        holder.addfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                sessionManager = new SessionManager( mCtx);
                sessionManager.checkLogin();
                HashMap<String, String> user = sessionManager.getUserDetail();
                getID = user.get(sessionManager.USERID);

                final int userID = Integer.parseInt(getID);
                final int mealID = dashboardMealList.get(position).getMealID();
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
        return dashboardMealList.size();
    }

    class DashBoardMealHolder extends RecyclerView.ViewHolder {
        TextView mealName, mealDate, mealDescription, fullname, dashrate;
        ImageView mealPhoto, userphoto, commentview, addlike, addfav;
        LinearLayout linearLayout;

        public DashBoardMealHolder(View itemView) {
            super(itemView);
            mealPhoto = itemView.findViewById(R.id.listImage);
            mealName = itemView.findViewById(R.id.textTitle);
            mealDate = itemView.findViewById(R.id.textdate);
            mealDescription = itemView.findViewById(R.id.textDescription);
            userphoto = itemView.findViewById(R.id.dash_picture);
            fullname = itemView.findViewById(R.id.textuser);
            dashrate = itemView.findViewById(R.id.textrate);
            commentview = itemView.findViewById(R.id.m_comment);
            linearLayout = itemView.findViewById(R.id.linear_dash);
            addlike = itemView.findViewById(R.id.m_like);
            addfav = itemView.findViewById(R.id.m_favorite);
        }
    }

}
