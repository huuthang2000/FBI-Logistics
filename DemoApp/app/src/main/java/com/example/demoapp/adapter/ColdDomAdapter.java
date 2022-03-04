package com.example.demoapp.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.databinding.RowDomColdBinding;
import com.example.demoapp.model.DomCold;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.dom.dom_cold.DialogDomColdDetail;

import java.util.List;

public class ColdDomAdapter extends RecyclerView.Adapter<ColdDomAdapter.ColdViewHolder> {
    private final Context context;
    private List<DomCold> listCold;

    @NonNull
    @Override
    public ColdViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ColdViewHolder(RowDomColdBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ColdViewHolder holder, int position) {

        DomCold domCold = listCold.get(position);
        holder.bind(domCold);

        holder.binding.rowCvDomCold.setOnClickListener(view -> goToDetail(domCold));

    }

    public void goToDetail(DomCold domCold) {
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        DialogFragment dialogFragment = DialogDomColdDetail.getInstance();

        Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.DOM_COLD_OBJECT, domCold);

        dialogFragment.setArguments(bundle);
        dialogFragment.show(fm, "Detail Dom Cold");
    }

    public ColdDomAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDomCold(List<DomCold> list) {
        this.listCold = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (listCold != null) {
            return listCold.size();
        }
        return 0;
    }

    public void filterList(List<DomCold> filteredList) {
        listCold = filteredList;
        notifyDataSetChanged();
    }

    public static class ColdViewHolder extends RecyclerView.ViewHolder {
        private final RowDomColdBinding binding;

        public ColdViewHolder(@NonNull RowDomColdBinding root) {
            super(root.getRoot());
            binding = root;
        }

        public void bind(DomCold domCold) {
            binding.tvDomColdProductStt.setText(domCold.getStt());
            binding.tvDomColdProductName.setText(domCold.getName());
            binding.tvDomColdWeight.setText(domCold.getWeight());
            binding.tvDomColdQuantityPallet.setText(domCold.getQuantityPallet());
            binding.tvDomColdQuantityCarton.setText(domCold.getQuantityCarton());
            binding.tvDomColdAddressReceive.setText(domCold.getAddressReceive());
            binding.tvDomColdAddressDelivery.setText(domCold.getAddressDelivery());
            binding.tvDomColdLength.setText(domCold.getLength());
            binding.tvRowDomColdHeight.setText(domCold.getHeight());
            binding.tvRowDomColdWidth.setText(domCold.getWidth());
        }
    }
}
