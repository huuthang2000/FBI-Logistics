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

import com.example.demoapp.databinding.RowDomImportBinding;
import com.example.demoapp.model.DomImport;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.dom.dom_import.DialogDomImportDetail;

import java.util.List;

public class ImportDomAdapter extends RecyclerView.Adapter<ImportDomAdapter.ImportViewHolder> {
    private final Context context;
    private List<DomImport> listImport;

    @NonNull
    @Override
    public ImportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImportViewHolder(RowDomImportBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImportViewHolder holder, int  position) {

        DomImport domImport = listImport.get(position);
        holder.bind(domImport);

        holder.binding.rowCvDomImport.setOnClickListener(view -> goToDetail(domImport));

    }

    public void goToDetail(DomImport domImport){
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        DialogFragment dialogFragment = DialogDomImportDetail.getInstance();

        Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.DOM_IMPORT_OBJECT, domImport);

        dialogFragment.setArguments(bundle);
        dialogFragment.show( fm,"Detail Dom Import");
    }

    public ImportDomAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDomImport(List<DomImport> list) {
        this.listImport = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (listImport != null) {
            return listImport.size();
        }
        return 0;
    }

    public void filterList(List<DomImport> filteredList) {
        listImport = filteredList;
        notifyDataSetChanged();
    }

    public static class ImportViewHolder extends RecyclerView.ViewHolder {
        private final RowDomImportBinding binding;

        public ImportViewHolder(@NonNull RowDomImportBinding root) {
            super(root.getRoot());
            binding = root;
        }

        public void bind(DomImport domImport) {
            binding.tvDomImportStt.setText(domImport.getStt());
            binding.tvDomImportProductName.setText(domImport.getName());
            binding.tvDomImportWeight.setText(domImport.getWeight());
            binding.tvDomImportQuantity.setText(domImport.getQuantity());
            binding.tvDomImportTemp.setText(domImport.getTemp());
            binding.tvDomImportAddress.setText(domImport.getAddress());
            binding.tvDomImportPortReceive.setText(domImport.getPortReceive());
            binding.tvDomImportLength.setText(domImport.getLength());
            binding.tvRowDomImportHeight.setText(domImport.getHeight());
            binding.tvRowDomImportWidth.setText(domImport.getWidth());
        }
    }
}
