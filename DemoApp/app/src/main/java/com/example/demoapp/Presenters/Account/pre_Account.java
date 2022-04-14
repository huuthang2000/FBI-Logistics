package com.example.demoapp.Presenters.Account;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Models.objectFirebase.account.fb_Account;
import com.example.demoapp.R;
import com.example.demoapp.SQLite.tb_Account;
import com.example.demoapp.SQLite.tb_AverageSpeed;
import com.example.demoapp.SQLite.tb_CurrentFamilyID;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.patternRegex;
import com.example.demoapp.view.driver.UISafety.SpeedService;
import com.example.demoapp.view.driver.autoStarService.GPSService;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;


public class pre_Account implements imp_Account {
    public static final String TAG = pre_Account.class.getSimpleName();

    private FirebaseAuth mAuth;
    private view_Account mView;
    private Context context;
    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private FirebaseStorage mStorage;

    public pre_Account(Context context, view_Account mView) {
        this.mView = mView;
        this.context = context;
        this.mAuth = FirebaseAuth.getInstance();
        this.mDatabase = FirebaseDatabase.getInstance();
        this.mUser = FirebaseAuth.getInstance().getCurrentUser();
        this.mStorage = FirebaseStorage.getInstance();
    }

    /**
     * Function signOut
     */
    @Override
    public void signOut() {
        AuthUI.getInstance().signOut(context)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Reset family id
                        tb_CurrentFamilyID.getInstance(context).deleteAllData();

                        //Delete all data speed
                        tb_AverageSpeed.getInstance(context).deleteAllSpeed();

                        //Remove password
                        removePasswordFromLocal(context);

                        context.stopService(new Intent(context, GPSService.class));
                        context.stopService(new Intent(context, SpeedService.class));

                        mView.resultOfActionAccount(true, context.getResources().getString(R.string.Logged_out_successfully), keyUtils.ResultLogout);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mView.resultOfActionAccount(false, e.getMessage(), keyUtils.ResultLogout);
                    }
                });

    }

    /**
     * Function login with email and password
     * @param email email
     * @param pwd   password
     */
    @Override
    public void actionSignIn(String email, String pwd) {

        if (email.matches(patternRegex.REGEX_EMAIL_ADDRESS) && pwd.length() >= 6) {

            //WaitDialog.show((AppCompatActivity) context,context.getResources().getString(R.string.Loading));

            mAuth.signInWithEmailAndPassword(email, pwd)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            savePasswordToLocal(context, pwd);
                            getInfoAccount();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, e.toString());
                            mView.resultOfActionAccount(false, e.getMessage(), keyUtils.ResultLogin);
                        }
                    });

        } else {
            String errorEmail = null;
            String errorPwd = null;

            if (!email.matches(patternRegex.REGEX_EMAIL_ADDRESS))
                errorEmail = context.getResources().getString(R.string.Invalid_email);

            if (email.matches(""))
                errorEmail = context.getResources().getString(R.string.Email_is_not_empty);

            if (pwd.matches(""))
                errorPwd = context.getResources().getString(R.string.Password_is_not_empty);

            if (pwd.length() < 6)
                errorPwd = context.getResources().getString(R.string.Password_is_at_least_6_characters);

            //Login fail
            mView.resultOfActionAccount(false, context.getResources().getString(R.string.Login_failed), keyUtils.ResultLogin);
            mView.errorEmailOrPassword(errorEmail, errorPwd);
        }

    }

    /**
     * save the password to local and encode password
     *
     * @param context  context
     * @param password password
     */
    public static void savePasswordToLocal(@NonNull Context context, @NonNull String password) {
        SharedPreferences prefs = context.getSharedPreferences(keyUtils.dataPassword, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String text = Base64.encodeToString(password.getBytes(), Base64.DEFAULT);
        editor.putString(keyUtils.password, text);
        editor.apply();
    }

    /**
     * @param context context
     */
    public static void removePasswordFromLocal(@NonNull Context context) {
        SharedPreferences prefs = context.getSharedPreferences(keyUtils.dataPassword, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(keyUtils.password);
    }

    /**
     * @param context context
     * @return password
     */
    public static String getCurrentPassword(@NonNull Context context) {
        SharedPreferences prefs = context.getSharedPreferences(keyUtils.dataPassword, Context.MODE_PRIVATE);
        byte[] data = Base64.decode(prefs.getString(keyUtils.password, ""), Base64.DEFAULT);
        String text = "";
        try {
            text = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return text;
    }

    /**
     * Get information account
     */
    private void getInfoAccount() {
        DatabaseReference mRefAccount = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(keyUtils.accountList)
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        mRefAccount.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    tb_Account.getInstance(context).addAccount(new objAccount(dataSnapshot.getKey(), dataSnapshot.getValue(fb_Account.class)));
                    mView.resultOfActionAccount(true, context.getResources().getString(R.string.Success), keyUtils.ResultLogin);
                }
                //Login fail
                else {
                    mView.resultOfActionAccount(false, context.getResources().getString(R.string.Login_failed), keyUtils.ResultLogin);
                    AuthUI.getInstance().signOut(context);
                    Log.e(TAG, "DataSnapshot account null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                AuthUI.getInstance().signOut(context);
                mView.resultOfActionAccount(false, context.getResources().getString(R.string.Login_failed), keyUtils.ResultLogin);
                Log.e(TAG, databaseError.getMessage());
            }
        });
    }

    /**
     * Function auto login
     * check current mUser
     */
    @Override
    public void autoSignIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            mView.resultOfActionAccount(true, context.getResources().getString(R.string.Success), keyUtils.ResultAutoLogin);
        } else {
            mView.resultOfActionAccount(false, context.getResources().getString(R.string.Failed), keyUtils.ResultAutoLogin);
        }
    }

    /**
     * Function register account
     *
     * @param email           email
     * @param password        password
     * @param confirmPassword confirm password
     */
    @Override
    public void actionSignUp(String email, String password, String confirmPassword) {

        if (checkLogicInputRegister(email, password, confirmPassword)) {


            //Create account
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Write database
                            if (task.isSuccessful()) {
                                writeAccountToDatabase(task.getResult().getUser(), keyUtils.EMAIL);
                                savePasswordToLocal(context, password);
                            } else
                                mView.resultOfActionAccount(false, context.getResources().getString(R.string.Registration_failed), keyUtils.ResultRegister);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mView.resultOfActionAccount(false, context.getResources().getString(R.string.Email_already_exists), keyUtils.ResultRegister);
                        }
                    });
        }
    }

    /**
     * Write data account to database
     *
     * @param user mUser after create
     */
    private void writeAccountToDatabase(final FirebaseUser user, String type) {
        DatabaseReference mRefAccount = mDatabase.getReference().child(keyUtils.accountList);
        final fb_Account mAccount = new fb_Account(user.getEmail(), type);
        mRefAccount.child(user.getUid()).setValue(mAccount)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        tb_Account.getInstance(context).addAccount(new objAccount(user.getUid(), mAccount));
                        mView.resultOfActionAccount(true, context.getResources().getString(R.string.Success), keyUtils.ResultRegister);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        mView.resultOfActionAccount(false, context.getResources().getString(R.string.Registration_failed), keyUtils.ResultRegister);
                        Log.e(TAG, e.toString());
                    }
                });
    }

    /**
     * Function check logic input
     *
     * @param email      email is not empty
     * @param pwd        password is not empty and at least 6 characters
     * @param confirmPwd confirm password is not empty and at least 6 characters
     * @return Invalid or valid
     */
    private boolean checkLogicInputRegister(String email, String pwd, String confirmPwd) {
        String errorPwd = null;
        String errorConfirmPwd = null;
        String errorEmail = null;

        //Check password with re-entered password
        if (!pwd.matches(confirmPwd)) {
            errorConfirmPwd = context.getResources().getString(R.string.Password_incorrect);
        }

        //Check password is not empty
        if (pwd.matches("")) {
            errorPwd = context.getResources().getString(R.string.Password_is_not_empty);
        }

        //Check confirm password is not empty
        if (confirmPwd.matches("")) {
            errorConfirmPwd = context.getResources().getString(R.string.Confirm_password_is_not_empty);
        }

        //Check email
        if (!email.matches(patternRegex.REGEX_EMAIL_ADDRESS)) {
            errorEmail = context.getResources().getString(R.string.Invalid_email);
        }

        //returns results to the view
        mView.errorInputRegister(errorEmail, errorPwd, errorConfirmPwd);

        return errorConfirmPwd == null &&
                errorEmail == null &&
                errorPwd == null;


}
    /**
     * @param uriAvatar uri avatar
     * @param mAccount  object account FireBase
     */
    @Override
    public void profileSetting(Uri uriAvatar, fb_Account mAccount) {
        if (checkInputSettingProfile(mAccount)) {



            //Update avatar
            if (uriAvatar != null) {
                updateAvatar(uriAvatar, mAccount);
            } else {
                mAccount.setAvatar(keyUtils.NULL);
                updateProfileToDatabase(mAccount);
            }

        }
    }


    /**
     * Check if there is no account in the database then write to the database and switch to the profile settings
     *
     * @param mUser Current user FireBase
     */
    @Override
    public void signInWithGoogle(final FirebaseUser mUser) {


        if (mUser != null) {

            //Check if the user already exists
            DatabaseReference mRefAccount = mDatabase.getReference().child(keyUtils.accountList).child(mUser.getUid());
            mRefAccount.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //User does not exist yet
                    if (dataSnapshot.getValue() == null) {

                        //Write account to database
                        writeAccountToDatabase(mUser, keyUtils.GOOGLE);

                    } else {
                        tb_Account.getInstance(context).addAccount(new objAccount(dataSnapshot.getKey(), dataSnapshot.getValue(fb_Account.class)));
                        mView.resultOfActionAccount(true, "Success", keyUtils.ResultLogin);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    mView.resultOfActionAccount(false, databaseError.getMessage(), keyUtils.ResultLogin);
                }
            });
        }
        //User null
        else {
            mView.resultOfActionAccount(false, context.getResources().getString(R.string.Login_failed), keyUtils.ResultLogin);
        }
    }

    /**
     * @param email email
     */
    @Override
    public void forgetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mView.resultOfActionAccount(true, context.getResources().getString(R.string.We_have_emailed_you_please_check_your_email), keyUtils.forgetPassword);
                        } else {
                            mView.resultOfActionAccount(false, context.getResources().getString(R.string.Unable_to_recover_your_password), keyUtils.forgetPassword);
                        }
                    }
                });
    }

    @Override
    public void updatePassword(String currentPassword, String newPassword, String passwordConfirm) {

    }


    private boolean queryPurchases(BillingClient mBillingClient) {
        try {
            Log.d(TAG, "jsonPurchases = " + "in queryPurchases");
            //Method not being used for now, but can be used if purchases ever need to be queried in the future
            Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.SUBS);
            try {
                Log.d(TAG, purchasesResult.getPurchasesList() + " = purchasesList");
            } catch (Exception e) {
                Log.d(TAG, "null" + " = purchasesList");
                e.getMessage();
            }
            if (purchasesResult != null) {
                List<Purchase> purchasesList = purchasesResult.getPurchasesList();
                if (purchasesList == null) {
                    return false;
                }
                if (!purchasesList.isEmpty()) {
                    for (Purchase purchase : purchasesList) {
                        String jsonPurchases = purchase.getOriginalJson();
                        Log.d(TAG, "jsonPurchases = " + jsonPurchases);

                    }
                }
            }
            return false;
        } catch (Exception e) {
            Log.d(TAG, "jsonPurchases = " + "Lỗi ở queryPurchases");
            e.getMessage();
            return false;
        }
    }

    /**
     * @param currentPassword current password
     * @param newPassword     new password
     * @param passwordConfirm password confirm
     */
    private boolean checkInputUpdatePassword(String currentPassword, String newPassword, String passwordConfirm) {
        String errorCurrentPwd = null;
        String errorNewPassword = null;
        String errorPasswordConfirm = null;
        //Check empty
        if (!currentPassword.matches("") && !newPassword.matches("") && !passwordConfirm.matches("")) {

            //Check current password
            if (!currentPassword.matches(getCurrentPassword(context)))
                errorCurrentPwd = context.getResources().getString(R.string.The_current_password_is_incorrect);

            if (!newPassword.matches(passwordConfirm))
                errorPasswordConfirm = context.getResources().getString(R.string.Confirm_password_does_not_match);

        } else {

            if (currentPassword.matches(""))
                errorCurrentPwd = context.getResources().getString(R.string.The_value_is_not_valid);

            if (newPassword.matches(""))
                errorNewPassword = context.getResources().getString(R.string.The_value_is_not_valid);

            if (passwordConfirm.matches(""))
                errorPasswordConfirm = context.getResources().getString(R.string.The_value_is_not_valid);
        }

        mView.errorInputEditPassword(errorCurrentPwd, errorNewPassword, errorPasswordConfirm);

        return errorCurrentPwd == null && errorNewPassword == null && errorPasswordConfirm == null;
    }


    /**
     * Upload avatar then write to database
     *
     * @param uriAvatar uri of avatar
     * @param mAccount  object account
     */
    public void updateAvatar(Uri uriAvatar, final fb_Account mAccount) {
        try {
            StorageReference avatarRef = mStorage
                    .getReference()
                    .child(keyUtils.REF_STORAGE_AVATAR)
                    .child(mUser.getUid() + ".JPEG");

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uriAvatar);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = avatarRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    mView.resultOfActionAccount(false, context.getResources().getString(R.string.Failed), keyUtils.ResultSettingProfile);
                    Log.e(TAG, exception.toString());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Download URL avatar
                    StorageReference storageReference = mStorage.getReference(taskSnapshot.getMetadata().getPath());
                    storageReference.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //Write to database
                                    mAccount.setAvatar(uri.toString());
                                    updateProfileToDatabase(mAccount);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    //Write to database
                                    mAccount.setAvatar(keyUtils.NULL);
                                    updateProfileToDatabase(mAccount);
                                }
                            });

                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
            mView.resultOfActionAccount(false, context.getResources().getString(R.string.Failed), keyUtils.ResultSettingProfile);
        }
    }

    /**
     * Write into the account information database
     *
     * @param mAccount object account
     */
    public void updateProfileToDatabase(final fb_Account mAccount) {
        DatabaseReference mRefAccount = mDatabase.getReference().child(keyUtils.accountList).child(mUser.getUid());

        mRefAccount.setValue(mAccount)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        tb_Account.getInstance(context).addOrUpdateAccount(new objAccount(mUser.getUid(), mAccount));
                        mView.resultOfActionAccount(true, context.getResources().getString(R.string.Success), keyUtils.ResultSettingProfile);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                        mView.resultOfActionAccount(false, context.getResources().getString(R.string.Failed), keyUtils.ResultSettingProfile);
                    }
                });
    }

    public void updateProfileToDatabase(String pathAvatar, String username, String phoneNumber, String gender, @NonNull onResultUpdateProfile result) {
        FirebaseDatabase.getInstance()
                .getReference()
                .child(keyUtils.accountList)
                .child(mUser.getUid())
                .child(keyUtils.name)
                .setValue(username)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        //Set phone number
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child(keyUtils.accountList)
                                .child(mUser.getUid())
                                .child(keyUtils.phone)
                                .setValue(phoneNumber)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        //Set gender
                                        FirebaseDatabase.getInstance()
                                                .getReference()
                                                .child(keyUtils.accountList)
                                                .child(mUser.getUid())
                                                .child(keyUtils.gender)
                                                .setValue(gender)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        if (!pathAvatar.matches("")) {
                                                            //Set avatar
                                                            FirebaseDatabase.getInstance()
                                                                    .getReference()
                                                                    .child(keyUtils.accountList)
                                                                    .child(mUser.getUid())
                                                                    .child(keyUtils.avatar)
                                                                    .setValue(pathAvatar)
                                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                        @Override
                                                                        public void onSuccess(Void aVoid) {
                                                                            tb_Account.getInstance(context).updateAccount(mUser.getUid(), pathAvatar, username, gender, phoneNumber);
                                                                            result.onResult(true, "");
                                                                        }
                                                                    })
                                                                    .addOnFailureListener(new OnFailureListener() {
                                                                        @Override
                                                                        public void onFailure(@NonNull Exception e) {
                                                                            result.onResult(false, e.toString());
                                                                        }
                                                                    });
                                                        }
                                                        //No update avatar
                                                        else {
                                                            tb_Account.getInstance(context).updateAccount(mUser.getUid(), pathAvatar, username, gender, phoneNumber);
                                                            result.onResult(true, "");
                                                        }

                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        result.onResult(false, e.toString());
                                                    }
                                                });
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                result.onResult(false, e.toString());
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.onResult(false, e.toString());
                    }
                });
    }

    public interface onResultUpdateProfile {
        void onResult(boolean isSuccess, String message);
    }

    public void updateAvatar(Uri uriAvatar, @NonNull onResultUpdateAvatar result) {
        try {
            StorageReference avatarRef = mStorage
                    .getReference()
                    .child(keyUtils.REF_STORAGE_AVATAR)
                    .child(mUser.getUid() + ".JPEG");

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uriAvatar);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = avatarRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    result.onResult(false, "", exception.getMessage());
                    Log.e(TAG, exception.toString());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    //Download URL avatar
                    StorageReference storageReference = mStorage.getReference(taskSnapshot.getMetadata().getPath());
                    storageReference.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.d(TAG, "update local avatar");
                                    //Reset local avatar
                                    tb_Account.getInstance(context).updateLocalAvatar(mUser.getUid(), "");

                                    //Write to database
                                    result.onResult(true, uri.toString(), "");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    result.onResult(false, "", e.getMessage());
                                }
                            });

                }
            });

        } catch (IOException e) {
            Log.e(TAG, e.toString());
            mView.resultOfActionAccount(false, context.getResources().getString(R.string.Failed), keyUtils.ResultSettingProfile);
        }
    }

    public interface onResultUpdateAvatar {
        void onResult(boolean isSuccess, String path, String message);
    }

    /**
     * Check input data of setting profile including username and phone number
     *
     * @param mAccount object account FireBase
     * @return valid or invalid
     */
    private boolean checkInputSettingProfile(fb_Account mAccount) {
        String username = mAccount.getName();
        String phoneNumber = mAccount.getPhone();

        String errorUsername = null;
        String errorPhoneNumber = null;

        if (username.trim().length() < 3) {
            errorUsername = context.getResources().getString(R.string.The_value_is_not_valid);
        }

        if (phoneNumber.length() < 5) {
            errorPhoneNumber = context.getResources().getString(R.string.The_value_is_not_valid);
        }

        mView.errorInputSettingProfile(errorUsername, errorPhoneNumber);

        return errorPhoneNumber == null && errorUsername == null;
    }


    /**
     * Check the input data of account information update
     *
     * @param username    username
     * @param phoneNumber phone number
     * @param familyName  family name
     * @return valid or invalid
     */
    public boolean checkInputEditProfile(String username, String phoneNumber, String familyName) {
        String errorUsername = null;
        String errorPhoneNumber = null;
        String errorFamilyName = null;

        if (familyName.matches(""))
            errorFamilyName = context.getResources().getString(R.string.Invalid_family_name);

        if (phoneNumber.matches(""))
            errorPhoneNumber = context.getResources().getString(R.string.Invalid_phone_number);

        if (username.matches(""))
            errorUsername = context.getResources().getString(R.string.Invalid_name);

        mView.errorInputEditProfile(errorUsername, errorPhoneNumber, errorFamilyName);

        return errorFamilyName == null && errorPhoneNumber == null && errorUsername == null;
    }}
