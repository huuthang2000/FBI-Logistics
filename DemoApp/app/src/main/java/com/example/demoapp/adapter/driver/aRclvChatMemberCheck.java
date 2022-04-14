package com.example.demoapp.adapter.driver;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.R;
import com.example.demoapp.SQLite.tb_Account;

import java.util.ArrayList;




public class aRclvChatMemberCheck extends RecyclerView.Adapter<aRclvChatMemberCheck.ViewHolder> {
    private final Context context;

    private ArrayList<String> idAccountList;
    private ArrayList<Boolean> checkedList;
    private actionClick listener;

    public void setOnClickListener(actionClick listener) {
        this.listener = listener;
    }

    public interface actionClick {
        void onClick(int position);
    }

    public ArrayList<String> getIdAccountList() {
        return idAccountList;
    }

    public ArrayList<String> getIdAccountListChecked(){
        ArrayList<String> tempList = new ArrayList<>();
        final int sizeList = checkedList.size();
        for(int i = 0; i < sizeList; i++){
            if(checkedList.get(i))
                tempList.add(idAccountList.get(i));
        }
        return tempList;
    }

    public aRclvChatMemberCheck(ArrayList<String> idAccountList, ArrayList<String> idAccountListChecked, Context context) {
        this.idAccountList = idAccountList;
        this.context = context;
        this.checkedList = new ArrayList<>();

        //Init list checked
        if(idAccountListChecked.size() == 0){
            for(String s : idAccountList){
                checkedList.add(false);
            }
        }else{
            for(int i=0; i < idAccountList.size(); i++){
                checkedList.add(false);
                for(String id : idAccountListChecked){
                    if(id.matches(idAccountList.get(i)))
                        checkedList.set(i,true);
                }
            }
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member_check, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String id = idAccountList.get(position);

        try {
            objAccount account = tb_Account.getInstance(context).getAccountByID(id);

            if (account != null) {

                holder.tvUsername.setText(account.getName());

                //If local Avatar does not exist then get the avatar URL
                Glide.with(context)
                        .load(account.getLocalAvatar().matches("") ? account.getAvatar() : account.getLocalAvatar())
                        .placeholder(R.color.colorLine)
                        .error(R.drawable.no_avatar)
                        .dontAnimate().apply(RequestOptions.circleCropTransform())
                        .into(holder.imvAvatar);


                holder.chkChecked.setChecked(checkedList.get(position));

//                holder.lnlMain.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        holder.chkChecked.setChecked(!holder.chkChecked.isChecked(), true);
//                    }
//                });
//
//                holder.chkChecked.setOnCheckedChangeListener(new CheckBox.OnCheckedChangeListener() {
//                    @Override
//                    public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
//                        checkedList.set(position, isChecked);
//                    }
//                });
            }
        }catch (Exception e){
            Log.e("CheckApp", e.toString());
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
        private CheckBox chkChecked;
        private LinearLayout lnlMain;

        private ViewHolder(View itemView) {
            super(itemView);

            imvAvatar = itemView.findViewById(R.id.imvAvatar);
            chkChecked = itemView.findViewById(R.id.chkChecked);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            lnlMain = itemView.findViewById(R.id.lnlMain);

        }

    }
}