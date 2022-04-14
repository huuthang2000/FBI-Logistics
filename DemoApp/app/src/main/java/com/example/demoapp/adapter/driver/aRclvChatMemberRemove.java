package com.example.demoapp.adapter.driver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.R;
import com.example.demoapp.SQLite.tb_Account;

import java.util.ArrayList;


public class aRclvChatMemberRemove extends RecyclerView.Adapter<aRclvChatMemberRemove.ViewHolder> {
    private final Context context;

    private ArrayList<String> idAccountList;
    private actionClick listener;

    public void setOnClickListener(actionClick listener) {
        this.listener = listener;
    }

    public interface actionClick {
        void onClick(int position);
    }

    public void removeItemWithPosition(int position){
        idAccountList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, idAccountList.size());
    }

    public ArrayList<String> getIdAccountList() {
        return idAccountList;
    }

    public aRclvChatMemberRemove(ArrayList<String> idAccountList, Context context) {
        this.idAccountList = idAccountList;
        this.context = context;
    }

    public String getItemWithPosition(int position){
        return idAccountList.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member_remove, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        String id = idAccountList.get(position);

        objAccount account = tb_Account.getInstance(context).getAccountByID(id);

        if(account!=null){
            holder.tvUsername.setText(account.getName());

            //If local Avatar does not exist then get the avatar URL
            Glide.with(context)
                    .load(account.getLocalAvatar().matches("") ? account.getAvatar() : account.getLocalAvatar())
                    .placeholder(R.color.colorLine)
                    .error(R.drawable.no_avatar)
                    .dontAnimate().apply(RequestOptions.circleCropTransform())
                    .into(holder.imvAvatar);


            holder.imvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener!=null)
                        listener.onClick(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (idAccountList == null) {
            return 0;
        }
        return idAccountList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imvAvatar;
        private TextView tvUsername;
        private ImageView imvDelete;

        private ViewHolder(View itemView) {
            super(itemView);

            imvAvatar = itemView.findViewById(R.id.imvAvatar);
            imvDelete = itemView.findViewById(R.id.imvDelete);
            tvUsername = itemView.findViewById(R.id.tvUsername);
        }
    }
}