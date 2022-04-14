package com.example.demoapp.adapter.driver;

import static com.example.demoapp.Utils.patternFormatDateTime.MMM_dd_yyyy;
import static com.example.demoapp.Utils.patternFormatDateTime.hh_mm_a;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Models.objApplication.objChat;
import com.example.demoapp.Models.objApplication.objMessage;
import com.example.demoapp.R;
import com.example.demoapp.SQLite.tb_Message;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.timeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;



public class aRclvListChat extends RecyclerView.Adapter<aRclvListChat.ViewHolder> {
    private final String TAG = aRclvListChat.class.getSimpleName();

    private ArrayList<objChat> chatsDetail;
    private Context context;

    private onAction listener;

    public void setOnClickListener(onAction listener){
        this.listener = listener;
    }


    public interface onAction{
        void actionClick(int i);
    }

    public aRclvListChat(ArrayList<objChat> chatsDetail, Context context) {
        this.chatsDetail = new ArrayList<>();
        for(objChat chat : chatsDetail){
            if(chat.getTotalMessage() > 0)
                this.chatsDetail.add(chat);
        }

        sortList();

        this.context = context;
    }

    public void setChatsDetail(ArrayList<objChat> chatsDetail) {
        this.chatsDetail = new ArrayList<>();
        for(objChat chat : chatsDetail){
            if(chat.getTotalMessage()>0)
                this.chatsDetail.add(chat);
        }

        sortList();

    }

    private void sortList(){
        Collections.sort(this.chatsDetail, new Comparator<objChat>() {
            @Override
            public int compare(objChat o1, objChat o2) {
                return (int) (o1.getTimeUpdate() - o2.getTimeUpdate());
            }
        });

        notifyDataSetChanged();
    }

    public ArrayList<objChat> getChatsDetail() {
        return chatsDetail;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_list_chat,parent,false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final objChat item = chatsDetail.get(position);

        try{
            ArrayList<objMessage> messageList = (ArrayList<objMessage>) tb_Message.getInstance(context).getAllMessageByChatID(item.getId().replace("/" + keyUtils.chatList, ""));
            objMessage lastMessage = messageList.get(messageList.size() > 0 ? messageList.size() - 1 : 0);

            //Compare to current date and time to get the time format
            SimpleDateFormat timeFormat = MMM_dd_yyyy;
            if(timeUtils.convertDateTimeToDate(Calendar.getInstance().getTimeInMillis()) == timeUtils.convertDateTimeToDate(lastMessage.getTime()))
                timeFormat = hh_mm_a;
            //Set time
            holder.tvTime.setText(timeUtils
                    .convertMillisecondToStringFormat(lastMessage
                            .getTime(),timeFormat));

            //Get auth detail
            objAccount account = objAccount.getAccountFromSQLite(context,lastMessage.getAuth());

            holder.imvNotSeen.setVisibility(lastMessage.getMembersListSeen().contains(objAccount.getCurrentUser().getUid()) ? View.GONE : View.VISIBLE);

            //set message
            if(lastMessage.getType().matches(keyUtils.TEXT)){
                String laterMessage ="<b>" + account.getName() +": </b>" + lastMessage.getContent();
                holder.tvLaterMessage.setText(Html.fromHtml(laterMessage));
            }else{
                String laterMessage =  account.getName() + context.getResources().getString(R.string.sent_photos);
                holder.tvLaterMessage.setText(Html.fromHtml(laterMessage));
            }
        }catch (Exception e){
            Log.e(TAG, e.toString());
        }

        holder.tvName.setText(item.getChatName());

        //Set visible avatar
        if(item.getMembersList().size() > 2){
            holder.frameLayoutMoreAvatar.setVisibility(View.VISIBLE);
            holder.imvAvatar.setVisibility(View.GONE);

            if (item.getMembersList().size() == 3) {
                holder.imvAvatar1.setVisibility(View.VISIBLE);
                holder.imvAvatar2.setVisibility(View.VISIBLE);
                holder.imvAvatar3.setVisibility(View.VISIBLE);
                holder.tvMore.setVisibility(View.GONE);

                objAccount account1 = objAccount.getAccountFromSQLite(context,item.getMembersList().get(0));
                if(account1 != null){

                    if(account1.getLocalAvatar().matches("")){
                        Glide.with(context).load(account1.getAvatar())
                                .placeholder(R.drawable.no_avatar)
                                .dontAnimate().apply(RequestOptions.circleCropTransform())
                                .into(holder.imvAvatar1);
                    }else{
                        Glide.with(context).load(account1.getLocalAvatar())
                                .placeholder(R.drawable.no_avatar)
                                .dontAnimate().apply(RequestOptions.circleCropTransform())
                                .into(holder.imvAvatar1);
                    }

                }

                objAccount account2 = objAccount.getAccountFromSQLite(context,item.getMembersList().get(1));
                if(account2 != null){

                    if(account2.getLocalAvatar().matches("")){
                        Glide.with(context).load(account2.getAvatar())
                                .placeholder(R.drawable.no_avatar)
                                .dontAnimate().apply(RequestOptions.circleCropTransform())
                                .into(holder.imvAvatar2);
                    }else{
                        Glide.with(context).load(account2.getLocalAvatar())
                                .placeholder(R.drawable.no_avatar)
                                .dontAnimate().apply(RequestOptions.circleCropTransform())
                                .into(holder.imvAvatar2);
                    }
                }

                objAccount account3 = objAccount.getAccountFromSQLite(context,item.getMembersList().get(2));
                if(account3 != null){
                    if(account3.getLocalAvatar().matches("")){
                        Glide.with(context).load(account3.getAvatar())
                                .placeholder(R.drawable.no_avatar)
                                .dontAnimate().apply(RequestOptions.circleCropTransform())
                                .into(holder.imvAvatar3);
                    }else{
                        Glide.with(context).load(account3.getLocalAvatar())
                                .placeholder(R.drawable.no_avatar)
                                .dontAnimate().apply(RequestOptions.circleCropTransform())
                                .into(holder.imvAvatar3);
                    }
                }

            }
            //more than 3
            else {
                holder.imvAvatar1.setVisibility(View.VISIBLE);
                holder.imvAvatar2.setVisibility(View.VISIBLE);
                holder.imvAvatar3.setVisibility(View.VISIBLE);
                holder.tvMore.setVisibility(View.VISIBLE);

                objAccount account1 = objAccount.getAccountFromSQLite(context,item.getMembersList().get(0));
                if(account1 != null){
                    Glide.with(context).load(account1.getLocalAvatar().matches("") ? account1.getAvatar() : account1.getLocalAvatar())
                            .placeholder(R.drawable.no_avatar)
                            .dontAnimate().apply(RequestOptions.circleCropTransform())
                            .into(holder.imvAvatar1);
                }

                objAccount account2 = objAccount.getAccountFromSQLite(context,item.getMembersList().get(1));
                if(account2 != null){
                    Glide.with(context).load(account2.getLocalAvatar().matches("") ? account2.getAvatar() : account2.getLocalAvatar())
                            .placeholder(R.drawable.no_avatar)
                            .dontAnimate().apply(RequestOptions.circleCropTransform())
                            .into(holder.imvAvatar2);
                }

                objAccount account3 = objAccount.getAccountFromSQLite(context,item.getMembersList().get(2));
                if(account3 != null){
                    Glide.with(context).load(account3.getLocalAvatar().matches("") ? account3.getAvatar() : account3.getLocalAvatar())
                            .placeholder(R.drawable.no_avatar)
                            .dontAnimate().apply(RequestOptions.circleCropTransform())
                            .into(holder.imvAvatar3);
                }

                holder.tvMore.setText("+"+(item.getMembersList().size() - 3));

            }

        }else if(item.getMembersList().size() == 2){
            holder.frameLayoutMoreAvatar.setVisibility(View.GONE);
            holder.imvAvatar.setVisibility(View.VISIBLE);
            objAccount account = objAccount.getAccountFromSQLite(context,item.getMembersList().get(0));

            String Uid0 = objAccount.getAccountFromSQLite(context,item.getMembersList().get(0)).getId();
            String currentID = objAccount.getCurrentUser().getUid();

            if(Uid0.matches(currentID))
                account = objAccount.getAccountFromSQLite(context,item.getMembersList().get(1));

            if(account != null){
                Glide.with(context).load(account.getLocalAvatar().matches("") ? account.getAvatar() : account.getLocalAvatar())
                        .placeholder(R.drawable.no_avatar)
                        .dontAnimate().apply(RequestOptions.circleCropTransform())
                        .into(holder.imvAvatar);
                holder.tvName.setText(account.getName());
            }
        }

        holder.lnlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.actionClick(position);
                }
            }
        });

    }
    @Override
    public int getItemCount() {
        return chatsDetail.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView tvTime;
        private TextView tvName;
        private TextView tvLaterMessage;

        private FrameLayout frameLayoutMoreAvatar;

        private ImageView imvAvatar;
        private ImageView imvAvatar1;
        private ImageView imvAvatar2;
        private ImageView imvAvatar3;
        private TextView tvMore;
        private ImageView imvNotSeen;

        private LinearLayout lnlMain;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tvTime);
            lnlMain = itemView.findViewById(R.id.lnlMain);

            imvAvatar = itemView.findViewById(R.id.imvAvatar);
            imvAvatar1 = itemView.findViewById(R.id.imvAvatar1);
            imvAvatar2 = itemView.findViewById(R.id.imvAvatar2);
            imvAvatar3 = itemView.findViewById(R.id.imvAvatar3);
            tvMore = itemView.findViewById(R.id.tvMore);
            imvNotSeen = itemView.findViewById(R.id.imvNotSeen);

            frameLayoutMoreAvatar = itemView.findViewById(R.id.frameMoreAvatar);

            tvName = itemView.findViewById(R.id.tvNameChat);
            tvLaterMessage = itemView.findViewById(R.id.tvLaterMessage);

        }
    }
}
