package com.example.demoapp.adapter.driver;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.Models.objApplication.objAreaCode;
import com.example.demoapp.R;

import java.util.ArrayList;


public class aRclvSelectCountries extends RecyclerView.Adapter<aRclvSelectCountries.ViewHolder> {
    private final Context context;

    private ArrayList<objAreaCode> items;
    private actionClick listener;

    public void setOnClickListener(actionClick listener) {
        this.listener = listener;
    }

    public interface actionClick {
        void onClick(int position);
    }

    public aRclvSelectCountries(Context context, ArrayList<objAreaCode> areaCodes) {
        this.items = areaCodes;
        this.context = context;
    }

    public String getCountriesName(int position){
        return items.get(position).getCountriesName();
    }

    public String getAreaCode(int position){
        return items.get(position).getId();
    }

    public objAreaCode getObjectAreaCode(int position){
        return items.get(position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_countries, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        objAreaCode item = items.get(position);

        holder.tvCountriesName.setText(item.getCountriesName());
        holder.tvAreaCode.setText(item.getId());

        holder.lnlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 0;
        }
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAreaCode;
        private TextView tvCountriesName;
        private LinearLayout lnlMain;

        private ViewHolder(View itemView) {
            super(itemView);
            tvAreaCode = itemView.findViewById(R.id.tvAreaCode);
            tvCountriesName = itemView.findViewById(R.id.tvCountriesName);

            lnlMain = itemView.findViewById(R.id.lnlMain);

        }
    }


}