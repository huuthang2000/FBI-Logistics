
package com.example.demoapp.Presenters.Account;


public interface view_Account {


    void errorEmailOrPassword(String errorEmail, String errorPassword);

    void resultOfActionAccount(boolean isSuccess, String message, String type);

    void errorInputRegister(String email, String password, String confirmPassword);

    void errorInputSettingProfile(String errorUsername, String errorPhoneNumber);

    void errorInputEditProfile(String errorUsername, String errorPhoneNumber, String errorFamilyName);

    void errorInputEditPassword(String errorCurrentPassword, String errorNewPassword, String errorPasswordConfirm);


}
