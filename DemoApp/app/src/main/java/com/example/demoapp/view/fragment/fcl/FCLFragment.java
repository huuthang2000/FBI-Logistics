package com.example.demoapp.view.fragment.fcl;

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
import com.example.demoapp.adapter.PriceListFclAdapter;
import com.example.demoapp.databinding.FragmentFclBinding;
import com.example.demoapp.model.Fcl;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.fcl.InsertFclDialog;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.FclViewModel;

import java.util.ArrayList;
import java.util.List;

public class FCLFragment extends Fragment implements View.OnClickListener {

    private FragmentFclBinding binding;

    private String month = "";
    private String continent = "";
    private String radioItem = "All";
    private SearchView searchView;

    private List<Fcl> listPriceList = new ArrayList<>();
    private PriceListFclAdapter priceListFclAdapter;

    private FclViewModel mFclViewModel;

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
        binding = FragmentFclBinding.inflate(inflater, container, false);

        View view = binding.getRoot();

        priceListFclAdapter = new PriceListFclAdapter(getContext());
        mFclViewModel = new ViewModelProvider(this).get(FclViewModel.class);

        CommunicateViewModel mCommunicateViewModel = new ViewModelProvider(requireActivity()).get(CommunicateViewModel.class);

        mCommunicateViewModel.needReloading.observe(getViewLifecycleOwner(), needLoading -> {
            if (needLoading) {
                onResume();
            }
        });

        setHasOptionsMenu(true);
        getAllData();
        setAdapterItems();
        setUpButtons();

        return view;
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
     * This method will set data by r,c,m
     *
     * @param m month
     * @param c continent
     * @param r radio
     */
    public void setDataForRecyclerView(String m, String c, String r) {
        if (!m.isEmpty() && !c.isEmpty()) {
            priceListFclAdapter.setDataFcl(prepareDataForRecyclerView(m, c, r));
            binding.priceListRcv.setAdapter(priceListFclAdapter);
            binding.priceListRcv.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    /**
     * this method will filter list data by month and continent
     *
     * @param m month
     * @param c continent
     * @return get list by month and continent
     */
    public List<Fcl> prepareDataForRecyclerView(String m, String c, String r) {
        // reset a list when user choose different
        List<Fcl> subList = new ArrayList<>();
        try {
            for (Fcl f : listPriceList) {
                if (r.equalsIgnoreCase("all")) {
                    if (f.getMonth().equalsIgnoreCase(m) && f.getContinent().equalsIgnoreCase(c)) {
                        subList.add(f);
                    }
                } else {
                    if (f.getMonth().equalsIgnoreCase(m) && f.getContinent().equalsIgnoreCase(c)
                            && f.getType().equalsIgnoreCase(r)) {
                        subList.add(f);
                    }
                }
            }
        } catch (NullPointerException nullPointerException) {
            Toast.makeText(getContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
        }
        return subList;
    }

    public List<Fcl> prepareDataForResume(String m, String c, String r, List<Fcl> list) {
        // reset a list when user choose different
        List<Fcl> subList = new ArrayList<>();
        try {
            for (Fcl f : list) {
                if (r.equalsIgnoreCase("all")) {
                    if (f.getMonth().equalsIgnoreCase(m) && f.getContinent().equalsIgnoreCase(c)) {
                        subList.add(f);
                    }
                } else {
                    if (f.getMonth().equalsIgnoreCase(m) && f.getContinent().equalsIgnoreCase(c)
                            && f.getType().equalsIgnoreCase(r)) {
                        subList.add(f);
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
        this.listPriceList = new ArrayList<>();

        mFclViewModel.getFclList().observe(getViewLifecycleOwner(), detailsPojoFcl -> this.listPriceList = sortArray(detailsPojoFcl));
    }

    /**
     * This method will sort a list
     *
     * @param list list to sort
     * @return sorted list
     */
    public List<Fcl> sortArray(List<Fcl> list) {
        List<Fcl> result = new ArrayList<>();
        try{
            for (int i = list.size() - 1; i >= 0; i--) {
                result.add(list.get(i));
            }
        }catch (NullPointerException e){
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
        }
        return result;
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

        binding.radioHc.setOnClickListener(this);

        binding.radioOt.setOnClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();

        priceListFclAdapter = new PriceListFclAdapter(getContext());
        mFclViewModel.getFclList().observe(getViewLifecycleOwner(), detailsPojoFcl ->
                priceListFclAdapter.setDataFcl(prepareDataForResume(month, continent, radioItem, sortArray(detailsPojoFcl))));

        binding.priceListRcv.setAdapter(priceListFclAdapter);
    }

    /**
     * this method used to set event for button click
     *
     * @param view click
     */
    @SuppressLint({"NonConstantResourceId", "NotifyDataSetChanged"})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fragment_fcl_fab:
                DialogFragment dialogFragment = InsertFclDialog.insertDialog();
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

            case R.id.radio_hc:
                radioItem = binding.radioHc.getText().toString();
                setDataForRecyclerView(month, continent, radioItem);
                break;

            case R.id.radio_ot:
                radioItem = binding.radioOt.getText().toString();
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
        List<Fcl> filteredList = new ArrayList<>();
        for( Fcl fcl: prepareDataForRecyclerView(month, continent, radioItem)){
            if(fcl.getPol().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(fcl);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        }else {
            priceListFclAdapter.filterList(filteredList);
        }
    }
}
