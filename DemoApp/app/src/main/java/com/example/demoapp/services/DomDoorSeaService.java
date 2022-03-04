package com.example.demoapp.services;

import com.example.demoapp.model.DomDoorSea;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DomDoorSeaService {

    @GET("GetDataDomDoorSea.php")
    Call<List<DomDoorSea>> getAllDomDoorSea();

    @FormUrlEncoded
    @POST("InsertDataDomDoorSea.php")
    Call<DomDoorSea> insertData(@Field("port_go") String portGo, @Field("port_come") String portCome, @Field("address_receive") String addressReceive, @Field("address_delivery") String addressDelivery, @Field("name") String name,
                             @Field("weight") String weight, @Field("quantity") String quantity, @Field("etd") String etd, @Field("type") String type, @Field("month") String month, @Field("continent") String continent, @Field("created_date") String createdDate);

    @FormUrlEncoded
    @POST("UpdateDataDomDoorSea.php")
    Call<DomDoorSea> updateData(@Field("stt") String stt, @Field("port_go") String portGo, @Field("port_come") String portCome, @Field("address_receive") String addressReceive, @Field("address_delivery") String addressDelivery, @Field("name") String name,
                             @Field("weight") String weight, @Field("quantity") String quantity, @Field("etd") String etd, @Field("type") String type, @Field("month") String month, @Field("continent") String continent);
}
