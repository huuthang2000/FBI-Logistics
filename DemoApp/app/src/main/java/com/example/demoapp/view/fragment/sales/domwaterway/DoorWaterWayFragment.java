package com.example.demoapp.view.fragment.sales.domwaterway;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.adapter.sale.PriceListDoorSeaDomAdapter;
import com.example.demoapp.databinding.FragmentDoorWaterWayBinding;
import com.example.demoapp.model.DomDoorSea;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomDoorSeaViewModel;

import java.util.ArrayList;
import java.util.List;


public class DoorWaterWayFragment extends Fragment {

    private FragmentDoorWaterWayBinding binding;
    private DomDoorSeaViewModel mDomDoorSeaViewModel;
    private PriceListDoorSeaDomAdapter mDoorSeaDomAdapter;
    private SearchView searchView;

    private List<DomDoorSea> mDomDoorSeaList = new ArrayList<>();

    private String month = "";
    private String continent = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDoorWaterWayBinding.inflate(inflater, container, false);
        View view =binding.getRoot();
        mDoorSeaDomAdapter = new PriceListDoorSeaDomAdapter(getContext());
        mDomDoorSeaViewModel = new ViewModelProvider(this).get(DomDoorSeaViewModel.class);

        CommunicateViewModel mCommunicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);

        mCommunicateViewModel.needReloading.observe(getViewLifecycleOwner(), needLoading -> {
            if (needLoading) {
                onResume();
            }
        });

        setHasOptionsMenu(true);
        getAllData();
        setAutoComplete();
        return view;
    }

    public void setUpRecyclerView(String m, String c) {
        if (!m.isEmpty() && !c.isEmpty()) {
            mDoorSeaDomAdapter.setDomDoorSea(filterData(m, c));
            binding.rcvDomDoorSea.setAdapter(mDoorSeaDomAdapter);
            binding.rcvDomDoorSea.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    public List<DomDoorSea> filterData(String m, String c) {
        List<DomDoorSea> subList = new ArrayList<>();
        try {
            for (DomDoorSea domDoorSea : mDomDoorSeaList) {
                if (domDoorSea.getMonth().equalsIgnoreCase(m) && domDoorSea.getContinent().equalsIgnoreCase(c)) {
                    subList.add(domDoorSea);
                }
            }
        } catch (NullPointerException nullPointerException) {
            Toast.makeText(getContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
        }
        return subList;
    }

    public List<DomDoorSea> filterDataResume(String m, String c, List<DomDoorSea> list) {
        List<DomDoorSea> subList = new ArrayList<>();
        try {
            for (DomDoorSea domDoorSea : list) {
                if (domDoorSea.getMonth().equalsIgnoreCase(m) && domDoorSea.getContinent().equalsIgnoreCase(c)) {
                    subList.add(domDoorSea);
                }
            }
        } catch (NullPointerException nullPointerException) {
            Toast.makeText(getContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
        }
        return subList;
    }

    public void setAutoComplete() {
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        binding.autoDomMonth.setAdapter(adapterItemsMonth);
        binding.autoDomContinent.setAdapter(adapterItemsContinent);

        binding.autoDomMonth.setOnItemClickListener((adapterView, view, i, l) -> {
            month = adapterView.getItemAtPosition(i).toString();
            setUpRecyclerView(month, continent);
        });

        binding.autoDomContinent.setOnItemClickListener((adapterView, view, i, l) -> {
            continent = adapterView.getItemAtPosition(i).toString();
            setUpRecyclerView(month, continent);
        });
    }

    public void getAllData() {
        try {
            this.mDomDoorSeaList = new ArrayList<>();

            mDomDoorSeaViewModel.getAllData().observe(getViewLifecycleOwner(), domDoorSeas ->
                    this.mDomDoorSeaList = sortDomDoorSea(domDoorSeas));
        }catch (NullPointerException nullPointerException){
            Toast.makeText(getContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public List<DomDoorSea> sortDomDoorSea(List<DomDoorSea> list){
        List<DomDoorSea> result = new ArrayList<>();
        for(int i = list.size()-1; i>=0; i--){
            result.add(list.get(i));
        }
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();

        mDomDoorSeaViewModel.getAllData().observe(getViewLifecycleOwner(), domDoorSeas -> mDoorSeaDomAdapter.setDomDoorSea(filterDataResume(month, continent, domDoorSeas)));

        binding.rcvDomDoorSea.setAdapter(mDoorSeaDomAdapter);
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
    private void filter(String text) {
        List<DomDoorSea> filteredList = new ArrayList<>();
        for (DomDoorSea domDoorSea : filterData(month, continent)) {
            if (domDoorSea.getPortGo().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(domDoorSea);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            mDoorSeaDomAdapter.filterList(filteredList);
        }

    }
}