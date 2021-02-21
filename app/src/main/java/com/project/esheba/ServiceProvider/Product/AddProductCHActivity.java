package com.project.esheba.ServiceProvider.Product;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.project.esheba.Constant;
import com.project.esheba.R;
import com.project.esheba.ServiceProvider.AllProductCHActivity;
import com.project.esheba.ServiceProvider.Product.model.ProductUploadCH;
import com.project.esheba.remote.ApiClient;
import com.project.esheba.remote.ApiInterface;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import es.dmoral.toasty.Toasty;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductCHActivity extends AppCompatActivity {
    EditText etxtProductNameCH, etxtQuantityCH, etxtCategoryCH, etxtPriceCHCH, etxtDescriptionCH;
    Button txtChooseImage, txtSubmit;
    ImageView imgProduct;
    String mediaPath, product_name, product_quantity, product_category, product_description, product_price, CHCell;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product_c_h);


        getSupportActionBar().setTitle("Add Service");
        getSupportActionBar().setHomeButtonEnabled(true); //for back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//for back button

        etxtProductNameCH = findViewById(R.id.etxt_CH_product_name);
        etxtCategoryCH = findViewById(R.id.etxt_CH_categoryProduct);
        etxtDescriptionCH = findViewById(R.id.etxt_CH_descriptionProduct);
        etxtPriceCHCH = findViewById(R.id.etxt_CH_priceProduct);
        imgProduct = findViewById(R.id.image_AddproductCH);
        etxtQuantityCH = findViewById(R.id.etxt_CH_quantityProduct);


        txtChooseImage = findViewById(R.id.btn_CH_imageAddProduct);
        txtSubmit = findViewById(R.id.txt_CH_submitAddProduct);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");

        //Fetching cell from shared preferences
        sharedPreferences = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        CHCell = sharedPreferences.getString(Constant.CELL_SHARED_PREF, "Not Available");


        //For choosing account type and open alert dialog
        etxtCategoryCH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String[] categoryList = {"Appliance Repair", "Painting & Renovation", "Shifting", "Cleaning & Pest Control ", "Trips & Travels ",
                        "Beauty & Salon", "Car Rental", "Car Care Services", "Human Services ", "Driver Service ", "Electric & Plumbing ", "Others"};

                AlertDialog.Builder builder = new AlertDialog.Builder(AddProductCHActivity.this);
                builder.setTitle("SELECT CATEGORY OR TYPE");
                //builder.setIcon(R.drawable.ic_gender);


                builder.setCancelable(false);
                builder.setItems(categoryList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int position) {
                        switch (position) {
                            case 0:
                                etxtCategoryCH.setText(categoryList[position]);
                                break;

                            case 1:
                                etxtCategoryCH.setText(categoryList[position]);
                                break;

                            case 2:
                                etxtCategoryCH.setText(categoryList[position]);
                                break;
                            case 3:
                                etxtCategoryCH.setText(categoryList[position]);
                                break;
                            case 4:
                                etxtCategoryCH.setText(categoryList[position]);
                                break;
                            case 5:
                                etxtCategoryCH.setText(categoryList[position]);
                                break;
                            case 6:
                                etxtCategoryCH.setText(categoryList[position]);
                                break;

                            case 7:
                                etxtCategoryCH.setText(categoryList[position]);
                                break;

                            case 8:
                                etxtCategoryCH.setText(categoryList[position]);
                                break;
                            case 9:
                                etxtCategoryCH.setText(categoryList[position]);
                                break;
                            case 10:
                                etxtCategoryCH.setText(categoryList[position]);
                                break;
                            case 11:
                                etxtCategoryCH.setText(categoryList[position]);
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


                AlertDialog categoryDialog = builder.create();

                categoryDialog.show();
            }

        });

        txtChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AddProductCHActivity.this, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, 1213);
            }
        });


        txtSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                product_name = etxtProductNameCH.getText().toString().trim();
                product_category = etxtCategoryCH.getText().toString().trim();
                product_description = etxtDescriptionCH.getText().toString().trim();
                product_price = etxtPriceCHCH.getText().toString().trim();
                product_quantity = etxtQuantityCH.getText().toString();

                if (product_name.isEmpty()) {
                    etxtProductNameCH.setError("Product name can't empty!");
                    etxtProductNameCH.requestFocus();
                } else if (product_category.isEmpty()) {
                    etxtCategoryCH.setError("Product category can't empty!");
                    etxtCategoryCH.requestFocus();
                } else if (product_quantity.isEmpty()) {
                    etxtQuantityCH.setError("Input product quantity per package");
                    etxtQuantityCH.requestFocus();
                } else if (product_price.isEmpty()) {
                    etxtPriceCHCH.setError("Product price can't empty!");
                    etxtPriceCHCH.requestFocus();
                } else if (product_description.isEmpty()) {
                    etxtDescriptionCH.setError("Product description can't empty!");
                    etxtDescriptionCH.requestFocus();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddProductCHActivity.this);
                    builder.setMessage("Want to Add Service ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {


                                    // Perform Your Task Here--When Yes Is Pressed.
                                    //call method
                                    uploadFile(product_name, product_category, product_price, product_description);
                                    dialog.cancel();

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Perform Your Task Here--When No is pressed
                                    dialog.cancel();
                                }
                            }).show();


                }


            }
        });


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            // When an Image is picked
            if (requestCode == 1213 && resultCode == RESULT_OK && null != data) {


                mediaPath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
                Bitmap selectedImage = BitmapFactory.decodeFile(mediaPath);
                imgProduct.setImageBitmap(selectedImage);


            }


        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
        }

    }


    // Uploading Image/Video
    private void uploadFile(String name, String category, String price, String description) {
        progressDialog.show();

        // Map is used to multipart the file using okhttp3.RequestBody
        File file = new File(mediaPath);

        // Parsing any Media type file
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody filename = RequestBody.create(MediaType.parse("text/plain"), file.getName());

        RequestBody p_name = RequestBody.create(MediaType.parse("text/plain"), product_name);
        RequestBody p_category = RequestBody.create(MediaType.parse("text/plain"), product_category);
        RequestBody p_quantity = RequestBody.create(MediaType.parse("text/plain"), product_quantity);
        RequestBody p_price = RequestBody.create(MediaType.parse("text/plain"), product_price);
        RequestBody p_description = RequestBody.create(MediaType.parse("text/plain"), product_description);
        RequestBody ch_cell = RequestBody.create(MediaType.parse("text/plain"), CHCell);


        ApiInterface getResponse = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ProductUploadCH> call = getResponse.uploadFileCH(fileToUpload, filename, p_name, p_category, p_quantity, p_price, p_description, ch_cell);
        call.enqueue(new Callback<ProductUploadCH>() {
            @Override
            public void onResponse(Call<ProductUploadCH> call, Response<ProductUploadCH> response) {
                ProductUploadCH serverResponse = response.body();
                if (serverResponse != null) {
                    if (serverResponse.getSuccess()) {
                        Toasty.success(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddProductCHActivity.this, AllProductCHActivity.class));

                    } else {
                        Toasty.error(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    assert serverResponse != null;
                    Log.v("Response", serverResponse.toString());
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ProductUploadCH> call, Throwable t) {

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
