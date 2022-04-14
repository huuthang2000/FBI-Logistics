package com.example.demoapp.adapter.driver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.demoapp.R;
import com.example.demoapp.Utils.keyUtils;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;


public class aRclvUploadImageMsg extends RecyclerView.Adapter<aRclvUploadImageMsg.ViewHolder> {
    private final Context context;

    private ArrayList<String> items;
    private ArrayList<String> statusList;
    private actionClick listener;

    public void setOnClickListener(actionClick listener) {
        this.listener = listener;
    }

    public interface actionClick {
        void onClick(int position);
    }

    public void setSuccessUpload(int position){
        statusList.set(position,keyUtils.SUCCESS);
        notifyItemChanged(position);
    }

    public void setFailUpload(int position){
        statusList.set(position,keyUtils.FAIL);
        notifyItemChanged(position);
    }

    public aRclvUploadImageMsg(ArrayList<String> items, Context context) {
        this.items = items;
        this.context = context;
        this.statusList = new ArrayList<>();
        for(String s : items){
            statusList.add(keyUtils.UPLOADING);
        }
    }

    public String getItemByPosition(int position){
        return items.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_image_upload, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);
        String status = statusList.get(position);

        //UPLOADING
        if(status.matches(keyUtils.UPLOADING)){
            holder.SpinKitLoading.setVisibility(View.VISIBLE);
            holder.imvDone.setVisibility(View.GONE);
        }
        //SUCCESS
        else if(status.matches(keyUtils.SUCCESS)){
            holder.SpinKitLoading.setVisibility(View.GONE);
            holder.imvDone.setVisibility(View.VISIBLE);
            holder.imvDone.setImageDrawable(context.getDrawable(R.drawable.ic_check_circle_green_18dp));
        }
        //FAIL
        else{
            holder.SpinKitLoading.setVisibility(View.GONE);
            holder.imvDone.setVisibility(View.VISIBLE);
            holder.imvDone.setImageDrawable(context.getDrawable(R.drawable.ic_cancel_red_18dp));
        }

        Glide.with(context)
                .load(item)
                .placeholder(R.color.colorLine)
                .error(R.drawable.no_image)
                .into(holder.imvImageUpload);
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imvImageUpload;
        private ImageView imvDone;
        private SpinKitView SpinKitLoading;

        private ViewHolder(View itemView) {
            super(itemView);

            imvImageUpload = itemView.findViewById(R.id.imvImageUpload);
            imvDone = itemView.findViewById(R.id.imvDone);
            SpinKitLoading = itemView.findViewById(R.id.SpinKitLoading);

        }

    }
}