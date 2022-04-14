package com.example.demoapp.adapter.driver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.R;

import java.util.ArrayList;



public class aRclvAvatarChecked extends RecyclerView.Adapter<aRclvAvatarChecked.ViewHolder> {
    private final Context context;

    private ArrayList<String> accountIDList;
    private actionClick listener;

    public void setOnClickListener(actionClick listener) {
        this.listener = listener;
    }

    public interface actionClick {
        void onClick(int position);
    }

    public aRclvAvatarChecked(ArrayList<String> accountList, Context context) {
        this.accountIDList = accountList;
        this.context = context;
    }

    public void addItem(String accountID){
        accountIDList.add(accountID);
        notifyItemInserted(accountIDList.size()-1);
    }

    public void removeItem(int position){
        accountIDList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, accountIDList.size());
    }

    public ArrayList<String> getAccountIDList(){
        return accountIDList;
    }

    public String getItemWithPosition(int position){
        return accountIDList.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_avatar_checked, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String accountID = accountIDList.get(position);
        objAccount account = objAccount.getAccountFromSQLite(context,accountID);

        if(account != null){

            //If local Avatar does not exist then get the avatar URL
            Glide.with(context)
                    .load(account.getLocalAvatar().matches("") ? account.getAvatar() : account.getLocalAvatar())
                    .placeholder(R.color.colorLine)
                    .error(R.drawable.no_avatar)
                    .dontAnimate().apply(RequestOptions.circleCropTransform())
                    .into(holder.imvAvatar);

            holder.imvChecked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeItem(position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        if (accountIDList == null) {
            return 0;
        }
        return accountIDList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imvAvatar;
        private ImageView imvChecked;

        private ViewHolder(View itemView) {
            super(itemView);
            imvAvatar = itemView.findViewById(R.id.imvAvatar);
            imvChecked = itemView.findViewById(R.id.imvChecked);
        }

    }
}