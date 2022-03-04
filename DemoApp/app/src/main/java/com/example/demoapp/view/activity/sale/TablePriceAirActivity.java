package com.example.demoapp.view.activity.sale;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demoapp.R;
import com.example.demoapp.adapter.sale.PriceListAIRSaleAdapter;
import com.example.demoapp.databinding.ActivityTablePriceAirBinding;
import com.example.demoapp.model.AirExport;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.air.air_export.InsertAirExportDialog;
import com.example.demoapp.viewmodel.AirExportViewModel;
import com.example.demoapp.viewmodel.CommunicateViewModel;

import java.util.ArrayList;
import java.util.List;

public class TablePriceAirActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityTablePriceAirBinding tablePriceAirBinding;
    private String month = "";
    private String continent = "";
    private  SearchView searchView;
    PriceListAIRSaleAdapter priceListAdapter;

    private AirExportViewModel mAirViewModel;

    private LinearLayoutManager linearLayoutManager;

    private List<AirExport> airList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tablePriceAirBinding = ActivityTablePriceAirBinding.inflate(getLayoutInflater());
        View view = tablePriceAirBinding.getRoot();
        setSupportActionBar(tablePriceAirBinding.toolbar);

        priceListAdapter = new PriceListAIRSaleAdapter(this);
        mAirViewModel = new ViewModelProvider(this).get(AirExportViewModel.class);

        linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);
        CommunicateViewModel mCommunicateViewModel = new ViewModelProvider(this).get(CommunicateViewModel.class);

        mCommunicateViewModel.needReloading.observe(this, needLoading -> {
            if (needLoading) {
                Log.d("onresume", String.valueOf(needLoading.toString()));
                onResume();
            }
        });

        getDataAIR();
        setAdapterItems();
        setUpButtons();
        setContentView(view);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);
    }

    private void setUpButtons() {
//        tablePriceAirBinding.fragmentAirFabActivity.setOnClickListener(this);
    }

    private void setAdapterItems() {
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        tablePriceAirBinding.autoCompleteMonth.setAdapter(adapterItemsMonth);
        tablePriceAirBinding.autoCompleteContinent.setAdapter(adapterItemsContinent);

        tablePriceAirBinding.autoCompleteMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                month = adapterView.getItemAtPosition(i).toString();
                setDataForRecyclerView(month, continent);
            }
        });

        tablePriceAirBinding.autoCompleteContinent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                continent = adapterView.getItemAtPosition(i).toString();
                setDataForRecyclerView(month, continent);
            }
        });

    }

    private void getDataAIR() {
        airList = new ArrayList<>();
        mAirViewModel.getLclList().observe(this, detailsPojoAir -> {
            this.airList = detailsPojoAir;
        });
    }
    public void setDataForRecyclerView(String m, String c) {
        if (!m.isEmpty() && !c.isEmpty()) {

            priceListAdapter.setDataAir(prepareDataForRecyclerView(m, c));
            tablePriceAirBinding.priceListRcvSaleAir.setAdapter(priceListAdapter);
            tablePriceAirBinding.priceListRcvSaleAir.setLayoutManager(linearLayoutManager);


        }

    }

    private List<AirExport> prepareDataForRecyclerView(String m, String c) {
        List<AirExport> list = new ArrayList<>();
        try {
            for (AirExport a : airList) {
                if (a.getMonth().equalsIgnoreCase(m) && a.getContinent().equalsIgnoreCase(c)) {
                    list.add(a);
                }
            }
        } catch (NullPointerException nullPointerException) {
            Toast.makeText(getApplicationContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
        }
        return list;
    }

    public List<AirExport> prepareDataForResume(String m, String c, List<AirExport> list) {
        // reset a list when user choose different
        List<AirExport> subList = new ArrayList<>();
        try {
            for (AirExport air : list) {
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
    protected void onResume() {
        super.onResume();

        mAirViewModel.getLclList().observe(this, airs -> {
            priceListAdapter.setDataAir(prepareDataForResume(month, continent, airs));
        });
        tablePriceAirBinding.priceListRcvSaleAir.setAdapter(priceListAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_air_fab_activity:
                DialogFragment dialogFragment = InsertAirExportDialog.insertDiaLogAIR();
                dialogFragment.show(getSupportFragmentManager(), "Insert Dialog");

                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) { ;
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
        List<AirExport> filteredList = new ArrayList<>();
        for( AirExport airExport: prepareDataForRecyclerView(month, continent)){
            if(airExport.getAol().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(airExport);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        }else {
            priceListAdapter.filterList(filteredList);
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