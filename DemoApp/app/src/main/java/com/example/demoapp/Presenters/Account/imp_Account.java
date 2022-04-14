package com.example.demoapp.Presenters.Account;


import android.net.Uri;

import com.example.demoapp.Models.objectFirebase.account.fb_Account;
import com.google.firebase.auth.FirebaseUser;

public interface imp_Account {

    void signOut();

    void actionSignIn(String email, String pwd);

    void autoSignIn();

    void actionSignUp(String email, String password, String confirmPassword);

    void profileSetting(Uri uriAvatar, fb_Account mAccount);

    void signInWithGoogle(FirebaseUser mUser);

    void forgetPassword(String email);

    void updatePassword(String currentPassword, String newPassword, String passwordConfirm);
    

}
