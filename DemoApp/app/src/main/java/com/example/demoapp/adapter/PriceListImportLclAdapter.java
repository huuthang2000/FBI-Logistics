package com.example.demoapp.adapter;

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
import com.example.demoapp.model.ImportLcl;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.imp.FragmentImportLclDetail;

import java.util.List;


public class PriceListImportLclAdapter extends RecyclerView.Adapter<PriceListImportLclAdapter.ViewHolder> {

    private Context context;
    private List<ImportLcl> listPriceList;

    public PriceListImportLclAdapter(Context context) {
        this.context = context;
    }

    public void setImports(List<ImportLcl> list) {
        this.listPriceList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PriceListImportLclAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_pricelist_import_lcl, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceListImportLclAdapter.ViewHolder holder, int position) {
        ImportLcl imp = listPriceList.get(position);
        if (listPriceList.size() > 0) {

            holder.stt.setText(imp.getStt());
            holder.term.setText(imp.getTerm());
            holder.pol.setText(imp.getPol());
            holder.pod.setText(imp.getPod());
            holder.cargo.setText(imp.getCargo());
            holder.of.setText(imp.getOf());

            holder.localPol.setText(imp.getLocalPol());
            holder.localPod.setText(imp.getLocalPod());

            holder.carrier.setText(imp.getCarrier());
            holder.schedule.setText(imp.getSchedule());
            holder.transit.setText(imp.getTransitTime());

            holder.valid.setText(imp.getValid());
            holder.note.setText(imp.getNote());
        } else {
            return;
        }

        holder.importCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToDetail(imp);
            }
        });
    }

    public void goToDetail(ImportLcl imp) {
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        DialogFragment dialogFragment = FragmentImportLclDetail.getInstance();

        Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.IMPORT_LCL_OBJECT, imp);
        dialogFragment.setArguments(bundle);
        dialogFragment.show(fm, "DetailImport");
    }

    @Override
    public int getItemCount() {
        if (listPriceList != null) {
            return listPriceList.size();
        }
        return 0;
    }

    public void filterList(List<ImportLcl> filteredList) {
        listPriceList = filteredList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView stt, term, pol, pod, cargo, of, localPol, localPod, carrier, schedule,
                transit, valid, note;
        ConstraintLayout importCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            importCardView = itemView.findViewById(R.id.row_cv_import_lcl);
            stt = itemView.findViewById(R.id.tv_row_price_import_lcl_stt);
            term = itemView.findViewById(R.id.tv_row_price_import_lcl_term);
            pol = itemView.findViewById(R.id.tv_row_price_import_lcl_pol);
            pod = itemView.findViewById(R.id.tv_row_price_import_lcl_pod);

            cargo = itemView.findViewById(R.id.tv_row_price_import_lcl_cargo);
            of = itemView.findViewById(R.id.tv_row_price_import_lcl_of);
            localPol = itemView.findViewById(R.id.tv_row_price_import_lcl_local_pol);

            localPod = itemView.findViewById(R.id.tv_row_price_import_lcl_local_pod);

            carrier = itemView.findViewById(R.id.tv_row_price_import_lcl_carrier);
            schedule = itemView.findViewById(R.id.tv_row_price_import_lcl_schedule);
            transit = itemView.findViewById(R.id.tv_row_price_import_lcl_transit);
            valid = itemView.findViewById(R.id.tv_row_price_import_lcl_valid);
            note = itemView.findViewById(R.id.tv_row_price_import_lcl_note);

        }
    }

}
