package com.example.budimadani;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    // Declare UI components
    private Spinner fuelType;
    private TextView marketPrice, totalCost, rebate, savingsCost;
    private EditText fuelUsage;
    private Switch budiSwitch;
    private Button calcButton;

    // Global variables to store live prices fetched from data.gov.my
    private double liveRon95 = 3.87; // Default fallback prices if API fails
    private double liveRon97 = 4.70; // Matches your current table row
    private double liveDiesel = 4.87;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main); // Link to XML [cite: 33]

        // Link the toolbar using the ID from your layout screenshot
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.AppToolbar);
        setSupportActionBar(toolbar);

        // 1. Initialize all Views by their XML IDs
        fuelType = findViewById(R.id.FuelType);
        marketPrice = findViewById(R.id.marketPrice);
        fuelUsage = findViewById(R.id.fuelUsage);
        budiSwitch = findViewById(R.id.budiSwitch);
        calcButton = findViewById(R.id.calcButton);
        totalCost = findViewById(R.id.totalCost);
        rebate = findViewById(R.id.rebate);
        savingsCost = findViewById(R.id.savingsCost);

        // Handle System Bar Padding (Default Android Studio code)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // FETCH LIVE PRICES IMMEDIATELY ON STARTUP
        fetchLiveFuelPrices();

        // 2. Handle Spinner Selection Logic
        fuelType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updatePriceDisplay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // 3. Handle Calculation Button Click
        calcButton.setOnClickListener(v -> {
            performCalculation();
        });
    }

    private void fetchLiveFuelPrices() {
        // Paste your exact raw GitHub URL here
        String url = "https://raw.githubusercontent.com/dylanshfk/budimadani/refs/heads/main/fuel_prices.json";

        com.android.volley.toolbox.JsonObjectRequest request = new com.android.volley.toolbox.JsonObjectRequest(
                com.android.volley.Request.Method.GET, url, null,
                response -> {
                    try {
                        // Extract the values directly from your custom JSON keys
                        liveRon95 = response.getDouble("ron95");
                        liveRon97 = response.getDouble("ron97");
                        liveDiesel = response.getDouble("diesel");

                        // Instantly push the numbers to your Spinner screen display
                        updatePriceDisplay();

                        android.widget.Toast.makeText(MainActivity.this,
                                "Fuel prices successfully synced from GitHub!",
                                android.widget.Toast.LENGTH_SHORT).show();

                    } catch (org.json.JSONException e) {
                        e.printStackTrace();
                        android.widget.Toast.makeText(MainActivity.this,
                                "Parsing Error: Using standard fallbacks",
                                android.widget.Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // If your laptop/tablet loses Wi-Fi entirely, it uses your hardcoded variables safely
                    android.widget.Toast.makeText(MainActivity.this,
                            "Offline Mode: Loaded standard pricing",
                            android.widget.Toast.LENGTH_SHORT).show();
                }
        );

        // Enqueue the request to run smoothly in the background
        com.android.volley.toolbox.Volley.newRequestQueue(this).add(request);
    }

    private void updatePriceDisplay() {
        String selected = fuelType.getSelectedItem().toString();

        // Dynamically update the UI based on your Spinner selection
        if (selected.equals("RON95")) {
            marketPrice.setText(String.format("RM %.2f", liveRon95));
            budiSwitch.setVisibility(View.VISIBLE);
        } else if (selected.equals("RON97")) {
            marketPrice.setText(String.format("RM %.2f", liveRon97));
            budiSwitch.setVisibility(View.GONE);
            budiSwitch.setChecked(false);
        } else {
            marketPrice.setText(String.format("RM %.2f", liveDiesel));
            budiSwitch.setVisibility(View.GONE);
            budiSwitch.setChecked(false);
        }
    }

    /**
     * Handles the logic for calculating petrol costs, rebates, and savings.
     * Also manages the visibility of results based on fuel type.
     */
    private void performCalculation() {
        String usageStr = fuelUsage.getText().toString();

        // Validate input to prevent app crashes
        if (usageStr.isEmpty()) {
            fuelUsage.setError("Enter fuel usage in liters");
            return;
        }

        try {
            // Parse inputs (Removing "RM" prefix if present to avoid errors)
            double price = Double.parseDouble(marketPrice.getText().toString().replace("RM ", ""));
            double usage = Double.parseDouble(usageStr);

            // Step 1: Calculate Total Petrol Cost [cite: 18, 25, 26]
            double totalPetrolCost = usage * price;
            totalCost.setText(String.format("Total Cost: RM %.2f", totalPetrolCost));

            // Step 2: Handle Visibility and Specific Logic for RON95
            if (budiSwitch.getVisibility() == View.VISIBLE && budiSwitch.isChecked()) {
                // Display rebate and savings for RON95 [cite: 5, 6]
                rebate.setVisibility(View.VISIBLE);
                savingsCost.setVisibility(View.VISIBLE);

                double budiRebate = 0;
                // Apply BUDI MADANI rebate if eligible [cite: 15, 20, 23, 28]
                if (budiSwitch.isChecked()) {
                    budiRebate = usage * 1.99; // Subsidy rate [cite: 20, 30]
                }

                // Step 3: Calculate Total Saving [cite: 21, 31, 32]
                double totalSaving = totalPetrolCost - budiRebate;

                rebate.setText(String.format("BUDI Rebate: RM %.2f", budiRebate));
                savingsCost.setText(String.format("Total Savings: RM %.2f", totalSaving));
            } else {
                // Hide extra fields for RON97 and Diesel as they are not eligible for BUDI MADANI, or for RON95 if BUDI95 is not selected [cite: 5, 6]
                rebate.setVisibility(View.GONE);
                savingsCost.setVisibility(View.GONE);
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid input. Please check your values.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // This loads your main_menu.xml into the toolbar [cite: 219]
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        // Get the ID of the item that was clicked [cite: 223]
        int id = item.getItemId();

        // Check if it matches your "About" item ID from the screenshot
        if (id == R.id.aboutApp) {
            // For now, we use a Toast to confirm it works [cite: 229, 230]
            android.content.Intent intent = new android.content.Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}