package com.example.demoapp.view.fragment.log;

import android.os.Bundle;
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
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.adapter.PriceListLogAdapter;
import com.example.demoapp.databinding.FragmentLogBinding;
import com.example.demoapp.model.Log;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.log.InsertLogFragment;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.LogViewModel;

import java.util.ArrayList;
import java.util.List;


public class LogFragment extends Fragment implements View.OnClickListener {

    private FragmentLogBinding logBinding;


    private ArrayAdapter<String> adapterItemsMonth, adapterItemsImportAndExport;
    private String month = "";
    private String importAndExport = "";
    private SearchView searchView;
    private PriceListLogAdapter mListLogAdapter;
    private LogViewModel mLogViewModel;

    private List<Log> mlistLog = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        logBinding =  FragmentLogBinding.inflate(inflater,container,false);
        View view = logBinding.getRoot();

        mListLogAdapter = new PriceListLogAdapter(getContext());
        mLogViewModel = new ViewModelProvider(this).get(LogViewModel.class);
        // Xử lý cập nhập insert
        CommunicateViewModel mCommunicateViewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);

        mCommunicateViewModel.needReloading.observe(getViewLifecycleOwner(), needLoading ->{
            if(needLoading){
                onResume();
            }
        });

        setHasOptionsMenu(true);
        getDataLog();
        setAdapterItems();
        setUpButtons();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    private void setUpButtons() {
        logBinding.fragmentLogFab.setOnClickListener(this);
    }

    private void setAdapterItems() {
        adapterItemsMonth = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        adapterItemsImportAndExport= new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, Constants.ITEMS_IMPORTANDEXPORT);

        logBinding.autoCompleteMonthLog.setAdapter(adapterItemsMonth);
        logBinding.autoCompleteContinentLog.setAdapter(adapterItemsImportAndExport);

        logBinding.autoCompleteMonthLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                month = adapterView.getItemAtPosition(i).toString();
                setDataForRecyclerView(month, importAndExport);
            }
        });

        logBinding.autoCompleteContinentLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                importAndExport = adapterView.getItemAtPosition(i).toString();
                setDataForRecyclerView(month, importAndExport);
            }
        });
    }

    public void setDataForRecyclerView(String m, String c) {
        if (!m.isEmpty() && !c.isEmpty()) {
            logBinding.priceListRcv.setHasFixedSize(true);

            logBinding.priceListRcv.setLayoutManager(new LinearLayoutManager(getContext()));

            mListLogAdapter.setDataLog(prepareDataForRecyclerView(m,c));

            logBinding.priceListRcv.setAdapter(mListLogAdapter);
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


    private void getDataLog() {
        mlistLog = new ArrayList<>();
        mLogViewModel.getLogList().observe(getViewLifecycleOwner(), detailsPojoLog -> {
            this.mlistLog = detailsPojoLog;
        });
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
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        mLogViewModel.getLogList().observe(getViewLifecycleOwner(), airs -> {
            mListLogAdapter.setDataLog( prepareDataForResume(month, importAndExport, airs));
        });
        logBinding.priceListRcv.setAdapter(mListLogAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fragment_log_fab:
                DialogFragment dialogFragment = InsertLogFragment.insertDiaLogLog();
                dialogFragment.show(getParentFragmentManager(),"Insert dialog Log");
                break;
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

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                filter(s);
                return false;
            }
        });

    }
    private void filter(String text){
        List<Log> filteredList = new ArrayList<>();
        for( Log log: prepareDataForRecyclerView(month, importAndExport)){
            if(log.getCangdi().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(log);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        }else {
            mListLogAdapter.filterList(filteredList);
        }
    }
}