package com.example.demoapp.view.activity.sale;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.demoapp.R;
import com.example.demoapp.adapter.ImportDomAdapter;
import com.example.demoapp.databinding.ActivityDomImportSaleBinding;
import com.example.demoapp.model.DomImport;
import com.example.demoapp.utilities.Constants;
import com.example.demoapp.viewmodel.CommunicateViewModel;
import com.example.demoapp.viewmodel.DomImportViewModel;

import java.util.ArrayList;
import java.util.List;

public class DomImportSaleActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityDomImportSaleBinding binding;
    private DomImportViewModel mDomImportViewModel;
    private ImportDomAdapter mImportDomAdapter;

    private List<DomImport> mDomImportList = new ArrayList<>();

    private String month = "";
    private String continent = "";
    private String radioItem = "All";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDomImportSaleBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();


        mImportDomAdapter = new ImportDomAdapter(this);
        mDomImportViewModel = new ViewModelProvider(this).get(DomImportViewModel.class);

        CommunicateViewModel mCommunicateViewModel = new ViewModelProvider(this).get(CommunicateViewModel.class);

        mCommunicateViewModel.needReloading.observe(this, needLoading -> {
            if (needLoading) {
                onResume();
            }
        });

        getAllData();
        setAutoComplete();
        setButtons();

        setContentView(view);
    }

    public void setUpRecyclerView(String m, String c, String r) {
        if (!m.isEmpty() && !c.isEmpty()) {
            mImportDomAdapter.setDomImport(filterData(m, c, r));
            binding.rcvDomImport.setAdapter(mImportDomAdapter);
            binding.rcvDomImport.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
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
            Toast.makeText(getApplicationContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
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
            Toast.makeText(getApplicationContext(), nullPointerException.toString(), Toast.LENGTH_LONG).show();
        }
        return subList;
    }

    public void setAutoComplete() {
        ArrayAdapter<String> adapterItemsMonth = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_item, Constants.ITEMS_MONTH);
        ArrayAdapter<String> adapterItemsContinent = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_item, Constants.ITEMS_CONTINENT);

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

        mDomImportViewModel.getAllData().observe(this, domImports -> this.mDomImportList = domImports);
    }

    @Override
    public void onResume() {
        super.onResume();

        mDomImportViewModel.getAllData().observe(this, domImports -> mImportDomAdapter.setDomImport(filterDataResume(month, continent, radioItem, domImports)));

        binding.rcvDomImport.setAdapter(mImportDomAdapter);
    }

    public void setButtons() {
        binding.domImportFab.setOnClickListener(this);

        binding.radioImportAll.setOnClickListener(this);
        binding.radioImportAll.performClick();

        binding.radioImportFr.setOnClickListener(this);

        binding.radioImportRf.setOnClickListener(this);

        binding.radioImportOt.setOnClickListener(this);

        binding.radioImportIso.setOnClickListener(this);

        binding.radioImportFt.setOnClickListener(this);
    }

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
}