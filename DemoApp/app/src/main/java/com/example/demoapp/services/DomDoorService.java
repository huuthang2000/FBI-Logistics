package com.example.demoapp.services;

import com.example.demoapp.model.DomDoor;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DomDoorService {

    @GET("GetDataDomDoor.php")
    Call<List<DomDoor>> getAllDomDoor();

    @FormUrlEncoded
    @POST("InsertDataDomDoor.php")
    Call<DomDoor> insertData(@Field("station_go") String stationGo, @Field("station_come") String stationCome, @Field("address_receive") String addressReceive, @Field("address_delivery") String addressDelivery, @Field("name") String name,
                             @Field("weight") String weight, @Field("quantity") String quantity, @Field("etd") String etd, @Field("type") String type, @Field("month") String month, @Field("continent") String continent, @Field("created_date") String createdDate);

    @FormUrlEncoded
    @POST("UpdateDataDomDoor.php")
    Call<DomDoor> updateData(@Field("stt") String stt, @Field("station_go") String stationGo, @Field("station_come") String stationCome, @Field("address_receive") String addressReceive, @Field("address_delivery") String addressDelivery, @Field("name") String name,
                             @Field("weight") String weight, @Field("quantity") String quantity, @Field("etd") String etd, @Field("type") String type, @Field("month") String month, @Field("continent") String continent);
}
