package com.cc107.mealhub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MHLogin extends AppCompatActivity {
    private EditText data_username,
             data_password;
    private Button button_login;
            TextView text_signup;
    //private ProgressBar progressBar;
    private static String URL_LOGIN ="https://jdrael.000webhostapp.com/MealHub/loginmealhub.php";
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealhub_login);
        sessionManager = new SessionManager( this);

        text_signup = findViewById(R.id.textView_signup);
        data_username = findViewById(R.id.username);
        data_password = findViewById(R.id.password);
        button_login = findViewById(R.id.button_login);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = data_username.getText().toString().trim();
                String pass = data_password.getText().toString().trim();

                if(!uname.isEmpty() || !pass.isEmpty()){
                    LogIn(uname, pass);
                }
                else{
                    Toast.makeText(MHLogin.this, "Username or paassword cannot be empty.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        text_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup = new Intent(MHLogin.this, MHRegistration.class);
                startActivity(signup);
            }
        });
    }

    private void LogIn(final String username, final String password){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Log In");
        progressDialog.setMessage("Logging in as "+data_username.getText());
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            JSONArray jsonArray = jsonObject.getJSONArray("login");

                            if(success.equals("1")){

                                for(int i= 0; i < jsonArray.length(); i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    String fullname = object.getString("fullname").trim();
                                    String username = object.getString("username");
                                    String email = object.getString("email");
                                    String gender = object.getString("gender");
                                    String joinDate = object.getString("joinDate");
                                    String userID = object.getString("userID");
                                    String userphoto = object.getString("userphoto");
                                    progressDialog.dismiss();

                                    sessionManager.createSession(fullname, email, username, gender, joinDate, userID, userphoto);

                                    Intent intent = new Intent(MHLogin.this, MHDashboard.class);
                                    intent.putExtra("fullname", fullname);
                                    intent.putExtra("email", email);
                                    startActivity(intent);

                                }

                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                            Toast.makeText(MHLogin.this, "Error! "+ e.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHLogin.this, "Error! "+ error.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                })
        {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setMessage("Close this Application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
