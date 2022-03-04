package com.example.demoapp.view.fragment.sales.truck;

import android.annotation.SuppressLint;
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
import com.example.demoapp.adapter.sale.PriceListImportDomAdapter;
import com.example.demoapp.databinding.FragmentContainerImportBinding;
import com.example.demoapp.model.DomImport;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomImportViewModel;

import java.util.ArrayList;
import java.util.List;


public class ContainerImportFragment extends Fragment implements View.OnClickListener {

    private DomImportViewModel mDomImportViewModel;
    private PriceListImportDomAdapter mImportDomAdapter;

    private List<DomImport> mDomImportList = new ArrayList<>();
    private SearchView searchView;

    private String month = "";
    private String continent = "";
    private String radioItem = "All";

    private FragmentContainerImportBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentContainerImportBinding.inflate(inflater, container, false);
        View view =  binding.getRoot();

        mImportDomAdapter = new PriceListImportDomAdapter(getContext());
        mDomImportViewModel = new ViewModelProvider(this).get(DomImportViewModel.class);

        CommunicateViewModel mCommunicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);

        mCommunicateViewModel.needReloading.observe(getViewLifecycleOwner(), needLoading -> {
            if (needLoading) {
                onResume();
            }
        });


        setHasOptionsMenu(true);
        getAllData();
        setAutoComplete();
        setButtons();

        return view;
    }

    public void setUpRecyclerView(String m, String c, String r) {
        if (!m.isEmpty() && !c.isEmpty()) {
            mImportDomAdapter.setDomImport(filterData(m, c, r));
            binding.rcvDomImport.setAdapter(mImportDomAdapter);
            binding.rcvDomImport.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    public List<DomImport> filterData(String m, String c, String r) {
        List<DomImport> subList = new ArrayList<>();
        try {
            for (DomImport domImport : mDomImportList) {
                if (r.equalsIgnoreCase("all")) {
                    if (domImport.getMonth().equalsIgnoreCase(m) && domImport.getContinent().equalsIgnoreCase(c)) {
                        subList.add(domImport);
                    }
                } else {
                    if (domImport.getMonth().equalsIgnoreCase(m) && domImport.getContinent().equalsIgnoreCase(c)
                            && domImport.getType().equalsIgnoreCase(r)) {
                        subList.add(domImport);
                    }
                }
            }
        } catch (NullPointerException nullPointerException) {
            Toast.makeText(getContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
        }
        return subList;
    }

    public List<DomImport> filterDataResume(String m, String c, String r, List<DomImport> list) {
        List<DomImport> subList = new ArrayList<>();
        try {
            for (DomImport domImport : list) {
                if (r.equalsIgnoreCase("all")) {
                    if (domImport.getMonth().equalsIgnoreCase(m) && domImport.getContinent().equalsIgnoreCase(c)) {
                        subList.add(domImport);
                    }
                } else {
                    if (domImport.getMonth().equalsIgnoreCase(m) && domImport.getContinent().equalsIgnoreCase(c)
                            && domImport.getType().equalsIgnoreCase(r)) {
                        subList.add(domImport);
                    }
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
            setUpRecyclerView(month, continent, radioItem);
        });

        binding.autoDomContinent.setOnItemClickListener((adapterView, view, i, l) -> {
            continent = adapterView.getItemAtPosition(i).toString();
            setUpRecyclerView(month, continent, radioItem);
        });
    }

    public void getAllData() {
        this.mDomImportList = new ArrayList<>();

        mDomImportViewModel.getAllData().observe(getViewLifecycleOwner(), domImports -> this.mDomImportList = domImports);
    }

    @Override
    public void onResume() {
        super.onResume();

        mDomImportViewModel.getAllData().observe(getViewLifecycleOwner(), domImports -> mImportDomAdapter.setDomImport(filterDataResume(month, continent, radioItem, domImports)));

        binding.rcvDomImport.setAdapter(mImportDomAdapter);
    }

    public void setButtons() {

        binding.radioImportAll.setOnClickListener(this);
        binding.radioImportAll.performClick();

        binding.radioImportFr.setOnClickListener(this);

        binding.radioImportRf.setOnClickListener(this);

        binding.radioImportOt.setOnClickListener(this);

        binding.radioImportIso.setOnClickListener(this);

        binding.radioImportFt.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.radio_import_all:
                radioItem = binding.radioImportAll.getText().toString();
                setUpRecyclerView(month, continent, radioItem);
                break;

            case R.id.radio_import_ft:
                radioItem = binding.radioImportFt.getText().toString();
                setUpRecyclerView(month, continent, radioItem);
                break;

            case R.id.radio_import_rf:
                radioItem = binding.radioImportRf.getText().toString();
                setUpRecyclerView(month, continent, radioItem);
                break;

            case R.id.radio_import_ot:
                radioItem = binding.radioImportOt.getText().toString();
                setUpRecyclerView(month, continent, radioItem);
                break;

            case R.id.radio_import_fr:
                radioItem = binding.radioImportFr.getText().toString();
                setUpRecyclerView(month, continent, radioItem);
                break;

            case R.id.radio_import_iso:
                radioItem = binding.radioImportIso.getText().toString();
                setUpRecyclerView(month, continent, radioItem);
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
    private void filter(String text) {
        List<DomImport> filteredList = new ArrayList<>();
        for (DomImport domImport : filterData(month, continent, radioItem)) {
            if (domImport.getPortReceive().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(domImport);
            }
        }
        if (filteredList.isEmpty()) {
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            mImportDomAdapter.filterList(filteredList);
        }
    }
}