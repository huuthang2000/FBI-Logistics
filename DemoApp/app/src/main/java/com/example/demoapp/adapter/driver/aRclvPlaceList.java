package com.example.demoapp.adapter.driver;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.Models.objApplication.objArea;
import com.example.demoapp.R;
import com.example.demoapp.view.driver.PlaceAlertsDetail;

import java.util.ArrayList;


public class aRclvPlaceList extends RecyclerView.Adapter<aRclvPlaceList.ViewHolder> {
    private final Context context;

    private ArrayList<objArea> areaList;
    private actionClick listener;

    public void setOnClickListener(actionClick listener) {
        this.listener = listener;
    }

    public interface actionClick {
        void onClick(int position);
    }

    public void addItem(objArea item){
        areaList.add(item);
        notifyDataSetChanged();
//        notifyItemInserted(areaList.size() - 1);
    }

    public void removeItem(String id){
        int position = -1;
        for(int i = 0; i< areaList.size(); i++){
            if(id.matches(areaList.get(i).getId())){
                position = i;
                break;
            }
        }

        if(position != -1){
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, areaList.size());
        }
    }

    public void setAreaList(ArrayList<objArea> areaList) {
        this.areaList = areaList;
        notifyDataSetChanged();
    }

    public aRclvPlaceList(ArrayList<objArea> areaList, Context context) {
        this.areaList = areaList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list_place_alerts, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        objArea objArea = areaList.get(position);

        String placeName = objArea.getRegionName();
        holder.txt_PlaceName.setText(placeName);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PlaceAlertsDetail.class);
                intent.putExtra("Place_Item", objArea);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (areaList == null) {
            return 0;
        }
        return areaList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_PlaceName;
        View mView;
        private ViewHolder(View itemView) {
            super(itemView);

            txt_PlaceName = itemView.findViewById(R.id.txt_PlaceName);
            mView = itemView;
        }

    }
}