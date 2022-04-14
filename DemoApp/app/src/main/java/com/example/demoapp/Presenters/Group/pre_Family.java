package com.example.demoapp.Presenters.Group;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Models.objApplication.objFamily;
import com.example.demoapp.Models.objApplication.objInviteCode;
import com.example.demoapp.Models.objectFirebase.family.fb_area;
import com.example.demoapp.Models.objectFirebase.family.fb_family;
import com.example.demoapp.SQLite.tb_Account;
import com.example.demoapp.SQLite.tb_CurrentFamilyID;
import com.example.demoapp.SQLite.tb_Family;
import com.example.demoapp.SQLite.tb_IDFamily;
import com.example.demoapp.SQLite.tb_InviteCode;
import com.example.demoapp.Utils.keyUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class pre_Family implements imp_Family{
    private static final String TAG = pre_Family.class.getSimpleName();
    private final Context context;

    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private com.example.demoapp.Presenters.Group.view_Family mView;

    private int countAllAccountInforInFamily;
    private int countAllIDFamilyByUserID;

    private static String randomInviteCode;

    public pre_Family(Context context, com.example.demoapp.Presenters.Group.view_Family mView) {
        this.context = context;
        this.mView = mView;
        this.mDatabase = FirebaseDatabase.getInstance();
        this.mUser = FirebaseAuth.getInstance().getCurrentUser();
    }


    @Override
    public void getAllIDFamilyByUserID(){
        countAllIDFamilyByUserID = 0;

        if(mUser != null){
            mDatabase.getReference()
                    .child(keyUtils.IDFamilyList)
                    .child(mUser.getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.getValue() != null){
                                GenericTypeIndicator<ArrayList<String>> t = new GenericTypeIndicator<ArrayList<String>>(){};
                                ArrayList<String> IDFamilyList = dataSnapshot.getValue(t);

                                if(IDFamilyList != null){
                                    final int sizeList = IDFamilyList.size();
                                    for(int i = 0 ; i < sizeList; i++){
                                        //Save to SQLite
                                        tb_IDFamily.getInstance(context).addOrUpdateFamilyID(i,mUser.getUid(),IDFamilyList.get(i));

                                        //Get information and save to SQLite
                                        getFamilyDetail(IDFamilyList.get(i), new onResultFamilyInformation() {
                                            @Override
                                            public void onCompleted(boolean isSuccess, objFamily mFamily, String message) {
                                                if(isSuccess){

                                                    tb_Family.getInstance(context).addOrUpdateFamily(mFamily);

                                                    countAllIDFamilyByUserID++;

                                                    if(countAllIDFamilyByUserID == sizeList){
                                                        mView.resultOfActionFamily(true,"Success", keyUtils.getAllIDFamilyByUserID);
                                                    }
                                                }
                                                //Fail
                                                else{
                                                    Log.d(TAG, message);
                                                    //getAllIDFamilyByUserID();
                                                }
                                            }
                                        });
                                    }
                                }
                                //IDFamilyList null
                                else
                                    mView.resultOfActionFamily(false,"IDFamilyList null", keyUtils.getAllIDFamilyByUserID);

                            }
                            //DataSnapshot null
                            else
                                mView.resultOfActionFamily(false,"dataSnapshot null", keyUtils.getAllIDFamilyByUserID);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            mView.resultOfActionFamily(false,databaseError.getMessage(), keyUtils.getAllIDFamilyByUserID);
                        }
                    });

        }
        //mUser null
        else
            mView.resultOfActionFamily(false,"User null", keyUtils.getAllIDFamilyByUserID);

    }

    /**
     * Check the user's family id
     * @param result interface result
     */
    public static void checkFamilyID(@NonNull onResultCheckFamilyID result){
        FirebaseDatabase.getInstance()
                .getReference()
                .child(keyUtils.IDFamilyList)
                .child(objAccount.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() != null)
                            result.onResult(true, dataSnapshot.getChildrenCount(), "");

                        //DataSnapshot null
                        else
                            result.onResult(true, 0, "");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        result.onResult(false,0,databaseError.getMessage());
                    }
                });
    }

    public interface onResultCheckFamilyID{
        void onResult(boolean isSuccess, long size, String message);
    }

    /**
     *
     * @param pathIDFamily path id of family
     * @param listener interface onResult
     */
    public void getFamilyDetail(final String pathIDFamily, @NonNull final onResultFamilyInformation listener){
        //Log.d(TAG, "getFamilyDetail: " + pathIDFamily);
        mDatabase.getReference()
                .child(pathIDFamily)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() != null)
                        {
                            listener.onCompleted(true,new objFamily(pathIDFamily,parseDataSnapshotToObjectFamily(dataSnapshot)), "");
                        }
                        //DataSnapshot null
                        else
                            listener.onCompleted(false,null, "getFamilyDetail: DataSnapshot null");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        listener.onCompleted(false,null, databaseError.getMessage());
                    }
                });
    }

    public interface onResultFamilyInformation{
        void onCompleted(boolean isSuccess, objFamily mFamily, String message);
    }

    @Override
    public void getFamilyInformation(final String pathIDFamily){
        //Log.d(TAG, "getFamilyInformation: " + pathIDFamily);
        mDatabase.getReference()
                .child(pathIDFamily)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        tb_Family.getInstance(context).addOrUpdateFamily(new objFamily(pathIDFamily,parseDataSnapshotToObjectFamily(dataSnapshot)));
                        mView.resultOfActionFamily(true,"Success", keyUtils.getFamilyInformation);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        mView.resultOfActionFamily(false,databaseError.getMessage(), keyUtils.getFamilyInformation);
                    }
                });
    }

    @Override
    public void getAllAccountInformationInFamily(final String pathIDFamily){
        objFamily mFamily = tb_Family.getInstance(context).getFamilyByID(pathIDFamily);

        if(mFamily != null){
            countAllAccountInforInFamily = 0;
            final int sizeMembers = mFamily.getMembersList().size();
            for(String uID : mFamily.getMembersList()){
                objAccount.getAccountInformationForSingleListener(uID, new objAccount.information() {
                    @Override
                    public void accountInformation(objAccount accountDetail, String msg) {
                        if(accountDetail != null){
                            tb_Account.getInstance(context).addAccount(accountDetail);
                        }
                        else
                            mView.resultOfActionFamily(false,msg, keyUtils.getAllAccountInformationInFamily);

                        countAllAccountInforInFamily++;

                        if(countAllAccountInforInFamily == sizeMembers){
                            mView.resultOfActionFamily(true,"Success", keyUtils.getAllAccountInformationInFamily);
                        }
                    }
                });
            }
        }
    }

    /**
     * Parse DataSnapshot To Object Family
     * @param dataSnapshot dataSnapshot fireBase family
     * @return object fb_family
     */
    public static fb_family parseDataSnapshotToObjectFamily(DataSnapshot dataSnapshot){
        fb_family family = new fb_family();
        List<fb_area> areaList = new ArrayList<>();
        List<String> memberList = new ArrayList<>();
        for(DataSnapshot data : dataSnapshot.child(keyUtils.areaList).getChildren()){
            fb_area area = data.getValue(fb_area.class);
            areaList.add(area);

        }
        for(DataSnapshot data : dataSnapshot.child(keyUtils.membersList).getChildren()){
            String member = data.getValue(String.class);
            memberList.add(member);
        }

        family.setCommonName(dataSnapshot.child(keyUtils.commonName).getValue(String.class));
        family.setInviteCode(dataSnapshot.child(keyUtils.inviteCode).getValue(String.class));
        family.setTimeCreate(Long.parseLong(dataSnapshot.child(keyUtils.timeCreate).getValue().toString()));
        family.setMembersList(memberList);
        family.setAreaList(areaList);
        return family;
    }


    /**
     * Get all information of family members
     * @param pathIDFamily path Family id
     * @param listener interface listener
     */
    private void getAllAccountInFamily(final String pathIDFamily, onResultGetAllAccount listener){

        objFamily mFamily = tb_Family.getInstance(context).getFamilyByID(pathIDFamily);

        if(mFamily != null){
            for(String uID : mFamily.getMembersList()){
                objAccount.getAccountInformationForSingleListener(uID, new objAccount.information() {
                    @Override
                    public void accountInformation(objAccount accountDetail, String msg) {
                        Log.d(TAG, "Download account detail: " +mFamily.getCommonName() + " - " + msg);
                        count_AllAccountInFamily++;
                        if(accountDetail != null){
                            tb_Account.getInstance(context).addOrUpdateAccount(accountDetail);
                            listener.onResult(true,"Success");
                        }
                        else
                            listener.onResult(false,msg);
                    }
                });

                //RealTime account
                objAccount.getAccountInformationListener(uID, new objAccount.information() {
                    @Override
                    public void accountInformation(objAccount accountDetail, String msg) {
                        if(accountDetail != null){
                            tb_Account.getInstance(context).addOrUpdateAccount(accountDetail);
                        }
                    }
                });
            }
        }
    }

    public interface onResultGetAllAccount{
        void onResult(boolean isSuccess, String message);
    }

    private int count_AllAccountInFamily;
    private int sizeAllMember;
    @Override
    public void getAllFamilyByUid() {
        count_AllAccountInFamily = 0;

        String Uid = mUser.getUid();
        ArrayList<objFamily> families = new ArrayList<>();
        ArrayList<String> IDFamilyList = tb_IDFamily.getInstance(context).getAllIDFamilyByUserID(Uid);
        for(String id : IDFamilyList){
            families.add(tb_Family.getInstance(context).getFamilyByID(id));
        }

        final int familySize = families.size();

        sizeAllMember = 0;
        for(objFamily family: families){
            sizeAllMember +=family.getMembersList().size();
        }

        if(familySize > 0){
            for(objFamily family : families){
                Log.d(TAG, "Get information account in family: "+ family.getCommonName());
                getAllAccountInFamily(family.getId(), new onResultGetAllAccount() {
                    @Override
                    public void onResult(boolean isSuccess, String message) {
                        int sizeMemberInSQLite = tb_Family.getInstance(context).getAllUserInFamilyByID(family.getId()).size();
                        int sizeMemberInFamily = family.getMembersList().size();

                        if(count_AllAccountInFamily == sizeAllMember){
                            sizeAllMember = 0;
                            count_AllAccountInFamily = 0;

                            Log.d(TAG, sizeMemberInFamily + " - " + sizeMemberInSQLite);
                            mView.resultFamilyList(families);
                            if(tb_CurrentFamilyID.getInstance(context).getCurrentFamilyID().matches(""))
                                tb_CurrentFamilyID.getInstance(context).addOrUpdateID(families.get(0).getId());
                        }
                    }
                });
            }
        }
        else
            mView.resultFamilyList(families);
    }

    /**
     * Get all invite code
     * @param context context
     * @param result interface result
     */
    public static void getAllInviteCode(Context context, @NonNull onResultAllInviteCode result){

        DatabaseReference mRefInviteCode = FirebaseDatabase.getInstance()
                .getReference()
                .child(keyUtils.inviteCodeList);

        mRefInviteCode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null){
                    ArrayList<objInviteCode> inviteCodes = new ArrayList<>();
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        inviteCodes.add(new objInviteCode(data.getKey(),data.getValue(String.class)));
                        tb_InviteCode.getInstance(context).addOrUpdateInviteCode(data.getKey(),data.getValue(String.class));
                    }
                    result.onResult(inviteCodes,"");
                }
                //DataSnapshot null
                else
                    result.onResult(null, "getAllInviteCode: DataSnapshot Null");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                result.onResult(null, databaseError.getMessage());
            }
        });
    }


    /**
     * Get all invite code
     * @param context context
     * @param result interface result
     */
    public static void checkInviteCode(Context context, String inviteCode , @NonNull onResultInviteCode result){

        DatabaseReference mRefInviteCode = FirebaseDatabase.getInstance()
                .getReference()
                .child(keyUtils.inviteCodeList)
                .child(inviteCode);

        mRefInviteCode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null)
                    result.onResult(new objInviteCode(dataSnapshot.getKey(),dataSnapshot.getValue(String.class)),"");
                //DataSnapshot null
                else
                    result.onResult(null, "checkInviteCode: DataSnapshot Null");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                result.onError(databaseError.getMessage());
            }
        });
    }

    public interface onResultInviteCode{
        void onResult(objInviteCode inviteCode, String message);
        void onError(String error);
    }

    public interface onResultAllInviteCode{
        void onResult(ArrayList<objInviteCode> inviteCodes, String message);
    }

    /**
     * Create a family and write in the Family List ID and invitation code
     * @param context context
     * @param result interface result
     */
    @SuppressLint("RestrictedApi")
    public static void createFamily(@NonNull Context context,String familyName, @NonNull onResultCreateFamily result){


        final DatabaseReference mRefFamily = FirebaseDatabase.getInstance()
                .getReference()
                .child(keyUtils.familyList)
                .child(objAccount.getCurrentUser().getUid())
                .push();

        //Create invite code
        randomInviteCode = tb_InviteCode.randomInviteCode();

        checkInviteCode(context, randomInviteCode, new onResultInviteCode() {
            @Override
            public void onResult(objInviteCode inviteCode, String message) {
                if(inviteCode == null){
                    fb_family family;

                    if(familyName.matches("")){
                        //Create object family
                        family = new fb_family(context, randomInviteCode);
                    }else{
                        family = new fb_family(context, familyName, randomInviteCode);
                    }


                    //Set value family
                    mRefFamily.setValue(family)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    //Family ID
                                    final String familyID = mRefFamily.getRef().getPath().toString();

                                    //Save current family id
                                    tb_CurrentFamilyID.getInstance(context).addOrUpdateID(familyID);

                                    //Write database to node IDFamilyList
                                    writeDatabaseNodeIDFamilyList(familyID, new onResultWriteDBNodeIDFamilyList() {
                                        @Override
                                        public void onResult(boolean isSuccess, String message) {
                                            if(isSuccess){
                                                //Write database to node invite code
                                                writeDatabaseNodeInviteCode(randomInviteCode, familyID, new onResultWriteDBNodeInviteCode() {
                                                    @Override
                                                    public void onResult(boolean isSuccess, String message) {
                                                        result.onResult(isSuccess,message);
                                                    }
                                                });
                                            }
                                            //Fail
                                            else
                                                onResult(false,message);
                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Set value mRefFamily(addOnFailureListener): " +e.getMessage());
                                    result.onResult(false,"Set value mRefFamily(addOnFailureListener): " +e.getMessage());
                                }
                            });
                }
                //Invite is exist
                else{
                    createFamily(context,familyName,result);
                }
            }

            @Override
            public void onError(String error) {
                Log.e("CheckApp", error);
                result.onResult(false,error);
            }
        });

    }

    public interface onResultCreateFamily{
        void onResult(boolean isSuccess, String message);
    }

    /**
     * Write to Database in Family ID List
     * @param familyID family ID
     * @param result interface result
     */
    public static void writeDatabaseNodeIDFamilyList(final String familyID, @NonNull onResultWriteDBNodeIDFamilyList result){

        //write family id of Uid
        DatabaseReference mRefIDFamilyList = FirebaseDatabase.getInstance()
                                            .getReference()
                                            .child(keyUtils.IDFamilyList)
                                            .child(objAccount.getCurrentUser().getUid());

        mRefIDFamilyList.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String position = "0";
                //Get last position
                if(dataSnapshot.getValue() != null){
                    position = String.valueOf(dataSnapshot.getChildrenCount());
                }

                mRefIDFamilyList.child(position)
                        //Value: /familyList/3WJ0NJTeOYcAodxGQWZtVrfgL952/-LxP8AbTFnY8iYM7qnwL
                        .setValue(familyID)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                result.onResult(true,"Success");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e(TAG,"writeDatabaseNodeIDFamilyList (addOnFailureListener): "+ e.getMessage());
                                result.onResult(false,"writeDatabaseNodeIDFamilyList (addOnFailureListener): " +e.getMessage());
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG,"writeDatabaseNodeIDFamilyList (onCancelled): " +databaseError.getMessage());
                result.onResult(false,"writeDatabaseNodeIDFamilyList (onCancelled): " +databaseError.getMessage());
            }
        });
    }

    public interface onResultWriteDBNodeIDFamilyList{
        void onResult(boolean isSuccess, String message);
    }

    /**
     * Add member ids to the family
     * @param familyID family id
     * @param result interface result
     */
    @SuppressLint("RestrictedApi")
    public static void addUidToFamilyID(Context context,String familyID, @NonNull onResultAddUidToFamilyID result){
        DatabaseReference mRefFamily = FirebaseDatabase.getInstance()
                                        .getReference()
                                        .child(familyID);

        mRefFamily.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() != null){
                            fb_family fireBaseFamily = parseDataSnapshotToObjectFamily(dataSnapshot);
                            fireBaseFamily.getMembersList().add(objAccount.getCurrentUser().getUid());
                            objFamily family = new objFamily(familyID,fireBaseFamily);

                            tb_Family.getInstance(context).addOrUpdateFamily(family);

                            getAllAccountDetail(context, (ArrayList<String>) family.getMembersList(), new onResultAllAccountDetail() {
                                @Override
                                public void onResult(boolean isSuccess, String message) {
                                    mRefFamily.child(keyUtils.membersList)
                                            .child(String.valueOf(fireBaseFamily.getMembersList().size()-1))
                                            .setValue(objAccount.getCurrentUser().getUid())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    writeDatabaseNodeIDFamilyList(familyID, new onResultWriteDBNodeIDFamilyList() {
                                                        @Override
                                                        public void onResult(boolean isSuccess, String message) {
                                                            result.onResult(isSuccess,message);
                                                        }
                                                    });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    result.onResult(false,e.getMessage());
                                                }
                                            });
                                }
                            });

                        }else
                            result.onResult(false,"DataSnapshot null");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        result.onResult(false,databaseError.getMessage());
                    }
                });
    }

    public interface onResultAddUidToFamilyID{
        void onResult(boolean isSuccess, String message);
    }

    /**
     * Write to Database in invite code
     * @param inviteCode invite code (Random)
     * @param familyID family ID
     * @param result interface result
     */
    public static void writeDatabaseNodeInviteCode(String inviteCode, String familyID, @NonNull onResultWriteDBNodeInviteCode result){

        //Write invite code
        FirebaseDatabase.getInstance()
                .getReference()
                .child(keyUtils.inviteCodeList)
                .child(inviteCode)
                //Value: /familyList/3WJ0NJTeOYcAodxGQWZtVrfgL952/-LxP8AbTFnY8iYM7qnwL
                .setValue(familyID)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        result.onResult(true,"Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "writeDatabaseNodeInviteCode (addOnFailureListener): " +e.getMessage());
                        result.onResult(false,"writeDatabaseNodeInviteCode (addOnFailureListener): " +e.getMessage());
                    }
                });
    }

    public interface onResultWriteDBNodeInviteCode{
        void onResult(boolean isSuccess, String message);
    }

    /**
     *
     * @param familyID path to family
     * @param familyName family name
     * @param result interface result
     */
    public static void editFamilyName(Context context, String familyID, String familyName, @NonNull onResultEditFamilyName result){
        FirebaseDatabase.getInstance()
                .getReference()
                .child(familyID)
                .child(keyUtils.commonName)
                .setValue(familyName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Update family name into SQLite
                        tb_Family.getInstance(context).updateFamilyName(familyID, familyName);
                        result.onResult(true,"");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.onResult(false,e.getMessage());
                    }
                });
    }

    public interface onResultEditFamilyName{
        void onResult(boolean isSuccess, String message);
    }


    private static int countAllAccountDetail;
    public static void getAllAccountDetail(Context context,ArrayList<String> UidList, @NonNull onResultAllAccountDetail result){
        countAllAccountDetail = 0;
        final int sizeUid = UidList.size();
        for(String Uid : UidList){
            objAccount.getAccountInformationForSingleListener(Uid, new objAccount.information() {
                @Override
                public void accountInformation(objAccount accountDetail, String message) {
                    Log.d("getAllAccountDetail",  message);
                    if(accountDetail!=null){
                        Log.d("getAllAccountDetail", "Account: "+ accountDetail.getName());
                        tb_Account.getInstance(context).addOrUpdateAccount(accountDetail);
                    }
                    countAllAccountDetail++;
                    if(countAllAccountDetail == sizeUid){
                        result.onResult(true,"Success");
                    }
                }
            });
        }
    }
    public interface onResultAllAccountDetail{
        void onResult(boolean isSuccess, String message);
    }

    /**
     * take the number of families created
     * @param result interface result
     */
    public static void getCountFamilyOfUid(@NonNull onResultCountFamily result){
        FirebaseDatabase.getInstance()
                .getReference()
                .child(keyUtils.familyList)
                .child(objAccount.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue() != null)
                            result.onSuccess(dataSnapshot.getChildrenCount());
                        else
                            result.onSuccess(0);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        result.onFailed(databaseError.getMessage());
                    }
                });
    }

    public interface onResultCountFamily{
        void onSuccess(long count);
        void onFailed(String message);
    }

    /**
     * Listener member family
     * @param context context
     */
    public static void listenerMemberInFamily(Context context){
        ArrayList<objFamily> families = tb_Family.getInstance(context).getAllFamilyByUid(objAccount.getCurrentUser().getUid());

        for(objFamily family : families){
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child(family.getId())
                    .child(keyUtils.membersList)
                    .addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            if(dataSnapshot.getValue() != null){
                                objAccount.getAccountInformationForSingleListener(dataSnapshot.getValue(String.class), new objAccount.information() {
                                    @Override
                                    public void accountInformation(objAccount accountDetail, String message) {
                                        if(accountDetail!=null){
                                            tb_Account.getInstance(context).addOrUpdateAccount(accountDetail);
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }


    }

}
