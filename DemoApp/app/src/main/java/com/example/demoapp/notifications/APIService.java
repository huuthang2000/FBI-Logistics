package com.example.demoapp.notifications;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {


    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAhlHCCDA:APA91bHOtfAIqeKcd3kuaeokYA7YxcQX5LZ96Q7TMekGxNTTh7mVaADvELNl8exF5YwXynm_71q9OUDL2jON1uJ7NzfwBsj3yi6emSRVBdOOxk3jqOupx4YZdrrsKeFieHmFG1ZBfiFM"
    })

    @POST("fcm/send")
    Call<Response> sendNotification(@Body Sender body);
}
