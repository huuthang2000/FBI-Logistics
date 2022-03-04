package com.example.demoapp.services;

import com.example.demoapp.model.DomCySea;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DomCySeaService {

    @GET("GetDataDomCySea.php")
    Call<List<DomCySea>> getAllDomCySea();

    @FormUrlEncoded
    @POST("InsertDataDomCySea.php")
    Call<DomCySea> insertData(@Field("port_go") String portGo, @Field("port_come") String portCome, @Field("name") String name,
                           @Field("weight") String weight, @Field("quantity") String quantity, @Field("etd") String etd, @Field("type") String type, @Field("month") String month, @Field("continent") String continent, @Field("created_date") String createdDate);

    @FormUrlEncoded
    @POST("UpdateDataDomCySea.php")
    Call<DomCySea> updateData(@Field("stt") String stt, @Field("port_go") String portGo, @Field("port_come") String portCome, @Field("name") String name,
                           @Field("weight") String weight, @Field("quantity") String quantity, @Field("etd") String etd, @Field("type") String type, @Field("month") String month, @Field("continent") String continent);
}
