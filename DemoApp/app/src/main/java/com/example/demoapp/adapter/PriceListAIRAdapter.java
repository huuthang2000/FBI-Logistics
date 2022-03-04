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
import com.example.demoapp.model.AirExport;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.air.air_export.FragmentAirDetail;

import java.util.List;

public class PriceListAIRAdapter extends RecyclerView.Adapter<PriceListAIRAdapter.PriceAirViewHolder> {
    private Context context;
    private List<AirExport> listAIRS;
    private List<AirExport> mlistAirOld;

    public PriceListAIRAdapter(Context context) {
        this.context = context;
        this.mlistAirOld = listAIRS;
    }

    @NonNull
    @Override
    public PriceAirViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pricelist_air, parent, false);

        return new PriceAirViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceAirViewHolder holder, int position) {
            AirExport priceAir = listAIRS.get(position);
        if ( listAIRS.size() > 0) {

            holder.tvstt.setText(priceAir.getStt());
            holder.tvaol.setText(priceAir.getAol());
            holder.tvaod.setText(priceAir.getAod());
            holder.tvdim.setText(priceAir.getDim());
            holder.tvgross.setText(priceAir.getGrossweight());
            holder.tvtype.setText(priceAir.getTypeofcargo());
            holder.tvairfreight.setText(priceAir.getAirfreight());
            holder.tvsurcharge.setText(priceAir.getSurcharge());
            holder.tvairlines.setText(priceAir.getAirlines());
            holder.tvschedule.setText(priceAir.getSchedule());
            holder.tvtransittime.setText(priceAir.getTransittime());
            holder.tvvalid.setText(priceAir.getValid());
            holder.tvnote.setText(priceAir.getNote());


        }else{
            return;
        }
        holder.airCarView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            goToDetail(priceAir);
        }
    });
    }
    public void filterList(List<AirExport> filterList){
        listAIRS = filterList;
        notifyDataSetChanged();
    }

    private void goToDetail(AirExport air) {
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        DialogFragment dialogFragment = FragmentAirDetail.getInstance();

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.AIR_OBJECT, air);
        dialogFragment.setArguments(bundle);
        dialogFragment.show( fm,"DetailAir");
    }

    @Override
    public int getItemCount() {
        if (listAIRS != null) {
            return listAIRS.size();
        }
        return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataAir(List<AirExport> mListDetailAir) {
        this.listAIRS = mListDetailAir;
        notifyDataSetChanged();
    }




    public class PriceAirViewHolder extends RecyclerView.ViewHolder {
        TextView tvstt, tvaol, tvaod, tvdim, tvgross, tvtype, tvairfreight, tvsurcharge, tvairlines,
                tvschedule, tvtransittime, tvvalid, tvnote;
        private ConstraintLayout airCarView;

        public PriceAirViewHolder(@NonNull View itemView) {
            super(itemView);
            airCarView = itemView.findViewById(R.id.row_cv_air);
            tvstt = itemView.findViewById(R.id.tv_row_price_asia_air_stt);
            tvaol = itemView.findViewById(R.id.tv_row_price_asia_air_aol);
            tvaod = itemView.findViewById(R.id.tv_row_price_asia_air_aod);
            tvdim = itemView.findViewById(R.id.tv_row_price_asia_air_dim);
            tvgross = itemView.findViewById(R.id.tv_row_price_asia_air_grossweight);
            tvtype = itemView.findViewById(R.id.tv_row_price_asia_air_typeofcargo);
            tvairfreight = itemView.findViewById(R.id.tv_row_price_asia_air_freight);
            tvsurcharge = itemView.findViewById(R.id.tv_row_price_asia_air_surcharge);
            tvairlines = itemView.findViewById(R.id.tv_row_price_asia_air_airlines);
            tvschedule = itemView.findViewById(R.id.tv_row_price_asia_air_schedule);
            tvtransittime = itemView.findViewById(R.id.tv_row_price_asia_air_transittime);
            tvvalid = itemView.findViewById(R.id.tv_row_price_asia_air_valid);
            tvnote = itemView.findViewById(R.id.tv_row_price_asia_air_note);
        }

    }

}
