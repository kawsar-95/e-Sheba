package com.project.esheba.Admin.Profile;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.esheba.Constant;
import com.project.esheba.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProfileAdminActivity extends AppCompatActivity {

    TextView txtNameAdmn, txtUserNameAdmn, txtMobileAdmn, txtEmailAdmn, txtPasswordCus;
    Button btnEditProfile;

    String UserCell;

    private ProgressDialog loading;

    SharedPreferences sharedPreferences;

    ImageView profilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_admin);

        //Fetching mobile from shared preferences
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String mobile = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");
        UserCell = mobile;
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("Profile");


        txtNameAdmn = findViewById(R.id.txtNameAdmn);
        txtUserNameAdmn = findViewById(R.id.txtUsernameAdmn);
        txtMobileAdmn = findViewById(R.id.txtMobileAdmn);
        txtEmailAdmn = findViewById(R.id.txtEmailAdmn);


        profilePic = findViewById(R.id.profile_image);
        btnEditProfile = findViewById(R.id.btnEditProfileAdmn);

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ProfileAdminActivity.this, EditProfileAdminActivity.class);
                intent.putExtra("name", txtNameAdmn.getText());
                intent.putExtra("mobile", txtMobileAdmn.getText());
                intent.putExtra("email", txtEmailAdmn.getText());

                startActivity(intent);
            }
        });

        //call function
        getData();

    }


    private void getData() {

        //loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        //showing progress dialog
        loading = new ProgressDialog(ProfileAdminActivity.this);
        loading.setMessage("Please wait....");
        loading.show();


        String url = Constant.PROFILECADMN_URL + UserCell;  // url for connecting php file

        Log.d("URL", url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("Volley Error", "Error:" + error);
                        Toasty.error(ProfileAdminActivity.this, "No Internet Connection!", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(ProfileAdminActivity.this);
        requestQueue.add(stringRequest);
    }


    private void showJSON(String response) {


        Log.d("RESPONSE", response);

        String name = "";

        String mobile = "";
        String email = "";


        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Constant.JSON_ARRAY);
            JSONObject ProfileData = result.getJSONObject(0);

            name = ProfileData.getString(Constant.KEY_NAME);

            mobile = ProfileData.getString(Constant.KEY_MOBILE);
            email = ProfileData.getString(Constant.KEY_EMAIL);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //textViewResult.setText("Name:\t"+name+"\nAddress:\t" +address+ "\nVice Chancellor:\t"+ vc);

        txtNameAdmn.setText(name);
        txtUserNameAdmn.setText(name);


//        if (gender.equals("Female"))
//        {
//            profilePic.setImageResource(R.drawable.girl);
//        }


        txtMobileAdmn.setText(mobile);
        txtEmailAdmn.setText(email);


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
