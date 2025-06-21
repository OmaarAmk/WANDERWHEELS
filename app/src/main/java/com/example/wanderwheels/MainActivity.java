package com.example.wanderwheels;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.NonNull;
import android.widget.ArrayAdapter;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import android.location.LocationManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Spinner locationSpinner;
    private EditText pickupDateInput, returnDateInput;
    private Calendar pickupCalendar, returnCalendar;
    private SimpleDateFormat dateFormat;
    private RecyclerView vehiclesRecyclerView, toursRecyclerView;
    private Button filterAllButton, filterVansButton, filterCaravansButton, filterRvButton, filter4x4Button;
    private VehicleAdapter vehicleAdapter;
    private TourAdapter tourAdapter;
    private BatteryReceiver batteryReceiver; // Receiver to monitor battery status
    private FusedLocationProviderClient fusedLocationClient;
    private TextView locationTextView;
    private TextView txtOutput;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Initialize date format
        dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

        // Initialize UI components
        initViews();
        setupLocationSpinner();
        setupDatePickers();
        setupFilterButtons();
        setupVehicleRecyclerView();
        setupTourRecyclerView();
        setupClickListeners();
        txtOutput = findViewById(R.id.txtOutput);


        // Register BatteryReceiver to listen for battery changes
        batteryReceiver = new BatteryReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryReceiver, filter);

        Button btnBattery = findViewById(R.id.btnBattery);
        btnBattery.setOnClickListener(v -> checkBatteryStatus());



    }

    private void initViews() {
        locationSpinner = findViewById(R.id.locationSpinner);
        pickupDateInput = findViewById(R.id.pickupDateInput);
        returnDateInput = findViewById(R.id.returnDateInput);
        vehiclesRecyclerView = findViewById(R.id.vehiclesRecyclerView);
        toursRecyclerView = findViewById(R.id.toursRecyclerView);
        filterAllButton = findViewById(R.id.filterAllButton);
        filterVansButton = findViewById(R.id.filterVansButton);
        filterCaravansButton = findViewById(R.id.filterCaravansButton);
        filterRvButton = findViewById(R.id.filterRvButton);
        filter4x4Button = findViewById(R.id.filter4x4Button);

        ImageButton menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> {
            // TODO: Implement mobile menu
            Toast.makeText(MainActivity.this, "Menu clicked", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupLocationSpinner() {
        // Create array adapter for spinner
        String[] locations = {
                "Select location",
                "Denver, CO",
                "Moab, UT",
                "Portland, OR",
                "Asheville, NC",
                "San Diego, CA"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, locations);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationSpinner.setAdapter(adapter);
    }

    private void setupDatePickers() {
        // Initialize calendars
        pickupCalendar = Calendar.getInstance();
        returnCalendar = Calendar.getInstance();
        returnCalendar.add(Calendar.DAY_OF_MONTH, 7); // Default rental period: 7 days

        // Set initial dates
        pickupDateInput.setText(dateFormat.format(pickupCalendar.getTime()));
        returnDateInput.setText(dateFormat.format(returnCalendar.getTime()));

        // Setup pickup date picker
        pickupDateInput.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        pickupCalendar.set(Calendar.YEAR, year);
                        pickupCalendar.set(Calendar.MONTH, month);
                        pickupCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        pickupDateInput.setText(dateFormat.format(pickupCalendar.getTime()));

                        // Ensure return date is after pickup date
                        if (returnCalendar.before(pickupCalendar)) {
                            returnCalendar.setTime(pickupCalendar.getTime());
                            returnCalendar.add(Calendar.DAY_OF_MONTH, 1);
                            returnDateInput.setText(dateFormat.format(returnCalendar.getTime()));
                        }
                    },
                    pickupCalendar.get(Calendar.YEAR),
                    pickupCalendar.get(Calendar.MONTH),
                    pickupCalendar.get(Calendar.DAY_OF_MONTH)
            );

            // Set minimum date to today
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        // Setup return date picker
        returnDateInput.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    MainActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        returnCalendar.set(Calendar.YEAR, year);
                        returnCalendar.set(Calendar.MONTH, month);
                        returnCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        returnDateInput.setText(dateFormat.format(returnCalendar.getTime()));
                    },
                    returnCalendar.get(Calendar.YEAR),
                    returnCalendar.get(Calendar.MONTH),
                    returnCalendar.get(Calendar.DAY_OF_MONTH)
            );

            // Set minimum date to pickup date
            datePickerDialog.getDatePicker().setMinDate(pickupCalendar.getTimeInMillis());
            datePickerDialog.show();
        });
    }

    private void setupFilterButtons() {
        View.OnClickListener filterClickListener = v -> {
            // Reset all buttons
            filterAllButton.setBackgroundColor(getResources().getColor(R.color.white));
            filterAllButton.setTextColor(getResources().getColor(R.color.gray_800));
            filterVansButton.setBackgroundColor(getResources().getColor(R.color.white));
            filterVansButton.setTextColor(getResources().getColor(R.color.gray_800));
            filterCaravansButton.setBackgroundColor(getResources().getColor(R.color.white));
            filterCaravansButton.setTextColor(getResources().getColor(R.color.gray_800));
            filterRvButton.setBackgroundColor(getResources().getColor(R.color.white));
            filterRvButton.setTextColor(getResources().getColor(R.color.gray_800));
            filter4x4Button.setBackgroundColor(getResources().getColor(R.color.white));
            filter4x4Button.setTextColor(getResources().getColor(R.color.gray_800));

            // Set selected button
            Button selectedButton = (Button) v;
            selectedButton.setBackgroundColor(getResources().getColor(R.color.blue_600));
            selectedButton.setTextColor(getResources().getColor(R.color.white));

            // Filter vehicles based on the selected category
            String filter = selectedButton.getText().toString();
            vehicleAdapter.filterVehicles(filter);
        };

        filterAllButton.setOnClickListener(filterClickListener);
        filterVansButton.setOnClickListener(filterClickListener);
        filterCaravansButton.setOnClickListener(filterClickListener);
        filterRvButton.setOnClickListener(filterClickListener);
        filter4x4Button.setOnClickListener(filterClickListener);
    }

    private void setupVehicleRecyclerView() {
        // Create layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        vehiclesRecyclerView.setLayoutManager(layoutManager);

        // Create and set adapter
        List<Vehicle> vehicles = createSampleVehicles();
        vehicleAdapter = new VehicleAdapter(vehicles);
        vehiclesRecyclerView.setAdapter(vehicleAdapter);
    }

    private void setupTourRecyclerView() {
        // Create layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        toursRecyclerView.setLayoutManager(layoutManager);

        // Create and set adapter
        List<Tour> tours = createSampleTours();
        tourAdapter = new TourAdapter(tours);
        toursRecyclerView.setAdapter(tourAdapter);
    }
    public class BatteryReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);

            String batteryStatus = "Battery Level: " + level + "%";

            if (status == BatteryManager.BATTERY_STATUS_CHARGING) {
                batteryStatus += " (Charging)";
            } else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
                batteryStatus += " (Discharging)";
            } else if (status == BatteryManager.BATTERY_STATUS_FULL) {
                batteryStatus += " (Full)";
            }

            // Display battery status in a toast message
            Toast.makeText(context, batteryStatus, Toast.LENGTH_SHORT).show();
        }
    }
    private void checkBatteryStatus() {
        IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = registerReceiver(null, iFilter);

        int level;
        if (batteryStatus != null) {
            level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        } else {
            level = -1;
        }

        txtOutput.setText("Niveau de batterie : " + level + "%");
    }

    private void setupClickListeners() {
        Button findVehiclesButton = findViewById(R.id.findVehiclesButton);
        findVehiclesButton.setOnClickListener(v -> {
            // Get search parameters
            String location = locationSpinner.getSelectedItem().toString();
            String pickupDate = pickupDateInput.getText().toString();
            String returnDate = returnDateInput.getText().toString();

            // Display search parameters in a toast (to be replaced with actual search)
            String message = "Searching for vehicles in " + location +
                    " from " + pickupDate + " to " + returnDate;
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        });

        Button browseRentalsButton = findViewById(R.id.browseRentalsButton);
        browseRentalsButton.setOnClickListener(v -> {
            // Scroll to vehicles section
            vehiclesRecyclerView.requestFocus();
            // TODO: Implement smooth scrolling
        });

        Button exploreToursButton = findViewById(R.id.exploreToursButton);
        exploreToursButton.setOnClickListener(v -> {
            // Scroll to tours section
            toursRecyclerView.requestFocus();
            // TODO: Implement smooth scrolling
        });

        Button viewAllVehiclesButton = findViewById(R.id.viewAllVehiclesButton);
        viewAllVehiclesButton.setOnClickListener(v -> {
            // Start VanCatalogueActivity when button is clicked
            Intent intent = new Intent(MainActivity.this, VanCatalogueActivity.class);
            startActivity(intent);
        });

        // Add click listener for Vans tab in bottom navigation
        LinearLayout navMap = findViewById(R.id.navMap);
        LinearLayout Tours = findViewById(R.id.navProfile);
        navMap.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, VanCatalogueActivity.class);
            startActivity(intent);
        });
        Tours.setOnClickListener(v -> {
            Intent intent = new Intent(this, TourActivity.class);
            startActivity(intent);
        });
    }

    private List<Vehicle> createSampleVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();

        // Sample Vehicle 1
        Vehicle vehicle1 = new Vehicle();
        vehicle1.setName("Mercedes Sprinter Camper");
        vehicle1.setImageResId(R.drawable.camper_van);
        vehicle1.setTagText("Popular");
        vehicle1.setTagColor(R.color.blue_600);
        vehicle1.setRating(4.7f);
        vehicle1.setReviewCount(128);
        vehicle1.setSleepCount(4);
        vehicle1.setFeature1Icon(R.drawable.ic_kitchen);
        vehicle1.setFeature1Text("Kitchen");
        vehicle1.setFeature2Icon(R.drawable.ic_shower);
        vehicle1.setFeature2Text("Shower");
        vehicle1.setPrice(189);
        vehicle1.setCategory("Camper Vans");
        vehicles.add(vehicle1);

        // Sample Vehicle 2
        Vehicle vehicle2 = new Vehicle();
        vehicle2.setName("Airstream Bambi Caravan");
        vehicle2.setImageResId(R.drawable.caravan);
        vehicle2.setTagText("Eco");
        vehicle2.setTagColor(R.color.green_600);
        vehicle2.setRating(5.0f);
        vehicle2.setReviewCount(64);
        vehicle2.setSleepCount(2);
        vehicle2.setFeature1Icon(R.drawable.ic_kitchen);
        vehicle2.setFeature1Text("Kitchen");
        vehicle2.setFeature2Icon(R.drawable.ic_solar);
        vehicle2.setFeature2Text("Solar");
        vehicle2.setPrice(159);
        vehicle2.setCategory("Caravans");
        vehicles.add(vehicle2);

        // Sample Vehicle 3
        Vehicle vehicle3 = new Vehicle();
        vehicle3.setName("Ford Transit 4x4");
        vehicle3.setImageResId(R.drawable.fourx4_van);
        vehicle3.setTagText("4x4");
        vehicle3.setTagColor(R.color.red_600);
        vehicle3.setRating(4.2f);
        vehicle3.setReviewCount(93);
        vehicle3.setSleepCount(2);
        vehicle3.setFeature1Icon(R.drawable.ic_mountain);
        vehicle3.setFeature1Text("Off-road");
        vehicle3.setFeature2Icon(R.drawable.ic_winch);
        vehicle3.setFeature2Text("Winch");
        vehicle3.setPrice(219);
        vehicle3.setCategory("4x4 Adventure");
        vehicles.add(vehicle3);

        return vehicles;
    }

    private List<Tour> createSampleTours() {
        List<Tour> tours = new ArrayList<>();

        // Sample Tour 1
        Tour tour1 = new Tour();
        tour1.setName("Southwest National Parks");
        tour1.setImageResId(R.drawable.southwest_tour);
        tour1.setDuration("7 days");
        tour1.setDescription("Explore the iconic landscapes of Utah and Arizona's national parks");
        tour1.setStartLocation("Start: Moab, UT");
        tour1.setDistance("850 miles");
        tours.add(tour1);

        return tours;
    }
}