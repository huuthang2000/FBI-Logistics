package com.example.demoapp.services;

import com.example.demoapp.model.Import;
import com.example.demoapp.model.ImportLcl;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ImportLclServices {
    @GET("GetImportLcl.php")
    Call<List<ImportLcl>> getStatusImport();

    @FormUrlEncoded
    @POST("InsertImportLcl.php")
    Call<ImportLcl> addData(@Field("term") String term,@Field("pol") String pol, @Field("pod") String pod,
                            @Field("cargo") String cargo, @Field("of") String of, @Field("local_pol") String localPol,
                            @Field("local_pod") String localPod, @Field("carrier") String carrier, @Field("schedule") String schedule,
                            @Field("transit_time") String transitTime, @Field("valid") String valid,
                            @Field("note") String note,@Field("month") String month, @Field("continent") String continent,
                            @Field("created_date") String createdDate);

    @FormUrlEncoded
    @POST("UpdateImportLcl.php")
    Call<ImportLcl> updateData(@Field("stt") String stt,@Field("term") String term,@Field("pol") String pol, @Field("pod") String pod,
                            @Field("cargo") String cargo, @Field("of") String of, @Field("local_pol") String localPol,
                            @Field("local_pod") String localPod, @Field("carrier") String carrier, @Field("schedule") String schedule,
                            @Field("transit_time") String transitTime, @Field("valid") String valid,
                            @Field("note") String note,@Field("month") String month, @Field("continent") String continent);


}
