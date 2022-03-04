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
import com.example.demoapp.adapter.PriceListAirImportAdapter;
import com.example.demoapp.databinding.FragmentAirImportBinding;
import com.example.demoapp.model.AirImport;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.air.air_import.InsertAirImportDialog;
import com.example.demoapp.viewmodel.AirImportViewModel;
import com.example.demoapp.viewmodel.CommunicateViewModel;

import java.util.ArrayList;
import java.util.List;


public class AirImportFragment extends Fragment implements View.OnClickListener {

    private FragmentAirImportBinding mAirImportBinding;

    private String month = "";
    private String continent = "";
    private SearchView searchView;
    private PriceListAirImportAdapter priceListAirImportAdapter;
    private AirImportViewModel mAirImportViewModel;
    private List<AirImport> airImportList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mAirImportBinding = FragmentAirImportBinding.inflate(inflater, container, false);
        View view = mAirImportBinding.getRoot();

        priceListAirImportAdapter = new PriceListAirImportAdapter(getContext());
        mAirImportViewModel = new ViewModelProvider(this).get(AirImportViewModel.class);

        CommunicateViewModel mCommunicateViewModel = new ViewModelProvider(getActivity()).get(CommunicateViewModel.class);
        mCommunicateViewModel.needReloading.observe(getViewLifecycleOwner(), needLoading ->{
            if (needLoading) {
                Log.d("onresume", String.valueOf(needLoading.toString()));
                onResume();
            }
        });
        setHasOptionsMenu(true);

        getDataAirImport();
        setAdapterItems();
        setUpButtons();
        return view;
    }

    private void setAdapterItems() {
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<String>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        mAirImportBinding.autoCompleteMonth.setAdapter(adapterItemsMonth);
        mAirImportBinding.autoCompleteContinent.setAdapter(adapterItemsContinent);

        mAirImportBinding.autoCompleteMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                month = adapterView.getItemAtPosition(i).toString();
                setDataForRecyclerView(month, continent);
        }
        });

        mAirImportBinding.autoCompleteContinent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
            mAirImportBinding.priceListRcv.setAdapter(priceListAirImportAdapter);
            mAirImportBinding.priceListRcv.setLayoutManager(new LinearLayoutManager(getContext()));
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
            Toast.makeText(getContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(getContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
        }

        return subList;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAirImportViewModel.getAirImportList().observe(getViewLifecycleOwner(), airs -> {
            priceListAirImportAdapter.setDataAir(prepareDataForResume(month, continent, airs));
        });
        mAirImportBinding.priceListRcv.setAdapter(priceListAirImportAdapter);
    }

    private void setUpButtons() {
        mAirImportBinding.fragmentAirFab.setOnClickListener(this);
    }

    private void getDataAirImport() {
        airImportList = new ArrayList<>();
        mAirImportViewModel.getAirImportList().observe(getViewLifecycleOwner(), detailsPojoAir ->{
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_air_fab:
                DialogFragment dialogFragment = InsertAirImportDialog.insertDiaLogAIRImport();
                dialogFragment.show(getParentFragmentManager(), "Insert Dialog");

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
        List<AirImport> filteredList = new ArrayList<>();
        for( AirImport airImport: prepareDataForRecyclerView(month, continent)){
            if(airImport.getAol().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(airImport);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        }else {
            priceListAirImportAdapter.filterList(filteredList);
        }
    }
}