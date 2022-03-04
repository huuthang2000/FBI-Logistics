package com.example.demoapp.view.activity.sale;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.adapter.sale.PriceListLogSaleAdapter;
import com.example.demoapp.databinding.ActivityLogBinding;
import com.example.demoapp.model.Log;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.log.InsertLogFragment;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.LogViewModel;

import java.util.ArrayList;
import java.util.List;

public class LogActivity extends AppCompatActivity implements View.OnClickListener {

    private ArrayAdapter<String> adapterItemsMonth, adapterItemsImportAndExport;
    private String month = "";
    private String importAndExport = "";
    private SearchView searchView;

    private PriceListLogSaleAdapter mListLogAdapter;
    private LogViewModel mLogViewModel;

    private List<Log> mlistLog = new ArrayList<>();

    private ActivityLogBinding mLogBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLogBinding = ActivityLogBinding.inflate(getLayoutInflater());
        View view = mLogBinding.getRoot();
        mListLogAdapter = new PriceListLogSaleAdapter(this);
        mLogViewModel = new ViewModelProvider(this).get(LogViewModel.class);
        // Xử lý cập nhập insert
        CommunicateViewModel mCommunicateViewModel = new ViewModelProvider(this).get(CommunicateViewModel.class);

        mCommunicateViewModel.needReloading.observe(this, needLoading ->{
            if(needLoading){
                onResume();
            }
        });

        setSupportActionBar(mLogBinding.toolbar);
        getDataLog();
        setAdapterItems();
//        setUpButtons();
        setContentView(view);
    }

    private void setUpButtons() {
        mLogBinding.fragmentLogFab.setOnClickListener(this);
    }

    private void setAdapterItems() {
        adapterItemsMonth = new ArrayAdapter<String>(this, R.layout.dropdown_item, Constants.ITEMS_MONTH);
        adapterItemsImportAndExport= new ArrayAdapter<String>(this, R.layout.dropdown_item, Constants.ITEMS_IMPORTANDEXPORT);

        mLogBinding.autoCompleteMonthLog.setAdapter(adapterItemsMonth);
        mLogBinding.autoCompleteContinentLog.setAdapter(adapterItemsImportAndExport);

        mLogBinding.autoCompleteMonthLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                month = adapterView.getItemAtPosition(i).toString();
                setDataForRecyclerView(month, importAndExport);
            }
        });

        mLogBinding.autoCompleteContinentLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                importAndExport = adapterView.getItemAtPosition(i).toString();
                setDataForRecyclerView(month, importAndExport);
            }
        });
    }

    public void setDataForRecyclerView(String m, String c) {
        if (!m.isEmpty() && !c.isEmpty()) {
            mLogBinding.priceListRcv.setHasFixedSize(true);

            mLogBinding.priceListRcv.setLayoutManager(new LinearLayoutManager(this));

            mListLogAdapter.setDataLog(prepareDataForRecyclerView(m,c));

            mLogBinding.priceListRcv.setAdapter(mListLogAdapter);
        }
    }

    private List<Log> prepareDataForRecyclerView(String m, String c) {
        List<Log> list = new ArrayList<>();

        for (Log a : mlistLog) {
            if (a.getMonth().equalsIgnoreCase(m) && a.getImportorexport().equalsIgnoreCase(c)) {
                list.add(a);
            }
        }
        return list;
    }


    public List<Log> prepareDataForResume(String m, String c, List<Log> list) {
        // reset a list when user choose different
        List<Log> subList = new ArrayList<>();
        for (Log log : list) {
            if (log.getMonth().equalsIgnoreCase(m) && log.getImportorexport().equalsIgnoreCase(c)) {
                subList.add(log);
            }
        }
        return subList;
    }
    private void getDataLog() {
        mlistLog = new ArrayList<>();
        mLogViewModel.getLogList().observe(this, detailsPojoLog -> {
            this.mlistLog = detailsPojoLog;
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        mLogViewModel.getLogList().observe(this, airs -> {
            mListLogAdapter.setDataLog( prepareDataForResume(month, importAndExport, airs));
        });
        mLogBinding.priceListRcv.setAdapter(mListLogAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_log_fab:
                DialogFragment dialogFragment = InsertLogFragment.insertDiaLogLog();
                dialogFragment.show(getSupportFragmentManager(),"Insert dialog Log");
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
        List<Log> filteredList = new ArrayList<>();
        for( Log log: prepareDataForRecyclerView(month, importAndExport)){
            if(log.getCangdi().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(log);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        }else {
            mListLogAdapter.filterList(filteredList);
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