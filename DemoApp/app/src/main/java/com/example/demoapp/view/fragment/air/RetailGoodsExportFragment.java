package com.example.demoapp.view.fragment.air;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.adapter.PriceListRetailGoddsAdapter;
import com.example.demoapp.databinding.FragmentRetailGoodsExportBinding;
import com.example.demoapp.model.RetailGoods;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.air.retailgoods.InsertRetailGoodsDialog;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.RetailGoodsViewModel;

import java.util.ArrayList;
import java.util.List;


public class RetailGoodsExportFragment extends Fragment implements View.OnClickListener {
    private FragmentRetailGoodsExportBinding mRetailGoodsDetailBinding;

    private String month = "";
    private String continent = "";
    private PriceListRetailGoddsAdapter mPriceListRetailGoddsAdapter;
    private RetailGoodsViewModel mRetailGoodsViewModel;
    private List<RetailGoods> retailGoodsList = new ArrayList<>();
    private SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRetailGoodsDetailBinding = FragmentRetailGoodsExportBinding.inflate(inflater, container, false);
        View view = mRetailGoodsDetailBinding.getRoot();

        mPriceListRetailGoddsAdapter = new PriceListRetailGoddsAdapter(getContext());
        mRetailGoodsViewModel = new ViewModelProvider(this).get(RetailGoodsViewModel.class);
        CommunicateViewModel mCommunicateViewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);
        mCommunicateViewModel.needReloading.observe(getViewLifecycleOwner(), needLoading ->{
            if(needLoading){
                Log.d("onresume", String.valueOf(needLoading.toString()));
                onResume();
            }
        });

        setHasOptionsMenu(true);
        getDataRetailGoods();
        setAdapterItems();
        setUpbutons();
        return view;
    }

    private void setAdapterItems() {
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        mRetailGoodsDetailBinding.autoCompleteMonthRetail.setAdapter(adapterItemsMonth);
        mRetailGoodsDetailBinding.autoCompleteContinentRetail.setAdapter(adapterItemsContinent);

        mRetailGoodsDetailBinding.autoCompleteMonthRetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                month = adapterView.getItemAtPosition(i).toString();
                setDataForRecyclerView(month, continent);

            }
        });

        mRetailGoodsDetailBinding.autoCompleteContinentRetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                continent = adapterView.getItemAtPosition(i).toString();
                setDataForRecyclerView(month, continent);
            }
        });

    }

    private void setDataForRecyclerView(String m, String c) {
        if(!m.isEmpty() && !c.isEmpty()){
            mPriceListRetailGoddsAdapter.setDataRetailGoods(prepareDataForRecycleView(m, c));
            mRetailGoodsDetailBinding.priceListRcvRetail.setAdapter(mPriceListRetailGoddsAdapter);
            mRetailGoodsDetailBinding.priceListRcvRetail.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    private List<RetailGoods> prepareDataForRecycleView(String m, String c) {
        List<RetailGoods> list = new ArrayList<>();
        try {
            for(RetailGoods rg : retailGoodsList){
                if(rg.getMonth().equalsIgnoreCase(m) && rg.getContinent().equalsIgnoreCase(c)){
                    list.add(rg);
                }
            }
        }catch (NullPointerException nullPointerException){
            Toast.makeText(getContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
        }

        return list;
    }

    private void setUpbutons() {
        mRetailGoodsDetailBinding.fragmentFabRetailGoods.setOnClickListener(this);
    }

    private void getDataRetailGoods() {
        try {
            retailGoodsList = new ArrayList<>();
            mRetailGoodsViewModel.getRetailGoodsList().observe(getViewLifecycleOwner(), detailsPojoRetailGoods ->{
                this.retailGoodsList = sortRetailGoods(detailsPojoRetailGoods);
            });
        }catch (NullPointerException nullPointerException){
            Toast.makeText(getContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
        }

    }

    private List<RetailGoods> sortRetailGoods(List<RetailGoods> list) {
        List<RetailGoods> result = new ArrayList<>();
        for (int i = list.size()-1; i>=0; i--){
            result.add(list.get(i));
        }
        return result;
    }
    public List<RetailGoods> preparedataForResume(String m, String c, List<RetailGoods> list){
        List<RetailGoods> subList = new ArrayList<>();
        try {
            for(RetailGoods rg : retailGoodsList){
                if(rg.getMonth().equalsIgnoreCase(m) && rg.getContinent().equalsIgnoreCase(c)){
                    subList.add(rg);
                }
            }
        }catch (NullPointerException nullPointerException){
            Toast.makeText(getContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
        }
        return subList;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRetailGoodsViewModel.getRetailGoodsList().observe(getViewLifecycleOwner(), retailGoods ->{
            mPriceListRetailGoddsAdapter.setDataRetailGoods(preparedataForResume(month,continent, retailGoods));
        });
        mRetailGoodsDetailBinding.priceListRcvRetail.setAdapter(mPriceListRetailGoddsAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fragment_fab_retail_goods:
                DialogFragment dialogFragment = InsertRetailGoodsDialog.insertDialogRetailGoods();
                dialogFragment.show(getParentFragmentManager(), "Insert Dialog");
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_IF_ROOM);
        searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
//                priceListAirImportAdapter.getFilter().filter(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                priceListAirImportAdapter.setDataAir(prepareDataForRecyclerView(month,continent));
                filter(s);
                return false;
            }
        });

    }
    private void filter(String text){
        List<RetailGoods> filteredList = new ArrayList<>();
        for( RetailGoods retailGoods: prepareDataForRecycleView(month, continent)){
            if(retailGoods.getPol().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(retailGoods);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        }else {
            mPriceListRetailGoddsAdapter.filterList(filteredList);
        }
    }
}