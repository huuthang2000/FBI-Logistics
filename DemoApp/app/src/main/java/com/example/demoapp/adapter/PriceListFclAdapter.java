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
import com.example.demoapp.model.Fcl;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.fcl.FragmentFclDetail;

import java.util.List;

public class PriceListFclAdapter extends RecyclerView.Adapter<PriceListFclAdapter.ViewHolder>  {

    private final Context context;
    private List<Fcl> mListDetailFcl;

    public PriceListFclAdapter(Context context) {
        this.context = context;

    }

    @NonNull
    @Override
    public PriceListFclAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_pricelist_fcl, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceListFclAdapter.ViewHolder holder, int position) {
        Fcl priceListModel = mListDetailFcl.get(position);
        if (mListDetailFcl.size() > 0) {

            holder.stt.setText(priceListModel.getStt());
            holder.pol.setText(priceListModel.getPol());
            holder.pod.setText(priceListModel.getPod());
            holder.of20.setText(priceListModel.getOf20());
            holder.of40.setText(priceListModel.getOf40());
            holder.of45.setText(priceListModel.getOf45());
            holder.su20.setText(priceListModel.getSu20());
            holder.su40.setText(priceListModel.getSu40());
            holder.line.setText(priceListModel.getLinelist());
            holder.notes1.setText(priceListModel.getNotes());
            holder.valid.setText(priceListModel.getValid());
            holder.notes2.setText(priceListModel.getNotes2());

        } else {
            return;
        }
        holder.fclCardView.setOnClickListener(view -> goToDetail(priceListModel));
    }

    /**
     * Start detail dialog
     *
     * @param fcl model
     */
    public void goToDetail(Fcl fcl) {
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        DialogFragment dialogFragment = FragmentFclDetail.getInstance();

        Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.FCL_OBJECT, fcl);

        dialogFragment.setArguments(bundle);
        dialogFragment.show(fm, "DetailFcl");
    }

    @Override
    public int getItemCount() {
        if (mListDetailFcl != null) {
            return mListDetailFcl.size();
        }
        return 0;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDataFcl(List<Fcl> list) {
        this.mListDetailFcl = list;
        notifyDataSetChanged();
    }

    public void filterList(List<Fcl> filteredList) {
        mListDetailFcl = filteredList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView stt, pol, pod, of20, of40, of45, su20, su40, line, notes1, valid, notes2, changeOf20;
        ConstraintLayout fclCardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fclCardView = itemView.findViewById(R.id.row_cv_fcl);
            stt = itemView.findViewById(R.id.tv_row_price_asia_stt);
            pol = itemView.findViewById(R.id.tv_row_price_asia_pol);
            pod = itemView.findViewById(R.id.tv_row_price_asia_pod);
            of20 = itemView.findViewById(R.id.tv_row_price_asia_of20);
            of40 = itemView.findViewById(R.id.tv_row_price_asia_of40);
            of45 = itemView.findViewById(R.id.tv_row_price_asia_of45);
            su20 = itemView.findViewById(R.id.tv_row_price_asia_su20);
            su40 = itemView.findViewById(R.id.tv_row_price_asia_su40);
            line = itemView.findViewById(R.id.tv_row_price_asia_line);
            notes1 = itemView.findViewById(R.id.tv_row_price_asia_notes1);
            valid = itemView.findViewById(R.id.tv_row_price_asia_valid);
            notes2 = itemView.findViewById(R.id.tv_row_price_asia_notes2);

            changeOf20 = itemView.findViewById(R.id.changeTextOf20);

        }

    }

}
