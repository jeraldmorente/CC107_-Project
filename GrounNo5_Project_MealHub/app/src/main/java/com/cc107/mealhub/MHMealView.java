package com.cc107.mealhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MHMealView extends AppCompatActivity {
    TextView mealName, mealDate, mealDescription, fullname, mealCategory, add_comment;
    ImageView mealPhoto, userphoto;
    SessionManager sessionManager;
    String getID;
    String data_mealName, data_mealDate, data_mealDescription, data_fullname, data_mealCategory,
            data_mealPhoto, data_userphoto;

    int data_mealID, data_userID;
    String set_meal;
    EditText combox;

    List<MealComments> mealCommentsList;
    private RecyclerView commentRecycler;
    AdapterComment adapter;
    private static final String GETCOMMENT= "https://jdrael.000webhostapp.com/MealHub/getComment.php?setMeal=";
    private static String URL_ADDCOMMENT ="https://jdrael.000webhostapp.com/MealHub/addComment.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealhub_mealview);
        sessionManager = new SessionManager( this);


        fullname = findViewById(R.id.mv_textuser);
        mealName = findViewById(R.id.mv_textTitle);
        mealDate = findViewById(R.id.mv_textdate);
        mealDescription = findViewById(R.id.mv_textDescription);
        mealCategory = findViewById(R.id.mv_categ);
        mealPhoto = findViewById(R.id.mv_mealImage);
        userphoto = findViewById(R.id.mv_picture);

        combox = findViewById(R.id.mv_commentbox);
        add_comment = findViewById(R.id.mv_comment);

        mealCommentsList = new ArrayList<>();
        commentRecycler = findViewById(R.id.comment_recycler);
        commentRecycler.setHasFixedSize(true);
        commentRecycler.setLayoutManager(new LinearLayoutManager(this));

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

        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(sessionManager.USERID);

        fullname.setText(data_fullname);
        mealName.setText(data_mealName);
        mealCategory.setText(data_mealCategory);
        mealDescription.setText(data_mealDescription);
        mealDate.setText(data_mealDate);

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

        getComment();
        add_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = combox.getText().toString();
                if(comment.isEmpty()){
                    Toast.makeText(MHMealView.this, "Empty Comment!", Toast.LENGTH_SHORT).show();
                }
                else{
                    AddComment();

                }
            }
        });
    }

    private void AddComment() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Adding Comment...");
        progressDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        final String comments = this.combox.getText().toString();
        final String userID = getID;
        final int mealID = data_mealID;
        final String commentDate = sdf.format(new Date());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADDCOMMENT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                mealCommentsList.clear();
                                getComment();
                                combox.setText("");
                                progressDialog.dismiss();
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                            Toast.makeText(MHMealView.this, "Failed to comment!\n"+ e.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHMealView.this, "Failed to comment!!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("comments", comments);
                params.put("mealID", String.valueOf(mealID));
                params.put("commentDate", commentDate);
                params.put("userID", userID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getComment() {
        set_meal = String.valueOf(data_mealID);
        ContentValues values = new ContentValues();
        values.put("1", set_meal);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, GETCOMMENT+set_meal,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dashMeal = new JSONArray(response);
                            for(int i=0; i<dashMeal.length();i++){
                                JSONObject mealObject = dashMeal.getJSONObject(i);
                                String commentID = mealObject.getString("commentID");
                                String comments = mealObject.getString("comments");
                                String commentDate = mealObject.getString("commentDate");
                                String mealID = mealObject.getString("mealID");
                                String userphoto = mealObject.getString("userphoto");
                                String fullname = mealObject.getString("fullname");
                                MealComments mealComments = new MealComments(commentID, comments, commentDate, mealID, userphoto, fullname);
                                mealCommentsList.add(mealComments);
                            }
                            adapter = new AdapterComment(MHMealView.this, mealCommentsList);
                            adapter.notifyDataSetChanged();
                            commentRecycler.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHMealView.this, "Error!\n"+ error.toString(), Toast.LENGTH_SHORT).show();
                    }

                });

        Volley.newRequestQueue(this).add(stringRequest);
    }
}
