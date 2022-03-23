//package com.example.demoapp.model;
//
//
//
//import android.content.Context;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class objFamily extends fb_family {
//
//    private String id;
//
//    public objFamily(String id, fb_family family) {
//        super(family.getAreaList(), family.getCommonName(), family.getInviteCode(), family.getMembersList(), family.getTimeCreate());
//        this.id = id;
//    }
//
//
//    public objFamily(String id, List<fb_area> areaList, String commonName, String inviteCode, List<String> membersList, long timeCreate) {
//        super(areaList, commonName, inviteCode, membersList, timeCreate);
//        this.id = id;
//    }
//
//    public objFamily() {
//        super();
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    @Override
//    public List<fb_area> getAreaList() {
//        return super.getAreaList();
//    }
//
//    @Override
//    public void setAreaList(List<fb_area> areaList) {
//        super.setAreaList(areaList);
//    }
//
//    @Override
//    public String getCommonName() {
//        return super.getCommonName();
//    }
//
//    @Override
//    public void setCommonName(String commonName) {
//        super.setCommonName(commonName);
//    }
//
//    @Override
//    public String getInviteCode() {
//        return super.getInviteCode();
//    }
//
//    @Override
//    public void setInviteCode(String inviteCode) {
//        super.setInviteCode(inviteCode);
//    }
//
//    @Override
//    public List<String> getMembersList() {
//        return super.getMembersList();
//    }
//
//    @Override
//    public void setMembersList(List<String> membersList) {
//        super.setMembersList(membersList);
//    }
//
//    @Override
//    public long getTimeCreate() {
//        return super.getTimeCreate();
//    }
//
//    @Override
//    public void setTimeCreate(long timeCreate) {
//        super.setTimeCreate(timeCreate);
//    }
//
//
//    public static objFamily getMyFamily(Context context){
//        return tb_Family.getInstance(context).getFamilyByID(tb_CurrentFamilyID.getInstance(context).getCurrentFamilyID());
//    }
//
//    /**
//     *
//     * @param context context
//     * @return account list
//     */
//    public static ArrayList<objAccount> getAllMemberInFamily(Context context){
//        objFamily family = objFamily.getMyFamily(context);
//        ArrayList<objAccount> accounts = new ArrayList<>();
//
//        for(String accountID : family.getMembersList()){
//            accounts.add(objAccount.getAccountFromSQLite(context,accountID));
//        }
//
//        return accounts;
//    }
//}
