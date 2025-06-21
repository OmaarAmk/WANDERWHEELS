package com.example.wanderwheels;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TourActivity extends AppCompatActivity {

    private ImageView navHomeIcon, navVansIcon, navToursIcon;
    private TextView navHomeText, navVansText, navToursText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.tourRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Create tour data
        List<Tour> tours = createSampleTours();

        // Set up the adapter
        TourAdapter adapter = new TourAdapter(tours);
        recyclerView.setAdapter(adapter);

        // Initialize navigation items
        navHomeIcon = findViewById(R.id.navHomeIcon);
        navVansIcon = findViewById(R.id.navVansIcon);
        navToursIcon = findViewById(R.id.navToursIcon);
        navHomeText = findViewById(R.id.navHomeText);
        navVansText = findViewById(R.id.navVansText);
        navToursText = findViewById(R.id.navToursText);
        View logoContainer = findViewById(R.id.logoContainer);
        logoContainer.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

        // Set the default active tab
        setActiveTab(R.id.navTours);

        // Navigation item click listeners
        findViewById(R.id.navHome).setOnClickListener(v -> {
            setActiveTab(R.id.navHome);
            startActivity(new Intent(TourActivity.this, MainActivity.class));
        });

        findViewById(R.id.navVans).setOnClickListener(v -> {
            setActiveTab(R.id.navVans);
            startActivity(new Intent(TourActivity.this, VanCatalogueActivity.class));
        });

        findViewById(R.id.navTours).setOnClickListener(v -> {
            setActiveTab(R.id.navTours);
        });
    }

    private void setActiveTab(int activeTabId) {
        int activeColor = ContextCompat.getColor(this, R.color.blue_600);
        int inactiveColor = ContextCompat.getColor(this, R.color.gray_500);

        // Reset all tabs to inactive state
        navHomeIcon.setColorFilter(inactiveColor);
        navHomeText.setTextColor(inactiveColor);
        navVansIcon.setColorFilter(inactiveColor);
        navVansText.setTextColor(inactiveColor);
        navToursIcon.setColorFilter(inactiveColor);
        navToursText.setTextColor(inactiveColor);

        // Activate the current tab
        if (activeTabId == R.id.navHome) {
            navHomeIcon.setColorFilter(activeColor);
            navHomeText.setTextColor(activeColor);
        }
        else if (activeTabId == R.id.navVans) {
            navVansIcon.setColorFilter(activeColor);
            navVansText.setTextColor(activeColor);
        }
        else if (activeTabId == R.id.navTours) {
            navToursIcon.setColorFilter(activeColor);
            navToursText.setTextColor(activeColor);
        }
    }
    private List<Tour> createSampleTours() {
        List<Tour> tours = new ArrayList<>();

        // Tour 1: Southwest National Parks
        Tour southwest = new Tour();
        southwest.setName("Southwest National Parks");
        southwest.setImageResId(R.drawable.southwest);
        southwest.setDuration("7 days");
        southwest.setDescription("Explore the iconic landscapes of Utah and Arizona's national parks");
        southwest.setStartLocation("Moab, UT");
        southwest.setDistance("850 miles");
        southwest.setTags(Arrays.asList("Hiking", "Camping", "Photography"));
        tours.add(southwest);

        // Tour 2: Coastal California
        Tour coastal = new Tour();
        coastal.setName("Coastal California");
        coastal.setImageResId(R.drawable.coastal_california);
        coastal.setDuration("5 days");
        coastal.setDescription("Experience the stunning Pacific coastline from San Francisco to San Diego");
        coastal.setStartLocation("San Francisco, CA");
        coastal.setDistance("500 miles");
        tours.add(coastal);

        // Tour 3: Rocky Mountain Adventure
        Tour rocky = new Tour();
        rocky.setName("Rocky Mountain Adventure");
        rocky.setImageResId(R.drawable.rocky_mountains);
        rocky.setDuration("6 days");
        rocky.setDescription("Discover majestic peaks, alpine lakes, and wildlife in Colorado");
        rocky.setStartLocation("Denver, CO");
        rocky.setDistance("350 miles");
        tours.add(rocky);

        // Tour 4: New England Fall Colors
        Tour newEngland = new Tour();
        newEngland.setName("New England Fall Colors");
        newEngland.setImageResId(R.drawable.new_england);
        newEngland.setDuration("4 days");
        newEngland.setDescription("Witness the breathtaking autumn colors across Vermont and New Hampshire");
        newEngland.setStartLocation("Boston, MA");
        newEngland.setDistance("300 miles");
        tours.add(newEngland);

        // Tour 5: Pacific Northwest
        Tour northwest = new Tour();
        northwest.setName("Pacific Northwest");
        northwest.setImageResId(R.drawable.pacific_nw);
        northwest.setDuration("8 days");
        northwest.setDescription("From rainforests to volcanoes, experience diverse landscapes of Washington and Oregon");
        northwest.setStartLocation("Seattle, WA");
        northwest.setDistance("600 miles");
        tours.add(northwest);

        return tours;
    }
}
