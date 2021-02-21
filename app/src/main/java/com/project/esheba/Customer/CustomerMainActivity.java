package com.project.esheba.Customer;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.project.esheba.Constant;
import com.project.esheba.ContactAdminActivity;
import com.project.esheba.Customer.Profile.ProfileCusActivity;
import com.project.esheba.LoginActivity;
import com.project.esheba.R;
import com.project.esheba.ServiceProvider.AllProductCHActivity;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import es.dmoral.toasty.Toasty;

public class CustomerMainActivity extends AppCompatActivity {

    CardView cardProfileCus, cardShopCus, cardAllProductsCus, cardOrdersCus, cardLogoutCus, cardContractAdminCus;

    SharedPreferences sp;
    SharedPreferences.Editor editor;


    ShimmerTextView txtWelcomeNameCus, txtHelloCus;
    Shimmer shimmerCus;

    String UserCell;

    private ProgressDialog loading;


    //for double back press to exit
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);

        getSupportActionBar().setTitle("Customer Panel");
        //Fetching mobile from shared preferences
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String mobile = sp.getString(Constant.CELL_SHARED_PREF, "Not Available");
        UserCell = mobile;

        cardProfileCus = findViewById(R.id.card_profileCus);
        cardAllProductsCus = findViewById(R.id.card_allProductCus);
        cardLogoutCus = findViewById(R.id.card_logoutCus);
        cardOrdersCus = findViewById(R.id.card_myOrderCus);
        txtWelcomeNameCus = findViewById(R.id.txtwelcomeNameCus);
        txtHelloCus = findViewById(R.id.txtwelcomeCus);
        cardShopCus = findViewById(R.id.card_viewShopCus);
        cardContractAdminCus = findViewById(R.id.card_contactadminCus);

        Typeface tf = Typeface.createFromAsset(getAssets(), "Milkshake.ttf");


        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();

        cardProfileCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerMainActivity.this, ProfileCusActivity.class);
                startActivity(intent);

            }
        });

        cardShopCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerMainActivity.this, ViewComapnyActivity.class);
                startActivity(intent);

            }
        });


        cardContractAdminCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CustomerMainActivity.this, ContactAdminActivity.class);
                startActivity(intent);

            }
        });


        cardOrdersCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(CustomerMainActivity.this, OrderListCusActivity.class);
                startActivity(intent);*/

            }
        });

        cardAllProductsCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(CustomerMainActivity.this, AllProductCHActivity.class);
                intent.putExtra("type", "Customer");
                startActivity(intent);

            }
        });


        cardLogoutCus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                editor.clear();
                editor.apply();
                // finishAffinity();
                Toasty.info(CustomerMainActivity.this, "Log out from customer panel!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(CustomerMainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        //call function
        getData();
    }

    private void getData() {

        //loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        //showing progress dialog
        loading = new ProgressDialog(CustomerMainActivity.this);
        loading.setMessage("Please wait....");
        loading.show();


        String url = Constant.PROFILECUS_URL + UserCell;  // url for connecting php file

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
                        Toasty.error(CustomerMainActivity.this, "No Internet Connection!", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(CustomerMainActivity.this);
        requestQueue.add(stringRequest);
    }


    private void showJSON(String response) {


        Log.d("RESPONSE", response);

        String name = "";


        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Constant.JSON_ARRAY);
            JSONObject ProfileData = result.getJSONObject(0);

            name = ProfileData.getString(Constant.KEY_NAME);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        txtWelcomeNameCus.setText(name);

    }


    //double backpress to exit
    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {

            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            //finish();
            finishAffinity();

        } else {
            Toasty.info(this, "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
}
