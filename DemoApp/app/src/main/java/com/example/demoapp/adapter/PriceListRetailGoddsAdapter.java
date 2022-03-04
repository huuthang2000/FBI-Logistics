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
import com.example.demoapp.model.RetailGoods;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.air.retailgoods.FragmentRetailGoodsDetail;

import java.util.List;

public class PriceListRetailGoddsAdapter extends  RecyclerView.Adapter<PriceListRetailGoddsAdapter.PriceRetialGoodsViewHolder>{
    private Context context;
    private List<RetailGoods> listRetailGoods;

    public  PriceListRetailGoddsAdapter(Context context){
        this.context = context;
    }
    @NonNull
    @Override
    public PriceRetialGoodsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_price_retail_goods, parent, false);
        return new PriceRetialGoodsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceRetialGoodsViewHolder holder, int position) {
        RetailGoods priceRetailGoods = listRetailGoods.get(position);
        if(listRetailGoods.size() > 0){
            holder.tvStt.setText(priceRetailGoods.getStt());
            holder.tvPol.setText(priceRetailGoods.getPol());
            holder.tvPod.setText(priceRetailGoods.getPod());
            holder.tvDim.setText(priceRetailGoods.getDim());
            holder.tvGross.setText(priceRetailGoods.getGrossweight());
            holder.tvType.setText(priceRetailGoods.getTypeofcargo());
            holder.tvOceanFreight.setText(priceRetailGoods.getOceanfreight());
            holder.tvLocalcharge.setText(priceRetailGoods.getLocalcharge());
            holder.tvCarrier.setText(priceRetailGoods.getCarrier());
            holder.tvSchedule.setText(priceRetailGoods.getSchedule());
            holder.tvTransittime.setText(priceRetailGoods.getTransittime());
            holder.tvValid.setText(priceRetailGoods.getValid());
//            holder.tvNote.setText(priceRetailGoods.getNote());

        }else {
            return;
        }
        holder.airCarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDetail(priceRetailGoods);
            }
        });

    }
    public void filterList(List<RetailGoods> filterList){
        listRetailGoods = filterList;
        notifyDataSetChanged();
    }


    private void goToDetail(RetailGoods retailGoods){
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        DialogFragment dialogFragment = FragmentRetailGoodsDetail.getInstance();

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.RETAIL_GOODS, retailGoods);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fm, "DetailRetailGoods");

    }

    @Override
    public int getItemCount() {
        if(listRetailGoods != null){
            return listRetailGoods.size();
        }
        return 0;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void setDataRetailGoods(List<RetailGoods> mListRetailGoods){
        this.listRetailGoods = mListRetailGoods;
        notifyDataSetChanged();
    }

    public class PriceRetialGoodsViewHolder extends RecyclerView.ViewHolder{
        TextView tvStt, tvPol, tvPod, tvDim, tvGross, tvType, tvOceanFreight, tvLocalcharge, tvCarrier,
                tvSchedule, tvTransittime, tvValid, tvNote;
        private ConstraintLayout airCarView;
        public PriceRetialGoodsViewHolder(@NonNull View itemView) {
            super(itemView);

            airCarView = itemView.findViewById(R.id.row_cv_retail_goods);
            tvStt = itemView.findViewById(R.id.tv_row_price_retail_goods_stt);
            tvPol = itemView.findViewById(R.id.tv_row_price_retail_goods_pol);
            tvPod = itemView.findViewById(R.id.tv_row_price_retail_goods_pod);
            tvDim = itemView.findViewById(R.id.tv_row_price_retail_goods_dim);
            tvGross = itemView.findViewById(R.id.tv_row_price_retail_goods_grossweight);
            tvType = itemView.findViewById(R.id.tv_row_price_retail_goods_typeofcargo);
            tvOceanFreight = itemView.findViewById(R.id.tv_row_price_retail_goods_ocean_freight);
            tvLocalcharge = itemView.findViewById(R.id.tv_row_price_retail_goods_localcharge);
            tvCarrier = itemView.findViewById(R.id.tv_row_price_retail_goods_carrier);
            tvSchedule = itemView.findViewById(R.id.tv_row_price_retail_goods_schedule);
            tvTransittime = itemView.findViewById(R.id.tv_row_price_retail_goods_transittime);
            tvValid = itemView.findViewById(R.id.tv_row_price_retail_goods_valid);
            tvNote = itemView.findViewById(R.id.tv_row_price_retail_goods_note);


        }
    }
}
