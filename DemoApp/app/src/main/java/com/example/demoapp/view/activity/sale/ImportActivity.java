package com.example.demoapp.view.activity.sale;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.example.demoapp.adapter.sale.PriceListImportSaleAdapter;
import com.example.demoapp.databinding.ActivityImportBinding;
import com.example.demoapp.model.Import;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.view.dialog.imp.InsertImportDialog;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.ImportViewModel;

import java.util.ArrayList;
import java.util.List;

public class ImportActivity extends AppCompatActivity implements View.OnClickListener {

    private String month = "";
    private String continent = "";
    private String radioItem = "All";

    List<Import> listPriceList = new ArrayList<>();
    private PriceListImportSaleAdapter priceListAdapter;
    private ImportViewModel mImportViewModel;
    private ActivityImportBinding mImportBinding;
    private SearchView searchView;
    public static String nameu,posi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImportBinding = ActivityImportBinding.inflate(getLayoutInflater());
        View view = mImportBinding.getRoot();

        setSupportActionBar(mImportBinding.toolbar);
        priceListAdapter = new PriceListImportSaleAdapter(this);
        mImportViewModel = new ViewModelProvider(this).get(ImportViewModel.class);

        CommunicateViewModel mCommunicateViewModel = new ViewModelProvider(this).get(CommunicateViewModel.class);

        mCommunicateViewModel.needReloading.observe(this, needLoading ->{
            if(needLoading){
                onResume();
            }
        });
        if(nameu==null && posi == null) {
            Intent intent = getIntent ();
            nameu = intent.getStringExtra ( "Keyusername" );
            posi = intent.getStringExtra ( "Keyposition" );
            Log.d ( "BB", posi + "  " + nameu );


        }
        SharedPreferences sharedPreferences = getSharedPreferences ( "UserInfo", MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferences.edit ();
        editor.putString ( "posi", posi );
        editor.commit ();
        setAdapterItems();
        setUpButtons();
        getAllData();
        setContentView(view);
    }

    public void setAdapterItems() {
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<String>(getApplicationContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

        mImportBinding.autoCompleteMonth.setAdapter(adapterItemsMonth);
        mImportBinding.autoCompleteContinent.setAdapter(adapterItemsContinent);

        mImportBinding.autoCompleteMonth.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                month = adapterView.getItemAtPosition(i).toString();
                setDataForRecyclerView(month, continent, radioItem);
            }
        });

        mImportBinding.autoCompleteContinent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                continent = adapterView.getItemAtPosition(i).toString();
                setDataForRecyclerView(month, continent, radioItem);
            }
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
            priceListAdapter.setImports(prepareDataForRecyclerView(month,continent,radioItem));
            mImportBinding.priceListRcv.setLayoutManager(new LinearLayoutManager(this));
            mImportBinding.priceListRcv.setAdapter(priceListAdapter);
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
        return subList;
    }


    /**
     * this method will get all data from database
     */
    public void getAllData() {

        mImportViewModel.getImportList().observe(this, detailsPojoImports -> {
            this.listPriceList = detailsPojoImports;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        priceListAdapter = new PriceListImportSaleAdapter(this);
        mImportViewModel.getImportList().observe(this, imp -> {
            priceListAdapter.setImports( prepareDataForResume(month, continent, radioItem, imp));
        });

        mImportBinding.priceListRcv.setAdapter(priceListAdapter);
    }

    /**
     * this method will set listen for buttons
     */
    public void setUpButtons() {
//        mImportBinding.fragmentFclFab.setOnClickListener(this);

        mImportBinding.radioAll.setOnClickListener(this);
        mImportBinding.radioAll.performClick();

        mImportBinding.radioGp.setOnClickListener(this);

        mImportBinding.radioFr.setOnClickListener(this);

        mImportBinding.radioRf.setOnClickListener(this);

        mImportBinding.radioHq.setOnClickListener(this);

        mImportBinding.radioOt.setOnClickListener(this);

        mImportBinding.radioTk.setOnClickListener(this);
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
                dialogFragment.show(getSupportFragmentManager(), "Insert Dialog");
                break;
            case R.id.radio_all:
                radioItem = mImportBinding.radioAll.getText().toString();
                setDataForRecyclerView(month, continent, radioItem);
                break;

            case R.id.radio_gp:
                radioItem = mImportBinding.radioGp.getText().toString();
                setDataForRecyclerView(month, continent, radioItem);
                break;

            case R.id.radio_fr:
                radioItem = mImportBinding.radioFr.getText().toString();
                setDataForRecyclerView(month, continent, radioItem);
                break;

            case R.id.radio_rf:
                radioItem = mImportBinding.radioRf.getText().toString();
                setDataForRecyclerView(month, continent, radioItem);
                break;

            case R.id.radio_hq:
                radioItem = mImportBinding.radioHq.getText().toString();
                setDataForRecyclerView(month, continent, radioItem);
                break;

            case R.id.radio_ot:
                radioItem = mImportBinding.radioOt.getText().toString();
                setDataForRecyclerView(month, continent, radioItem);
                break;
            case R.id.radio_tk:
                radioItem = mImportBinding.radioTk.getText().toString();
                setDataForRecyclerView(month, continent, radioItem);
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
        List<Import> filteredList = new ArrayList<>();
        for( Import container: prepareDataForRecyclerView(month, continent, radioItem)){
            if(container.getPol().toLowerCase().contains(text.toLowerCase())){
                filteredList.add(container);
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