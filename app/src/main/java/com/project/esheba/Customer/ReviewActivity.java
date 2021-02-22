package com.project.esheba.Customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
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
import com.romainpiel.shimmer.Shimmer;
import com.romainpiel.shimmer.ShimmerTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class ReviewActivity extends AppCompatActivity {

    ShimmerTextView tv2;
    Shimmer shimmer2;

    RatingBar ratingBar;
    EditText txtReview;
    TextView txtSubmit, txtRating, txtRatingComment;
    float getRating;
    String id;

    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("Give Review & Rating");

        tv2 = (ShimmerTextView) findViewById(R.id.txtReviewTitle);

        //Typeface tf = Typeface.createFromAsset(getAssets(), "Quicksand-Regular.otf");
        Typeface tf = Typeface.createFromAsset(getAssets(), "MachowDemo-6Yjoq.ttf");
        //txtTitle.setTypeface(tf);
        tv2.setTypeface(tf);

        shimmer2 = new Shimmer();
        shimmer2.start(tv2);


        ratingBar = findViewById(R.id.rating);
        txtReview = findViewById(R.id.txt_review);

        txtSubmit = findViewById(R.id.txt_submit);
        txtRating = findViewById(R.id.txt_rating);
        txtRatingComment = findViewById(R.id.txt_ratingcommend);

        id = getIntent().getExtras().getString("id");


        //load submitted review
        getData();

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                txtRating.setText("Rating: " + rating + " | 5.0");
                getRating = rating;
                if (rating == 1) {

                    txtRatingComment.setText("Rating Comment: Average ");
                    Toast.makeText(ReviewActivity.this, "Average", Toast.LENGTH_SHORT).show();

                } else if (rating == 2) {
                    txtRatingComment.setText("Rating Comment: Good ");
                    Toast.makeText(ReviewActivity.this, "Good", Toast.LENGTH_SHORT).show();

                } else if (rating == 3) {
                    txtRatingComment.setText("Rating Comment: Very Good ");
                    Toast.makeText(ReviewActivity.this, "Very Good", Toast.LENGTH_SHORT).show();

                } else if (rating == 4) {
                    txtRatingComment.setText("Rating Comment: Amazing ");
                    Toast.makeText(ReviewActivity.this, "Amazing", Toast.LENGTH_SHORT).show();

                } else if (rating == 5) {
                    txtRatingComment.setText("Rating Comment: Excellent ");
                    Toast.makeText(ReviewActivity.this, "Excellent", Toast.LENGTH_SHORT).show();

                }
            }
        });

        txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reviewSubmit(getRating);

            }
        });


    }

    //for submit review
    public void reviewSubmit(float getRating) {

        final String review = txtReview.getText().toString();
        final String rating = "" + getRating;


        if (review.isEmpty()) {
            txtReview.setError("Please write review");
            txtReview.requestFocus();
        } else {

            loading = new ProgressDialog(this);
            loading.setMessage("Review Submit...Please wait...");
            loading.show();

            String URL = Constant.ADD_REVIEW_URL;

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

                                Intent intent = new Intent(ReviewActivity.this, CustomerMainActivity.class);
                                Toasty.success(ReviewActivity.this, "Review Successfully Submitted!", Toast.LENGTH_SHORT).show();
                                startActivity(intent);

                            }


                            //If we are getting success from server
                            else if (response.equals("failure")) {

                                loading.dismiss();
                                //Starting profile activity

                                Intent intent = new Intent(ReviewActivity.this, CustomerMainActivity.class);
                                Toasty.error(ReviewActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
                                startActivity(intent);

                            } else {

                                loading.dismiss();
                                Toast.makeText(ReviewActivity.this, "Network Error", Toast.LENGTH_SHORT).show();

                            }

                        }
                    },

                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //You can handle error here if you want

                            Toast.makeText(ReviewActivity.this, "No Internet Connection or \nThere is an error !!!", Toast.LENGTH_LONG).show();
                            loading.dismiss();
                        }
                    }) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    //Adding parameters to request

                    // params.put(Constant.KEY_ID, getID);
                    params.put(Constant.KEY_REVIEW, review);
                    params.put(Constant.KEY_RATING, rating);
                    params.put(Constant.KEY_ID, id);


                    Log.d("ID", id + " " + rating + " " + review);

                    //returning parameter
                    return params;
                }
            };


            //Adding the string request to the queue
            RequestQueue requestQueue = Volley.newRequestQueue(ReviewActivity.this);
            requestQueue.add(stringRequest);
        }

    }


    private void getData() {

        //showing progress dialog
        loading = new ProgressDialog(ReviewActivity.this);
        loading.setMessage("Please wait....");
        loading.show();


        String url = Constant.GET_REVIEW_URL + id;  // url for connecting php file

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
                        Toasty.error(ReviewActivity.this, "No Internet Connection!", Toast.LENGTH_LONG).show();
                        loading.dismiss();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(ReviewActivity.this);
        requestQueue.add(stringRequest);
    }


    private void showJSON(String response) {


        Log.d("RESPONSE", response);

        String review = "";
        String rating = "";


        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Constant.JSON_ARRAY);
            JSONObject ProfileData = result.getJSONObject(0);

            review = ProfileData.getString(Constant.KEY_REVIEW);
            rating = ProfileData.getString(Constant.KEY_RATING);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        //textViewResult.setText("Name:\t"+first_name+"\nAddress:\t" +address+ "\nVice Chancellor:\t"+ vc);


        if (review.isEmpty() || review.equals("null")) {
            txtReview.setText("");
        } else {
            txtReview.setText(review);
        }


        if (rating.isEmpty() || rating.equals("null")) {
            txtRating.setText("");
        } else {
            txtRating.setText("Rating: " + rating + " | 5.0");
            ratingBar.setRating(Float.parseFloat(rating));

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
