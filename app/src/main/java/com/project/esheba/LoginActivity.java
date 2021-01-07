package com.project.esheba;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
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
import com.project.esheba.Admin.AdminMainActivity;
import com.project.esheba.Customer.CustomerMainActivity;
import com.project.esheba.ServiceProvider.SpMainActivity;
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    ShimmerTextView tv;
    Shimmer shimmer;

    TextView txtSignin;
    Button btnSignUp, btnLogin;
    EditText etxtCell, etxtAccountType, etxtPassword;

    private ProgressDialog loading;

    String UserCell, UserPassword;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tv = (ShimmerTextView) findViewById(R.id.txtLoginTitle);

        etxtCell = findViewById(R.id.cell);
        etxtPassword = findViewById(R.id.password);
        etxtAccountType = findViewById(R.id.ac_type);
        btnLogin = findViewById(R.id.btn_login);
        btnSignUp = findViewById(R.id.btn_signup);

        //Fetching mobile from shared preferences
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        String mobile = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");
        String get_password = sharedPreferences.getString(Constant.PASSWORD_SHARED_PREF, "0");
        String get_ac_type = sharedPreferences.getString(Constant.AC_TYPE_SHARED_PREF, "Not Available");
        UserCell = mobile;
        UserPassword = get_password;


        //Typeface tf = Typeface.createFromAsset(getAssets(), "Quicksand-Regular.otf");
        Typeface tf = Typeface.createFromAsset(getAssets(), "AsapCondensed-Regular.ttf");
        //txtTitle.setTypeface(tf);
        tv.setTypeface(tf);

        getSupportActionBar().setTitle("Login Panel");


        shimmer = new Shimmer();
        shimmer.start(tv);

        if (!UserCell.equals("Not Available") && !UserPassword.equals("0")) {
            etxtCell.setText(UserCell);
            etxtPassword.setText(UserPassword);
            etxtAccountType.setText(get_ac_type);

            login();

        }


        //For choosing account type and open alert dialog
        etxtAccountType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] typeList = {"Customer", "ServiceProvider", "Admin"};

                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("SELECT TYPE");
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
                            case 2:
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


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login(); //calling login method

            }
        });
    }

    private void login() {
        //Getting values from edit texts
        final String userCell = etxtCell.getText().toString().trim();
        final String password = etxtPassword.getText().toString().trim();
        final String account_type = etxtAccountType.getText().toString();


        if (account_type.isEmpty()) {

            etxtAccountType.setError("Please Select Type !");
            etxtAccountType.requestFocus();
        }

        if (userCell.length() != 11 || userCell.contains(" ") || userCell.charAt(0) != '0' || userCell.charAt(1) != '1') {

            etxtCell.setError("Please enter Mobile Number !");
            etxtCell.requestFocus();
        }

        //Checking password field/validation
        else if (password.length() < 6) {

            etxtPassword.setError("Password at least 6 characters long !");
            etxtPassword.requestFocus();

        } else {
            //showing progress dialog
            loading = new ProgressDialog(this);
            loading.setMessage("Please wait....");
            loading.show();

            //Creating a string request
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.LOGIN_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            String myResponse = response.trim();


                            Log.d("Response", response);
                            //If we are getting success from server
                            if (myResponse.equals("admin")) {
                                //Creating a shared preference
                                sharedPreferences = LoginActivity.this.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                //Creating editor to store values to shared preferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                //Adding values to editor
                                editor.putString(Constant.AC_TYPE_SHARED_PREF, account_type);
                                editor.putString(Constant.CELL_SHARED_PREF, userCell);
                                editor.putString(Constant.PASSWORD_SHARED_PREF, password);


                                //editor.putString(Config.NAME_SHARED_PREF, name);

                                //Saving values to editor
                                editor.apply();

                                loading.dismiss();
                                //Starting profile activity

                                Toasty.success(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT, true).show();


                                //warningDialog();

                                Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                                startActivity(intent);


                            } else if (myResponse.equals("customer")) {
                                //Creating a shared preference
                                sharedPreferences = LoginActivity.this.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                //Creating editor to store values to shared preferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                //Adding values to editor
                                editor.putString(Constant.AC_TYPE_SHARED_PREF, account_type);
                                editor.putString(Constant.CELL_SHARED_PREF, userCell);
                                editor.putString(Constant.PASSWORD_SHARED_PREF, password);


                                //editor.putString(Config.NAME_SHARED_PREF, name);

                                //Saving values to editor
                                editor.apply();

                                loading.dismiss();
                                //Starting profile activity

                                Toasty.success(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT, true).show();


                                //warningDialog();

                                Intent intent = new Intent(LoginActivity.this, CustomerMainActivity.class);
                                startActivity(intent);


                            } else if (myResponse.equalsIgnoreCase("service_provider")) {
                                //Creating a shared preference
                                sharedPreferences = LoginActivity.this.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

                                //Creating editor to store values to shared preferences
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                //Adding values to editor
                                editor.putString(Constant.AC_TYPE_SHARED_PREF, account_type);
                                editor.putString(Constant.CELL_SHARED_PREF, userCell);
                                editor.putString(Constant.PASSWORD_SHARED_PREF, password);


                                //editor.putString(Config.NAME_SHARED_PREF, name);

                                //Saving values to editor
                                editor.apply();

                                loading.dismiss();
                                //Starting profile activity

                                Toasty.success(LoginActivity.this, "Login Success!", Toast.LENGTH_SHORT, true).show();


                                //warningDialog();

                                Intent intent = new Intent(LoginActivity.this, SpMainActivity.class);
                                startActivity(intent);


                            } else {
                                //If the server response is not success
                                //Displaying an error message on toast

                                Toasty.error(LoginActivity.this, "Invalid mobile number or password", Toast.LENGTH_SHORT, true).show();
                                //Toast.makeText(Activity_Signin.this, "Invalid Cell or password", Toast.LENGTH_LONG).show();
                                loading.dismiss();
                            }
                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want

                            Toasty.error(LoginActivity.this, "Error in connection!!", Toast.LENGTH_SHORT, true).show();
                            // Toast.makeText(Activity_Signin.this, "Error in connection!!!", Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request
                    params.put(Constant.KEY_TYPE, account_type);
                    params.put(Constant.KEY_MOBILE, userCell);
                    params.put(Constant.KEY_PASSWORD, password);

                    Log.d("Values", account_type + " " + userCell + " " + password);


                    //returning parameter
                    return params;
                }
            };

            //Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

    }


//
//
//    private void requestAllPermission() {
//        Dexter.withActivity(this)
//                .withPermissions(
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.ACCESS_NETWORK_STATE,
//                        Manifest.permission.READ_CONTACTS,
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.ACCESS_COARSE_LOCATION,
//                        Manifest.permission.ACCESS_FINE_LOCATION)
//                .withListener(new MultiplePermissionsListener() {
//                    @Override
//                    public void onPermissionsChecked(MultiplePermissionsReport report) {
//                        // check if all permissions are granted
//                        if (report.areAllPermissionsGranted()) {
//                            // Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
//                        }
//
//                        // check for permanent denial of any permission
//                        if (report.isAnyPermissionPermanentlyDenied()) {
//                            // show alert dialog navigating to Settings
//                            //showSettingsDialog();
//                        }
//                    }
//
//                    @Override
//                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
//                        token.continuePermissionRequest();
//                    }
//                }).
//                withErrorListener(new PermissionRequestErrorListener() {
//                    @Override
//                    public void onError(DexterError error) {
//                        //Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .onSameThread()
//                .check();
//    }
//


}
