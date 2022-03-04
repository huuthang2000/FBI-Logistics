package com.example.demoapp.view.fragment.imp;

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
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.adapter.PriceListImportAdapter;
import com.example.demoapp.databinding.FragmentImportBinding;
import com.example.demoapp.model.Import;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.imp.InsertImportDialog;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.ImportViewModel;

import java.util.ArrayList;
import java.util.List;

public class ImportFragment extends Fragment implements View.OnClickListener {

    private FragmentImportBinding binding;

    private String month = "";
    private String continent = "";
    private String radioItem = "All";
    private SearchView searchView;

    List<Import> listPriceList = new ArrayList<>();
    private PriceListImportAdapter priceListAdapter;
    private ImportViewModel mImportViewModel;

    /**
     * this method will create a view (fragment)
     *
     * @param inflater           fragment
     * @param container          container
     * @param savedInstanceState save
     * @return view
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentImportBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        priceListAdapter = new PriceListImportAdapter(getContext());
        mImportViewModel = new ViewModelProvider(this).get(ImportViewModel.class);

        CommunicateViewModel mCommunicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);

        mCommunicateViewModel.needReloading.observe(getViewLifecycleOwner(), needLoading -> {
            if (needLoading) {
                onResume();
            }
        });

        setHasOptionsMenu(true);
        setAdapterItems();
        setUpButtons();
        getAllData();

        return root;
    }

    /**
     * this method will listen a event of auto complete (month, continent)
     */
    public void setAdapterItems() {
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        binding.autoCompleteMonth.setAdapter(adapterItemsMonth);
        binding.autoCompleteContinent.setAdapter(adapterItemsContinent);

        binding.autoCompleteMonth.setOnItemClickListener((adapterView, view, i, l) -> {
            month = adapterView.getItemAtPosition(i).toString();
            setDataForRecyclerView(month, continent, radioItem);
        });

        binding.autoCompleteContinent.setOnItemClickListener((adapterView, view, i, l) -> {
            continent = adapterView.getItemAtPosition(i).toString();
            setDataForRecyclerView(month, continent, radioItem);
        });

    }

    /**
     * this method will set data for recycler view
     *
     * @param m month
     * @param c continent
     * @param r radio
     */
    public void setDataForRecyclerView(String m, String c, String r) {
        if (!m.isEmpty() && !c.isEmpty()) {
            priceListAdapter.setImports(prepareDataForRecyclerView(month, continent, r));
            binding.priceListRcv.setLayoutManager(new LinearLayoutManager(getContext()));
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
    public List<Import> prepareDataForRecyclerView(String m, String c, String r) {
        // reset a list when user choose different
        List<Import> list = new ArrayList<>();

        for (Import imp : listPriceList) {
            if (r.equalsIgnoreCase("all")) {
                if (imp.getMonth().equalsIgnoreCase(m) && imp.getContinent().equalsIgnoreCase(c)) {
                    list.add(imp);
                }
            } else {
                if (imp.getMonth().equalsIgnoreCase(m) && imp.getContinent().equalsIgnoreCase(c)
                        && imp.getType().equalsIgnoreCase(r)) {
                    list.add(imp);
                }
            }
        }
        return list;
    }

    public List<Import> prepareDataForResume(String m, String c, String r, List<Import> list) {
        // reset a list when user choose different
        List<Import> subList = new ArrayList<>();

        try {
            for (Import imp : list) {
                if (r.equalsIgnoreCase("all")) {
                    if (imp.getMonth().equalsIgnoreCase(m) && imp.getContinent().equalsIgnoreCase(c)) {
                        subList.add(imp);
                    }
                } else {
                    if (imp.getMonth().equalsIgnoreCase(m) && imp.getContinent().equalsIgnoreCase(c)
                            && imp.getType().equalsIgnoreCase(r)) {
                        subList.add(imp);
                    }
                }
            }

        } catch (NullPointerException nullPointerException) {
            Toast.makeText(getContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
        }
        return subList;
    }


    /**
     * this method will get all data from database
     */
    public void getAllData() {

        try {
            mImportViewModel.getImportList().observe(getViewLifecycleOwner(), detailsPojoImports ->
                    this.listPriceList = sortImport(detailsPojoImports));

        } catch (NullPointerException exception) {
            Toast.makeText(getContext(), exception.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public List<Import> sortImport(List<Import> list){
        List<Import> result = new ArrayList<>();
        for (int i = list.size()-1; i>=0 ; i--){
            result.add(list.get(i));
        }
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
        priceListAdapter = new PriceListImportAdapter(getContext());
        mImportViewModel.getImportList().observe(getViewLifecycleOwner(), imp ->
                priceListAdapter.setImports(prepareDataForResume(month, continent, radioItem, imp)));

        binding.priceListRcv.setAdapter(priceListAdapter);
    }

    /**
     * this method will set listen for buttons
     */
    public void setUpButtons() {
        binding.fragmentFclFab.setOnClickListener(this);

        binding.radioAll.setOnClickListener(this);
        binding.radioAll.performClick();

        binding.radioGp.setOnClickListener(this);

        binding.radioFr.setOnClickListener(this);

        binding.radioRf.setOnClickListener(this);

        binding.radioHq.setOnClickListener(this);

        binding.radioOt.setOnClickListener(this);

        binding.radioTk.setOnClickListener(this);
    }

    /**
     * this method used to set event for button click
     *
     * @param view click
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_fcl_fab:
                DialogFragment dialogFragment = InsertImportDialog.insertDialog();
                dialogFragment.show(getParentFragmentManager(), "Insert Dialog");
                break;
            case R.id.radio_all:
                radioItem = binding.radioAll.getText().toString();
                setDataForRecyclerView(month, continent, radioItem);
                break;

            case R.id.radio_gp:
                radioItem = binding.radioGp.getText().toString();
                setDataForRecyclerView(month, continent, radioItem);
                break;

            case R.id.radio_fr:
                radioItem = binding.radioFr.getText().toString();
                setDataForRecyclerView(month, continent, radioItem);
                break;

            case R.id.radio_rf:
                radioItem = binding.radioRf.getText().toString();
                setDataForRecyclerView(month, continent, radioItem);
                break;

            case R.id.radio_hq:
                radioItem = binding.radioHq.getText().toString();
                setDataForRecyclerView(month, continent, radioItem);
                break;

            case R.id.radio_ot:
                radioItem = binding.radioOt.getText().toString();
                setDataForRecyclerView(month, continent, radioItem);
                break;
            case R.id.radio_tk:
                radioItem = binding.radioTk.getText().toString();
                setDataForRecyclerView(month, continent, radioItem);
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
        List<Import> filteredList = new ArrayList<>();
        for( Import importPro: prepareDataForRecyclerView(month, continent, radioItem)){
            if(importPro.getPol().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(importPro);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        }else {
            priceListAdapter.filterList(filteredList);
        }
    }
}
