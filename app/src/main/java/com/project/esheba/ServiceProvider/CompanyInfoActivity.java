package com.project.esheba.ServiceProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class CompanyInfoActivity extends AppCompatActivity {
    EditText extOwnernameshop, extCompanynameshop, extLocationCompany, extAddressCompany, extLicencenumshop;
    ImageView imgProfile;

    TextView txtNameCompany;
    Button btnSaveshop, btnEditshop;
    SharedPreferences sharedPreferences;
    String UserCell;
    ProgressDialog loading;
    String imagePath = "", getImage;
    ImageView imgLicense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);

        extOwnernameshop = findViewById(R.id.txt_editownernameCompany);
        extCompanynameshop = findViewById(R.id.txt_editshopnameCompany);
        // extMobileshop = findViewById(R.id.txt_editmobileCompany);
        extLocationCompany = findViewById(R.id.txt_editlocationCompany);
        extAddressCompany = findViewById(R.id.txt_editaddressCompany);
        extLicencenumshop = findViewById(R.id.txt_editlicencenumshop);

        btnSaveshop = findViewById(R.id.btn_saveshop);
        btnEditshop = findViewById(R.id.btn_editshop);

        btnSaveshop.setVisibility(View.INVISIBLE);


        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("Company Information");

        //Fetching cell from shared preferences
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        UserCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");


        extOwnernameshop.setEnabled(false);
        extCompanynameshop.setEnabled(false);
        // extMobileshop.setEnabled(false);
        extLocationCompany.setEnabled(false);
        extAddressCompany.setEnabled(false);
        extLicencenumshop.setEnabled(false);
        getData();


        //For choosing gender and open alert dialog
        extLocationCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] cityList = {"Dhaka", "Chittagong", "Sylhet", "Rajshahi", "Barishal", "Khulna", "Rangpur", "Mymensingh"};

                AlertDialog.Builder builder = new AlertDialog.Builder(CompanyInfoActivity.this);
                builder.setTitle("SELECT DIVISION");
                //builder.setIcon(R.drawable.ic_location);


                builder.setCancelable(false);
                builder.setItems(cityList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:

                                extLocationCompany.setText("Dhaka");
                                break;

                            case 1:

                                extLocationCompany.setText("Chittagong");
                                break;

                            case 2:

                                extLocationCompany.setText("Sylhet");
                                break;

                            case 3:

                                extLocationCompany.setText("Rajshahi");
                                break;

                            case 4:

                                extLocationCompany.setText("Barishal");
                                break;

                            case 5:

                                extLocationCompany.setText("Khulna");
                                break;

                            case 6:

                                extLocationCompany.setText("Rangpur");
                                break;

                            case 7:

                                extLocationCompany.setText("Mymensingh");
                                break;
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        dialog.dismiss();
                    }
                });


                AlertDialog locationTypeDialog = builder.create();

                locationTypeDialog.show();
            }

        });

        btnSaveshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               /* if (!imagePath.isEmpty())
                {

                    uploadFile();
                }*/
                updateData();
            }
        });


        btnEditshop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                extOwnernameshop.setEnabled(true);
                extCompanynameshop.setEnabled(true);
                //extMobileshop.setEnabled(true);
                extLocationCompany.setEnabled(true);
                extAddressCompany.setEnabled(true);
                extLicencenumshop.setEnabled(true);
                btnSaveshop.setVisibility(View.VISIBLE);

            }
        });
    }

    private void getData() {

        //showing progress dialog
        loading = new ProgressDialog(CompanyInfoActivity.this);
        loading.setMessage("Please wait....");
        loading.show();


        String url = Constant.SHOP_INFO_URL + UserCell;  // url for connecting php file

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
                        Toasty.error(CompanyInfoActivity.this, "No Internet Connection!", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(CompanyInfoActivity.this);
        requestQueue.add(stringRequest);
    }


    private void showJSON(String response) {


        Log.d("RESPONSE", response);

        String shop_name = "";
        String owner_name = "";
        String shop_lic = "";
        String shop_div = "";
        String address = "";


        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Constant.JSON_ARRAY);
            JSONObject ProfileData = result.getJSONObject(0);

            shop_name = ProfileData.getString(Constant.KEY_NAME);
            owner_name = ProfileData.getString(Constant.KEY_OWNERNAME);
            shop_lic = ProfileData.getString(Constant.KEY_SHOPLICENCE);
            shop_div = ProfileData.getString(Constant.KEY_LOCATION);
            address = ProfileData.getString(Constant.KEY_ADDRESS);
            // profileImage = ProfileData.getString(Constant.KEY_PROFILE_IMAGE);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //textViewResult.setText("Name:\t"+first_name+"\nAddress:\t" +address+ "\nVice Chancellor:\t"+ vc);


        extOwnernameshop.setText(shop_name);
        extCompanynameshop.setText(owner_name);
        extLocationCompany.setText(shop_div);
        extAddressCompany.setText(address);
        extLicencenumshop.setText(shop_lic);


     /*   if (!profileImage.isEmpty()) {
            Picasso.with(this)
                    .load(Constant.LOAD_LICENSE_IMAGE_URL + profileImage)
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.placeholder)
                    .into(imgLicense);
        }*/


    }


    public void updateData() {

        final String shop_name = extOwnernameshop.getText().toString().trim();
        final String owner_name = extCompanynameshop.getText().toString().trim();
        final String shop_div = extLocationCompany.getText().toString().trim();
        final String shop_lic = extLicencenumshop.getText().toString().trim();
        final String address = extAddressCompany.getText().toString().trim();

        if (owner_name.isEmpty()) {
            extOwnernameshop.setError("Company Owner Name Can't Empty");
            extOwnernameshop.requestFocus();
        } else if (shop_name.isEmpty()) {
            extCompanynameshop.setError("Company Name Can't Empty");
            extCompanynameshop.requestFocus();
        } else if (shop_div.isEmpty()) {
            extLocationCompany.setError("Company Division Can't Empty");
            extLocationCompany.requestFocus();
        } else if (shop_lic.isEmpty()) {
            extLicencenumshop.setError("Company Licence Number Can't Empty");
            extLicencenumshop.requestFocus();
        } else if (address.isEmpty()) {
            extAddressCompany.setError("Address Can't Empty");
            extAddressCompany.requestFocus();
        } else {
            loading = new ProgressDialog(this);
            // loading.setIcon(R.drawable.wait_icon);
            // loading.setTitle("Update");
            loading.setMessage("Update...Please wait...");
            loading.show();

            String URL = Constant.ADD_SHOP_INFO_URL;

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

                                Intent intent = new Intent(CompanyInfoActivity.this, SpMainActivity.class);
                                Toasty.success(CompanyInfoActivity.this, "Successfully Updated!", Toast.LENGTH_SHORT).show();
                                startActivity(intent);

                            }

                            //If we are getting success from server
                            else if (response.equals("failure")) {

                                loading.dismiss();
                                //Starting profile activity

                                Intent intent = new Intent(CompanyInfoActivity.this, SpMainActivity.class);
                                Toasty.error(CompanyInfoActivity.this, "Update fail!", Toast.LENGTH_SHORT).show();
                                startActivity(intent);

                            } else {

                                loading.dismiss();
                                Toast.makeText(CompanyInfoActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                            }

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want

                            Toast.makeText(CompanyInfoActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request

                    // params.put(Constant.KEY_ID, getID);
                    params.put(Constant.KEY_NAME, shop_name);
                    params.put(Constant.KEY_OWNERNAME, owner_name);
                    params.put(Constant.KEY_LOCATION, shop_div);
                    params.put(Constant.KEY_SHOPLICENCE, shop_lic);
                    params.put(Constant.KEY_ADDRESS, address);
                    params.put(Constant.KEY_CELL, UserCell);


                    //Log.d("ID", getID);

                    //returning parameter
                    return params;
                }
            };


            //Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(CompanyInfoActivity.this);
            requestQueue.add(stringRequest);
        }

    }


    //for request focus
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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



