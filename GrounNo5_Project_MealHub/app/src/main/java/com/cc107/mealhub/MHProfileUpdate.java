package com.cc107.mealhub;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MHProfileUpdate extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = MHProfileUpdate.class.getSimpleName();
    private EditText fullname,
                     email,
                     username;
    Spinner spin_gender;
    SessionManager sessionManager;
    String [] data_gender = {"Male", "Female", "Others"};
    String getID, getJoinDate, getPhoto;
    private static String URL_READ = "https://jdrael.000webhostapp.com/MealHub/read_detail.php";
    private static String URL_EDIT = "https://jdrael.000webhostapp.com/MealHub/edit_detail.php";
    private static String URL_UPLOAD = "https://jdrael.000webhostapp.com/MealHub/upload.php";
    private Button button_update,
                   button_upload;
    private Bitmap bitmap;
    CircleImageView user_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealhub_profile_update);

        sessionManager = new SessionManager( this);

        spin_gender = findViewById(R.id.gender);
        fullname = findViewById(R.id.u_fullname);
        email = findViewById(R.id.u_email);
        username = findViewById(R.id.u_username);
        user_photo = findViewById(R.id.u_picture);
        button_update = findViewById(R.id.button_update);
        button_upload = findViewById(R.id.btnUpload);

        spin_gender.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, data_gender);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_gender.setAdapter(aa);

        sessionManager.checkLogin();
        HashMap<String, String> user = sessionManager.getUserDetail();
        getID = user.get(sessionManager.USERID);
        getJoinDate = user.get(sessionManager.JOINDATE);
        getPhoto = user.get(sessionManager.PHOTO);

        button_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        new AlertDialog.Builder(MHProfileUpdate.this)
                .setMessage("Save profile update?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveEditDetail();
                    }
                })
                .setNegativeButton("No", null)
                .show();

            }
        });

        button_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(MHProfileUpdate.this)
                        .setMessage("Select Photo?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                chooseFile();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });

    }

    public void onItemSelected(AdapterView<?>arg0, View arg1, int posistion, long id){


    }

    public void onNothingSelected(AdapterView<?>arg0){

    }

    private void getUserDetail(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_READ, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                Log.i(TAG, response.toString());
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("read");
                    if(success.equals("1")){

                        for(int i= 0; i < jsonArray.length(); i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            String d_fullname = object.getString("fullname").trim();
                            String d_username = object.getString("username");
                            String d_email = object.getString("email");
                            String d_gender = object.getString("gender");
                            String d_joinDate = object.getString("joinDate");
                            String d_userID = object.getString("userID");
                            String d_userphoto = object.getString("userphoto");

                            if (d_userphoto != null){
                                Glide.with(MHProfileUpdate.this)
                                        .load(d_userphoto).error(R.mipmap.ic_launcher)
                                        .apply(RequestOptions.circleCropTransform())
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(user_photo);
                            }
                            else if(d_userphoto == null){
                                String default_photo = "https://jdrael.000webhostapp.com/MealHub/photo/mh_default.png";
                                Glide.with(MHProfileUpdate.this)
                                        .load(default_photo).error(R.mipmap.ic_launcher)
                                        .apply(RequestOptions.circleCropTransform())
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .skipMemoryCache(true)
                                        .into(user_photo);
                            }

                            fullname.setText(d_fullname);
                            username.setText(d_username);
                            email.setText(d_email);


                            if (d_gender.equals("Male")){
                                spin_gender.setSelection(0);
                            }
                            else if (d_gender.equals("Female")){
                                spin_gender.setSelection(1);
                            }
                            else {
                                spin_gender.setSelection(2);
                            }

                        }
                    }
                }
                catch(JSONException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(MHProfileUpdate.this, "Error Reading Detail "+ e.toString(), Toast.LENGTH_SHORT).show();
                }
                }
            },
            new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error){
                    progressDialog.dismiss();
                    Toast.makeText(MHProfileUpdate.this, "Error Reading Detail "+ error.toString(), Toast.LENGTH_SHORT).show();
                }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userID", getID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void saveEditDetail() {
        final String e_fullname = this.fullname.getText().toString().trim();
        final String e_username = this.username.getText().toString().trim();
        final String e_email = this.email.getText().toString().trim();
        final String e_gender = this.spin_gender.getSelectedItem().toString().trim();
        final String e_userID = getID;
        final String e_joindate = getJoinDate;
        final String e_photo = getPhoto;

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Updating...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_EDIT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                sessionManager.createSession(e_fullname, e_email, e_username, e_gender, e_joindate, e_userID, e_photo);
                                Intent mh_profile = new Intent(MHProfileUpdate.this, MHProfile.class);
                                startActivity(mh_profile);
                        }
                }
                catch(JSONException e){
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(MHProfileUpdate.this, "Error Reading Detail "+ e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        progressDialog.dismiss();
                        Toast.makeText(MHProfileUpdate.this, "Error Reading Detail "+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fullname", e_fullname);
                params.put("username", e_username);
                params.put("gender", e_gender);
                params.put("email", e_email);
                params.put("userID", e_userID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDetail();
    }

    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile photo"), 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                user_photo.setImageBitmap(bitmap);
            }
            catch(IOException e){
                e.printStackTrace();
            }
            UploadPicture(getID, getStringImage(bitmap));
        }
    }

    private void UploadPicture(final String userID, final String photo)
    {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response.toString());
                        progressDialog.dismiss();
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                Intent mh_profile = new Intent(MHProfileUpdate.this, MHProfileUpdate.class);
                                startActivity(mh_profile);
                                finish();
                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(MHProfileUpdate.this, "Error Reading Detail "+ e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        progressDialog.dismiss();
                        Toast.makeText(MHProfileUpdate.this, "Error Reading Detail "+ error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userphoto",photo);
                params.put("userID", userID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte []imageByteArray = byteArrayOutputStream.toByteArray();
        String encodeImage = Base64.encodeToString(imageByteArray, Base64.DEFAULT);
        return encodeImage;

    }

    public void onBackPressed(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Go back to profile?");
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent b= new Intent(MHProfileUpdate.this, MHProfile.class );
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
