package com.example.demoapp.adapter.driver;

import static com.example.demoapp.Utils.patternFormatDateTime.MMM_dd_yyyy_hh_mm_a;
import static com.example.demoapp.Utils.patternFormatDateTime.hh_mm_a;


import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.elyeproj.loaderviewlibrary.LoaderTextView;
import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.Models.objectFirebase.chat.fb_Message;
import com.example.demoapp.R;
import com.example.demoapp.SQLite.tb_Account;
import com.example.demoapp.Utils.keyUtils;
import com.example.demoapp.Utils.timeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class aRclvChat extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<fb_Message> messageList;
    private List<String> memberList;
    private final long ONE_MINUTE = 60000;
    private Context context;

    private static final int MSG_TYPE_RIGHT = 0;
    private static final int MSG_TYPE_LEFT = 1;
    private static final int LOAD_MORE = 2;

    private onAction listener;

    public void setOnClickListener(onAction listener){
        this.listener = listener;
    }

    public interface onAction{
        void actionClickImage(String url, int position);
        void actionClickImage(int position);
    }

    public void addItemChat(fb_Message message){
        messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
//        if(position>0)
//            notifyItemChanged(position-1);
    }

    public void addItemToPosition(fb_Message message, int position){
        messageList.add(position,message);
        notifyItemInserted(position);
    }

    public void setMessageList(List<fb_Message> messageList) {
        this.messageList = messageList;
        notifyItemRangeChanged(0,messageList.size()-1);
    }

    public fb_Message getItemMessage(int position){
        return messageList.get(position);
    }

    public void setMemberList(List<String> memberList){
        this.memberList = memberList;
        notifyDataSetChanged();
    }

    public aRclvChat(List<fb_Message> messageList, Context context, List<String> memberList) {
        this.messageList = messageList;
        this.context = context;
        this.memberList = memberList;
    }

    public List<fb_Message> getMessageList() {
        return messageList;
    }

    @Override
    public int getItemViewType(int position) {
        if(messageList.get(position).getAuth().matches(objAccount.getCurrentUser().getUid()))
            return MSG_TYPE_RIGHT;
        else
            return MSG_TYPE_LEFT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == MSG_TYPE_RIGHT) {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_message_right, parent, false);
            return new ViewHolderRight(view);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_message_left, parent, false);
            return new ViewHolderLeft(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {

        final fb_Message item = messageList.get(position);

        LinearLayout.LayoutParams lp = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 40, 0, 0);

        //View holder right
        if(getItemViewType(position) == MSG_TYPE_RIGHT){

            final ViewHolderRight viewHolder = (ViewHolderRight) holder;

            //Set time
            String timeFormat;
            if(timeUtils.convertDateTimeToDate(item.getTime()) == timeUtils.convertDateTimeToDate(Calendar.getInstance().getTimeInMillis()))
                timeFormat = timeUtils.convertMillisecondToStringFormat(item.getTime(),hh_mm_a);
            else
                timeFormat = timeUtils.convertMillisecondToStringFormat(item.getTime(), MMM_dd_yyyy_hh_mm_a);
            viewHolder.tvTime.setText(timeFormat);

            //MULTI_PICTURE
            if(item.getType().trim().toLowerCase().matches(keyUtils.MULTI_PICTURE)){

                ArrayList<String> imageList = new Gson().fromJson(item.getContent(), new TypeToken<ArrayList<String>>(){}.getType());

                //Only one image in the list will display the Picture layout
                if(imageList.size() == 1){
                    //Set up picture right
                    setupTypePictureRight(viewHolder,imageList.get(0),position, keyUtils.MULTI_PICTURE);
                }else {
                    viewHolder.lnlPicture.setVisibility(View.GONE);
                    viewHolder.lnlMessage.setVisibility(View.GONE);
                    viewHolder.lnlMultiPicture.setVisibility(View.VISIBLE);
                    aMultiPicture adapter = new aMultiPicture(context, imageList, position);
                    viewHolder.gvMultiPicture.setAdapter(adapter);
                }

            }
            //Picture
            else if(item.getType().trim().toLowerCase().matches(keyUtils.PICTURE)){
                //Set up picture right
                setupTypePictureRight(viewHolder,item.getContent(),position, keyUtils.PICTURE);
            }
            //Text
            else{
                viewHolder.lnlPicture.setVisibility(View.GONE);
                viewHolder.lnlMessage.setVisibility(View.VISIBLE);
                viewHolder.lnlMultiPicture.setVisibility(View.GONE);
                viewHolder.tvMessage.setText(item.getContent());
            }

            viewHolder.lnlName.setVisibility(View.VISIBLE);

            //Set margin layout main
            viewHolder.rltlMain.setLayoutParams(lp);

            //Hide time of the chat
            viewHolder.rltlTimeGroup.setVisibility(View.GONE);

            //setup layout multiple messages in a minute and show time of message group
            if(messageList.size() > 0){
                int previousPosition = position - 1;
                if(previousPosition < 0)
                    previousPosition = 0;

                //Set the margin of the message in 1 minute
                if(position != previousPosition &&
                        item.getTime() - messageList.get(previousPosition).getTime() <= ONE_MINUTE &&
                        messageList.get(previousPosition).getAuth().matches(item.getAuth())){

                    //Set margin layout main
                    lp.setMargins(0, 4, 0, 0);
                    viewHolder.rltlMain.setLayoutParams(lp);
                    viewHolder.lnlName.setVisibility(View.GONE);
                }

                //Show time of message group
                if(position != previousPosition &&
                        timeUtils.convertDateTimeToDate(item.getTime()) != timeUtils.convertDateTimeToDate(messageList.get(previousPosition).getTime())){

                    viewHolder.rltlTimeGroup.setVisibility(View.VISIBLE);
                    viewHolder.tvTimeGroup.setText(timeUtils.convertMillisecondToStringFormat(item.getTime(),MMM_dd_yyyy_hh_mm_a));

                }else if(position == 0){
                    viewHolder.rltlTimeGroup.setVisibility(View.VISIBLE);
                    viewHolder.tvTimeGroup.setText(timeUtils.convertMillisecondToStringFormat(item.getTime(),MMM_dd_yyyy_hh_mm_a));

                }
            }
        }

        //View holder left
        else {

            final ViewHolderLeft viewHolder = (ViewHolderLeft) holder;

            Glide.with(context).load(R.color.colorLine)
                    .error(R.color.colorTextLight)
                    .into(viewHolder.imvAvatar);

            //Load image Avatar
            objAccount detailAccount = tb_Account.getInstance(context).getAccountByID(item.getAuth());
            if(detailAccount!=null){

                //If local Avatar does not exist then get the avatar URL
                Glide.with(context)
                        .load(detailAccount.getLocalAvatar().matches("") ? detailAccount.getAvatar() : detailAccount.getLocalAvatar())
                        .placeholder(R.color.colorLine)
                        .error(R.drawable.no_avatar)
                        .dontAnimate().apply(RequestOptions.circleCropTransform())
                        .into(viewHolder.imvAvatar);

                //If the member is deleted, dash
                if(!memberList.contains(detailAccount.getId()))
                    viewHolder.tvName.setPaintFlags(viewHolder.tvName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                viewHolder.tvName.setText(detailAccount.getName());
            }

            //Set time
            String timeFormat = "";
            if(timeUtils.convertDateTimeToDate(item.getTime()) == timeUtils.convertDateTimeToDate(Calendar.getInstance().getTimeInMillis()))
                timeFormat = timeUtils.convertMillisecondToStringFormat(item.getTime(),hh_mm_a);
            else
                timeFormat = timeUtils.convertMillisecondToStringFormat(item.getTime(), MMM_dd_yyyy_hh_mm_a);
            viewHolder.tvTime.setText(timeFormat);

            //Multi picture
            if(item.getType().trim().toLowerCase().matches(keyUtils.MULTI_PICTURE)){

                ArrayList<String> imageList = new Gson().fromJson(item.getContent(), new TypeToken<ArrayList<String>>(){}.getType());

                //Only one image in the list will display the Picture layout
                if(imageList.size() == 1){
                    //Set up picture left
                    setupTypePictureLeft(viewHolder,imageList.get(0),position, keyUtils.MULTI_PICTURE);
                }else {
                    viewHolder.lnlPicture.setVisibility(View.GONE);
                    viewHolder.lnlMessage.setVisibility(View.GONE);
                    viewHolder.lnlMultiPicture.setVisibility(View.VISIBLE);
                    aMultiPicture adapter = new aMultiPicture(context, imageList, position);
                    viewHolder.gvMultiPicture.setAdapter(adapter);
                }

            }
            //Picture
            else if(item.getType().trim().toLowerCase().matches(keyUtils.PICTURE)){

                //Set up picture left
                setupTypePictureLeft(viewHolder,item.getContent(),position, keyUtils.PICTURE);

            }
            //Text
            else{
                viewHolder.lnlPicture.setVisibility(View.GONE);
                viewHolder.lnlMultiPicture.setVisibility(View.GONE);
                viewHolder.lnlMessage.setVisibility(View.VISIBLE);
                viewHolder.tvMessage.setText(item.getContent());
            }

            viewHolder.imvAvatar.setVisibility(View.VISIBLE);
            viewHolder.lnlName.setVisibility(View.VISIBLE);

            //Set margin layout main
            viewHolder.frameMain.setLayoutParams(lp);

            viewHolder.rltlTimeGroup.setVisibility(View.GONE);

            //setup layout multiple messages in a minute and show time of message group
            if(messageList.size() > 0){

                int previousPosition = position - 1;
                if(previousPosition < 0)
                    previousPosition = 0;
                //Set the margin of the message in 1 minute
                if(position != previousPosition &&
                        item.getTime() - messageList.get(previousPosition).getTime() <= ONE_MINUTE &&
                        messageList.get(previousPosition).getAuth().matches(item.getAuth())){

                    //Set margin layout main
                    lp.setMargins(0, 4, 0, 0);
                    viewHolder.frameMain.setLayoutParams(lp);
                    viewHolder.imvAvatar.setVisibility(View.GONE);
                    viewHolder.lnlName.setVisibility(View.GONE);
                }

                //Show time of message group
                if(position != previousPosition &&
                        timeUtils.convertDateTimeToDate(item.getTime()) != timeUtils.convertDateTimeToDate(messageList.get(previousPosition).getTime())){

                    viewHolder.rltlTimeGroup.setVisibility(View.VISIBLE);
                    viewHolder.tvTimeGroup.setText(timeUtils.convertMillisecondToStringFormat(item.getTime(),MMM_dd_yyyy_hh_mm_a));
                }else if(position == 0){
                    viewHolder.rltlTimeGroup.setVisibility(View.VISIBLE);
                    viewHolder.tvTimeGroup.setText(timeUtils.convertMillisecondToStringFormat(item.getTime(),MMM_dd_yyyy_hh_mm_a));
                }
            }
        }
    }

    private void setupTypePictureLeft(ViewHolderLeft viewHolder, String url, int position, String type){
        viewHolder.lnlPicture.setVisibility(View.VISIBLE);
        viewHolder.lnlMessage.setVisibility(View.GONE);
        viewHolder.lnlMultiPicture.setVisibility(View.GONE);

        //Load Image
        Glide.with(context).load(url)
                .placeholder(R.color.colorLine)
                .error(R.color.colorLine)
                .into(viewHolder.imvPicture);

        //Action ImageClick
        viewHolder.imvPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null && type.matches(keyUtils.PICTURE))
                    listener.actionClickImage(position);
                else if(listener != null && type.matches(keyUtils.MULTI_PICTURE))
                    listener.actionClickImage(url, position);
            }
        });
    }

    private void setupTypePictureRight(ViewHolderRight viewHolder, String url, int position, String type){
        viewHolder.lnlPicture.setVisibility(View.VISIBLE);
        viewHolder.lnlMessage.setVisibility(View.GONE);
        viewHolder.lnlMultiPicture.setVisibility(View.GONE);

        //Load Image
        Glide.with(context).load(url)
                .placeholder(R.color.colorLine)
                .error(R.color.colorLine)
                .into(viewHolder.imvPicture);

        //Action ImageClick
        viewHolder.imvPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null && type.matches(keyUtils.PICTURE))
                    listener.actionClickImage(position);
                else if(listener != null && type.matches(keyUtils.MULTI_PICTURE))
                    listener.actionClickImage(url, position);
            }
        });
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    class ViewHolderLeft extends RecyclerView.ViewHolder{

        private ImageView imvAvatar;
        private LoaderTextView tvName;
        private LoaderTextView tvTime;
        private LoaderTextView tvMessage;
        private ImageView imvPicture;
        private LinearLayout  lnlMessage;
        private LinearLayout lnlPicture;
        private LinearLayout lnlMultiPicture;

        private GridView gvMultiPicture;

        private LinearLayout lnlName;
        private LinearLayout lnlMain;
        private RelativeLayout rltlTimeGroup;
        private TextView tvTimeGroup;

        private FrameLayout frameMain;

        ViewHolderLeft(@NonNull View itemView) {
            super(itemView);

            imvAvatar = itemView.findViewById(R.id.imvAvatar);
            tvName = itemView.findViewById(R.id.tvUsername);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvMessage = itemView.findViewById(R.id.tvMessage);

            imvPicture = itemView.findViewById(R.id.imvResultPicture);

            lnlMessage = itemView.findViewById(R.id.lnlMessage);
            lnlPicture = itemView.findViewById(R.id.lnlPicture);
            lnlMultiPicture = itemView.findViewById(R.id.lnlMultiPicture);
            lnlName = itemView.findViewById(R.id.lnlName);
            lnlMain = itemView.findViewById(R.id.lnlMain);

            frameMain = itemView.findViewById(R.id.frameMain);
            rltlTimeGroup = itemView.findViewById(R.id.rltlTimeGroup);
            tvTimeGroup = itemView.findViewById(R.id.tvTimeGroup);
            gvMultiPicture = itemView.findViewById(R.id.gvMultiPicture);
        }
    }

    class ViewHolderRight extends RecyclerView.ViewHolder{

        private LoaderTextView tvTime;
        private LoaderTextView tvMessage;

        private ImageView imvPicture;
        private LinearLayout lnlMessage;
        private LinearLayout lnlPicture;
        private LinearLayout lnlMultiPicture;
        private LinearLayout lnlName;

        private GridView gvMultiPicture;

        private RelativeLayout rltlMain;
        private RelativeLayout rltlTimeGroup;

        private TextView tvTimeGroup;

        ViewHolderRight(@NonNull View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tvTime);
            tvMessage = itemView.findViewById(R.id.tvMessage);

            imvPicture = itemView.findViewById(R.id.imvResultPicture);

            lnlMessage = itemView.findViewById(R.id.lnlMessage);
            lnlPicture = itemView.findViewById(R.id.lnlPicture);
            lnlMultiPicture = itemView.findViewById(R.id.lnlMultiPicture);

            lnlName = itemView.findViewById(R.id.lnlName);
            rltlMain = itemView.findViewById(R.id.rltlMain);

            rltlTimeGroup = itemView.findViewById(R.id.rltlTimeGroup);
            tvTimeGroup = itemView.findViewById(R.id.tvTimeGroup);
            gvMultiPicture = itemView.findViewById(R.id.gvMultiPicture);
        }
    }


    class aMultiPicture extends BaseAdapter{

        private final Context context;
        private ArrayList<String> imageList;
        private final LayoutInflater mInflater;
        private final int rootPosition;

        private aMultiPicture(Context context, ArrayList<String> imageList, int rootPosition) {
            this.context = context;
            this.imageList = imageList;
            this.rootPosition = rootPosition;
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = mInflater.inflate(R.layout.item_multi_image_in_message, null);
            }

            final ImageView imageView = convertView.findViewById(R.id.imvMultiImage);

            try {
                Glide.with(context).load(imageList.get(position))
                        .placeholder(R.color.colorLine)
                        .error(R.drawable.no_image)
                        .into(imageView);

            }catch (Exception e){
                Log.e("CheckApp", e.toString());
            }

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null){
                        listener.actionClickImage(imageList.get(position), rootPosition);
                    }
                }
            });

            return convertView;
        }
    }

}
