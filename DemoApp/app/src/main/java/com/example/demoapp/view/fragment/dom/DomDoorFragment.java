package com.example.demoapp.view.fragment.dom;

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
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.adapter.DoorDomAdapter;
import com.example.demoapp.databinding.FragmentDomDoorBinding;
import com.example.demoapp.model.DomDoor;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.dom.dom_door.DialogDomDoorInsert;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomDoorViewModel;

import java.util.ArrayList;
import java.util.List;


public class DomDoorFragment extends Fragment {

    private FragmentDomDoorBinding binding;
    private DomDoorViewModel mDomDoorViewModel;
    private DoorDomAdapter mDoorDomAdapter;
    private SearchView searchView;

    private List<DomDoor> mDomDoorList = new ArrayList<>();

    private String month = "";
    private String continent = "";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDomDoorBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        mDoorDomAdapter = new DoorDomAdapter(getContext());
        mDomDoorViewModel = new ViewModelProvider(this).get(DomDoorViewModel.class);

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

    public void setUpRecyclerView(String m, String c) {
        if (!m.isEmpty() && !c.isEmpty()) {
            mDoorDomAdapter.setDomDoor(filterData(m, c));
            binding.rcvDomDoor.setAdapter(mDoorDomAdapter);
            binding.rcvDomDoor.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

    public List<DomDoor> filterData(String m, String c) {
        List<DomDoor> subList = new ArrayList<>();
        try {
            for (DomDoor domDoor : mDomDoorList) {
                if (domDoor.getMonth().equalsIgnoreCase(m) && domDoor.getContinent().equalsIgnoreCase(c)) {
                    subList.add(domDoor);
                }
            }
        } catch (NullPointerException nullPointerException) {
            Toast.makeText(getContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
        }
        return subList;
    }

    public List<DomDoor> filterDataResume(String m, String c, List<DomDoor> list) {
        List<DomDoor> subList = new ArrayList<>();
        try {
            for (DomDoor domDoor : list) {
                if (domDoor.getMonth().equalsIgnoreCase(m) && domDoor.getContinent().equalsIgnoreCase(c)) {
                    subList.add(domDoor);
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
            this.mDomDoorList = new ArrayList<>();

            mDomDoorViewModel.getAllData().observe(getViewLifecycleOwner(), domDoors ->
                    this.mDomDoorList = sortDomDoor(domDoors));
        }catch (NullPointerException nullPointerException){
            Toast.makeText(getContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
        }

    }

    public List<DomDoor> sortDomDoor(List<DomDoor> list){
        List<DomDoor> result = new ArrayList<>();
            for(int i = list.size()-1; i>=0 ; i--){
                result.add(list.get(i));
            }
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();

        mDomDoorViewModel.getAllData().observe(getViewLifecycleOwner(), domDoors -> mDoorDomAdapter.setDomDoor(filterDataResume(month, continent, domDoors)));

        binding.rcvDomDoor.setAdapter(mDoorDomAdapter);
    }

    public void setButtons() {
        binding.domDoorFab.setOnClickListener(view -> {
            DialogFragment dialogFragment = DialogDomDoorInsert.getInstance();
            dialogFragment.show(getChildFragmentManager(), "Door Insert");
        });
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
        List<DomDoor> filteredList = new ArrayList<>();
        for( DomDoor domDoor: filterData(month, continent)){
            if(domDoor.getStationGo().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(domDoor);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(getContext(), "No Data Found..", Toast.LENGTH_SHORT).show();
        }else {
            mDoorDomAdapter.filterList(filteredList);
        }
    }
}