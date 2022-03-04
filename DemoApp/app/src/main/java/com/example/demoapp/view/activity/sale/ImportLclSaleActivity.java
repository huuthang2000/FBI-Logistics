package com.example.demoapp.view.activity.sale;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.adapter.sale.PriceListImportLclSaleAdapter;
import com.example.demoapp.databinding.ActivityImportLclSaleBinding;
import com.example.demoapp.model.ImportLcl;
import com.example.demoapp.model.Log;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.ImportLclViewModel;

import java.util.ArrayList;
import java.util.List;

public class ImportLclSaleActivity extends AppCompatActivity {

    private ActivityImportLclSaleBinding binding;
    private String month = "";
    private String continent = "";

    private SearchView searchView;
    List<ImportLcl> listPriceList = new ArrayList<>();
    private PriceListImportLclSaleAdapter priceListAdapter;
    private ImportLclViewModel mImportViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityImportLclSaleBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        priceListAdapter = new PriceListImportLclSaleAdapter(this);
        mImportViewModel = new ViewModelProvider(this).get(ImportLclViewModel.class);

        CommunicateViewModel mCommunicateViewModel = new ViewModelProvider(this).get(CommunicateViewModel.class);

        mCommunicateViewModel.needReloading.observe(this, needLoading -> {
            if (needLoading) {
                onResume();
            }
        });

        setSupportActionBar(binding.toolbar);
        setAdapterItems();
        getAllData();
        setContentView(view);
    }

    public void setAdapterItems() {
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        binding.autoCompleteMonth.setAdapter(adapterItemsMonth);
        binding.autoCompleteContinent.setAdapter(adapterItemsContinent);

        binding.autoCompleteMonth.setOnItemClickListener((adapterView, view, i, l) -> {
            month = adapterView.getItemAtPosition(i).toString();
            setDataForRecyclerView(month, continent);
        });

        binding.autoCompleteContinent.setOnItemClickListener((adapterView, view, i, l) -> {
            continent = adapterView.getItemAtPosition(i).toString();
            setDataForRecyclerView(month, continent);
        });

    }

    /**
     * this method will set data for recycler view
     *
     * @param m month
     * @param c continent
     */
    public void setDataForRecyclerView(String m, String c) {
        if (!m.isEmpty() && !c.isEmpty()) {
            priceListAdapter.setImports(prepareDataForRecyclerView(month, continent));
            binding.priceListRcv.setLayoutManager(new LinearLayoutManager(this));
            binding.priceListRcv.setAdapter(priceListAdapter);
        }
    }

    /**
     * this method will filter list data by month and continent
     *
     * @param m month
     * @param c continent
     * @return get list by month and continent
     */
    public List<ImportLcl> prepareDataForRecyclerView(String m, String c) {
        // reset a list when user choose different
        List<ImportLcl> list = new ArrayList<>();

        for (ImportLcl imp : listPriceList) {

            if (imp.getMonth().equalsIgnoreCase(m) && imp.getContinent().equalsIgnoreCase(c)) {
                list.add(imp);
            }
        }
        return list;
    }

    public List<ImportLcl> prepareDataForResume(String m, String c, List<ImportLcl> list) {
        // reset a list when user choose different
        List<ImportLcl> subList = new ArrayList<>();

        try {
            for (ImportLcl imp : list) {
                if (imp.getMonth().equalsIgnoreCase(m) && imp.getContinent().equalsIgnoreCase(c)) {
                    subList.add(imp);
                }
            }

        } catch (NullPointerException nullPointerException) {
            Toast.makeText(getApplicationContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
        }
        return subList;
    }


    /**
     * this method will get all data from database
     */
    public void getAllData() {

        try {
            mImportViewModel.getImportList().observe(this, detailsPojoImports ->
                    this.listPriceList = detailsPojoImports);
        } catch (NullPointerException exception) {
            Toast.makeText(getApplicationContext(), exception.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        priceListAdapter = new PriceListImportLclSaleAdapter(this);
        mImportViewModel.getImportList().observe(this, imp ->
                priceListAdapter.setImports(prepareDataForResume(month, continent, imp)));

        binding.priceListRcv.setAdapter(priceListAdapter);
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
        List<ImportLcl> filteredList = new ArrayList<>();
        for( ImportLcl importLcl: prepareDataForRecyclerView(month, continent)){
            if(importLcl.getPol().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(importLcl);
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