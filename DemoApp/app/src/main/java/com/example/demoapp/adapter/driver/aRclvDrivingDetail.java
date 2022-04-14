package com.example.demoapp.adapter.driver;

import static com.example.demoapp.Utils.patternFormatDateTime.MMM_dd_yyyy_hh_mm_a;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.Models.objApplication.objDrivingDetail;
import com.example.demoapp.Models.objApplication.objSpeed;
import com.example.demoapp.R;
import com.example.demoapp.Utils.timeUtils;

import java.util.ArrayList;
import java.util.List;


public class aRclvDrivingDetail extends RecyclerView.Adapter<aRclvDrivingDetail.ViewHolder> {
    private final Context context;

    private ArrayList<objDrivingDetail> drivingDetailList;
    private actionClick listener;

    public void setOnClickListener(actionClick listener) {
        this.listener = listener;
    }

    public interface actionClick {
        void onClick(int position);
    }

    public aRclvDrivingDetail(ArrayList<objDrivingDetail> drivingDetailList, Context context) {
        this.drivingDetailList = drivingDetailList;
        this.context = context;
    }

    public void addItem(objDrivingDetail item){
        drivingDetailList.add(item);
        notifyItemInserted(drivingDetailList.size()-1);
    }

    public void setDrivingDetailList(ArrayList<objDrivingDetail> drivingDetailList){
        this.drivingDetailList = drivingDetailList;
        notifyDataSetChanged();
    }

    public void removeAllItem(){
        int size = drivingDetailList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                drivingDetailList.remove(0);
            }
            notifyItemRangeRemoved(0, size);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_driving_detail, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        objDrivingDetail item = drivingDetailList.get(position);

        //Calculate the average speed
        List<Double> listAverageSpeed = item.getAverageSpeed().getListAverageSpeed();
        final int sizeAverageSpeed = listAverageSpeed.size();
        double averageSpeed = 0;
        for(double avg : listAverageSpeed){
            averageSpeed += avg;
        }

        averageSpeed = objSpeed.round(averageSpeed / sizeAverageSpeed,2);


        holder.tvAverageSpeed.setText(averageSpeed + " km/hr");
        holder.tvTopSpeed.setText(item.getTopSpeed());

        holder.tvWeek.setText(String.valueOf(item.getWeek()));
        holder.tvYear.setText(String.valueOf(item.getYear()));

        holder.tvTimeUpdateTopSpeed.setText("("+timeUtils.convertMillisecondToStringFormat(item.getTimeUpdateTopSpeed(),MMM_dd_yyyy_hh_mm_a)+")");
        holder.tvTimeUpdateAverageSpeed.setText("("+timeUtils.convertMillisecondToStringFormat(item.getAverageSpeed().getTimeUpdateAverageSpeed(),MMM_dd_yyyy_hh_mm_a)+")");

    }

    @Override
    public int getItemCount() {
        if (drivingDetailList == null) {
            return 0;
        }
        return drivingDetailList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvWeek;
        private TextView tvYear;
        private TextView tvTopSpeed;
        private TextView tvAverageSpeed;
        private TextView tvTimeUpdateAverageSpeed;
        private TextView tvTimeUpdateTopSpeed;

        private ViewHolder(View itemView) {
            super(itemView);

            tvWeek = itemView.findViewById(R.id.tvWeek);
            tvYear = itemView.findViewById(R.id.tvYear);
            tvTopSpeed = itemView.findViewById(R.id.tvTopSpeed);
            tvAverageSpeed = itemView.findViewById(R.id.tvAverageSpeed);
            tvTimeUpdateAverageSpeed = itemView.findViewById(R.id.tvTimeUpdateAverageSpeed);
            tvTimeUpdateTopSpeed = itemView.findViewById(R.id.tvTimeUpdateTopSpeed);
        }

    }
}