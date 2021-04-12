package com.cc107.mealhub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MHRegistration extends AppCompatActivity {
    private EditText data_fullname,
             data_username,
             data_email,
             data_password,
             data_cpassword;
    private Button button_signup;
    private TextView text_login;
    private static String URL_REGIST = "https://jdrael.000webhostapp.com/MealHub/registermealhub.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealhub_registration);

        data_fullname = findViewById(R.id.fullname);
        data_username = findViewById(R.id.s_username);
        data_email = findViewById(R.id.email);
        data_password = findViewById(R.id.s_password);
        data_cpassword = findViewById(R.id.c_password);
        text_login = findViewById(R.id.textView_login);
        button_signup = findViewById(R.id.button_signup);

        button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MHRegistration.this);
                alertDialogBuilder.setTitle("Join Meal Hub");
                alertDialogBuilder.setMessage("Continue Signing up an account?");
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        final String pass2 = data_cpassword.getText().toString().trim();
                        final String pass1 = data_password.getText().toString().trim();
                        final String name = data_fullname.getText().toString();
                        final String email = data_email.getText().toString();
                        final String username = data_username.getText().toString();
                        if(name.isEmpty()){
                            Toast.makeText(MHRegistration.this, "Please enter Full Name", Toast.LENGTH_SHORT).show();
                        }
                        else if(email.isEmpty()){
                            Toast.makeText(MHRegistration.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                        }
                        else if(username.isEmpty()){
                            Toast.makeText(MHRegistration.this, "Please provide username", Toast.LENGTH_SHORT).show();
                        }
                        else if(pass1.isEmpty()){
                            Toast.makeText(MHRegistration.this, "Please provide password", Toast.LENGTH_SHORT).show();
                        }
                        else if(pass2.isEmpty()){
                            Toast.makeText(MHRegistration.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                        }

                        else if(!pass1.equals(pass2)){
                            Toast.makeText(MHRegistration.this, "Password do not match! ", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Register();
                        }


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

        });
    }

    private void Register(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Signing up an Account!");
        progressDialog.show();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        final String fullname = this.data_fullname.getText().toString().trim();
        final String username = this.data_username.getText().toString().trim();
        final String email = this.data_email.getText().toString().trim();
        final String password = this.data_password.getText().toString().trim();
        final String joindate = sdf.format(new Date());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");
                            if(success.equals("1")){
                                Toast.makeText(MHRegistration.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                                data_fullname.setText("");
                                data_username.setText("");
                                data_email.setText("");
                                data_password.setText("");
                                data_cpassword.setText("");

                            }
                        }
                        catch(JSONException e){
                            e.printStackTrace();
                            Toast.makeText(MHRegistration.this, "Register Error! "+ e.toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MHRegistration.this, "Register Error! "+ error.toString(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("fullname", fullname);
                params.put("username", username);
                params.put("email", email);
                params.put("password", password);
                params.put("joinDate", joindate);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
