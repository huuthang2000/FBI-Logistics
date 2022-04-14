package com.example.demoapp.view.driver.UIChat;

import static com.example.demoapp.Utils.patternFormatDateTime.MMM_dd_yyyy_hh_mm_a;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demoapp.adapter.driver.aRclvAllImageMsg;
import com.example.demoapp.Models.objApplication.objChat;
import com.example.demoapp.Models.objApplication.objDetailImage;
import com.example.demoapp.Models.objApplication.objMessage;
import com.example.demoapp.Models.objectFirebase.chat.fb_Chat;
import com.example.demoapp.Models.objectFirebase.chat.fb_Message;
import com.example.demoapp.Presenters.Chat.pre_Chat;
import com.example.demoapp.Presenters.Chat.view_Chat;
import com.example.demoapp.R;
import com.example.demoapp.SQLite.tb_Account;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.timeUtils;
import com.example.demoapp.Utils.viewUtils;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;



public class UIViewImageMessage extends AppCompatActivity implements view_Chat {
    private final Context context = this;
    private pre_Chat preChat;

    private RecyclerView rclvAllImageMessage;


    private TextView tvUsername;
    private TextView tvTimeCreate;
    private PhotoView photoView;

    private String urlImage;
    private fb_Message mMessage;
    private String idChat;


    private ArrayList<objDetailImage> detailImageList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image_message);
        getDataBundle();

        initView();

        setDataToView(urlImage, tb_Account.getInstance(context).getAccountByID(mMessage.getAuth()).getName(),mMessage.getTime());

    }

    private void setDataToView(String url, String username, long timeCreate) {
        Glide.with(context).load(url)
                .placeholder(R.color.black)
                .error(R.drawable.no_image)
                .into(photoView);

        tvUsername.setText(username);
        tvTimeCreate.setText(timeUtils.convertMillisecondToStringFormat(timeCreate,MMM_dd_yyyy_hh_mm_a));
    }

    private void getDataBundle() {
        Bundle bundle = getIntent().getBundleExtra(keyUtils.data);
        if(bundle != null){
            idChat = bundle.getString(keyUtils.dataIDChat,keyUtils.NULL);
            urlImage = bundle.getString(keyUtils.dataURLImage,keyUtils.NULL);
            mMessage = (fb_Message) bundle.getSerializable(keyUtils.dataMessage);

            if(idChat.matches(keyUtils.NULL) || urlImage.matches(keyUtils.NULL) || mMessage == null)
                finish();

        }else
            finish();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initView() {
        findViewById(R.id.imvBack)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        viewUtils.setColorStatusBar((AppCompatActivity) context,R.color.black);

        photoView = findViewById(R.id.imageView);
        rclvAllImageMessage = findViewById(R.id.rclvAllImageMessage);
        tvUsername = findViewById(R.id.tvUsername);
        tvTimeCreate = findViewById(R.id.tvTimeCreate);
        preChat = new pre_Chat(context,this);
        preChat.getAllImageFromMessage(idChat);

        detailImageList = new ArrayList<>();

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
    public void resultAllImage(final ArrayList<objDetailImage> detailImageList) {
        this.detailImageList = detailImageList;
        aRclvAllImageMsg adapter = new aRclvAllImageMsg(detailImageList,context);
        rclvAllImageMessage.setAdapter(adapter);
        rclvAllImageMessage.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
        rclvAllImageMessage.setHasFixedSize(true);
        adapter.setOnClickListener(new aRclvAllImageMsg.actionClick() {
            @Override
            public void onClick(int position) {
                objDetailImage detailImage = detailImageList.get(position);
                setDataToView(detailImage.getUrlImage(),detailImage.getUsername(),detailImage.getTimeCreate());
            }
        });
    }


}
