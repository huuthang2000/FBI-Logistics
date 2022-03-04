package com.example.demoapp.services;

import com.example.demoapp.model.RetailGoods;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RetailGoodsService {
    @FormUrlEncoded
    @POST("InsertRetailGoodsExport.php")
    Call<RetailGoods> addRetailGoodsData(@Field("pol") String pol,
                                     @Field("pod") String pod, @Field("dim") String dim,
                                     @Field("grossweight") String grossweight, @Field("typeofcargo") String typeofcargo,
                                     @Field("oceanfreight") String oceanfreight, @Field("localcharge") String localcharge,
                                     @Field("carrier") String carrier, @Field("schedule") String schedule,
                                     @Field("transittime") String transittime, @Field("valid") String valid,
                                     @Field("note") String note, @Field("month") String month,
                                     @Field("continent") String continent, @Field("date_created") String date_created) ;

    @FormUrlEncoded
    @POST("UpdateRetailGoodsExport.php")
    Call<RetailGoods> updateRetailGoodsData(@Field("stt") String stt, @Field("pol") String pol,
                                        @Field("pod") String pod, @Field("dim") String dim,
                                        @Field("grossweight") String grossweight, @Field("typeofcargo") String typeofcargo,
                                        @Field("oceanfreight") String oceanfreight, @Field("localcharge") String localcharge,
                                        @Field("carrier") String carrier, @Field("schedule") String schedule,
                                        @Field("transittime") String transittime, @Field("valid") String valid,
                                        @Field("note") String note, @Field("month") String month,
                                        @Field("continent") String continent) ;
    @GET("GetDataRetailGoodsExport.php")
    Call<List<RetailGoods>> getPriceRetailGoods();
}
