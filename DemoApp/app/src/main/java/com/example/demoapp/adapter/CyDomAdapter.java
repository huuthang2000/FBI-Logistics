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

import com.example.demoapp.databinding.RowDomCyBinding;
import com.example.demoapp.model.DomCy;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.dom.dom_cy.DialogDomCyDetail;

import java.util.List;

public class CyDomAdapter extends RecyclerView.Adapter<CyDomAdapter.CyViewHolder> {
    private final Context context;
    private List<DomCy> listDry;

    @NonNull
    @Override
    public CyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CyViewHolder(RowDomCyBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CyViewHolder holder, int position) {

        DomCy domCy = listDry.get(position);
        holder.bind(domCy);

        holder.binding.rowCvDomCy.setOnClickListener(view -> goToDetail(domCy));

    }

    public void goToDetail(DomCy domCy) {
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        DialogFragment dialogFragment = DialogDomCyDetail.getInstance();

        Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.DOM_CY_OBJECT, domCy);

        dialogFragment.setArguments(bundle);
        dialogFragment.show(fm, "Detail Dom Cy");
    }

    public CyDomAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDomCy(List<DomCy> list) {
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

    public void filterList(List<DomCy> filteredList) {
        listDry = filteredList;
        notifyDataSetChanged();
    }

    public static class CyViewHolder extends RecyclerView.ViewHolder {
        private final RowDomCyBinding binding;

        public CyViewHolder(@NonNull RowDomCyBinding root) {
            super(root.getRoot());
            binding = root;
        }

        public void bind(DomCy domCy) {
            binding.tvDomCyStationStt.setText(domCy.getStt());
            binding.tvDomCyStationGo.setText(domCy.getStationGo());
            binding.tvDomCyStationCome.setText(domCy.getStationCome());
            binding.tvDomCyName.setText(domCy.getName());
            binding.tvDomCyWeight.setText(domCy.getWeight());
            binding.tvDomCyQuantity.setText(domCy.getQuantity());
            binding.tvDomCyEtd.setText(domCy.getEtd());

        }
    }
}
