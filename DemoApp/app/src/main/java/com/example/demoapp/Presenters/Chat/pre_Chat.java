package com.example.demoapp.Presenters.Chat;


import static com.example.demoapp.Utils.keyUtils.LIMIT_MESSAGE;
import static com.example.demoapp.Utils.keyUtils.LIMIT_MESSAGE_NOTIFICATION;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Models.objApplication.objChat;
import com.example.demoapp.Models.objApplication.objDetailImage;
import com.example.demoapp.Models.objApplication.objMessage;
import com.example.demoapp.Models.objectFirebase.account.fb_Account;
import com.example.demoapp.Models.objectFirebase.chat.fb_Chat;
import com.example.demoapp.Models.objectFirebase.chat.fb_Message;
import com.example.demoapp.SQLite.tb_Account;
import com.example.demoapp.SQLite.tb_Chat;
import com.example.demoapp.SQLite.tb_CurrentFamilyID;
import com.example.demoapp.SQLite.tb_Message;
import com.example.demoapp.Utils.keyUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class pre_Chat implements imp_Chat{

    private final Context context;
    private view_Chat mView;

    private FirebaseDatabase mDatabase;
    private FirebaseStorage mStorage;
    private String familyID;

    private DatabaseReference mRefMessage;

    private static int messageID;

    private int start;
    private boolean flag;

    public pre_Chat(Context context, view_Chat mView) {
        this.context = context;
        this.mView = mView;

        this.mDatabase = FirebaseDatabase.getInstance();
//        mDatabase.setPersistenceEnabled(true);

        this.mStorage  = FirebaseStorage.getInstance();
        this.familyID = tb_CurrentFamilyID.getInstance(context).getCurrentFamilyID().replace("/"+ keyUtils.familyList,"");
    }


    /**
     *
     * @param idChat id chat
     */
    @Override
    public void getChatDetail(String idChat) {
        final DatabaseReference mRefChat = mDatabase
                .getReference()
                .child(keyUtils.chatList)
                .child(idChat);

        mRefChat.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshotChat) {
                if(dataSnapshotChat.getValue() != null)
                    mView.resultChatDetail(dataSnapshotChat.getValue(fb_Chat.class));
                else{
                    mView.resultChatDetail(null);
                    mView.resultOfAction(false,"Chat detail null",keyUtils.getChatDetail);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.resultOfAction(false,databaseError.getMessage(),keyUtils.getChatDetail);
            }
        });
    }

    /**
     * @param idChat id chat
     */
    @SuppressLint("RestrictedApi")
    @Override
    public void getMessageList(String idChat) {

        final Query mRefChat = mDatabase
                .getReference()
                .child(keyUtils.messageList)
                .child(idChat)
                .limitToLast(LIMIT_MESSAGE);

        flag = false;
        mRefChat.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getValue() != null){
                    if(!flag){
                        start = Integer.parseInt(dataSnapshot.getKey());
                        flag = true;
                    }

                    mView.resultMessage(new objMessage(dataSnapshot.getKey(),idChat,dataSnapshot.getValue(fb_Message.class)));
                }
                //NULL DataSnapshot
                else
                    mView.resultOfAction(false,"Message list null",keyUtils.getMessageList);

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

    public void getMessageLoadMore(String idChat, @NonNull interfaceLoadMore result){

        int endAt = start - 1;

        final Query mRefChat = mDatabase
                .getReference()
                .child(keyUtils.messageList)
                .child(idChat)
                .orderByKey()
                .endAt(String.valueOf(endAt))
                .limitToLast(LIMIT_MESSAGE);

        if(endAt >= 0){
            mRefChat.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.getValue()!=null){
                        ArrayList<objMessage> messageList = new ArrayList<>();
                        for(DataSnapshot data : dataSnapshot.getChildren()){
                            messageList.add(new objMessage(data.getKey(), idChat, data.getValue(fb_Message.class)));
                        }

                        if(messageList.size()>0)
                            start = Integer.parseInt(messageList.get(0).getId());

                        result.onResult(messageList);

                    }else
                        result.onResult(null);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    result.onResult(null);
                }
            });

        }
        else
            result.onResult(null);
    }

    public interface interfaceLoadMore{
        void onResult(ArrayList<objMessage> messageList);
    }

    /**
     * @param idChat id chat
     * @param content content
     * @param bmImage bitmap image
     * @param type type of action
     */
    @Override
    public void postMessage(final String idChat, final String content, final Bitmap bmImage, final String type) {
        final Query mRefChat = mDatabase
                .getReference()
                .child(keyUtils.messageList)
                .child(idChat)
                .limitToLast(1);

        //Get last message
        mRefChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshotChat) {
                if(dataSnapshotChat.getValue() != null){
                    messageID = 0;
                    try{
                        for(DataSnapshot data : dataSnapshotChat.getChildren()){
                            messageID = Integer.parseInt(data.getKey()) + 1;
                        }
                    }catch (Exception e){ }

                    mRefMessage = mDatabase
                            .getReference()
                            .child(keyUtils.messageList)
                            .child(idChat)
                            .child(String.valueOf(messageID));
                }
                //Message list does not yet exist, create a new message
                else
                    mRefMessage = mDatabase
                            .getReference()
                            .child(keyUtils.messageList)
                            .child(idChat)
                            .child("0");


                //Post message
                //
                //Type TEXT
                if(type.matches(keyUtils.TEXT)){

                    mRefMessage.setValue(new fb_Message(content,type))
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mView.resultOfAction(false,e.getMessage(),keyUtils.postMessage);
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    setTotalMessageAndTimeUpdate(messageID, idChat, new onResultSetTotalMessage() {
                                        @Override
                                        public void onResult(boolean isSuccess, String message) {
                                            mView.resultOfAction(isSuccess,message,keyUtils.postMessage);
                                        }
                                    });
                                }
                            });
                }

                //Type Picture, Upload the image
                else if(type.matches(keyUtils.PICTURE)){

                    StorageReference mountainsRef = mStorage.getReference()
                            .child(keyUtils.message)
                            .child(objAccount.getCurrentUser().getUid() + "_" + Calendar.getInstance().getTimeInMillis() + ".jpg");

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bmImage.compress(Bitmap.CompressFormat.JPEG, 60, baos);
                    byte[] data = baos.toByteArray();

                    //Upload image
                    UploadTask uploadTask = mountainsRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            mView.resultOfAction(false,exception.getMessage(),keyUtils.postMessage);
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Download URL Image
                            StorageReference storageReference = mStorage.getReference(taskSnapshot.getMetadata().getPath());
                            storageReference.getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            //Set value
                                            mRefMessage.setValue(new fb_Message(uri.toString(),type))
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            mView.resultOfAction(false,e.getMessage(),keyUtils.postMessage);
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            mView.resultOfAction(false,e.getMessage(),keyUtils.postMessage);
                                        }
                                    });
                        }
                    });
                }

                //Type multi image
                else if(type.matches(keyUtils.MULTI_PICTURE)){
                    mRefMessage.setValue(new fb_Message(content,type))
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mView.resultOfAction(false,e.getMessage(),keyUtils.postMessage);
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.resultOfAction(false,databaseError.getMessage(),keyUtils.postMessage);
            }
        });
    }

    public static void postMessageFromNotification(String idChat, String message, @NonNull onResultPostMessageNotification result){
        final Query mRefChat = FirebaseDatabase.getInstance()
                .getReference()
                .child(keyUtils.messageList)
                .child(idChat)
                .limitToLast(1);

        mRefChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference mRefMessage;
                messageID = 0;
                if(dataSnapshot.getValue() != null){
                    try{
                        for(DataSnapshot data : dataSnapshot.getChildren()){
                            messageID = Integer.parseInt(data.getKey()) + 1;
                        }
                    }catch (Exception e){
                    }

                    mRefMessage = FirebaseDatabase.getInstance()
                            .getReference()
                            .child(keyUtils.messageList)
                            .child(idChat)
                            .child(String.valueOf(messageID));
                }
                //Message list does not yet exist, create a new message
                else
                    mRefMessage = FirebaseDatabase.getInstance()
                            .getReference()
                            .child(keyUtils.messageList)
                            .child(idChat)
                            .child("0");


                mRefMessage.setValue(new fb_Message(message,keyUtils.TEXT))
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                result.onResult(false,e.toString());
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Set totalMessage
                                setTotalMessageAndTimeUpdate(messageID, idChat, new onResultSetTotalMessage() {
                                    @Override
                                    public void onResult(boolean isSuccess, String message) {
                                        if(isSuccess)
                                            result.onResult(true,"Success");
                                        else
                                            result.onResult(false,message);
                                    }
                                });
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface onResultPostMessageNotification{
        void onResult(boolean isSuccess, String message);
    }

    /**
     * Set total message of chat
     * @param totalMessage total of message
     * @param idChat id of chat
     * @param result interface result
     */
    public static void setTotalMessageAndTimeUpdate(long totalMessage, String idChat, @NonNull onResultSetTotalMessage result){

        FirebaseDatabase.getInstance().getReference()
                .child(keyUtils.chatList)
                .child(idChat)
                .child(keyUtils.totalMessage)
                .setValue(totalMessage + 1)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //set time update
                        FirebaseDatabase.getInstance().getReference()
                                .child(keyUtils.chatList)
                                .child(idChat)
                                .child(keyUtils.timeUpdate)
                                .setValue(Calendar.getInstance().getTimeInMillis())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        result.onResult(true,"Success");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        result.onResult(false,e.getMessage());
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

    /**
     * interface of function setTotalMessageAndTimeUpdate
     */
    public interface onResultSetTotalMessage{
        void onResult(boolean isSuccess, String message);
    }

    public static void getNewMessageOfUid(Context context, @NonNull onResultNotification result) {

        //Get chat of user
        ArrayList<objChat> listChat = (ArrayList<objChat>) tb_Chat.getInstance(context).getAllChatByUid(objAccount.getCurrentUser().getUid());

        //loop through the chat list to listen for new messages
        for(objChat chat : listChat){
            addChatQuery(chat, result);
        }

        //Add event listen id chat
        tb_Chat.getInstance(context).setOnAddChatListener(new tb_Chat.onAddChatListener() {
            @Override
            public void onAddChat(objChat chat) {
                Log.d("CheckApp", "Add chat query");
                addChatQuery(chat, result);
            }
        });
    }

    //Events listen for new messages of the chat to notify
    private static void addChatQuery(objChat chat, @NonNull onResultNotification result){
        final Query chatQuery  = FirebaseDatabase.getInstance()
                .getReference()
                .child(keyUtils.messageList)
                .child(chat.getId())
                .limitToLast(LIMIT_MESSAGE_NOTIFICATION);

        chatQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.getValue()!=null){

                    fb_Message fbMessage = dataSnapshot.getValue(fb_Message.class);
                    if(!fbMessage.getMembersListSeen().contains(objAccount.getCurrentUser().getUid())){
                        int id = 0;
                        try{
                            id = Integer.parseInt(dataSnapshot.getKey());

                        }catch (Exception e){
                            Log.e("CheckApp", "getNewMessageOfUid: "+ e.toString());
                        }

                        result.onResult(fbMessage,
                                chat,
                                chat.getId(),
                                id);
                    }
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

    //Check new message
    public static boolean isNewMessage(Context context){
        ArrayList<objChat> allChat = (ArrayList<objChat>) tb_Chat.getInstance(context).getAllChatByUid(objAccount.getCurrentUser().getUid());

        boolean flag = false;

        for(objChat chat : allChat){
            ArrayList<objMessage> messageList = (ArrayList<objMessage>) tb_Message.getInstance(context).getAllMessageByChatID(chat.getId().replace("/" + keyUtils.chatList, ""));
            objMessage lastMessage = messageList.get(messageList.size() > 0 ? messageList.size() - 1 : 0);
            if(!lastMessage.getMembersListSeen().contains(objAccount.getCurrentUser().getUid())){
                flag = true;
                break;
            }
        }

        return flag;
    }

    public interface onResultNotification{
        void onResult(fb_Message message, fb_Chat chatDetail, String idChat, int position);
    }

    /**
     *
     * @param position position
     * @param pathImage image path
     * @param result interface onResult
     */
    @Override
    public void uploadImageToFolderMessage(final int position, final String pathImage, final onResultUploadImage result){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver() , Uri.fromFile(new File(pathImage)));
            StorageReference mountainsRef = mStorage.getReference()
                    .child(keyUtils.message)
                    .child(objAccount.getCurrentUser().getUid() + "_" + Calendar.getInstance().getTimeInMillis() + ".jpeg");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            byte[] data = baos.toByteArray();

            //Upload image
            UploadTask uploadTask = mountainsRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                    if(result!=null)
                        result.onResult(false,keyUtils.NULL,position, exception.toString());
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Download URL Image
                    StorageReference storageReference = mStorage.getReference(taskSnapshot.getMetadata().getPath());
                    storageReference.getDownloadUrl()
                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //Set value
                                    if(result!=null)
                                        result.onResult(true,uri.toString(),position, "Success");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if(result!=null)
                                        result.onResult(false,keyUtils.NULL,position, e.toString());
                                }
                            });
                }
            });
        } catch (IOException e) {
            if(result!=null)
                result.onResult(false,keyUtils.NULL,position, e.toString());
        }
    }

    public interface onResultUploadImage {
        void onResult(boolean isSuccess, String urlImage, int position, String message);
    }

    /**
     *
     * @param idChat id chat
     */
    @Override
    public void getAllImageFromMessage(String idChat) {

        final Query mRefChat = mDatabase
                .getReference()
                .child(keyUtils.messageList)
                .child(idChat)
                .limitToLast(LIMIT_MESSAGE);

        mRefChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final List<fb_Message> messageList = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    messageList.add(data.getValue(fb_Message.class));
                }

                final int sizeMessage = messageList.size();
                ArrayList<objDetailImage> detailImageList = new ArrayList<>();
                //set image detail
                for(int i = 0; i < sizeMessage; i++){
                    fb_Message msg = messageList.get(i);
                    fb_Account account = tb_Account.getInstance(context).getAccountByID(msg.getAuth());

                    //Type Picture
                    if(msg.getType().matches(keyUtils.PICTURE)){
                        detailImageList.add(new objDetailImage(account.getName(),msg.getContent(), msg.getTime()));
                    }
                    //Type multi picture
                    else if(msg.getType().matches(keyUtils.MULTI_PICTURE)){
                        final ArrayList<String> imageList = new Gson().fromJson(msg.getContent(), new TypeToken<ArrayList<String>>(){}.getType());
                        final int sizeImage = imageList.size();
                        for(int j = 0; j<sizeImage; j++){
                            detailImageList.add(new objDetailImage(account.getName(),imageList.get(j), msg.getTime()));
                        }
                    }
                }

                mView.resultAllImage(detailImageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.resultOfAction(false,databaseError.getMessage(),keyUtils.getAllImageFromMessage);
            }
        });
    }


    @Override
    public void getAllListChatOfUid() {

        final DatabaseReference mRefChat = mDatabase
                .getReference()
                .child(keyUtils.chatList)
                .child(familyID);

        mRefChat.addValueEventListener(new ValueEventListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshotChat) {

                if(dataSnapshotChat.getValue() != null){

                    //Get chat data
                    ArrayList<objChat> chatListDataAll = new ArrayList<>();
                    for(DataSnapshot data : dataSnapshotChat.getChildren()){
                        String chatID = data.getRef().getPath().toString().replace("/"+ keyUtils.chatList, "");
                        chatListDataAll.add(new objChat(chatID, data.getValue(fb_Chat.class)));
                    }

                    ArrayList<objChat> dataChatOfUid = new ArrayList<>();
                    for(objChat chat : chatListDataAll){
                        if(chat.getMembersList().contains(objAccount.getCurrentUser().getUid())){
                            dataChatOfUid.add(chat);
                        }
                    }

                    if(dataChatOfUid.size() > 0){
                        for(objChat chat : dataChatOfUid){
                            tb_Chat.getInstance(context).addChatAndUpdate(chat.getId(), chat.getFireBaseChat());

                            mView.resultListChat(dataChatOfUid);
                            //Get message data of chat id
                            getDataMessage(chat.getId(),LIMIT_MESSAGE, new onDataMessage() {
                                @Override
                                public void onResult(ArrayList<fb_Message> messages) {
                                }
                            });
                        }
                    }else{
                        mView.resultListChat(new ArrayList<>());
                    }
                }
                //dataSnapshotChat NULL
                else{
                    mView.resultListChat(new ArrayList<>());
                    Log.d("CheckApp", "getAllListChatOfUid: dataSnapshotChat NULL");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.resultOfAction(false,databaseError.getMessage(),keyUtils.getAllListChatOfUid);
            }
        });
    }


    private void getDataMessage(final String idChat,int limit, @NonNull onDataMessage result){
        Query mRefMessage = mDatabase
                .getReference()
                .child(keyUtils.messageList)
                .child(idChat)
                .limitToLast(limit);

        mRefMessage.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<fb_Message> messageAll = new ArrayList<>();
                //Save to SQLite
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    int id = Integer.parseInt(data.getKey());
                    fb_Message message = data.getValue(fb_Message.class);

                    tb_Message.getInstance(context).addMessage(id,idChat,message);

                    messageAll.add(message);
                    //mView.realTime_listChat();
                }
                result.onResult(messageAll);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                result.onResult(null);
            }
        });
    }

    private interface onDataMessage{
        void onResult(ArrayList<fb_Message> messages);
    }

    @Override
    public void editDetailChat(String idChat, ArrayList<String> members, String chatName) {
        if(members.size() > 0){
            //Show waiting dialog


            final DatabaseReference mRefChat = mDatabase
                    .getReference()
                    .child(keyUtils.chatList)
                    .child(idChat)
                    .child(keyUtils.membersList);

            //Get the latest members list
            mRefChat.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //convert from DataSnapshot to List
                    GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>(){};
                    final List<String> membersLatest = dataSnapshot.getValue(t);

                    final List<String> temp = new ArrayList<>();
                    temp.addAll(membersLatest);

                    //If not in the list, add it
                    for(String Uid : members){
                        if(!membersLatest.contains(Uid))
                            temp.add(Uid);
                    }

                    //Set value
                    mRefChat.setValue(temp)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    if(!chatName.matches("")){
                                        mDatabase
                                                .getReference()
                                                .child(idChat)
                                                .child(keyUtils.chatName)
                                                .setValue(chatName)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        mView.resultOfAction(true,"Success",keyUtils.editDetailMSG);
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        mView.resultOfAction(false,e.getMessage(),keyUtils.editDetailMSG);
                                                        Log.e("CheckApp", e.getMessage());
                                                    }
                                                });
                                    }
                                    //Chat name empty
                                    else
                                        mView.resultOfAction(true,"Success",keyUtils.editDetailMSG);

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mView.resultOfAction(false,e.getMessage(),keyUtils.editDetailMSG);
                                    Log.e("CheckApp", e.getMessage());
                                }
                            });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    mView.resultOfAction(false,databaseError.getMessage(),keyUtils.editDetailMSG);
                    Log.e("CheckApp", databaseError.getMessage());
                }
            });
        }
        //Size = 0 and exists name
        else if(!chatName.matches("")){
            //Show waiting dialog


            mDatabase.getReference()
                    .child(keyUtils.chatList)
                    .child(idChat)
                    .child(keyUtils.chatName)
                    .setValue(chatName)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mView.resultOfAction(true,"Success",keyUtils.editDetailMSG);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            mView.resultOfAction(false,e.getMessage(),keyUtils.editDetailMSG);
                            Log.e("CheckApp", e.getMessage());
                        }
                    });
        }
    }

    /**
     *
     * @param idChat id chat
     * @param idMember id member
     * @param type type of remove (remove or leave group)
     */
    @Override
    public void removeMember(String idChat, final String idMember, String type) {

        final DatabaseReference mRefChat = mDatabase
                .getReference()
                .child(keyUtils.chatList)
                .child(idChat)
                .child(keyUtils.membersList);

        mRefChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //convert from DataSnapshot to List
                GenericTypeIndicator<List<String>> t = new GenericTypeIndicator<List<String>>(){};
                final List<String> membersLatest = dataSnapshot.getValue(t);

                membersLatest.remove(idMember);

                if(membersLatest.size() != 0){
                    mRefChat.setValue(membersLatest)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mView.resultOfAction(true,"Success",type);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mView.resultOfAction(false,e.getMessage(),type);
                                    Log.e("CheckApp", e.getMessage());
                                }
                            });
                }
                //Size = 0, delete chat
                else{
                   deleteChat(idChat, new onResultDeleteChat() {
                       @Override
                       public void onResult(boolean isSuccess, String message) {
                           mView.resultOfAction(isSuccess,message,type);
                       }
                   });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mView.resultOfAction(false,databaseError.getMessage(),type);
                Log.e("CheckApp", databaseError.getMessage());
            }
        });
    }

    /**
     * Delete the chat
     * @param idChat id of chat
     * @param result interface result
     */
    private void deleteChat(String idChat, @NonNull onResultDeleteChat result){
        final DatabaseReference mRefChat = mDatabase
                .getReference()
                .child(keyUtils.chatList)
                .child(idChat);

        mRefChat.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        tb_Chat.getInstance(context).deleteChat(idChat);

                        //Remove list message
                        mDatabase.getReference()
                                .child(keyUtils.messageList)
                                .child(idChat)
                                .removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        tb_Message.getInstance(context).deleteAllMessageByChatID(idChat);
                                        result.onResult(true,"Success");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        result.onResult(false,e.getMessage());
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

    public interface onResultDeleteChat{
        void onResult(boolean isSuccess, String message);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void findChatIDByUid(final String Uid, @NonNull final onResultFindChatIDByUid result){
        final DatabaseReference mRefChat = mDatabase
                .getReference()
                .child(keyUtils.chatList)
                .child(familyID);

        mRefChat.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<objChat> chatDataAll = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    chatDataAll.add(new objChat(data.getRef().getPath().toString().replace("/"+keyUtils.chatList,""), data.getValue(fb_Chat.class)));
                }

                //Get a list of your Uid chats
                final int size = chatDataAll.size();
                for(int i = 0; i < size ; i++){
                    if(chatDataAll.get(i).getMembersList().size() == 2 &&
                            chatDataAll.get(i).getMembersList().contains(objAccount.getCurrentUser().getUid()) &&
                            chatDataAll.get(i).getMembersList().contains(Uid) &&
                            chatDataAll.get(i).getType().matches(keyUtils.PRIVATE))
                    {
                        result.onResult(chatDataAll.get(i).getId(),chatDataAll.get(i),"Success");
                        return;
                    }
                }
                result.onResult("",null,"No message");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                result.onResult("",null,databaseError.getMessage());
            }
        });
    }

    public interface onResultFindChatIDByUid{
        void onResult(String idChat,fb_Chat detailChat, String message);
    }

    public void createNewMessageID(fb_Chat chatDetail , @NonNull onResultMessageID result){

        final DatabaseReference mRefChat = mDatabase.getReference()
                .child(keyUtils.chatList)
                .child(familyID)
                .push();

        mRefChat.setValue(chatDetail)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onSuccess(Void aVoid) {
                        ///chatList/3WJ0NJTeOYcAodxGQWZtVrfgL952/aaiaiweq/-LwwLsp5Q7JzYjhOvU7i
                        result.onResult(mRefChat.getRef().getPath().toString().replace("/"+keyUtils.chatList,""), "Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        result.onResult( "", e.toString());
                    }
                });
    }

    public interface onResultMessageID{
        void onResult(String idChat, String message);
    }

    @Override
    public void setSeenTheMessage(String idChat, String idMessage,  fb_Message message){
        tb_Message.getInstance(context).updateMessage(Integer.parseInt(idMessage),idChat,message);
        final String currentUid = objAccount.getCurrentUser().getUid();
        if(!message.getMembersListSeen().contains(currentUid)){

            List<String> memberListSeen = message.getMembersListSeen();
            memberListSeen.add(currentUid);

            tb_Message.getInstance(context).updateMessage(Integer.parseInt(idMessage),idChat,message);

            mDatabase.getReference()
                    .child(keyUtils.messageList)
                    .child(idChat)
                    .child(idMessage)
                    .child(keyUtils.membersListSeen)
                    .setValue(memberListSeen);
        }
    }

    @Override
    public void setReceivedTheMessage(String idChat, String idMessage,  fb_Message message){
        final String currentUid = objAccount.getCurrentUser().getUid();
        if(!message.getMembersListReceived().contains(currentUid)){
            List<String> memberReceived = message.getMembersListReceived();
            memberReceived.add(currentUid);

            tb_Message.getInstance(context).updateMessage(Integer.parseInt(idMessage),idChat,message);

            mDatabase.getReference()
                    .child(keyUtils.messageList)
                    .child(idChat)
                    .child(idMessage)
                    .child(keyUtils.membersListReceived)
                    .setValue(memberReceived);
        }
    }



}
