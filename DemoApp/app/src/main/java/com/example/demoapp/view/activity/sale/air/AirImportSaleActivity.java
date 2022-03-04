package com.example.demoapp.view.activity.sale.air;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.adapter.sale.PriceListAIRSaleImportAdapter;
import com.example.demoapp.databinding.ActivityAirImportSaleBinding;
import com.example.demoapp.model.AirImport;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.AirImportViewModel;
import com.example.demoapp.viewmodel.CommunicateViewModel;

import java.util.ArrayList;
import java.util.List;

public class AirImportSaleActivity extends AppCompatActivity {

    private ActivityAirImportSaleBinding mAirImportSaleBinding;
    private String month = "";
    private String continent = "";
    private PriceListAIRSaleImportAdapter priceListAirImportAdapter;
    private AirImportViewModel mAirImportViewModel;
    private List<AirImport> airImportList = new ArrayList<>();
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAirImportSaleBinding = ActivityAirImportSaleBinding.inflate(getLayoutInflater());
        View view = mAirImportSaleBinding.getRoot();

        setSupportActionBar(mAirImportSaleBinding.toolbar);
        priceListAirImportAdapter = new PriceListAIRSaleImportAdapter(this);
        mAirImportViewModel = new ViewModelProvider(this).get(AirImportViewModel.class);

        CommunicateViewModel mCommunicateViewModel = new ViewModelProvider(this).get(CommunicateViewModel.class);
        mCommunicateViewModel.needReloading.observe(this, needLoading ->{
            if (needLoading) {
                Log.d("onresume", String.valueOf(needLoading.toString()));
                onResume();
            }
        });
        getDataAirImport();
        setAdapterItems();
        setContentView(view);
    }
    private void setAdapterItems() {
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        mAirImportSaleBinding.autoCompleteMonth.setAdapter(adapterItemsMonth);
        mAirImportSaleBinding.autoCompleteContinent.setAdapter(adapterItemsContinent);

        mAirImportSaleBinding.autoCompleteMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                month = adapterView.getItemAtPosition(i).toString();
                setDataForRecyclerView(month, continent);
            }
        });

        mAirImportSaleBinding.autoCompleteContinent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                continent = adapterView.getItemAtPosition(i).toString();
                setDataForRecyclerView(month, continent);
            }
        });

    }

    private void setDataForRecyclerView(String month, String continent) {
        if(!month.isEmpty() && !continent.isEmpty()){
            priceListAirImportAdapter.setDataAir(prepareDataForRecyclerView(month, continent));
            mAirImportSaleBinding.priceListRcv.setAdapter(priceListAirImportAdapter);
            mAirImportSaleBinding.priceListRcv.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        }
    }

    private List<AirImport> prepareDataForRecyclerView(String m, String c) {
        List<AirImport> list = new ArrayList<>();
        try {
            for (AirImport a : airImportList) {
                if (a.getMonth().equalsIgnoreCase(m) && a.getContinent().equalsIgnoreCase(c)) {
                    list.add(a);
                }
            }
        } catch (NullPointerException nullPointerException) {
            Toast.makeText(getApplicationContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
        }
        return list;
    }

    public List<AirImport> prepareDataForResume(String m, String c, List<AirImport> list) {

        List<AirImport> subList = new ArrayList<>();
        try {
            for (AirImport air : list) {
                if (air.getMonth().equalsIgnoreCase(m) && air.getContinent().equalsIgnoreCase(c)) {
                    subList.add(air);
                }
            }
        }catch (NullPointerException nullPointerException){
            Toast.makeText(getApplicationContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
        }

        return subList;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAirImportViewModel.getAirImportList().observe(this, airs -> {
            priceListAirImportAdapter.setDataAir(prepareDataForResume(month, continent, airs));
        });
        mAirImportSaleBinding.priceListRcv.setAdapter(priceListAirImportAdapter);
    }



    private void getDataAirImport() {
        airImportList = new ArrayList<>();
        mAirImportViewModel.getAirImportList().observe(this, detailsPojoAir ->{
            this.airImportList = sortAirImport(detailsPojoAir);
        });

    }
    public List<AirImport> sortAirImport(List<AirImport> list){
        List<AirImport> result = new ArrayList<>();
        for (int i = list.size()-1; i>=0 ; i--){
            result.add(list.get(i));
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return  true;
    }

    private void filter(String text){
        List<AirImport> filteredList = new ArrayList<>();
        for( AirImport airImport: prepareDataForRecyclerView(month, continent)){
            if(airImport.getAol().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(airImport);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        }else {
            priceListAirImportAdapter.filterList(filteredList);
        }
    }

    @Override
    public void onBackPressed() {
        if(!searchView.isIconified()){
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();

    }
}