package com.project.esheba.Admin.Profile;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.project.esheba.Constant;
import com.project.esheba.R;

import java.util.HashMap;
import java.util.Map;

public class EditProfileAdminActivity extends AppCompatActivity {
    EditText extUsernameAdmn, extMobileAdmn, extEmailAdmn,  extPasswordAdmn;
    ImageView imgProfile;

    ProgressDialog loading;
    TextView txtNameAdmn;
    Button btnUpdateProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_admin);

        extUsernameAdmn = findViewById(R.id.txt_editusernameAdmn);
        extMobileAdmn = findViewById(R.id.txt_editmobileAdmn);
        extEmailAdmn = findViewById(R.id.txt_editemailAdmn);

        extPasswordAdmn = findViewById(R.id.txt_editpasswordAdmn);
        txtNameAdmn = findViewById(R.id.txteditNameAdmn);
        btnUpdateProfile = findViewById(R.id.btn_updateAdmn);
        imgProfile = findViewById(R.id.profile_image);


        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("Update Profile");


        String getName = getIntent().getExtras().getString("name");
        String getMobile = getIntent().getExtras().getString("mobile");
        String getEmail = getIntent().getExtras().getString("email");



        extUsernameAdmn.setText(getName);
        txtNameAdmn.setText(getName);
        extMobileAdmn.setText(getMobile);
        extMobileAdmn.setEnabled(false);
        extEmailAdmn.setText(getEmail);


        //extPasswordAdmn.setText("*******");






        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditProfileAdminActivity.this);
                builder.setMessage("Want to Update Profile?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                // Perform Your Task Here--When Yes Is Pressed.
                                UpdateProfile();
                                dialog.cancel();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Perform Your Task Here--When No is pressed
                                dialog.cancel();
                            }
                        }).show();
            }
        });
    }


    //update contact method
    public void UpdateProfile() {

        final String name = extUsernameAdmn.getText().toString();
        final String mobile = extMobileAdmn.getText().toString();
        final String email = extEmailAdmn.getText().toString();

        final String password = extPasswordAdmn.getText().toString();


        if (name.isEmpty()) {
            extUsernameAdmn.setError("Name Can't Empty");
            extUsernameAdmn.requestFocus();
        } else if (mobile.length() != 11) {
            extMobileAdmn.setError("Invalid Mobile Number");
            extMobileAdmn.requestFocus();

        } else if (email.isEmpty()) {
            extEmailAdmn.setError("Email can't be empty");
            extEmailAdmn.requestFocus();
        }  else if (password.length() < 4) {
            extPasswordAdmn.setError("Password too short! or Invalid Password");
            extPasswordAdmn.requestFocus();
        } else {
            loading = new ProgressDialog(this);
            // loading.setIcon(R.drawable.wait_icon);
            // loading.setTitle("Update");
            loading.setMessage("Update...Please wait...");
            loading.show();

            String URL = Constant.PROFILEADMN_UPDATE_URL;


            //Creating a string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            //for track response in logcat
                            Log.d("RESPONSE", response);
                            // Log.d("RESPONSE", userCell);


                            //If we are getting success from server
                            if (response.equals("success")) {

                                loading.dismiss();
                                //Starting profile activity

                                Intent intent = new Intent(EditProfileAdminActivity.this, ProfileAdminActivity.class);
                                Toasty.success(EditProfileAdminActivity.this, " Profile Successfully Updated!", Toast.LENGTH_SHORT).show();
                                startActivity(intent);

                            }


                            //If we are getting success from server
                            else if (response.equals("failure")) {

                                loading.dismiss();
                                //Starting profile activity

                                Intent intent = new Intent(EditProfileAdminActivity.this, ProfileAdminActivity.class);
                                Toasty.error(EditProfileAdminActivity.this, " Profile Update fail!", Toast.LENGTH_SHORT).show();
                                //startActivity(intent);

                            } else {

                                loading.dismiss();
                                Toasty.error(EditProfileAdminActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                            }

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want

                            Toasty.error(EditProfileAdminActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request

                    // params.put(Constant.KEY_ID, getID);
                    params.put(Constant.KEY_NAME, name);
                    params.put(Constant.KEY_MOBILE, mobile);
                    params.put(Constant.KEY_EMAIL, email);

                    params.put(Constant.KEY_PASSWORD, password);


                    //Log.d("ID", getID);

                    //returning parameter
                    return params;
                }
            };


            //Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(EditProfileAdminActivity.this);
            requestQueue.add(stringRequest);
        }

    }


    //for back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

