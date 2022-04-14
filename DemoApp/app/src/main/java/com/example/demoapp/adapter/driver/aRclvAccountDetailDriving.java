package com.example.demoapp.adapter.driver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.R;
import com.example.demoapp.Utils.keyUtils;

import java.util.ArrayList;


public class aRclvAccountDetailDriving extends RecyclerView.Adapter<aRclvAccountDetailDriving.ViewHolder> {
    private final Context context;

    private ArrayList<objAccount> items;
    private actionClick listener;

    public void setOnClickListener(actionClick listener) {
        this.listener = listener;
    }

    public interface actionClick {
        void onClick(int position);
    }

    public aRclvAccountDetailDriving(ArrayList<objAccount> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public String getPhoneNumber(int position){
        return items.get(position).getPhone();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_account_detail_driving, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        objAccount item = items.get(position);

        //If local Avatar does not exist then get the avatar URL
        Glide.with(context)
                .load(item.getLocalAvatar().matches("") ? item.getAvatar() : item.getLocalAvatar())
                .placeholder(R.color.colorLine)
                .error(R.drawable.no_avatar)
                .dontAnimate().apply(RequestOptions.circleCropTransform())
                .into(holder.imvAvatar);


        holder.tvPhoneNumber.setText(item.getPhone().matches(keyUtils.NULL) ? context.getResources().getString(R.string.Unknown) : item.getPhone());
        holder.tvUsername.setText(item.getName());

        holder.tvPercentBattery.setText(item.getBatteryPercent());
        holder.imvBattery.setImageDrawable(objAccount.getBatteryIcon(context,item.getBatteryPercent(),-1));

        holder.lnlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onClick(position);
            }
        });

        Drawable icon_gender;
        if(item.getGender().toLowerCase().matches(keyUtils.MALE))
            icon_gender = context.getResources().getDrawable(R.drawable.ic_male);
        else if(item.getGender().toLowerCase().matches(keyUtils.FEMALE))
            icon_gender = context.getResources().getDrawable(R.drawable.ic_female);
        else
            icon_gender = context.getResources().getDrawable(R.drawable.ic_sexual);

        holder.imvGender.setImageDrawable(icon_gender);
        holder.imvStatusGPS.setImageDrawable(context.getResources().getDrawable(item.getGps().getStatus() ? R.drawable.ic_location_on_red_18dp : R.drawable.ic_location_off_red_18dp));
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imvAvatar;
        private TextView tvUsername;
        private TextView tvPhoneNumber;
        private ImageView imvBattery;
        private ImageView imvGender;
        private TextView tvPercentBattery;
        private LinearLayout lnlMain;
        private ImageView imvStatusGPS;

        private ViewHolder(View itemView) {
            super(itemView);

            imvAvatar = itemView.findViewById(R.id.imvAvatar);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            imvBattery = itemView.findViewById(R.id.imvBattery);
            imvGender = itemView.findViewById(R.id.imvGender);
            tvPercentBattery = itemView.findViewById(R.id.tvPercentBattery);
            imvStatusGPS = itemView.findViewById(R.id.imvStatusGPS);
            lnlMain = itemView.findViewById(R.id.lnlMain);
        }

    }
}