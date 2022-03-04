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

import com.example.demoapp.databinding.RowDomCySeaBinding;
import com.example.demoapp.model.DomCySea;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.dom.dom_cy_sea.DialogDomCySeaDetail;

import java.util.List;

public class CySeaDomAdapter extends RecyclerView.Adapter<CySeaDomAdapter.CySeaViewHolder> {
    private final Context context;
    private List<DomCySea> listCySea;

    @NonNull
    @Override
    public CySeaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CySeaViewHolder(RowDomCySeaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CySeaViewHolder holder, int position) {

        DomCySea domCySea = listCySea.get(position);
        holder.bind(domCySea);

        holder.binding.rowCvDomCySea.setOnClickListener(view -> goToDetail(domCySea));

    }

    public void goToDetail(DomCySea domCySea) {
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        DialogFragment dialogFragment = DialogDomCySeaDetail.getInstance();

        Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.DOM_CY_SEA_OBJECT, domCySea);

        dialogFragment.setArguments(bundle);
        dialogFragment.show(fm, "Detail Dom Cy Sea");
    }

    public CySeaDomAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDomCySea(List<DomCySea> list) {
        this.listCySea = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (listCySea != null) {
            return listCySea.size();
        }
        return 0;
    }

    public void filterList(List<DomCySea> filteredList) {
        listCySea = filteredList;
        notifyDataSetChanged();
    }

    public static class CySeaViewHolder extends RecyclerView.ViewHolder {
        private final RowDomCySeaBinding binding;

        public CySeaViewHolder(@NonNull RowDomCySeaBinding root) {
            super(root.getRoot());
            binding = root;
        }

        public void bind(DomCySea domCySea) {
            binding.tvDomCySeaPortStt.setText(domCySea.getStt());
            binding.tvDomCySeaPortGo.setText(domCySea.getPortGo());
            binding.tvDomCySeaPortCome.setText(domCySea.getPortCome());
            binding.tvDomCySeaName.setText(domCySea.getName());
            binding.tvDomCySeaWeight.setText(domCySea.getWeight());
            binding.tvDomCySeaQuantity.setText(domCySea.getQuantity());
            binding.tvDomCySeaEtd.setText(domCySea.getEtd());

        }
    }
}
