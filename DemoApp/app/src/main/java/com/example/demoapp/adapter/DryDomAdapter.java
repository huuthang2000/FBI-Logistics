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

import com.example.demoapp.databinding.RowDomDryBinding;
import com.example.demoapp.model.DomDry;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.dom.dom_dry.DialogDomDryDetail;

import java.util.List;

public class DryDomAdapter extends RecyclerView.Adapter<DryDomAdapter.DryViewHolder> {
    private final Context context;
    private List<DomDry> listDry;

    @NonNull
    @Override
    public DryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DryViewHolder(RowDomDryBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DryViewHolder holder, int position) {

        DomDry domDry = listDry.get(position);
        holder.bind(domDry);

        holder.binding.rowCvDomDry.setOnClickListener(view -> goToDetail(domDry));

    }

    public void goToDetail(DomDry domDry) {
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        DialogFragment dialogFragment = DialogDomDryDetail.getInstance();

        Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.DOM_DRY_OBJECT, domDry);

        dialogFragment.setArguments(bundle);
        dialogFragment.show(fm, "Detail Dom Dry");
    }

    public DryDomAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDomDry(List<DomDry> list) {
        this.listDry = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (listDry != null) {
            return listDry.size();
        }
        return 0;
    }

    public void filterList(List<DomDry> filteredList) {
        listDry = filteredList;
        notifyDataSetChanged();
    }

    public static class DryViewHolder extends RecyclerView.ViewHolder {
        private final RowDomDryBinding binding;

        public DryViewHolder(@NonNull RowDomDryBinding root) {
            super(root.getRoot());
            binding = root;
        }

        public void bind(DomDry domDry) {
            binding.tvDomDryProductStt.setText(domDry.getStt());
            binding.tvDomDryProductName.setText(domDry.getName());
            binding.tvDomDryWeight.setText(domDry.getWeight());
            binding.tvDomDryQuantityPallet.setText(domDry.getQuantityPallet());
            binding.tvDomDryQuantityCarton.setText(domDry.getQuantityCarton());
            binding.tvDomDryAddressReceive.setText(domDry.getAddressReceive());
            binding.tvDomDryAddressDelivery.setText(domDry.getAddressDelivery());
            binding.tvDomDryLength.setText(domDry.getLength());
            binding.tvRowDomDryHeight.setText(domDry.getHeight());
            binding.tvRowDomDryWidth.setText(domDry.getWidth());
        }
    }
}
