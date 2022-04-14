package com.example.demoapp.adapter.driver;

import static com.example.demoapp.Utils.timeUtils.getTimeAgo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.demoapp.Models.objApplication.objAccount;
import com.example.demoapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class aRclvListMember extends RecyclerView.Adapter<aRclvListMember.ViewHolder> {
    private final Context context;

    private ArrayList<objAccount> objAccountsList;
    private actionClick listener;

    public void setOnClickListener(actionClick listener) {
        this.listener = listener;
    }

    public interface actionClick {
        void onClick(int position);
    }

    public void addItem(objAccount item){
        objAccountsList.add(item);
        notifyDataSetChanged();
    }

    public objAccount getAccount(int position){
        return objAccountsList.get(position);
    }

    public void setAreaList(ArrayList<objAccount> objAccounts) {
        this.objAccountsList = objAccounts;
        notifyDataSetChanged();
    }

    public aRclvListMember(ArrayList<objAccount> objAccounts, Context context) {
        this.objAccountsList = objAccounts;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_member, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        objAccount account = objAccountsList.get(position);

        long timeStatus = Long.parseLong(account.getNetwork());
        Log.d("times",(System.currentTimeMillis() - timeStatus)+"");
        String networkStatus ;
        if((System.currentTimeMillis() - timeStatus) < 3*60000)
        {
            networkStatus = "online";
        }
        else if((System.currentTimeMillis() - timeStatus) > 3*60000 && (System.currentTimeMillis() - timeStatus) < 40*60000)
        {
            networkStatus = "online";
        }
        else {
            networkStatus = "offline";

        }
        holder.txt_MemberName.setText(account.getName());
        if(account.getGps().getStatus() && networkStatus.equals("online"))
        {
            holder.tvPhoneNumber.setText(context.getResources().getString(R.string.since)+ " " +(getTimeAgo(account.getLocation().getTimeUpdate())));
            holder.tvPhoneNumber.setTextColor(context.getResources().getColor(R.color.black));

        }
        else if(account.getGps().getStatus() && networkStatus.equals("offline"))
        {
            holder.tvPhoneNumber.setText("No network or phone off");
            holder.tvPhoneNumber.setTextColor(context.getResources().getColor(R.color.orange_red));
        }
        else if(!(account.getGps().getStatus()) && networkStatus.equals("offline"))
        {
            holder.tvPhoneNumber.setText("Location/GPS turned off");
            holder.tvPhoneNumber.setTextColor(context.getResources().getColor(R.color.orange));
        }
        else if(!(account.getGps().getStatus()) && networkStatus.equals("online"))
        {
            holder.tvPhoneNumber.setText("Location/GPS turned off");
            holder.tvPhoneNumber.setTextColor(context.getResources().getColor(R.color.orange));
        }
        else {
            holder.tvPhoneNumber.setText(context.getResources().getString(R.string.since)+ " " +(getTimeAgo(account.getLocation().getTimeUpdate())));
            holder.tvPhoneNumber.setTextColor(context.getResources().getColor(R.color.black));
        }

        //If local Avatar does not exist then get the avatar URL
        if (account.getLocalAvatar().matches("")) {
            Glide.with(context).load(account.getAvatar())
                    .placeholder(R.drawable.no_avatar)
                    .dontAnimate()
                    .dontAnimate()
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.circleImageView);
        } else {
            Glide.with(context).load(account.getLocalAvatar())
                    .placeholder(R.drawable.no_avatar)
                    .dontAnimate()
                    .dontAnimate()
                    .apply(RequestOptions.circleCropTransform())
                    .into(holder.circleImageView);
        }
        // let's just get the uid and from the uid get the list account of people fragment class then pass that list over here.
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null)
                    listener.onClick(position);
                // show dialog với UID
//                showDialog(account.getId(), context);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (objAccountsList == null) {
            return 0;
        }
        return objAccountsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_MemberName;
        private CircleImageView circleImageView;
        private TextView tvPhoneNumber;
        private View mView;

        private ViewHolder(View itemView) {
            super(itemView);

            txt_MemberName = itemView.findViewById(R.id.txt_name_member);
            tvPhoneNumber = itemView.findViewById(R.id.tvPhoneNumber);
            circleImageView = itemView.findViewById(R.id.img_avatar_member);
            mView = itemView;
        }

    }
}