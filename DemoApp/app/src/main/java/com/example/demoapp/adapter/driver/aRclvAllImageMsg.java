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
import com.example.demoapp.Models.objApplication.objDetailImage;
import com.example.demoapp.R;

import java.util.ArrayList;



public class aRclvAllImageMsg extends RecyclerView.Adapter<aRclvAllImageMsg.ViewHolder> {
    private final Context context;

    private ArrayList<objDetailImage> items;
    private actionClick listener;

    public void setOnClickListener(actionClick listener) {
        this.listener = listener;
    }

    public interface actionClick {
        void onClick(int position);
    }

    public aRclvAllImageMsg(ArrayList<objDetailImage> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_all_image_message, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        objDetailImage item = items.get(position);

        Glide.with(context).load(item.getUrlImage())
                .placeholder(R.color.colorLine)
                .error(R.drawable.no_image)
                .into(holder.imvMsg);

        holder.imvMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null){
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imvMsg;

        private ViewHolder(View itemView) {
            super(itemView);
            imvMsg = itemView.findViewById(R.id.imvImageMsg);
        }

    }
}