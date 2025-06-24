package com.example.wanderwheels;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvVanName, tvVanPrice, tvCustomerName, tvCustomerEmail, tvBookingDetails;
    private TextInputEditText etCardNumber, etCardHolder, etExpiryDate, etCvv;
    private RadioGroup rgPaymentMethod;
    private MaterialCardView cardVisa, cardMastercard, cardPaypal;
    private Button btnConfirmPayment;

    private String vanName, fullName, email, phone, date, time, city, requests;
    private int vanPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initializeViews();
        displayBookingDetails();
        setupPaymentMethodSelection();
        setupConfirmButton();
    }

    private void initializeViews() {
        tvVanName = findViewById(R.id.tvVanName);
        tvVanPrice = findViewById(R.id.tvVanPrice);
        tvCustomerName = findViewById(R.id.tvCustomerName);
        tvCustomerEmail = findViewById(R.id.tvCustomerEmail);
        tvBookingDetails = findViewById(R.id.tvBookingDetails);

        etCardNumber = findViewById(R.id.etCardNumber);
        etCardHolder = findViewById(R.id.etCardHolder);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        etCvv = findViewById(R.id.etCvv);

        rgPaymentMethod = findViewById(R.id.rgPaymentMethod);
        cardVisa = findViewById(R.id.cardVisa);
        cardMastercard = findViewById(R.id.cardMastercard);
        cardPaypal = findViewById(R.id.cardPaypal);

        btnConfirmPayment = findViewById(R.id.btnConfirmPayment);
    }

    private void displayBookingDetails() {
        Intent intent = getIntent();
        vanName = intent.getStringExtra("vanName");
        vanPrice = intent.getIntExtra("vanPrice", 0);
        fullName = intent.getStringExtra("FULL_NAME");
        email = intent.getStringExtra("EMAIL");
        phone = intent.getStringExtra("PHONE");
        date = intent.getStringExtra("START_DATE");
        time = intent.getStringExtra("TIME");
        city = intent.getStringExtra("CITY");
        requests = intent.getStringExtra("ADDITIONAL_REQUESTS");

        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.FRANCE);
        String formattedPrice = format.format(vanPrice);

        tvVanName.setText(vanName);
        tvVanPrice.setText(formattedPrice);
        tvCustomerName.setText(fullName);
        tvCustomerEmail.setText(email);

        String bookingDetails = String.format("📅 %s à %s\n📞 %s\n📍 %s\n📝 %s",
                date, time, phone, city, requests.isEmpty() ? "Aucune demande particulière" : requests);
        tvBookingDetails.setText(bookingDetails);
    }

    private void setupPaymentMethodSelection() {
        // Réinitialiser la sélection au démarrage
        rgPaymentMethod.clearCheck();
        resetCardSelection();
        // Gestion des clics sur les cartes
        cardVisa.setOnClickListener(v -> rgPaymentMethod.check(R.id.rbVisa));
        cardMastercard.setOnClickListener(v -> rgPaymentMethod.check(R.id.rbMastercard));
        cardPaypal.setOnClickListener(v -> rgPaymentMethod.check(R.id.rbPaypal));

        rgPaymentMethod.setOnCheckedChangeListener((group, checkedId) -> updateCardSelection(checkedId));
    }

    private void updateCardSelection(int checkedId) {
        resetCardSelection();

        if (checkedId == R.id.rbVisa) {
            cardVisa.setStrokeColor(ContextCompat.getColor(this, R.color.blue_500));
            cardVisa.setStrokeWidth(4);
        } else if (checkedId == R.id.rbMastercard) {
            cardMastercard.setStrokeColor(ContextCompat.getColor(this, R.color.red_700));
            cardMastercard.setStrokeWidth(4);
        } else if (checkedId == R.id.rbPaypal) {
            cardPaypal.setStrokeColor(ContextCompat.getColor(this, R.color.blue_500));
            cardPaypal.setStrokeWidth(4);
        }
    }

    private void resetCardSelection() {
        cardVisa.setStrokeWidth(0);
        cardMastercard.setStrokeWidth(0);
        cardPaypal.setStrokeWidth(0);
    }

    private void setupConfirmButton() {
        btnConfirmPayment.setOnClickListener(v -> {
            if (validatePaymentForm()) {
                processPayment();
            }
        });
    }

    private void processPayment() {
        String cardHolder = etCardHolder.getText().toString();
        String cardNumber = etCardNumber.getText().toString();
        String expiryDate = etExpiryDate.getText().toString();
        String paymentMethod = ((RadioButton) findViewById(rgPaymentMethod.getCheckedRadioButtonId())).getText().toString();

        JSONObject json = new JSONObject();
        try {
            json.put("vanName", vanName);
            json.put("vanPrice", vanPrice);
            json.put("fullName", fullName);
            json.put("email", email);
            json.put("phone", phone);
            json.put("date", date);
            json.put("time", time);
            json.put("city", city);
            json.put("requests", requests);
            json.put("paymentMethod", paymentMethod);

            JSONObject cardInfo = new JSONObject();
            cardInfo.put("cardHolder", cardHolder);
            cardInfo.put("cardNumber", cardNumber);
            cardInfo.put("expiryDate", expiryDate);

            json.put("cardInfo", cardInfo);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Erreur JSON", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = ApiClient.BASE_URL + "/booking"; // 🔗 ton endpoint backend

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, json,
                response -> {
                    Toast.makeText(getApplicationContext(), "Réservation enregistrée !", Toast.LENGTH_SHORT).show();
                    retourAccueil();
                },
                error -> {
                    Toast.makeText(getApplicationContext(), "Erreur de réseau ou serveur", Toast.LENGTH_LONG).show();
                    Log.e("VolleyError", "Erreur Volley : " + error.getMessage(), error);

                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void retourAccueil() {
        btnConfirmPayment.setEnabled(false);
        btnConfirmPayment.setText("Paiement confirmé !");
        btnConfirmPayment.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green_600));

        new android.os.Handler().postDelayed(() -> {
            Intent i = new Intent(PaymentActivity.this, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        }, 2000);
    }

    private boolean validatePaymentForm() {
        boolean isValid = true;
        if (!validatePaymentMethod()) isValid = false;
        if (!validateCardNumber()) isValid = false;
        if (!validateCardHolder()) isValid = false;
        if (!validateExpiryDate()) isValid = false;
        if (!validateCvv()) isValid = false;
        return isValid;
    }

    private boolean validatePaymentMethod() {
        if (rgPaymentMethod.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Veuillez sélectionner un mode de paiement", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private boolean validateCardNumber() {
        String number = etCardNumber.getText().toString().trim();
        if (number.length() != 16) {
            etCardNumber.setError("Numéro de carte invalide (16 chiffres requis)");
            return false;
        }
        etCardNumber.setError(null);
        return true;
    }

    private boolean validateCardHolder() {
        String holder = etCardHolder.getText().toString().trim();
        if (holder.isEmpty()) {
            etCardHolder.setError("Veuillez entrer le nom du titulaire");
            return false;
        }
        etCardHolder.setError(null);
        return true;
    }

    private boolean validateExpiryDate() {
        String date = etExpiryDate.getText().toString().trim();
        if (!date.matches("^(0[1-9]|1[0-2])[0-9]{2}$"
        )) {
            etExpiryDate.setError("Format MM/AA invalide (ex: 0525)");
            return false;
        }
        etExpiryDate.setError(null);
        return true;
    }


    private boolean validateCvv() {
        String cvv = etCvv.getText().toString().trim();
        if (cvv.length() != 3) {
            etCvv.setError("Code CVV invalide (3 chiffres requis)");
            return false;
        }
        etCvv.setError(null);
        return true;
    }
}