package com.project.esheba.Customer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;

public class ViewComapnyActivity extends AppCompatActivity {
    ListView CustomList;
    Button btnSearch;
    EditText etxtSearch;
    private ImageView imgNoData;
    private ProgressDialog loading;
    int MAX_SIZE = 999;
    public String shopCell[] = new String[MAX_SIZE];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_comapny);

        CustomList = findViewById(R.id.listView);
        btnSearch = findViewById(R.id.btnSearchShop);
        etxtSearch = findViewById(R.id.etxt_search);

        imgNoData = findViewById(R.id.img_no_data);

        imgNoData.setVisibility(View.INVISIBLE);

        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button
        getSupportActionBar().setTitle("Companies List");


        //call function
        getData("");


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = etxtSearch.getText().toString().trim();

                if (searchText.isEmpty()) {
                    Toasty.error(ViewComapnyActivity.this, "Please input shop name!", Toast.LENGTH_SHORT).show();
                } else {
                    getData(searchText);
                }
            }
        });
    }


    private void getData(String s) {

        String getSearchText = s;
        //showing progress dialog
        loading = new ProgressDialog(this);
        loading.setMessage("Please wait....");
        loading.show();

        if (!s.isEmpty()) {
            getSearchText = s;
        }


        String url = Constant.SHOP_LIST_URL + "?text=" + getSearchText;

        Log.d("URL", url);
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response", response);
                loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        loading.dismiss();
                        Toasty.error(ViewComapnyActivity.this, "Network Error!", Toast.LENGTH_LONG).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showJSON(String response) {


        Log.d("Response 2", response);

        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Constant.JSON_ARRAY);

            if (result.length() == 0) {
                Toasty.error(ViewComapnyActivity.this, "No Companies Found!", Toast.LENGTH_SHORT).show();
                imgNoData.setImageResource(R.drawable.nodatafound);
                imgNoData.setVisibility(View.VISIBLE);

            } else {
                for (int i = 0; i < result.length(); i++) {
                    JSONObject jo = result.getJSONObject(i);
                    String name = jo.getString(Constant.KEY_NAME);
                    String owner_name = jo.getString(Constant.KEY_OWNERNAME);
                    String location = jo.getString(Constant.KEY_LOCATION);
                    String licence = jo.getString(Constant.KEY_LICENCE);
                    String address = jo.getString(Constant.KEY_ADDRESS);
                    String cell = jo.getString(Constant.KEY_CELL);

                    shopCell[i] = cell;

                    HashMap<String, String> user_msg = new HashMap<>();
                    user_msg.put(Constant.KEY_NAME, "Owner Name : " + name);
                    user_msg.put(Constant.KEY_OWNERNAME, owner_name);
                    user_msg.put(Constant.KEY_LOCATION, "Division : " + location);
                    user_msg.put(Constant.KEY_ADDRESS, "Address : " + address);
                    user_msg.put(Constant.KEY_LICENCE, "Licence Number : " + licence);
                    user_msg.put(Constant.KEY_CELL, "Contact: " + cell);


                    list.add(user_msg);
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                ViewComapnyActivity.this, list, R.layout.shop_list_item,
                new String[]{Constant.KEY_NAME, Constant.KEY_OWNERNAME, Constant.KEY_LOCATION, Constant.KEY_ADDRESS, Constant.KEY_CELL, Constant.KEY_LICENCE},
                new int[]{R.id.owner_name, R.id.shop_name, R.id.divisionShoplist, R.id.shop_address, R.id.cellnumberShop, R.id.licence_number});
        CustomList.setAdapter(adapter);

        CustomList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int position, long l) {


                new AlertDialog.Builder(ViewComapnyActivity.this)
                        //.setMessage("Want to Call this Shop Now?")
                        .setMessage("Choose about this Company")
                        .setCancelable(false)
                        .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + shopCell[position]));
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("See Products", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                //amake ekhane edit korte hobe
                                Intent intent = new Intent(ViewComapnyActivity.this, ShopProductsActivity.class);
                                //intent.setData(Uri.parse("cell" + shopCell[position]));
                                String shopcell=shopCell[position];
                                intent.putExtra("getcell", shopcell);

                                //intent.putExtra("type", "Shop");
                                startActivity(intent);

                            }
                        })
                        /* .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                             public void onClick(DialogInterface dialog, int which) {
                                 // Perform Your Task Here--When No is pressed
                                 dialog.cancel();
                             }
                         })*/
                        .show();


            }
        });

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


