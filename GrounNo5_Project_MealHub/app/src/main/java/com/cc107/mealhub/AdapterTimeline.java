package com.cc107.mealhub;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class AdapterTimeline extends RecyclerView.Adapter<AdapterTimeline.TimelineHolder> {
    private Context mCtx;
    private List<UsersTimeline> usersTimelineList;
    onClickInterface onClickInterface;
    SessionManager sessionManager;
    String getID;
    private static String URL_ADDFAV ="https://jdrael.000webhostapp.com/MealHub/addfav.php";
    private static String DLTMEAL="https://jdrael.000webhostapp.com/MealHub/deleteMeal.php";

    public AdapterTimeline(Context mCtx, List<UsersTimeline> usersTimelineList, onClickInterface onClickInterface) {
        this.mCtx = mCtx;
        this.usersTimelineList = usersTimelineList;
        this.onClickInterface = onClickInterface;
    }

    @NonNull
    @Override
    public TimelineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_timeline, parent, false);
        return new TimelineHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TimelineHolder holder, final int position) {
        UsersTimeline usersTimeline = usersTimelineList.get(position);
        holder.mealName.setText(usersTimeline.getMealName());
        holder.mealDate.setText(usersTimeline.getMealDate());
        holder.fullname.setText(usersTimeline.getFullname());
        String category = usersTimeline.getMealCategory();
        holder.mealCategory.setText("Meal Category: "+category);
        Glide.with(mCtx)
                .load(usersTimeline.getMealPhoto()).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.mealPhoto);

        Glide.with(mCtx)
                .load(usersTimeline.getUserphoto()).error(R.mipmap.ic_launcher)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.userphoto);


        holder.editmeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                Intent intent = new Intent(mCtx, MHMealUpdate.class);
                intent.putExtra("mealID", usersTimelineList.get(position).getMealID());
                intent.putExtra("mealName", usersTimelineList.get(position).getMealName());
                intent.putExtra("mealCategory", usersTimelineList.get(position).getMealCategory());
                intent.putExtra("mealPhoto", usersTimelineList.get(position).getMealPhoto());
                intent.putExtra("mealProcedure", usersTimelineList.get(position).getMealProcedure());
                mCtx.startActivity(intent);
            }
        });

        holder.deletemeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.setClick(position);
                new AlertDialog.Builder(mCtx).setMessage("Delete this Meal?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog progressDialog = new ProgressDialog(mCtx);
                            progressDialog.setMessage("Updating...");
                            progressDialog.show();
                            final String mealID = usersTimelineList.get(position).getMealID();
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, DLTMEAL,
                                    new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try{
                                                JSONObject jsonObject = new JSONObject(response);
                                                String success = jsonObject.getString("success");
                                                if(success.equals("1")){
                                                    Toast.makeText(mCtx, "Meal deleted.", Toast.LENGTH_SHORT).show();
                                                    progressDialog.dismiss();
                                                    Intent refreshT = new Intent(mCtx, MHTimeline.class);
                                                    mCtx.startActivity(refreshT);
                                                    ((Activity)mCtx).finish();
                                                }
                                            }
                                            catch(JSONException e){
                                                e.printStackTrace();
                                                Toast.makeText(mCtx, "Failed to delete!\n"+ e.toString(), Toast.LENGTH_SHORT).show();
                                                progressDialog.dismiss();
                                            }

                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Toast.makeText(mCtx, "Failed to delete!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();

                                        }
                                    })
                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> params = new HashMap<>();
                                    params.put("mealID", mealID);
                                    return params;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(mCtx);
                            requestQueue.add(stringRequest);

                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return usersTimelineList.size();
    }

    class TimelineHolder extends RecyclerView.ViewHolder {
        TextView mealName, mealDate, fullname, mealCategory;
        ImageView mealPhoto, userphoto, deletemeal, editmeal;
        LinearLayout linearLayout;

        public TimelineHolder(View itemView) {
            super(itemView);
            mealPhoto = itemView.findViewById(R.id.timeline_listImage);
            mealName = itemView.findViewById(R.id.timeline_textTitle);
            mealDate = itemView.findViewById(R.id.timeline_textdate);
            userphoto = itemView.findViewById(R.id.timeline_picture);
            fullname = itemView.findViewById(R.id.timeline_textuser);
            deletemeal = itemView.findViewById(R.id.timeline_delete);
            editmeal = itemView.findViewById(R.id.timeline_edit);
            mealCategory = itemView.findViewById(R.id.timeline_category);
        }
    }
}
