package com.project.esheba.ServiceProvider;

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
import com.project.esheba.LoginActivity;
import com.project.esheba.R;
import com.project.esheba.ServiceProvider.Product.AddProductCHActivity;
import com.project.esheba.ServiceProvider.Product.Order.OrderListCHActivity;
import com.project.esheba.ServiceProvider.Profile.ProfileSpActivity;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import es.dmoral.toasty.Toasty;

public class SpMainActivity extends AppCompatActivity {

    CardView cardProfileSp, cardShopSp, cardAddProductsSp, cardMyProductsSp, cardOrdersSp, cardLogoutSp, cardContractAdminSp;

    SharedPreferences sp;
    SharedPreferences.Editor editor;


    ShimmerTextView txtWelcomeNameSp, txtHelloSp;
    Shimmer shimmerSp;

    String UserCell;

    private ProgressDialog loading;


    //for double back press to exit
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sp_main);


        getSupportActionBar().setTitle("Service Provider Panel");
        //Fetching mobile from shared preferences
        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String mobile = sp.getString(Constant.CELL_SHARED_PREF, "Not Available");
        UserCell = mobile;

        cardProfileSp = findViewById(R.id.card_profileSP);
        cardAddProductsSp = findViewById(R.id.card_addproductSP);
        cardLogoutSp = findViewById(R.id.card_logoutSP);
        cardMyProductsSp = findViewById(R.id.card_myproductsSP);
        cardOrdersSp = findViewById(R.id.card_ordersSP);
        cardShopSp = findViewById(R.id.card_shopinfoSP);
        txtWelcomeNameSp = findViewById(R.id.txtwelcomeNameSp);
        txtHelloSp = findViewById(R.id.txtwelcomeSp);

        cardContractAdminSp = findViewById(R.id.card_contactadminSP);

        //Typeface tf = Typeface.createFromAsset(getAssets(), "Milkshake.ttf");
        Typeface tf = Typeface.createFromAsset(getAssets(), "KaramellDemo-vmRDM.ttf");
        //txtTitle.setTypeface(tf);


        sp = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();

        cardProfileSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpMainActivity.this, ProfileSpActivity.class);
                startActivity(intent);

            }
        });

        cardShopSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpMainActivity.this, CompanyInfoActivity.class);
                startActivity(intent);

            }
        });


        cardContractAdminSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpMainActivity.this, ContactAdminActivity.class);
                startActivity(intent);

            }
        });


        cardOrdersSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpMainActivity.this, OrderListCHActivity.class);
                startActivity(intent);

            }
        });

        cardAddProductsSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpMainActivity.this, AddProductCHActivity.class);
                intent.putExtra("type", "ServiceProvider");
                startActivity(intent);

            }
        });

        cardMyProductsSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SpMainActivity.this, AllProductCHActivity.class);
                intent.putExtra("type", "ServiceProvider");
                startActivity(intent);

            }
        });


        cardLogoutSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                editor.clear();
                editor.apply();
                //finishAffinity();
                Toasty.info(SpMainActivity.this, "Log out from service provider panel!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SpMainActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

        //call function
        getData();
    }

    private void getData() {

        //loading = ProgressDialog.show(this,"Please wait...","Fetching...",false,false);

        //showing progress dialog
        loading = new ProgressDialog(SpMainActivity.this);
        loading.setMessage("Please wait....");
        loading.show();


        String url = Constant.PROFILESP_URL + UserCell;  // url for connecting php file

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
                        Toasty.error(SpMainActivity.this, "No Internet Connection!", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(SpMainActivity.this);
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

        txtWelcomeNameSp.setText(name);

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