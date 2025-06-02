package com.example.wanderwheels;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

import java.text.NumberFormat;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private TextView tvVanName, tvVanPrice, tvCustomerName, tvCustomerEmail, tvBookingDetails;
    private TextInputEditText etCardNumber, etCardHolder, etExpiryDate, etCvv;
    private RadioGroup rgPaymentMethod;
    private MaterialCardView cardVisa, cardMastercard, cardPaypal;
    private Button btnConfirmPayment;

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

        String vanName = intent.getStringExtra("vanName");
        int vanPrice = intent.getIntExtra("vanPrice", 0);
        String fullName = intent.getStringExtra("FULL_NAME");
        String email = intent.getStringExtra("EMAIL");
        String phone = intent.getStringExtra("PHONE");
        String date = intent.getStringExtra("DATE");
        String time = intent.getStringExtra("TIME");
        String city = intent.getStringExtra("CITY");
        String requests = intent.getStringExtra("ADDITIONAL_REQUESTS");

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
        cardVisa.setOnClickListener(v -> {
            rgPaymentMethod.check(R.id.rbVisa);
            Log.d("Payment", "Visa selected");
        });

        cardMastercard.setOnClickListener(v -> {
            rgPaymentMethod.check(R.id.rbMastercard);
            Log.d("Payment", "Mastercard selected");
        });

        cardPaypal.setOnClickListener(v -> {
            rgPaymentMethod.check(R.id.rbPaypal);
            Log.d("Payment", "PayPal selected");
        });

        // Suivi des changements de sélection
        rgPaymentMethod.setOnCheckedChangeListener((group, checkedId) -> {
            Log.d("Payment", "RadioGroup selection changed: " + checkedId);
            updateCardSelection(checkedId);
        });
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
        btnConfirmPayment.setEnabled(false);
        btnConfirmPayment.setText("Paiement confirmé !");
        btnConfirmPayment.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.green_600));

        Toast.makeText(this, "Paiement effectué avec succès", Toast.LENGTH_SHORT).show();

        new android.os.Handler().postDelayed(() -> {
            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
            Log.e("Validation", "Aucune méthode de paiement sélectionnée");
            return false;
        }
        Log.d("Validation", "Méthode de paiement valide");
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