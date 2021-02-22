package com.project.esheba.ServiceProvider;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.project.esheba.Customer.CustomerMainActivity;
import com.project.esheba.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class OrderCHActivity extends AppCompatActivity {
    TextView txtName, txtPrice, txtProgramDate;
    Button btnPlus, btnMinus;
    EditText etxtFullAddress, etxBkashTexID;

    RadioButton rbCashOnDelivery, rbbKashPayment;

    ProgressDialog loading;
    SharedPreferences sharedPreferences;
    String name, id, price, getPrice, program_date, address, cus_cell, ch_cell, bkash_tex;

    Button btnOrderSubmit;
    //String TAG = "SSL",transactionId;

    int weight = 1;
    //for date
    public Calendar myCalendar = Calendar.getInstance();
    //  public DatePickerDialog.OnDateSetListener date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_c_h);
        getSupportActionBar().setTitle("Order Panel");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        txtName = findViewById(R.id.txt_product_nameCH_order);
        txtPrice = findViewById(R.id.txt_product_priceCH_order);
        txtProgramDate = findViewById(R.id.txt_programDateCH_order);
        btnOrderSubmit = findViewById(R.id.txt_submit_orderCH_order);

//        btnMinus = findViewById(R.id.btn_minus);
//        btnPlus = findViewById(R.id.btn_plus);

        // transactionId = UUID.randomUUID().toString();

        //Fetching cell from shared preferences
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        cus_cell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");


        etxtFullAddress = findViewById(R.id.etxt_full_addressCH_order);
        etxBkashTexID = findViewById(R.id.etx_bkash_txtIDCH_order);
        //etxBkashTexID.setVisibility(View.INVISIBLE);
        //etxBkashTexID.setEnabled(false);

       /* rbCashOnDelivery = findViewById(R.id.rb_cash_on_delivery);
        rbbKashPayment = findViewById(R.id.rb_online_payment);*/


        final String getName = getIntent().getExtras().getString("name");
        getPrice = getIntent().getExtras().getString("price");
        ch_cell = getIntent().getExtras().getString("ch_cell");
        id = getIntent().getExtras().getString("id");

        txtName.setText(getName);
        txtPrice.setText("" + Integer.valueOf(getPrice) * 1);


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        txtProgramDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(OrderCHActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        btnOrderSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = getName;
                price = txtPrice.getText().toString();
                program_date = txtProgramDate.getText().toString();
                address = etxtFullAddress.getText().toString();
                bkash_tex = etxBkashTexID.getText().toString();

              /* if (rbCashOnDelivery.isChecked()) {
                    payment_method = "cod";
                } else {
                    payment_method = "bkash";
                }
                if (payment_method.equals("bkash")) {
                    //etxBkashTexID.setVisibility(View.VISIBLE);
                    etxBkashTexID.setEnabled(true);
                     if (bkash_tex.isEmpty()) {
                        etxBkashTexID.setError("Enter bKash TexID");
                        etxBkashTexID.requestFocus();
                    }
                }*/

                if (address.isEmpty()) {
                    etxtFullAddress.setError("Enter full address");
                    etxtFullAddress.requestFocus();
                } else if (bkash_tex.isEmpty()) {
                    etxBkashTexID.setError("Enter bKash TexID");
                    etxBkashTexID.requestFocus();
                } else {
                    orderSubmit();

                }
            }
        });

/*

        btnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                weight++;
                txtQuantity.setText(weight + " PIECE");

                int mPrice = weight * Integer.valueOf(getPrice);
                txtPrice.setText("" + mPrice);
            }
        });

        btnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (weight <= 1) {
                    Toasty.warning(OrderActivity.this, "Minimum quantity is 1 Piece", Toast.LENGTH_SHORT).show();
                } else {
                    weight--;
                    txtQuantity.setText(weight + " PIECE");

                    int mPrice = weight * Integer.valueOf(getPrice);
                    txtPrice.setText("" + mPrice);
                }
            }
        });
*/


    }


    //for date input
    private void updateLabel() {
        String myFormat = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        txtProgramDate.setText(sdf.format(myCalendar.getTime()));
    }


    private void orderSubmit() {


        //showing progress dialog
//
        loading = new ProgressDialog(OrderCHActivity.this);
        loading.setMessage("Please wait....");
        loading.show();

        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.ORDER_SUBMIT_CH_URL,
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
                            Intent intent = new Intent(OrderCHActivity.this, CustomerMainActivity.class);
                            Toasty.success(OrderCHActivity.this, "Order successful", Toast.LENGTH_SHORT).show();
                            startActivity(intent);

                        } else if (myResponse.equalsIgnoreCase(Constant.USER_EXISTS)) {

                            Toasty.error(OrderCHActivity.this, "Program date already booked!\nPlease select another date.", Toast.LENGTH_SHORT).show();
                            loading.dismiss();

                        }


                        //If we are getting success from server
                        if (myResponse.equals("failure")) {


                            loading.dismiss();

                            Toasty.success(OrderCHActivity.this, "Order failed!", Toast.LENGTH_SHORT).show();


                        }

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //You can handle error here if you want

                        Toasty.error(OrderCHActivity.this, "Error in connection!", Toast.LENGTH_LONG).show();
                        // loading.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //Adding parameters to request


                params.put(Constant.KEY_NAME, name);
                params.put(Constant.KEY_PRICE, price);
                params.put(Constant.KEY_PROGRAM_DATE, program_date);
                params.put(Constant.KEY_ADDRESS, address);
                params.put(Constant.KEY_BKASHTEX, bkash_tex);
                // params.put(Constant.KEY_PAYMENT_METHOD, payment_method);

                params.put(Constant.KEY_CUS_CELL, cus_cell);
                params.put(Constant.KEY_CH_CELL, ch_cell);

                params.put(Constant.KEY_ID, id);

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

}
