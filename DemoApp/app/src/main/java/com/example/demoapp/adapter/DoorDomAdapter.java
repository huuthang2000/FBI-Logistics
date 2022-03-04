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

import com.example.demoapp.databinding.RowDomDoorBinding;
import com.example.demoapp.model.DomDoor;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.dom.dom_door.DialogDomDoorDetail;

import java.util.List;

public class DoorDomAdapter extends RecyclerView.Adapter<DoorDomAdapter.DoorViewHolder> {
    private final Context context;
    private List<DomDoor> listDoor;

    @NonNull
    @Override
    public DoorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DoorViewHolder(RowDomDoorBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DoorDomAdapter.DoorViewHolder holder, int position) {

        DomDoor domDoor = listDoor.get(position);
        holder.bind(domDoor);

        holder.binding.rowCvDomDoor.setOnClickListener(view -> goToDetail(domDoor));

    }

    public void goToDetail(DomDoor domDoor) {
        FragmentActivity activity = (FragmentActivity) context;
        FragmentManager fm = activity.getSupportFragmentManager();
        DialogFragment dialogFragment = DialogDomDoorDetail.getInstance();

        Bundle bundle = new Bundle();

        bundle.putSerializable(Constants.DOM_DOOR_OBJECT, domDoor);

        dialogFragment.setArguments(bundle);
        dialogFragment.show(fm, "Detail Dom Door");
    }

    public DoorDomAdapter(Context context) {
        this.context = context;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDomDoor(List<DomDoor> list) {
        this.listDoor = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (listDoor != null) {
            return listDoor.size();
        }
        return 0;
    }

    public void filterList(List<DomDoor> filteredList) {
        listDoor = filteredList;
        notifyDataSetChanged();
    }

    public static class DoorViewHolder extends RecyclerView.ViewHolder {
        private final RowDomDoorBinding binding;

        public DoorViewHolder(@NonNull RowDomDoorBinding root) {
            super(root.getRoot());
            binding = root;
        }

        public void bind(DomDoor domDoor) {
            binding.tvDomDoorStationStt.setText(domDoor.getStt());
            binding.tvDomDoorStationGo.setText(domDoor.getStationGo());
            binding.tvDomDoorStationCome.setText(domDoor.getStationCome());
            binding.tvDomDoorAddressReceive.setText(domDoor.getAddressReceive());
            binding.tvDomDoorAddressDelivery.setText(domDoor.getAddressDelivery());
            binding.tvDomDoorName.setText(domDoor.getName());
            binding.tvDomDoorWeight.setText(domDoor.getWeight());
            binding.tvDomDoorQuantity.setText(domDoor.getQuantity());
            binding.tvDomDoorEtd.setText(domDoor.getEtd());

        }
    }
}
