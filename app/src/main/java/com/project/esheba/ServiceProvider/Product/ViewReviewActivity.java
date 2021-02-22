package com.project.esheba.ServiceProvider.Product;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class ViewReviewActivity extends AppCompatActivity {

    ListView CustomList;
    Button btnSearch;
    EditText etxtSearch;
    private ProgressDialog loading;
    TextView txtAvgRating;
    ShimmerTextView tv2;
    Shimmer shimmer2;

    String cell, ch_cell, Name;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_review);

        CustomList = findViewById(R.id.listViewReview);
        txtAvgRating = findViewById(R.id.txt_AverageRating);

        tv2 = (ShimmerTextView) findViewById(R.id.txtReviewsTitle);

        //Typeface tf = Typeface.createFromAsset(getAssets(), "Quicksand-Regular.otf");
        //Typeface tf = Typeface.createFromAsset(getAssets(), "MachowDemo-6Yjoq.ttf");
        Typeface tf = Typeface.createFromAsset(getAssets(), "KaramellDemo-vmRDM.ttf");
        //txtTitle.setTypeface(tf);
        tv2.setTypeface(tf);

        shimmer2 = new Shimmer();
        shimmer2.start(tv2);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("Service Review & Ratings");

        ch_cell = getIntent().getExtras().getString("ch_cell");
        Name = getIntent().getExtras().getString("name");


        //Fetching cell from shared preferences
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        cell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");


        //call function
        getData("");


    }


    private void getData(String s) {

        String getSearchText = s;
        //showing progress dialog
        loading = new ProgressDialog(this);
        loading.setMessage("Please wait....");
        loading.show();

        if (!s.isEmpty()) {
            getSearchText = ch_cell;
        }
        System.out.println(Name);
        System.out.println(ch_cell);
        String url = Constant.REVIEW_LIST_URL +Name  + "&text=" +ch_cell ;

        Log.d("URL", url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                loading.dismiss();
                showJSON(response);
                // showJSON1(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loading.dismiss();
                        Toasty.error(ViewReviewActivity.this, "Network Error!", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {


        Log.d("Response 2", response);
        //String Avgreview = "";
        float Avgreview = 0, avg = 0;

        int count = 0;
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Constant.JSON_ARRAY);
            //JSONObject ProfileData = result.getJSONObject(0);

            //name = ProfileData.getString(Constant.KEY_NAME);
            // Avgreview = ProfileData.getString(Constant.KEY_AGV_REVIEW);

            if (result.length() == 0) {
                Toasty.info(ViewReviewActivity.this, "No Review Found!", Toast.LENGTH_SHORT).show();
                finish();


            } else {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);


                    String rating = jo.getString(Constant.KEY_RATING);
                    String review = jo.getString(Constant.KEY_REVIEW);
                    //String Avgreview = jo.getString(Constant.KEY_AGV_REVIEW);


                    HashMap<String, String> user_msg = new HashMap<>();

                    if (rating.isEmpty() || rating.equals("null")) {
                        //nothing
                    } else {
                        user_msg.put(Constant.KEY_REVIEW, review);
                        user_msg.put(Constant.KEY_RATING, "Rating: " + rating + " | 5.0");
                        Avgreview = Avgreview + Float.valueOf(rating).floatValue();
                        count++;
                    }

                    list.add(user_msg);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

      /*  NumberFormat numberformat = NumberFormat.getInstance(Avgreview.getResources().getConfiguration().locale);
        numberformat.setMaximumFractionDigits(2);
        numberformat.setMaximumIntegerDigits(1);
        numberformat.setMinimumFractionDigits(2);
        String text = numberformat.format(result);*/
        avg = Avgreview / count;

        String.format("%.2f", avg);
        String textavg = new DecimalFormat("#.#").format(avg);
        txtAvgRating.setText(String.valueOf("Average Rating of this service : " + textavg));

        ListAdapter adapter = new SimpleAdapter(
                ViewReviewActivity.this, list, R.layout.review_list_items,
                new String[]{Constant.KEY_REVIEW, Constant.KEY_RATING},
                new int[]{R.id.txt_review, R.id.txt_rating});
        CustomList.setAdapter(adapter);


    }

    //new average rating
/*
    private void showJSON1(String response) {


        Log.d("RESPONSE", response);

        String Avgreview = "";


        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Constant.JSON_ARRAY);
            JSONObject ProfileData = result.getJSONObject(0);

            //name = ProfileData.getString(Constant.KEY_NAME);
             Avgreview = ProfileData.getString(Constant.KEY_AGV_REVIEW);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        txtAvgRating.setText(Avgreview);

    }*/

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
