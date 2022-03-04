package com.example.demoapp.services;

import com.example.demoapp.model.DomCold;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DomColdService {

    @GET("GetDataDomCold.php")
    Call<List<DomCold>> getAllDomCold();

    @FormUrlEncoded
    @POST("InsertDataDomCold.php")
    Call<DomCold> insertData(@Field("name") String name, @Field("weight") String weight, @Field("quantity_pallet") String quantityPallet,
                            @Field("quantity_carton") String quantityCarton, @Field("address_receive") String addressReceive, @Field("address_delivery") String addressDelivery,
                            @Field("length") String length, @Field("height") String height, @Field("width") String width,
                            @Field("type") String type, @Field("month") String month, @Field("continent") String continent, @Field("created_date") String createdDate);

    @FormUrlEncoded
    @POST("UpdateDataDomCold.php")
    Call<DomCold> updateData(@Field("stt") String stt, @Field("name") String name, @Field("weight") String weight, @Field("quantity_pallet") String quantityPallet,
                            @Field("quantity_carton") String quantityCarton, @Field("address_receive") String addressReceive, @Field("address_delivery") String addressDelivery,
                            @Field("length") String length, @Field("height") String height, @Field("width") String width,
                            @Field("type") String type, @Field("month") String month, @Field("continent") String continent);
}
