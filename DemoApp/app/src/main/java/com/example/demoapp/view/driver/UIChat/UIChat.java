package com.example.demoapp.view.driver.UIChat;

import static com.example.demoapp.Utils.keyUtils.REQUEST_PERMISSION;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Models.objApplication.objChat;
import com.example.demoapp.Models.objApplication.objDetailImage;
import com.example.demoapp.Models.objApplication.objMessage;
import com.example.demoapp.Models.objectFirebase.chat.fb_Chat;
import com.example.demoapp.Presenters.Chat.pre_Chat;
import com.example.demoapp.Presenters.Chat.view_Chat;
import com.example.demoapp.R;
import com.example.demoapp.Utils.directionalController;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.viewUtils;
import com.example.demoapp.adapter.driver.aRclvChat;
import com.example.demoapp.adapter.driver.aRclvUploadImageMsg;
import com.example.demoapp.view.driver.UIMain;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.ArrayList;


public class UIChat extends AppCompatActivity implements view_Chat {
    private final Context context = this;

    private ImageView imvSend,imvSendImage;

    private EditText edtMessage;

    private Toolbar toolbar;

    private RecyclerView rclvChat;
    private RecyclerView rclvImageUpload;

    private aRclvChat adapterChat;

    private aRclvUploadImageMsg adapterUploadImage;

    private String idChat;

    private String activityType;

    private fb_Chat mChat;

    private pre_Chat preChat;

    private LinearLayout lnlProgress;

    private SpinKitView SpinKitLoading;

    private RelativeLayout rltlChat;

    private LinearLayout lnlLoadMore;

    public static Bitmap bmImageCamera;
    public static String previousActivity = "";
    public static ArrayList<String> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        UIMain.isFocusActivityCHAT = true;

        getDataBundle();

        initPermission();

        initView();

        setActionToView();

        setupRclvChat();
    }


    private void setActionToView() {
        imvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!edtMessage.getText().toString().trim().matches("")){
                    preChat.postMessage(idChat,edtMessage.getText().toString().trim(),null,keyUtils.TEXT);
                    lnlProgress.setVisibility(View.VISIBLE);
                    edtMessage.setText("");
                    scrollBottom();
                }
            }
        });

        imvSendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogSelectSendImage();
            }
        });


    }

    private void showDialogSelectSendImage(){
        DialogPlus dialogPlus= DialogPlus.newDialog(this)
                .setContentHolder(new ViewHolder(R.layout.dialog_choose_send_image))
                .setGravity(Gravity.BOTTOM)
                .create();

        TextView tvUploadPhoto = (TextView) dialogPlus.findViewById(R.id.tvUploadPhoto);
        TextView tvTakePhoto = (TextView) dialogPlus.findViewById(R.id.tvTakePhoto);

        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, UICamera.class));
                dialogPlus.dismiss();
            }
        });

        tvUploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, UIChooseImageFromGallery.class));
                dialogPlus.dismiss();
            }
        });

        dialogPlus.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat,menu);
        //If the type is private then hide the menu
        if(mChat.getType().matches(keyUtils.PRIVATE))
            menu.getItem(0).setVisible(false);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.iMore && mChat.getType().matches(keyUtils.GROUP)){
            Bundle bundle = new Bundle();
            bundle.putString(keyUtils.dataIDChat, idChat);
            bundle.putSerializable(keyUtils.dataChat, mChat);

            Intent intent = new Intent(context, com.example.demoapp.view.driver.UIChat.UIMessageGroupDetail.class);
            intent.putExtra(keyUtils.data,bundle);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        preChat = new pre_Chat(context,this);

        viewUtils.setupToolbar(context,R.id.toolbar,R.color.white,mChat != null ? mChat.getChatName() : " ",R.color.colorThemeStatusBarChat);

        toolbar = findViewById(R.id.toolbar);
        rclvChat = findViewById(R.id.rclvChat);
        rclvImageUpload = findViewById(R.id.rclvImageUpload);

        preChat.getMessageList(idChat);

        imvSend = findViewById(R.id.imvSendChat);
        lnlLoadMore = findViewById(R.id.lnlLoadMore);

        imvSendImage = findViewById(R.id.imvSendImage);

        edtMessage = findViewById(R.id.edtMessage);
        lnlProgress = findViewById(R.id.lnlProgress);

        SpinKitLoading = findViewById(R.id.SpinKitLoading);

        rltlChat = findViewById(R.id.rltlChat);

        bmImageCamera = null;
        previousActivity = "";
        imageList = null;


        if(activityType.matches(UINewMessage.TAG))
            onLoad(false);
        else
            onLoad(true);

        setTitleToolbar();

    }

    private void setTitleToolbar(){
        String title = mChat.getChatName();
        if(mChat.getType().matches(keyUtils.PRIVATE) && mChat.getMembersList().size() == 2){
            String Uid_Person = mChat.getMembersList().get(0).matches(objAccount.getCurrentUser().getUid()) ? mChat.getMembersList().get(1) : mChat.getMembersList().get(0);
            title = objAccount.getAccountFromSQLite(context,Uid_Person).getName();
        }
        toolbar.setTitle(title);
    }

    private void setupRclvChat(){

        adapterChat = new aRclvChat(new ArrayList<>(),context, mChat.getMembersList());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setStackFromEnd(true);
        rclvChat.setLayoutManager(layoutManager);
        rclvChat.setAdapter(adapterChat);
        rclvChat.setItemAnimator(new DefaultItemAnimator());

        rclvChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) rclvChat.getLayoutManager();

                if (linearLayoutManager != null && linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    lnlLoadMore.setVisibility(View.VISIBLE);
                    preChat.getMessageLoadMore(idChat, new pre_Chat.interfaceLoadMore() {
                        @Override
                        public void onResult(ArrayList<objMessage> messageList) {
                            if(messageList!=null){
                                final int size = messageList.size();
                                for(int i = 0; i < size; i++){
                                    adapterChat.addItemToPosition(messageList.get(i), i);
                                }
                            }
                            lnlLoadMore.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        adapterChat.setOnClickListener(new aRclvChat.onAction() {
            @Override
            public void actionClickImage(String url, int position) {
                directionalController.goToUIImageViewer(context,idChat,url,adapterChat.getItemMessage(position));
            }

            @Override
            public void actionClickImage(int position) {
                directionalController.goToUIImageViewer(context,idChat,adapterChat.getItemMessage(position).getContent(),adapterChat.getItemMessage(position));
            }
        });


    }


    private void getDataBundle() {
        Bundle bundle = getIntent().getBundleExtra(keyUtils.data);
        if(bundle != null){
            idChat = bundle.getString(keyUtils.dataIDChat,keyUtils.NULL);
            activityType = bundle.getString(keyUtils.dataType,keyUtils.NULL);
            mChat = (fb_Chat) bundle.getSerializable(keyUtils.dataChat);

            if(idChat.matches(keyUtils.NULL) || mChat == null || activityType.matches(keyUtils.NULL))
                finish();

        }else
            finish();
    }

    @Override
    public void resultListChat(ArrayList<objChat> chatList) {

    }

    @Override
    public void resultOfAction(boolean isSuccess, String message, String type) {
        if(type.matches(keyUtils.editDetailMSG)){
            if(isSuccess)
                Toast.makeText(context, R.string.Update_successful, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, R.string.Update_Error, Toast.LENGTH_SHORT).show();
        }

        if(type.matches(keyUtils.getChatDetail) || type.matches(keyUtils.getMessageList)){
            if(!isSuccess){

                setTitleToolbar();

                adapterChat.setMemberList(mChat.getMembersList());
                onLoad(false);
            }
        }
    }

    @Override
    public void resultChatDetail(fb_Chat chats) {
        if(chats != null){
            onLoad(false);

            mChat = chats;

            setTitleToolbar();

            adapterChat.setMemberList(chats.getMembersList());

            if(!mChat.getMembersList().contains(objAccount.getCurrentUser().getUid())){
                Toast.makeText(context, R.string.You_have_been_removed_from_the_chat, Toast.LENGTH_LONG).show();
                finish();
            }
        }
        else{
            finish();
        }


    }

    @Override
    public void resultMessage(objMessage newMessage) {
        lnlProgress.setVisibility(View.GONE);
        rclvImageUpload.setVisibility(View.GONE);

        adapterChat.addItemChat(newMessage.getFireBaseMessage());
        scrollBottom();

        if(UIMain.isFocusActivityCHAT){
            preChat.setSeenTheMessage(newMessage.getIdChat(), newMessage.getId(), newMessage.getFireBaseMessage());
        }

    }

    @Override
    public void resultAllImage(ArrayList<objDetailImage> detailImageList) {

    }

    /**
     * Move to the last item
     */
    private void scrollBottom(){
        rclvChat.post(new Runnable() {
            @Override
            public void run() {
                rclvChat.scrollToPosition(adapterChat.getItemCount()-1);
                onLoad(false);
            }
        });
    }

    /**
     * Show progress and hide layout chat if not finished downloading
     * @param isLoading status of loading or completed loading
     */
    private void onLoad(boolean isLoading){
        if(isLoading){
            SpinKitLoading.setVisibility(View.VISIBLE);
            rltlChat.setVisibility(View.GONE);
        }else{
            SpinKitLoading.setVisibility(View.GONE);
            rltlChat.setVisibility(View.VISIBLE);
        }
    }


    private void initPermission(){
        //init permission camera;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{Manifest.permission.CAMERA }, REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED ) {
                Toast.makeText(context, getResources().getString(R.string.permission_don_not_granted), Toast.LENGTH_SHORT).show();
            }
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @Override
    protected void onPause() {
        super.onPause();
        UIMain.isFocusActivityCHAT = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UIMain.isFocusActivityCHAT = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //UICamera
        if(previousActivity.matches(UICamera.TAG) && bmImageCamera != null){
            //Upload image
            preChat.postMessage(idChat,null,bmImageCamera,keyUtils.PICTURE);
            lnlProgress.setVisibility(View.VISIBLE);
            rclvImageUpload.setVisibility(View.GONE);

            edtMessage.setText("");

            //Reset image bitmap
            bmImageCamera = null;
            previousActivity = "";

            scrollBottom();
        }

        //UIChooseImageFromGallery
        else if(previousActivity.matches(com.example.demoapp.view.driver.UIChat.UIChooseImageFromGallery.TAG) && imageList != null){

            //Upload image list
            lnlProgress.setVisibility(View.GONE);
            rclvImageUpload.setVisibility(View.VISIBLE);

            adapterUploadImage = new aRclvUploadImageMsg(imageList,context);
            rclvImageUpload.setAdapter(adapterUploadImage);
            rclvImageUpload.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));

            final ArrayList<String> urlImageList = new ArrayList<>();
            final int sizeImage = adapterUploadImage.getItemCount();

            for(int i = 0; i < sizeImage; i++){
                //Upload image to serve
                preChat.uploadImageToFolderMessage(i, adapterUploadImage.getItemByPosition(i), new pre_Chat.onResultUploadImage() {
                    @Override
                    public void onResult(boolean isSuccess, String urlImage, int position, String message) {
                        urlImageList.add(urlImage);

                        if(isSuccess)
                            adapterUploadImage.setSuccessUpload(position);
                        else{
                            adapterUploadImage.setFailUpload(position);
                            Log.e("CheckApp", message);
                        }

                        //When the upload is successful, send a message
                        if(sizeImage == urlImageList.size()){
                            ArrayList<String> temp = new ArrayList<>();
                            for(String url : urlImageList){
                                if(!url.matches(keyUtils.NULL))
                                    temp.add(url);
                            }

                            preChat.postMessage(idChat,new Gson().toJson(temp),null,keyUtils.MULTI_PICTURE);

                            imageList = null;
                        }
                    }
                });
            }

            //Reset image list
            previousActivity = "";

            scrollBottom();
        }

        if(!mChat.getMembersList().contains(objAccount.getCurrentUser().getUid())){
            finish();
        }

        UIMain.isFocusActivityCHAT = true;

        preChat.getChatDetail(idChat);
    }


}
