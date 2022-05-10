package com.example.demoapp.services;

import com.example.demoapp.model.Log;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface LogService {
    @Multipart
    @POST("InsertLog.php")
    Call<Log> addData(@Part("tenhang") RequestBody tenhang,@Part("hscode")  RequestBody hscode,
                      @Part("congdung")  RequestBody congdung, @Part MultipartBody.Part hinhanh,
                      @Part("cangdi")  RequestBody cangdi, @Part("cangden")  RequestBody cangden,
                      @Part("loaihang")  RequestBody loaihang, @Part("soluongcuthe")  RequestBody soluongcuthe,
                      @Part("yeucaudacbiet")  RequestBody yeucaudacbiet, @Part("price")  RequestBody price,
                      @Part("month")  RequestBody month,
                      @Part("importorexport")  RequestBody importorexport,
                      @Part("type")  RequestBody type, @Part("date_created")  RequestBody date_created) ;

    @Multipart
    @POST("UpdateDataLog.php")
    Call<Log> updateData(@Field("stt") String stt, @Field("tenhang") String tenhang, @Field("hscode") String hscode,
                      @Field("congdung") String congdung, @Field("hinhanh") String hinhanh,
                      @Field("cangdi") String cangdi, @Field("cangden") String cangden,
                      @Field("loaihang") String loaihang, @Field("soluongcuthe") String soluongcuthe,
                      @Field("yeucaudacbiet") String yeucaudacbiet, @Field("price") String price
            , @Field("month") String month, @Field("importorexport") String importorexport,
                      @Field("type") String type) ;
    @Multipart
    @POST("UpdateDataLog.php")
    Call<Log> updateDataImage(@Field("stt") String stt,@Part MultipartBody.Part image) ;

    @GET("GetDataLog.php")
    Call<List<Log>> getpriceLog();
}
