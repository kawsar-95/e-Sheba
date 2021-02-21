package com.project.esheba.remote;

import com.project.esheba.Constant;
import com.project.esheba.ServiceProvider.Product.model.ProductCH;
import com.project.esheba.ServiceProvider.Product.model.ProductUploadCH;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("retrofit/POST/readcontacts.php")
    Call<List<ProductCH>> getContacts();

    @FormUrlEncoded
    @POST("retrofit/POST/addcontact.php")
    public Call<ProductCH> insertUser(
            @Field("name") String name,
            @Field("email") String email);

    @FormUrlEncoded
    @POST("retrofit/POST/editcontact.php")
    public Call<ProductCH> editUser(
            @Field("id") String id,
            @Field("name") String name,
            @Field("email") String email);


    @FormUrlEncoded
    @POST("retrofit/POST/deletecontact.php")
    Call<ProductCH> deleteUser(
            @Field("id") int id
    );

    //for live data search
    @GET("productCH.php")
    Call<List<ProductCH>> getProductCH(
            @Query("item_type") String item_type,
            @Query("key") String keyword,
            @Query("cell") String cell
    );

    //for live data search
    @GET("all_productsCH.php")
    Call<List<ProductCH>> getAllProductCH(
            @Query("item_type") String item_type,
            @Query("key") String keyword,
            @Query("cell") String cell
    );

    //for upload image and info
    @Multipart
    @POST("upload_productCH.php")
    Call<ProductUploadCH> uploadFileCH(@Part MultipartBody.Part file,
                                       @Part(Constant.KEY_FILE) RequestBody name,
                                       @Part(Constant.KEY_PRODUCT_NAME) RequestBody product_name,
                                       @Part(Constant.KEY_CATEGORY) RequestBody category,
                                       @Part(Constant.KEY_QUANTITY) RequestBody stock,
                                       @Part(Constant.KEY_PRICE) RequestBody price,
                                       @Part(Constant.KEY_DESCRIPTION) RequestBody description,
                                       @Part(Constant.KEY_CH_CELL) RequestBody ch_cell);
}