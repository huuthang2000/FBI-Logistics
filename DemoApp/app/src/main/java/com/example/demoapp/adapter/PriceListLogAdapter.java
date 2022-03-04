package com.example.demoapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.R;
import com.example.demoapp.model.Log;

import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.log.FragmentLogDetail;

import java.util.List;

public class PriceListLogAdapter extends RecyclerView.Adapter<PriceListLogAdapter.LogViewHolder> {
    private Context context;
    private List<Log> mPriceListLog;

    public PriceListLogAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_price_log, parent,false);
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        Log pricelog = mPriceListLog.get(position);
        if(mPriceListLog.size()>0){

            holder.tvStt.setText(pricelog.getStt());
            holder.tvTenHang.setText(pricelog.getTenhang());
            holder.tvHScode.setText(pricelog.getHscode());
            holder.tvCongdung.setText(pricelog.getCongdung());
            holder.tvHinhanh.setText(pricelog.getHinhanh());
            holder.tvCangdi.setText(pricelog.getCangdi());
            holder.tvCangden.setText(pricelog.getCangden());
            holder.tvLoaihang.setText(pricelog.getLoaihang());
            holder.tvSoluongcuthe.setText(pricelog.getSoluongcuthe());
            holder.tvYeucaudacbiet.setText(pricelog.getYeucaudacbiet());
            holder.tvPrice.setText(pricelog.getPrice());

        }else{
            return;
        }
        holder.logCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goToDetail(pricelog);
            }
        });
    }

    private void goToDetail(Log log1) {
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        DialogFragment dialogFragment = FragmentLogDetail.getInstance();

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.LOG_OBJECT, log1);
        dialogFragment.setArguments(bundle);
        dialogFragment.show( fm,"DetailLog");
    }

    @Override
    public int getItemCount() {
        if(mPriceListLog != null && mPriceListLog.size()>0){
            return mPriceListLog.size();
        }
        return 0;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setDataLog(List<Log> mListDetailLog) {
        this.mPriceListLog = mListDetailLog;
        notifyDataSetChanged();
    }

    public void filterList(List<Log> filteredList) {
        mPriceListLog = filteredList;
        notifyDataSetChanged();
    }

    public class LogViewHolder extends RecyclerView.ViewHolder{
        private TextView tvStt, tvTenHang, tvHScode, tvCongdung, tvHinhanh, tvCangdi, tvCangden,
        tvLoaihang, tvSoluongcuthe, tvYeucaudacbiet, tvPrice;
        ConstraintLayout logCardView;

        public LogViewHolder(@NonNull View itemView) {
            super(itemView);
            logCardView = itemView.findViewById(R.id.row_cv_log);
            tvStt = itemView.findViewById(R.id.tv_row_price_log_stt);
            tvTenHang = itemView.findViewById(R.id.tv_row_price_log_tenhang);
            tvHScode = itemView.findViewById(R.id.tv_row_price_log_hscode);
            tvCongdung = itemView.findViewById(R.id.tv_row_price_log_congdung);
            tvHinhanh = itemView.findViewById(R.id.tv_row_price_log_hinhanh);
            tvCangdi = itemView.findViewById(R.id.tv_row_price_log_cangdi);
            tvCangden = itemView.findViewById(R.id.tv_row_price_log_cangden);
            tvLoaihang = itemView.findViewById(R.id.tv_row_price_log_loaihang);
            tvSoluongcuthe = itemView.findViewById(R.id.tv_row_price_log_soluongcuthe);
            tvYeucaudacbiet = itemView.findViewById(R.id.tv_row_price_log_yeucaudacbiet);
            tvPrice = itemView.findViewById(R.id.tv_row_price_log_valid);


        }
    }
}
