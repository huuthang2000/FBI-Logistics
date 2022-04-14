package com.example.demoapp.view.driver.UIChat;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
import com.example.demoapp.Utils.directionalController;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.viewUtils;
import com.example.demoapp.adapter.driver.aRclvAvatarChecked;
import com.example.demoapp.adapter.driver.aRclvChatMember;
import com.kongzue.dialog.interfaces.OnInputDialogButtonClickListener;
import com.kongzue.dialog.util.BaseDialog;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.v3.InputDialog;

import java.util.ArrayList;



public class UINewMessage extends AppCompatActivity implements view_Chat {
    private final Context context = this;
    public static final String TAG = UINewMessage.class.getSimpleName();

    private RecyclerView rclvAllMember;
    private RecyclerView rclvMemberListChecked;

    private aRclvChatMember adapterMemberList;

    private Button btnChat;

    private ArrayList<String> listMemberAll;
    private ArrayList<String> listMemberAfterSearch;
    private ArrayList<String> listMemberChecked;

    private SearchView searchName;

    private pre_Chat preChat;

    private aRclvAvatarChecked adapterAvatarChecked;

    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        getDataBundle();
        setLayoutTop();

        initView();

        setupRclvAllMember(listMemberAll);

        searchName.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listMemberAfterSearch = new ArrayList<>();
                if(newText.matches(""))
                    listMemberAfterSearch = listMemberAll;
                else{
                    for(String s : listMemberAll){
                        if(objAccount.getAccountFromSQLite(context,s).getName().indexOf(newText) > 0){
                            listMemberAfterSearch.add(s);
                        }
                    }
                }

                setupRclvAllMember(listMemberAfterSearch);
                return false;
            }
        });

        if(type.matches(keyUtils.NewGroup)){
            setupRclvMemberChecked();
            btnChat.setVisibility(View.VISIBLE);
        }else
            btnChat.setVisibility(View.GONE);

    }

    private void getDataBundle() {
        Bundle bundle = getIntent().getBundleExtra(keyUtils.data);
        if(bundle!=null){
            type = bundle.getString(keyUtils.dataType,"");
            if(type.matches(""))
                finish();
        }else
            finish();
    }

    private void initView() {
        rclvAllMember = findViewById(R.id.rclvAllMember);
        rclvMemberListChecked = findViewById(R.id.rclvMemberListChecked);
        searchName = findViewById(R.id.searchName);
        btnChat = findViewById(R.id.btnChat);

        preChat = new pre_Chat(context,this);

        listMemberAll = new ArrayList<>();
        listMemberAfterSearch = new ArrayList<>();

        listMemberAll = (ArrayList<String>) objFamily.getMyFamily(context).getMembersList();
        //Remove yourself
        listMemberAll.remove(objAccount.getCurrentUser().getUid());

        listMemberChecked = new ArrayList<>();

        //Action create chat
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listMemberChecked.size() > 0){
                    //Show dialog name input
                    InputDialog.show((AppCompatActivity) context, getResources().getString(R.string.Enter_the_name_of_the_chat), null, getResources().getString(R.string.ok), getResources().getString(R.string.Cancel))
                            .setOnOkButtonClickListener(new OnInputDialogButtonClickListener() {
                                @Override
                                public boolean onClick(BaseDialog baseDialog, View v, String inputStr) {
                                    if(!inputStr.matches("") ){

                                        //set member list
                                        ArrayList<String> memberList = new ArrayList<>();
                                        memberList.addAll(adapterAvatarChecked.getAccountIDList());
                                        memberList.add(objAccount.getCurrentUser().getUid());
                                        //set detail chat
                                        fb_Chat detailChat = new fb_Chat(inputStr,
                                                memberList,
                                                keyUtils.GROUP);

                                        //Create new message id
                                        preChat.createNewMessageID(detailChat,new pre_Chat.onResultMessageID() {
                                            @Override
                                            public void onResult(String idChat, String message) {
                                                if(!idChat.matches("")){
                                                    directionalController.goToUIChat(context,idChat, TAG , detailChat);
                                                    finish();
                                                }else
                                                    Toast.makeText(UINewMessage.this, message, Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        return false;
                                    }else{
                                        Toast.makeText(context, R.string.The_value_is_not_valid, Toast.LENGTH_SHORT).show();
                                        return true;
                                    }
                                }
                            })
                            .setInputInfo(new InputInfo().setInputType(InputType.TYPE_CLASS_TEXT));
                }

            }
        });
    }

    private void setupRclvAllMember(final ArrayList<String> idAccountList){
        final aRclvChatMember adapterAllMember = new aRclvChatMember(idAccountList, context);
        rclvAllMember.setAdapter(adapterAllMember);
        rclvAllMember.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));

        adapterAllMember.setOnClickListener(new aRclvChatMember.actionClick() {
            @Override
            public void onClick(int position) {

                if(type.matches(keyUtils.NewGroup)){
                    if(!adapterAvatarChecked.getAccountIDList().contains(idAccountList.get(position)))
                        adapterAvatarChecked.addItem(idAccountList.get(position));

                }else{
                    preChat.findChatIDByUid(adapterAllMember.getItemWithPosition(position), new pre_Chat.onResultFindChatIDByUid() {
                        @Override
                        public void onResult(String idChat, fb_Chat detailChat,String message) {
                            //Chat already exists
                            if(!idChat.matches("") && detailChat != null){
                                directionalController.goToUIChat(context,idChat, TAG , detailChat);
                                finish();
                            }
                            //Chat does not exist
                            else{
                                //set member list
                                ArrayList<String> memberList = new ArrayList<>();
                                memberList.add(objAccount.getCurrentUser().getUid());
                                memberList.add(adapterAllMember.getItemWithPosition(position));
                                //Set chat detail
                                final fb_Chat newChatDetail = new fb_Chat(objAccount.getAccountFromSQLite(context,adapterAllMember.getItemWithPosition(position)).getName(),
                                        memberList,
                                        keyUtils.PRIVATE);

                                //Create new message ID
                                preChat.createNewMessageID(newChatDetail,new pre_Chat.onResultMessageID() {
                                    @Override
                                    public void onResult(String idChat, String message) {

                                        if(!idChat.matches("")){
                                            directionalController.goToUIChat(context,idChat, TAG , newChatDetail);
                                            finish();
                                        }else
                                            Toast.makeText(UINewMessage.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }

            }
        });
    }


    private void setupRclvMemberChecked(){
        adapterAvatarChecked = new aRclvAvatarChecked(listMemberChecked, context);
        rclvMemberListChecked.setAdapter(adapterAvatarChecked);
        rclvMemberListChecked.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
    }

    private void setLayoutTop() {
        //Set transparent status bar and padding status bar
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
        linearParams.setMargins(0, viewUtils.getStatusBarHeight((AppCompatActivity) context), 0, 0);
        findViewById(R.id.lnlTop).setPadding(0, viewUtils.getStatusBarHeight((AppCompatActivity) context), 0, 0);
        viewUtils.setTransparentStatusBar((AppCompatActivity) context);
        viewUtils.setupToolbar(context,R.id.toolbar,R.color.white,getResources().getString(R.string.New_chat),-1);
    }


    @Override
    public void resultListChat(ArrayList<objChat> chatList) {

    }

    @Override
    public void resultOfAction(boolean isSuccess, String message, String type) {

    }

    @Override
    public void resultChatDetail(fb_Chat chats) {

    }

    @Override
    public void resultMessage(objMessage newMessage) {

    }

    @Override
    public void resultAllImage(ArrayList<objDetailImage> detailImageList) {

    }

}
