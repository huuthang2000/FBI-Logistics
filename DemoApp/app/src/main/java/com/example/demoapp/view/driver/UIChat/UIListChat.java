package com.example.demoapp.view.driver.UIChat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.adapter.driver.aRclvListChat;
import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Models.objApplication.objChat;
import com.example.demoapp.Models.objApplication.objDetailImage;
import com.example.demoapp.Models.objApplication.objFamily;
import com.example.demoapp.Models.objApplication.objMessage;
import com.example.demoapp.Models.objectFirebase.chat.fb_Chat;
import com.example.demoapp.Presenters.Chat.pre_Chat;
import com.example.demoapp.Presenters.Chat.view_Chat;
import com.example.demoapp.R;
import com.example.demoapp.SQLite.tb_Chat;
import com.example.demoapp.Utils.directionalController;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.viewUtils;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;




public class UIListChat extends AppCompatActivity implements view_Chat {

    private final Context context = this;
    public static final String TAG = UIListChat.class.getSimpleName();

    private RecyclerView rclvListChat;
    private aRclvListChat adapterListChat;

    private ArrayList<objChat> chatList;

    private TextView tvFamilyName;

    private pre_Chat mChat;

    private FloatingActionButton fabNewMessage;
    private FloatingActionButton fabNewGroupMessage;
    private FloatingActionMenu fabMenuNewMessage;

    private FrameLayout frlMainChat;
    private SpinKitView SpinKitLoading;
    private TextView tvNoDataToDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_chat);

        setLayoutTop();

        initView();
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
        findViewById(R.id.imvBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        rclvListChat = findViewById(R.id.rclvListChat);
        tvFamilyName = findViewById(R.id.tvFamilyName);
        frlMainChat = findViewById(R.id.frlMainChat);
        SpinKitLoading = findViewById(R.id.SpinKitLoading);
        tvNoDataToDisplay = findViewById(R.id.tvNoDataToDisplay);

        fabNewMessage = findViewById(R.id.fabNewMessage);
        fabNewGroupMessage = findViewById(R.id.fabNewGroupMessage);
        fabMenuNewMessage = findViewById(R.id.fabMenuNewMessage);

        mChat = new pre_Chat(context,this);

        tvFamilyName.setText(objFamily.getMyFamily(context).getCommonName());


        fabNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionalController.goToUINewMessage(context,keyUtils.NewMessage);
                fabMenuNewMessage.close(true);
            }
        });

        fabNewGroupMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                directionalController.goToUINewMessage(context,keyUtils.NewGroup);
                fabMenuNewMessage.close(true);
            }
        });

        setupRclvListChat();


    }

    private void setupRclvListChat(){
        ArrayList<objChat> chats = (ArrayList<objChat>) tb_Chat.getInstance(context).getAllChatByUid(objAccount.getCurrentUser().getUid());
        chatList = new ArrayList<>();
        for(objChat c : chats){
            if(c.getTotalMessage()>0)
                chatList.add(c);
        }
        if(chatList.size() > 0)
            onLoad(false,chatList.size());
        else
            onLoad(true,0);

        adapterListChat = new aRclvListChat(chatList,context);
        rclvListChat.setAdapter(adapterListChat);
        rclvListChat.setLayoutManager(new LinearLayoutManager(context,RecyclerView.VERTICAL,false));

        //Hide FloatingActionButton on scroll of RecyclerView
        rclvListChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) {
                    // Scroll Down
                    if (!fabMenuNewMessage.isMenuButtonHidden())
                        fabMenuNewMessage.hideMenuButton(true);
                }
                else if (dy <0) {
                    // Scroll Up
                    if (fabMenuNewMessage.isMenuButtonHidden())
                        fabMenuNewMessage.showMenuButton(true);
                }
            }
        });

        adapterListChat.setOnClickListener(new aRclvListChat.onAction() {
            @Override
            public void actionClick(int i) {
                fabMenuNewMessage.close(true);
                directionalController.goToUIChat(context,adapterListChat.getChatsDetail().get(i).getId(), TAG , adapterListChat.getChatsDetail().get(i).getFireBaseChat());
            }
        });

        mChat.getAllListChatOfUid();
    }

    /**
     * Show progress and hide layout chat if not finished downloading
     * @param isLoading status of loading or completed loading
     */
    private void onLoad(boolean isLoading, int size){
        if(isLoading){
            SpinKitLoading.setVisibility(View.VISIBLE);
            frlMainChat.setVisibility(View.GONE);
            tvNoDataToDisplay.setVisibility(View.GONE);
        }else{
            tvNoDataToDisplay.setVisibility(size == 0 ? View.VISIBLE : View.GONE);
            SpinKitLoading.setVisibility(View.GONE);
            frlMainChat.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void resultListChat(ArrayList<objChat> chatList) {
        this.chatList = chatList;
        adapterListChat.setChatsDetail(chatList);
        onLoad(false,adapterListChat.getChatsDetail().size());
    }

    @Override
    public void resultOfAction(boolean isSuccess, String message, String type) {
        Log.e(TAG, message);
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

    @Override
    protected void onResume() {
        super.onResume();
        adapterListChat.notifyDataSetChanged();
    }

}
