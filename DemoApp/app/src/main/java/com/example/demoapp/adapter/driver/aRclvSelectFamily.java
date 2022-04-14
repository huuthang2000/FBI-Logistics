package com.example.demoapp.adapter.driver;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.demoapp.Models.objApplication.objFamily;
import com.example.demoapp.R;
import com.example.demoapp.SQLite.tb_CurrentFamilyID;

import java.util.ArrayList;

public class aRclvSelectFamily extends RecyclerView.Adapter<aRclvSelectFamily.ViewHolder> {

    private final Context context;

    private ArrayList<objFamily> familyList;
    private actionClick listener;

    public void setOnClickListener(actionClick listener) {
        this.listener = listener;
    }

    public interface actionClick {
        void onClick(int position);
    }

    public aRclvSelectFamily(ArrayList<objFamily> familyList, Context context) {
        this.familyList = familyList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_select_family, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        objFamily family = familyList.get(position);
        final int sizeMemberList = family.getMembersList().size();
        if(sizeMemberList == 1){
            holder.frameMoreAvatar.setVisibility(View.GONE);
            holder.imvAvatar.setVisibility(View.VISIBLE);

            objAccount account = objAccount.getAccountFromSQLite(context,family.getMembersList().get(0));

            Glide.with(context)
                    .load(account.getLocalAvatar().matches("") ? account.getAvatar() : account.getLocalAvatar())
                    .placeholder(R.color.colorLine)
                    .error(R.drawable.no_avatar)
                    .dontAnimate().apply(RequestOptions.circleCropTransform())
                    .into(holder.imvAvatar);

        }else if(sizeMemberList == 2){
            holder.frameMoreAvatar.setVisibility(View.VISIBLE);
            holder.imvAvatar.setVisibility(View.GONE);

            objAccount account1 = objAccount.getAccountFromSQLite(context,family.getMembersList().get(0));
            Glide.with(context)
                    .load(account1.getLocalAvatar().matches("") ? account1.getAvatar() : account1.getLocalAvatar())
                    .placeholder(R.color.colorLine)
                    .error(R.drawable.no_avatar)
                    .dontAnimate().apply(RequestOptions.circleCropTransform())
                    .into(holder.imvAvatar1);


            objAccount account2 = objAccount.getAccountFromSQLite(context,family.getMembersList().get(1));
            Glide.with(context)
                    .load(account2.getLocalAvatar().matches("") ? account2.getAvatar() : account2.getLocalAvatar())
                    .placeholder(R.color.colorLine)
                    .error(R.drawable.no_avatar)
                    .dontAnimate().apply(RequestOptions.circleCropTransform())
                    .into(holder.imvAvatar2);

            holder.tvMorePeople.setVisibility(View.GONE);

        }else if(sizeMemberList > 2){
            holder.frameMoreAvatar.setVisibility(View.VISIBLE);
            holder.imvAvatar.setVisibility(View.GONE);

            objAccount account1 = objAccount.getAccountFromSQLite(context,family.getMembersList().get(0));
            Glide.with(context)
                    .load(account1.getLocalAvatar().matches("") ? account1.getAvatar() : account1.getLocalAvatar())
                    .placeholder(R.color.colorLine)
                    .error(R.drawable.no_avatar)
                    .dontAnimate().apply(RequestOptions.circleCropTransform())
                    .into(holder.imvAvatar1);


            objAccount account2 = objAccount.getAccountFromSQLite(context,family.getMembersList().get(1));
            Glide.with(context)
                    .load(account2.getLocalAvatar().matches("") ? account2.getAvatar() : account2.getLocalAvatar())
                    .placeholder(R.color.colorLine)
                    .error(R.drawable.no_avatar)
                    .dontAnimate().apply(RequestOptions.circleCropTransform())
                    .into(holder.imvAvatar2);

            holder.tvMorePeople.setText("+" + (sizeMemberList - 2));
        }

        holder.tvFamilyName.setText(family.getCommonName());

        holder.lnlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onClick(position);
            }
        });

        holder.viewLineHorizontal.setVisibility(View.INVISIBLE);
        if(family.getId().matches(tb_CurrentFamilyID.getInstance(context).getCurrentFamilyID())){
            holder.lnlMain.setBackgroundColor(context.getResources().getColor(R.color.colorLine));
            holder.viewLineHorizontal.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        if (familyList == null) {
            return 0;
        }
        return familyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imvAvatar;
        private ImageView imvAvatar1;
        private ImageView imvAvatar2;

        private TextView tvMorePeople;

        private TextView tvFamilyName;
        private LinearLayout lnlMain;
        private FrameLayout frameMoreAvatar;

        private View viewLineHorizontal;

        private ViewHolder(View itemView) {
            super(itemView);

            imvAvatar = itemView.findViewById(R.id.imvAvatar);
            imvAvatar1 = itemView.findViewById(R.id.imvAvatar1);
            imvAvatar2 = itemView.findViewById(R.id.imvAvatar2);
            tvMorePeople = itemView.findViewById(R.id.tvMorePeople);
            tvFamilyName = itemView.findViewById(R.id.tvFamilyName);
            lnlMain = itemView.findViewById(R.id.lnlMain);
            frameMoreAvatar = itemView.findViewById(R.id.frameMoreAvatar);
            viewLineHorizontal = itemView.findViewById(R.id.viewLineHorizontal);
        }

    }
}