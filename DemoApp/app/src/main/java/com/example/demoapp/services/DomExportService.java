package com.example.demoapp.services;

import com.example.demoapp.model.DomExport;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface DomExportService {

    @GET("GetDataDomExport.php")
    Call<List<DomExport>> getAllDomExport();

    @FormUrlEncoded
    @POST("InsertDataDomExport.php")
    Call<DomExport> insertData(@Field("name") String name, @Field("weight") String weight, @Field("quantity") String quantity,
                               @Field("temp") String temp, @Field("address") String address, @Field("port_export") String portExport,
                               @Field("length") String length, @Field("height") String height, @Field("width") String width,
                               @Field("type") String type, @Field("month") String month, @Field("continent") String continent, @Field("created_date") String createdDate);

    @FormUrlEncoded
    @POST("UpdateDataDomExport.php")
    Call<DomExport> updateData(@Field("stt") String stt,@Field("name") String name, @Field("weight") String weight, @Field("quantity") String quantity,
                               @Field("temp") String temp, @Field("address") String address, @Field("port_export") String portExport,
                               @Field("length") String length, @Field("height") String height, @Field("width") String width,
                               @Field("type") String type, @Field("month") String month, @Field("continent") String continent);
}
