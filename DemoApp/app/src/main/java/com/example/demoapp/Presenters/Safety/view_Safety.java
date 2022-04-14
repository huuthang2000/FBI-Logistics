package com.example.demoapp.Presenters.Safety;

import com.example.demoapp.Models.objApplication.objDrivingDetail;

import java.util.ArrayList;


public interface view_Safety {

    void resultOfAction(boolean isSuccess, String message, String type);

    void drivingDetails(objDrivingDetail drivingDetail);

    void allDrivingDetailOfUser(ArrayList<objDrivingDetail> drivingDetailList);




}
