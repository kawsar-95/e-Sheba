package com.project.esheba;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class RegistrationActivity extends AppCompatActivity {
    ShimmerTextView tv1;
    Shimmer shimmer1;
    private ProgressDialog loading;


    String name, accountType, mobile, email, address, gender, location, password;

    Button btnRegistration;
    EditText etxtName, etxtAccountType, etxtCell, etxtGender, etxtLocation, etxtEmail, etxtPassword, etxtAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        tv1 = (ShimmerTextView) findViewById(R.id.txtTitlesignup);

        //Typeface tf = Typeface.createFromAsset(getAssets(), "Quicksand-Regular.otf");
        Typeface tf = Typeface.createFromAsset(getAssets(), "AsapCondensed-Regular.ttf");
        //txtTitle.setTypeface(tf);
        tv1.setTypeface(tf);

        getSupportActionBar().setTitle("Registration Panel");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        shimmer1 = new Shimmer();
        shimmer1.start(tv1);


        etxtName = findViewById(R.id.fullname);
        etxtAccountType = findViewById(R.id.ac_type);
        etxtCell = findViewById(R.id.cell);
        etxtLocation = findViewById(R.id.location);
        etxtAddress = findViewById(R.id.fulladdress);
        etxtEmail = findViewById(R.id.email);
        etxtPassword = findViewById(R.id.password);
        etxtGender = findViewById(R.id.gender);

        btnRegistration = findViewById(R.id.btn_signup_submit);


        //For choosing account type and open alert dialog
        etxtGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] genderList = {"Male", "Female"};

                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                builder.setTitle("SELECT GENDER");
                //builder.setIcon(R.drawable.ic_gender);


                builder.setCancelable(false);
                builder.setItems(genderList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:
                                etxtGender.setText(genderList[position]);
                                break;

                            case 1:
                                etxtGender.setText(genderList[position]);
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


                AlertDialog accountTypeDialog = builder.create();

                accountTypeDialog.show();
            }

        });


        //For choosing account type and open alert dialog
        etxtAccountType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] typeList = {"Customer", "ServiceProvider"};

                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                builder.setTitle("SELECT ACCOUNT TYPE");
                //builder.setIcon(R.drawable.ic_gender);


                builder.setCancelable(false);
                builder.setItems(typeList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:
                                etxtAccountType.setText(typeList[position]);
                                break;

                            case 1:
                                etxtAccountType.setText(typeList[position]);
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


                AlertDialog accountTypeDialog = builder.create();

                accountTypeDialog.show();
            }

        });


        //For choosing gender and open alert dialog
        etxtLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] cityList = {"Dhaka", "Chittagong", "Sylhet", "Rajshahi", "Barishal", "Khulna", "Rangpur", "Mymensingh"};

                AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
                builder.setTitle("SELECT DIVISION");
                //builder.setIcon(R.drawable.ic_location);


                builder.setCancelable(false);
                builder.setItems(cityList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:

                                etxtLocation.setText("Dhaka");
                                break;

                            case 1:

                                etxtLocation.setText("Chittagong");
                                break;

                            case 2:

                                etxtLocation.setText("Sylhet");
                                break;

                            case 3:

                                etxtLocation.setText("Rajshahi");
                                break;

                            case 4:

                                etxtLocation.setText("Barishal");
                                break;

                            case 5:

                                etxtLocation.setText("Khulna");
                                break;

                            case 6:

                                etxtLocation.setText("Rangpur");
                                break;

                            case 7:

                                etxtLocation.setText("Mymensingh");
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

        btnRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Getting values from edit texts
                name = etxtName.getText().toString().trim();
                accountType = etxtAccountType.getText().toString();
                mobile = etxtCell.getText().toString().trim();
                email = etxtEmail.getText().toString().trim();
                location = etxtLocation.getText().toString().trim();
                address = etxtAddress.getText().toString().trim();
                password = etxtPassword.getText().toString().trim();
                gender = etxtGender.getText().toString().trim();


                //Checking  field/validation


                //Checking  field/validation
                if (accountType.isEmpty()) {
                    etxtAccountType.setError("Please select account type!");
                    requestFocus(etxtName);
                } else if (name.isEmpty()) {
                    etxtName.setError("Please enter your full name !");
                    requestFocus(etxtName);
                }


                //Checking username field/validation
                else if (mobile.length() != 11 || mobile.contains(" ") || mobile.charAt(0) != '0' || mobile.charAt(1) != '1') {
                    etxtCell.setError("Please enter correct mobile !");
                    requestFocus(etxtCell);
                } else if (email.isEmpty()) {
                    etxtName.setError("Please enter your email address !");
                    requestFocus(etxtEmail);
                }

                //Checking  field/validation

                //Checking username field/validation
                else if (location.isEmpty()) {
                    etxtLocation.setError("Please enter your division!");
                    requestFocus(etxtLocation);
                } else if (address.isEmpty()) {
                    etxtAddress.setError("Please enter your full address !");
                    requestFocus(etxtAddress);
                }


                //Checking password field/validation
                else if (password.length() < 6) {

                    etxtPassword.setError("Password at least 6 characters long !");
                    requestFocus(etxtPassword);

                } else {


                    signup();


                }

            }
        });

    }


    private void signup() {


        //showing progress dialog
//
        loading = new ProgressDialog(RegistrationActivity.this);
        loading.setMessage("Please wait....");
        loading.show();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.SIGNUP_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String myResponse = response.trim();


                        //for logcat
                        Log.d("RESPONSE", response);


                        //If we are getting success from server
                        if (myResponse.equals("success")) {


                            loading.dismiss();
                            //Starting profile activity
                            Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                            Toasty.success(RegistrationActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                        } else if (myResponse.equalsIgnoreCase(Constant.USER_EXISTS)) {

                            Toasty.error(RegistrationActivity.this, "User Already exists!", Toast.LENGTH_SHORT).show();
                            loading.dismiss();

                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        Toasty.error(RegistrationActivity.this, "Error in connection!", Toast.LENGTH_LONG).show();
                        // loading.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request

                params.put(Constant.KEY_TYPE, accountType);
                params.put(Constant.KEY_NAME, name);
                params.put(Constant.KEY_MOBILE, mobile);
                params.put(Constant.KEY_EMAIL, email);
                params.put(Constant.KEY_GENDER, gender);
                params.put(Constant.KEY_LOCATION, location);
                params.put(Constant.KEY_ADDRESS, address);
                params.put(Constant.KEY_PASSWORD, password);

                //returning parameter
                return params;
            }
        };


        //Adding the string request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);


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





