package com.example.demoapp.view.driver.UIChat;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Models.objApplication.objChat;
import com.example.demoapp.Models.objApplication.objDetailImage;
import com.example.demoapp.Models.objApplication.objFamily;
import com.example.demoapp.Models.objApplication.objMessage;
import com.example.demoapp.Models.objectFirebase.chat.fb_Chat;
import com.example.demoapp.Presenters.Chat.pre_Chat;
import com.example.demoapp.Presenters.Chat.view_Chat;
import com.example.demoapp.R;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.viewUtils;
import com.example.demoapp.adapter.driver.aRclvChatMemberCheck;
import com.example.demoapp.adapter.driver.aRclvChatMemberRemove;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.kongzue.dialog.interfaces.OnDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.DialogSettings;
import com.kongzue.dialog.v3.MessageDialog;

import java.util.ArrayList;
import java.util.Objects;


public class UIMessageGroupDetail extends AppCompatActivity implements view_Chat {

    private final Context context = this;

    private String idChat;

    private fb_Chat mChat;

    private pre_Chat preChat;

    private TextView tvAddMembers;

    private TextInputLayout tilNameGroup;

    private RecyclerView rclvMemberList;

    private RecyclerView rclvMemberListAdd;

    private aRclvChatMemberRemove adapterChatMember;

    private ArrayList<String> memberListChatAdd;

    private Button btnSave;

    private TextView tvLeaveGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_group_detail);
        getDataBundle();

        initView();

        setDataToView();

        setupTextViewLeaveGroup();

        tvAddMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogChooseMember();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName = tilNameGroup.getEditText().getText().toString().trim();
                if(tilNameGroup.getEditText().getText().toString().trim().matches(mChat.getChatName()))
                    newName = "";

                if(!newName.matches("") || memberListChatAdd.size() > 0)
                    preChat.editDetailChat(idChat, memberListChatAdd, newName);
                else
                    finish();

            }
        });
    }

    private void setupTextViewLeaveGroup() {
        tvLeaveGroup.setVisibility(mChat.getMembersList().size() == 2 && mChat.getMembersList().contains(objAccount.getCurrentUser().getUid()) ? View.GONE : View.VISIBLE);

        tvLeaveGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Show confirm dialog
             MessageDialog.build((AppCompatActivity) context)
                        .setStyle(DialogSettings.STYLE.STYLE_MATERIAL)
                        .setTheme(DialogSettings.THEME.LIGHT)
                        .setTitle(R.string.Leave_group)
                        .setMessage(R.string.Do_you_want_to_leave_the_group)
                        .setOkButton(R.string.YES, new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                preChat.removeMember(idChat, objAccount.getCurrentUser().getUid(), keyUtils.leaveGroup);
                                return false;
                            }
                        })
                        .setCancelButton(R.string.NO, new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                baseDialog.doDismiss();
                                return false;
                            }
                        })
                        .show();
            }
        });
    }

    private void setDataToView() {
        tilNameGroup.getEditText().setText(mChat.getChatName());
    }

    private void initView() {
        viewUtils.setupToolbar(context,R.id.toolbar,R.color.white,getResources().getString(R.string.Details_of_the_chat),R.color.colorThemePurple);
        preChat = new pre_Chat(context,this);
        tilNameGroup = findViewById(R.id.tilNameGroup);

        tvLeaveGroup = findViewById(R.id.tvLeaveGroup);

        btnSave = findViewById(R.id.btnSave);

        rclvMemberList = findViewById(R.id.rclvMemberList);
        rclvMemberListAdd = findViewById(R.id.rclvMemberListAdd);
        tvAddMembers = findViewById(R.id.tvAddMembers);

        preChat.getChatDetail(idChat);

        Log.d("idchat" , idChat);

        memberListChatAdd = new ArrayList<>();
    }

    private void getDataBundle() {
        Bundle bundle = getIntent().getBundleExtra(keyUtils.data);
        if(bundle != null){
            idChat = bundle.getString(keyUtils.dataIDChat,keyUtils.NULL);
            mChat = (fb_Chat) bundle.getSerializable(keyUtils.dataChat);

            if(idChat.matches(keyUtils.NULL) || mChat == null)
                finish();

        }else
            finish();
    }

    private void setupRclvMemberList(ArrayList<String> idAccountList){
        adapterChatMember = new aRclvChatMemberRemove(idAccountList,context);
        rclvMemberList.setAdapter(adapterChatMember);
        rclvMemberList.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        rclvMemberList.setHasFixedSize(true);

        //Action remove
        adapterChatMember.setOnClickListener(new aRclvChatMemberRemove.actionClick() {
            @Override
            public void onClick(int position) {
              //  Show confirm dialog
                MessageDialog.build((AppCompatActivity) context)
                        .setStyle(DialogSettings.STYLE.STYLE_MATERIAL)
                        .setTheme(DialogSettings.THEME.LIGHT)
                        .setTitle(R.string.Remove_members)
                        .setMessage(R.string.You_want_to_remove_members)
                        .setOkButton(R.string.YES, new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                preChat.removeMember(idChat, adapterChatMember.getItemWithPosition(position), keyUtils.removeMember);
                                return false;
                            }
                        })
                        .setCancelButton(R.string.NO, new OnDialogButtonClickListener() {
                            @Override
                            public boolean onClick(BaseDialog baseDialog, View v) {
                                baseDialog.doDismiss();
                                return false;
                            }
                        })
                        .show();

            }
        });
    }

    private void setupRclvMemberListAdd(ArrayList<String> idAccountList){
        final aRclvChatMemberRemove adapter = new aRclvChatMemberRemove(idAccountList,context);
        rclvMemberListAdd.setAdapter(adapter);
        rclvMemberListAdd.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));
        rclvMemberListAdd.setHasFixedSize(true);

        //Action remove
        adapter.setOnClickListener(new aRclvChatMemberRemove.actionClick() {
            @Override
            public void onClick(int position) {
                adapter.removeItemWithPosition(position);
            }
        });
    }

    @Override
    public void resultListChat(ArrayList<objChat> chatList) {

    }

    @Override
    public void resultOfAction(boolean isSuccess, String message, String type) {
        //Edit detail message
        if(type.matches(keyUtils.editDetailMSG)){
            if(isSuccess){
                memberListChatAdd = new ArrayList<>();
                setupRclvMemberListAdd(memberListChatAdd);
                Toast.makeText(context, R.string.Update_successful, Toast.LENGTH_SHORT).show();
            }
            else
                Toast.makeText(context, R.string.Update_Error, Toast.LENGTH_SHORT).show();
        }
        //Remove member
        else if(type.matches(keyUtils.removeMember)){
            if(isSuccess)
                Toast.makeText(context, R.string.Remove_successful, Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, R.string.Remove_error, Toast.LENGTH_SHORT).show();
        }
        //Leave group
        else if(type.matches(keyUtils.leaveGroup)){
            if(isSuccess){
                Toast.makeText(context, R.string.Success, Toast.LENGTH_SHORT).show();
                finish();
            }
            else
                Toast.makeText(context, R.string.Leaving_group_failed, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void resultChatDetail(fb_Chat chats) {
        if(chats!=null){
            mChat = chats;
            ArrayList<String> memberList = (ArrayList<String>) chats.getMembersList();
            memberList.remove(objAccount.getCurrentUser().getUid());

            setupRclvMemberList(memberList);
        }else
            finish();
    }

    @Override
    public void resultMessage(objMessage newMessage) {

    }


    @Override
    public void resultAllImage(ArrayList<objDetailImage> detailImageList) {

    }

    private void showDialogChooseMember(){

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_choose_member_add);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);

        ArrayList<String> membersFamily = (ArrayList<String>) objFamily.getMyFamily(context).getMembersList();
        Log.d("CheckApp", new Gson().toJson(membersFamily));
        //The list of members is not part of the chat
        ArrayList<String> members = new ArrayList<>();

        //get the member list is not part of the chat
        for(String id : membersFamily){
            if(!mChat.getMembersList().contains(id))
                members.add(id);
        }
        //Remove current id
        members.remove(objAccount.getCurrentUser().getUid());

        RecyclerView rclvAllMember = dialog.findViewById(R.id.rclvAllMember);
        final aRclvChatMemberCheck adapter = new aRclvChatMemberCheck(members, memberListChatAdd, context);
        rclvAllMember.setAdapter(adapter);
        rclvAllMember.setHasFixedSize(true);
        rclvAllMember.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));

        //Cancel
        dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //OK
        dialog.findViewById(R.id.btnOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                memberListChatAdd = adapter.getIdAccountListChecked();
                setupRclvMemberListAdd(memberListChatAdd);
                dialog.dismiss();
            }
        });

        dialog.show();
        Objects.requireNonNull(dialog.getWindow()).setLayout((int)(context.getResources().getDisplayMetrics().widthPixels * 0.9f),
                (int)(context.getResources().getDisplayMetrics().heightPixels * 0.8f));
    }
}
