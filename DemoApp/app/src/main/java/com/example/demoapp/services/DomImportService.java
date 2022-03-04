package com.example.demoapp.services;

import com.example.demoapp.model.DomExport;
import com.example.demoapp.model.DomImport;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DomImportService {

    @GET("GetDataDomImport.php")
    Call<List<DomImport>> getAllDomImport();

    @FormUrlEncoded
    @POST("InsertDataDomImport.php")
    Call<DomImport> insertData(@Field("name") String name, @Field("weight") String weight, @Field("quantity") String quantity,
                               @Field("temp") String temp, @Field("address") String address, @Field("port_receive") String portReceive,
                               @Field("length") String length, @Field("height") String height, @Field("width") String width,
                               @Field("type") String type, @Field("month") String month, @Field("continent") String continent, @Field("created_date") String createdDate);

    @FormUrlEncoded
    @POST("UpdateDataDomImport.php")
    Call<DomImport> updateData(@Field("stt") String stt,@Field("name") String name, @Field("weight") String weight, @Field("quantity") String quantity,
                               @Field("temp") String temp, @Field("address") String address, @Field("port_receive") String portReceive,
                               @Field("length") String length, @Field("height") String height, @Field("width") String width,
                               @Field("type") String type, @Field("month") String month, @Field("continent") String continent);
}
