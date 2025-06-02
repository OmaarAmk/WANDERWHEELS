package com.example.wanderwheels;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VanCatalogueActivity extends AppCompatActivity {

    private RecyclerView vanRecyclerView;
    private VanAdapter vanAdapter;
    private Button filterAll, filterClassic, filterLuxury, filterCompact, filterFamily;
    private TextView availableVansCount;
    private Spinner sortSpinner;

    private List<Van> fullVanList;
    private List<Van> displayedVanList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_van_catalogue);

        initViews();
        setupRecyclerView();
        setupFilterButtons();
        setupSortSpinner();
        setupNavigationButtons();
        setupAccountButton();
    }

    private void initViews() {
        vanRecyclerView = findViewById(R.id.vanRecyclerView);
        availableVansCount = findViewById(R.id.availableVansCount);
        filterAll = findViewById(R.id.filterAll);
        filterClassic = findViewById(R.id.filterClassic);
        filterLuxury = findViewById(R.id.filterLuxury);
        filterCompact = findViewById(R.id.filterCompact);
        filterFamily = findViewById(R.id.filterFamily);
        sortSpinner = findViewById(R.id.sortSpinner);

        View logoContainer = findViewById(R.id.logoContainer);
        logoContainer.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        fullVanList = createSampleVans();
        displayedVanList = new ArrayList<>(fullVanList);

        availableVansCount.setText(displayedVanList.size() + " vans disponibles");

        vanAdapter = new VanAdapter(displayedVanList);
        vanRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        vanRecyclerView.setAdapter(vanAdapter);
    }

    private void setupFilterButtons() {
        View.OnClickListener filterClickListener = v -> {
            resetFilterButtons();
            Button selectedButton = (Button) v;
            selectedButton.setBackgroundResource(R.drawable.filter_button_active);
            selectedButton.setTextColor(ContextCompat.getColor(this, R.color.white));
            filterVans(selectedButton.getText().toString());
        };

        filterAll.setOnClickListener(filterClickListener);
        filterClassic.setOnClickListener(filterClickListener);
        filterLuxury.setOnClickListener(filterClickListener);
        filterCompact.setOnClickListener(filterClickListener);
        filterFamily.setOnClickListener(filterClickListener);
    }

    private void filterVans(String category) {
        displayedVanList.clear();

        if (category.equalsIgnoreCase("Tous")) {
            displayedVanList.addAll(fullVanList);
        } else {
            for (Van van : fullVanList) {
                if (van.getCategory().equalsIgnoreCase(category)) {
                    displayedVanList.add(van);
                }
            }
        }
        applySort(sortSpinner.getSelectedItemPosition());
        availableVansCount.setText(displayedVanList.size() + " vans disponibles");
        vanAdapter.notifyDataSetChanged();
    }

    private void setupSortSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.sort_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sortSpinner.setAdapter(adapter);

        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applySort(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void applySort(int sortOption) {
        if (displayedVanList == null) return;

        switch (sortOption) {
            case 1:
                Collections.sort(displayedVanList, (v1, v2) -> Integer.compare(v2.getCurrentPrice(), v1.getCurrentPrice()));
                break;
            case 2:
                Collections.sort(displayedVanList, (v1, v2) -> Integer.compare(v1.getCurrentPrice(), v2.getCurrentPrice()));
                break;
            default:
                Collections.sort(displayedVanList, (v1, v2) -> v1.getName().compareToIgnoreCase(v2.getName()));
                break;
        }

        vanAdapter.notifyDataSetChanged();
    }

    private void setupNavigationButtons() {
        View homeNav = findViewById(R.id.navHome);
        homeNav.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    private void setupAccountButton() {
        ImageButton accountButton = findViewById(R.id.accountButton);
        accountButton.setOnClickListener(v -> {
            FirstFragment fragment = new FirstFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer, fragment); // Assurez-vous que fragmentContainer existe
            transaction.addToBackStack(null);
            transaction.commit();
        });
    }

    private void resetFilterButtons() {
        int inactiveColor = ContextCompat.getColor(this, R.color.gray_600);

        filterAll.setBackgroundResource(R.drawable.filter_button_inactive);
        filterAll.setTextColor(inactiveColor);
        filterClassic.setBackgroundResource(R.drawable.filter_button_inactive);
        filterClassic.setTextColor(inactiveColor);
        filterLuxury.setBackgroundResource(R.drawable.filter_button_inactive);
        filterLuxury.setTextColor(inactiveColor);
        filterCompact.setBackgroundResource(R.drawable.filter_button_inactive);
        filterCompact.setTextColor(inactiveColor);
        filterFamily.setBackgroundResource(R.drawable.filter_button_inactive);
        filterFamily.setTextColor(inactiveColor);
    }

    private List<Van> createSampleVans() {
        List<Van> vans = new ArrayList<>();
        vans.add(new Van("Mercedes Sprinter Luxe", R.drawable.van_luxury, "Luxe", R.color.blue_600, 4.5f, 24, 4, "Lit double", 120, 150, new String[]{"WC", "Douche", "Cuisine"}, "Luxe"));
        vans.add(new Van("Volkswagen California", R.drawable.van_classic, "Classique", R.color.green_600, 4.0f, 18, 2, "Toit relevable", 90, 0, new String[]{"Cuisine", "Frigo"}, "Classique"));
        vans.add(new Van("Fiat Ducato Family", R.drawable.van_family, "Familial", R.color.purple_500, 5.0f, 32, 6, "3 lits", 110, 0, new String[]{"WC", "Douche", "Cuisine", "TV"}, "Familial"));
        vans.add(new Van("Citroën Berlingo", R.drawable.van_compact, "Compact", R.color.orange_500, 4.0f, 15, 2, "Économique", 65, 0, new String[]{"Lit", "Frigo"}, "Compact"));
        return vans;
    }
}