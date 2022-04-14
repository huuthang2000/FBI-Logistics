package com.example.demoapp.Presenters.Group;


import com.example.demoapp.Models.objApplication.objFamily;

import java.util.ArrayList;

public interface view_Family {
    void resultOfActionFamily(boolean isSuccess, String message, String type);

    void resultFamilyList(ArrayList<objFamily> families);
}
