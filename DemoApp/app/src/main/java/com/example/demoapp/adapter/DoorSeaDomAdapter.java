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

import com.example.demoapp.databinding.RowDomDoorSeaBinding;
import com.example.demoapp.model.DomDoorSea;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.dom.dom_door_sea.DialogDomDoorSeaDetail;

import java.util.List;

public class DoorSeaDomAdapter extends RecyclerView.Adapter<DoorSeaDomAdapter.DoorSeaViewHolder> {
    private final Context context;
    private List<DomDoorSea> listDoorSea;

    @NonNull
    @Override
    public DoorSeaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoorSeaViewHolder(RowDomDoorSeaBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DoorSeaDomAdapter.DoorSeaViewHolder holder, int position) {

        DomDoorSea domDoorSea = listDoorSea.get(position);
        holder.bind(domDoorSea);

        holder.binding.rowCvDomDoorSea.setOnClickListener(view -> goToDetail(domDoorSea));

    }

    public void goToDetail(DomDoorSea domDoorSea) {
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        DialogFragment dialogFragment = DialogDomDoorSeaDetail.getInstance();

        Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.DOM_DOOR_SEA_OBJECT, domDoorSea);

        dialogFragment.setArguments(bundle);
        dialogFragment.show(fm, "Detail Dom Door Sea");
    }

    public DoorSeaDomAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDomDoorSea(List<DomDoorSea> list) {
        this.listDoorSea = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (listDoorSea != null) {
            return listDoorSea.size();
        }
        return 0;
    }

    public void filterList(List<DomDoorSea> filteredList) {
        listDoorSea = filteredList;
        notifyDataSetChanged();
    }

    public static class DoorSeaViewHolder extends RecyclerView.ViewHolder {
        private final RowDomDoorSeaBinding binding;

        public DoorSeaViewHolder(@NonNull RowDomDoorSeaBinding root) {
            super(root.getRoot());
            binding = root;
        }

        public void bind(DomDoorSea domDoorSea) {
            binding.tvDomDoorSeaStt.setText(domDoorSea.getStt());
            binding.tvDomDoorSeaPortGo.setText(domDoorSea.getPortGo());
            binding.tvDomDoorSeaPortCome.setText(domDoorSea.getPortCome());
            binding.tvDomDoorSeaAddressReceive.setText(domDoorSea.getAddressReceive());
            binding.tvDomDoorSeaAddressDelivery.setText(domDoorSea.getAddressDelivery());
            binding.tvDomDoorSeaName.setText(domDoorSea.getName());
            binding.tvDomDoorSeaWeight.setText(domDoorSea.getWeight());
            binding.tvDomDoorSeaQuantity.setText(domDoorSea.getQuantity());
            binding.tvDomDoorSeaEtd.setText(domDoorSea.getEtd());

        }
    }
}
