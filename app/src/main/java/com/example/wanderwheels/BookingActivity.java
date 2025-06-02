package com.example.wanderwheels;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class BookingActivity extends AppCompatActivity {

    private TextInputLayout tilFullName, tilEmail, tilPhoneNumber, tilBookingDate, tilBookingTime, tilCity;
    private TextInputEditText etFullName, etEmail, etPhoneNumber, etBookingDate, etBookingTime, etAdditionalRequests, etCity;
    private Button btnProceedToPayment;
    private Calendar calendar;
    private SimpleDateFormat dateFormatter, timeFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        initializeViews();
        setupCalendarAndFormatters();
        setupDateAndTimeFields();
        setupSubmitButton();
    }

    private void initializeViews() {
        tilFullName = findViewById(R.id.tilFullName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPhoneNumber = findViewById(R.id.tilPhoneNumber);
        tilBookingDate = findViewById(R.id.tilBookingDate);
        tilBookingTime = findViewById(R.id.tilBookingTime);
        tilCity = findViewById(R.id.tilCity);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etBookingDate = findViewById(R.id.etBookingDate);
        etBookingTime = findViewById(R.id.etBookingTime);
        etAdditionalRequests = findViewById(R.id.etAdditionalRequests);
        etCity = findViewById(R.id.etCity);

        btnProceedToPayment = findViewById(R.id.btnProceedToPayment);
    }

    private void setupCalendarAndFormatters() {
        calendar = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        timeFormatter = new SimpleDateFormat("HH:mm", Locale.getDefault());

        Calendar defaultTime = Calendar.getInstance();
        defaultTime.add(Calendar.HOUR_OF_DAY, 1);

        etBookingDate.setText(dateFormatter.format(calendar.getTime()));
        etBookingTime.setText(timeFormatter.format(defaultTime.getTime()));
    }

    private void setupDateAndTimeFields() {
        etBookingDate.setOnClickListener(v -> showDatePicker());
        etBookingTime.setOnClickListener(v -> showTimePicker());
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    etBookingDate.setText(dateFormatter.format(calendar.getTime()));
                    tilBookingDate.setError(null);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) -> {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    etBookingTime.setText(timeFormatter.format(calendar.getTime()));
                    tilBookingTime.setError(null);
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    private void setupSubmitButton() {
        btnProceedToPayment.setOnClickListener(v -> {
            if (validateForm()) {
                btnProceedToPayment.setEnabled(false);
                btnProceedToPayment.setText("Réservation confirmée !");
                btnProceedToPayment.setBackgroundTintList(getResources().getColorStateList(R.color.blue_500));

                new Handler(Looper.getMainLooper()).postDelayed(this::proceedToPayment, 1500);
            }
        });
    }

    private boolean validateForm() {
        boolean isValid = true;

        // Validation du nom
        String fullName = etFullName.getText().toString().trim();
        if (fullName.isEmpty() || fullName.length() < 3) {
            tilFullName.setError("Veuillez entrer un nom valide");
            isValid = false;
        } else {
            tilFullName.setError(null);
        }

        // Validation de l'email
        String email = etEmail.getText().toString().trim();
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
        if (email.isEmpty() || !emailPattern.matcher(email).matches()) {
            tilEmail.setError("Veuillez entrer une adresse email valide");
            isValid = false;
        } else {
            tilEmail.setError(null);
        }

        // Validation du téléphone
        String phone = etPhoneNumber.getText().toString().trim();
        Pattern phonePattern = Pattern.compile("[0-9]{10,15}");
        if (phone.isEmpty() || !phonePattern.matcher(phone).matches()) {
            tilPhoneNumber.setError("Veuillez entrer un numéro de téléphone valide");
            isValid = false;
        } else {
            tilPhoneNumber.setError(null);
        }

        // Validation de la ville
        String city = etCity.getText().toString().trim();
        Pattern cityPattern = Pattern.compile("^[a-zA-Z\\séèêëàâäîïôöùûüç-]{2,50}$");
        if (city.isEmpty() || !cityPattern.matcher(city).matches()) {
            tilCity.setError("Veuillez entrer un nom de ville valide (2-50 caractères)");
            isValid = false;
        } else {
            tilCity.setError(null);
        }

        // Validation de la date
        if (etBookingDate.getText().toString().trim().isEmpty()) {
            tilBookingDate.setError("Veuillez sélectionner une date");
            isValid = false;
        } else {
            tilBookingDate.setError(null);
        }

        // Validation de l'heure
        if (etBookingTime.getText().toString().trim().isEmpty()) {
            tilBookingTime.setError("Veuillez sélectionner une heure");
            isValid = false;
        } else {
            tilBookingTime.setError(null);
        }

        return isValid;
    }

    private void proceedToPayment() {
        Intent intent = new Intent(BookingActivity.this, PaymentActivity.class);
        intent.putExtra("vanName", getIntent().getStringExtra("vanName"));
        intent.putExtra("vanPrice", getIntent().getIntExtra("vanPrice", 0));
        intent.putExtra("FULL_NAME", etFullName.getText().toString().trim());
        intent.putExtra("EMAIL", etEmail.getText().toString().trim());
        intent.putExtra("PHONE", etPhoneNumber.getText().toString().trim());
        intent.putExtra("DATE", etBookingDate.getText().toString().trim());
        intent.putExtra("TIME", etBookingTime.getText().toString().trim());
        intent.putExtra("CITY", etCity.getText().toString().trim());
        intent.putExtra("ADDITIONAL_REQUESTS", etAdditionalRequests.getText().toString().trim());
        startActivity(intent);
    }
}