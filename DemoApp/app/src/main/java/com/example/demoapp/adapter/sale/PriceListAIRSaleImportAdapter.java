package com.example.demoapp.adapter.sale;

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
import com.example.demoapp.model.AirImport;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.air.sale.FragmentAirImportSaleDetail;

import java.util.List;

public class PriceListAIRSaleImportAdapter extends RecyclerView.Adapter<PriceListAIRSaleImportAdapter.PriceAirImportViewHolder>{
    private Context context;
    private List<AirImport> listAIRS;


    public PriceListAIRSaleImportAdapter(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public PriceAirImportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_pricelist_air_import, parent, false);

        return new PriceAirImportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceAirImportViewHolder holder, int position) {
        AirImport priceAir = listAIRS.get(position);
        if ( listAIRS.size() > 0) {

            holder.tvStt.setText(priceAir.getStt());
            holder.tvPol.setText(priceAir.getAol());
            holder.tvPod.setText(priceAir.getAod());
            holder.tvDim.setText(priceAir.getDim());
            holder.tvGross.setText(priceAir.getGrossweight());
            holder.tvType.setText(priceAir.getTypeofcargo());
            holder.tvAirFreight.setText(priceAir.getAirfreight());
            holder.tvSurcharge.setText(priceAir.getSurcharge());
            holder.tvAirlines.setText(priceAir.getAirlines());
            holder.tvSchedule.setText(priceAir.getSchedule());
            holder.tvTransittime.setText(priceAir.getTransittime());
            holder.tvValid.setText(priceAir.getValid());
            holder.tvNote.setText(priceAir.getNote());


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

    private void goToDetail(AirImport air) {
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        DialogFragment dialogFragment = FragmentAirImportSaleDetail.getInstance();

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.AIR_IMPORT, air);
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
    public void setDataAir(List<AirImport> mListDetailAir) {
        this.listAIRS = mListDetailAir;
        notifyDataSetChanged();
    }

    public void filterList(List<AirImport> filteredList) {
        listAIRS = filteredList;
        notifyDataSetChanged();
    }


    public class PriceAirImportViewHolder extends RecyclerView.ViewHolder {
        TextView tvStt, tvPol, tvPod, tvDim, tvGross, tvType, tvAirFreight, tvSurcharge, tvAirlines,
                tvSchedule, tvTransittime, tvValid, tvNote;
        private ConstraintLayout airCarView;

        public PriceAirImportViewHolder(@NonNull View itemView) {
            super(itemView);
            airCarView = itemView.findViewById(R.id.row_cv_air);
            tvStt = itemView.findViewById(R.id.tv_row_price_asia_air_stt_import);
            tvPol = itemView.findViewById(R.id.tv_row_price_asia_air_pol_import);
            tvPod = itemView.findViewById(R.id.tv_row_price_asia_air_pod_import);
            tvDim = itemView.findViewById(R.id.tv_row_price_asia_air_dim_import);
            tvGross = itemView.findViewById(R.id.tv_row_price_asia_air_grossweight_import);
            tvType = itemView.findViewById(R.id.tv_row_price_asia_air_typeofcargo_import);
            tvAirFreight = itemView.findViewById(R.id.tv_row_price_asia_air_freight_import);
            tvSurcharge = itemView.findViewById(R.id.tv_row_price_asia_air_surcharge_import);
            tvAirlines = itemView.findViewById(R.id.tv_row_price_asia_air_airlines_import);
            tvSchedule = itemView.findViewById(R.id.tv_row_price_asia_air_schedule_import);
            tvTransittime = itemView.findViewById(R.id.tv_row_price_asia_air_transittime_import);
            tvValid = itemView.findViewById(R.id.tv_row_price_asia_air_valid_import);
            tvNote = itemView.findViewById(R.id.tv_row_price_asia_air_note_import);
        }

    }
}
