package com.example.wanderwheels;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Pattern;

public class BookingActivity extends AppCompatActivity {

    private TextInputLayout tilFullName, tilEmail, tilPhoneNumber, tilStartDate, tilEndDate, tilBookingTime, tilCity;
    private TextInputEditText etFullName, etEmail, etPhoneNumber, etStartDate, etEndDate, etBookingTime, etAdditionalRequests, etCity;
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
        tilStartDate = findViewById(R.id.tilStartDate);
        tilEndDate = findViewById(R.id.tilEndDate);
        tilBookingTime = findViewById(R.id.tilBookingTime);
        tilCity = findViewById(R.id.tilCity);

        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etStartDate = findViewById(R.id.etStartDate);
        etEndDate = findViewById(R.id.etEndDate);
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

        etBookingTime.setText(timeFormatter.format(defaultTime.getTime()));
    }

    private void setupDateAndTimeFields() {
        etStartDate.setOnClickListener(v -> showStartDatePicker());
        etEndDate.setOnClickListener(v -> showEndDatePicker());
        etBookingTime.setOnClickListener(v -> showTimePicker());
    }

    private void showStartDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    etStartDate.setText(dateFormatter.format(calendar.getTime()));
                    tilStartDate.setError(null);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showEndDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    etEndDate.setText(dateFormatter.format(calendar.getTime()));
                    tilEndDate.setError(null);
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

        // Nom
        String fullName = etFullName.getText().toString().trim();
        if (fullName.isEmpty() || fullName.length() < 3) {
            tilFullName.setError("Veuillez entrer un nom valide");
            isValid = false;
        } else {
            tilFullName.setError(null);
        }

        // Email
        String email = etEmail.getText().toString().trim();
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
        if (email.isEmpty() || !emailPattern.matcher(email).matches()) {
            tilEmail.setError("Veuillez entrer une adresse email valide");
            isValid = false;
        } else {
            tilEmail.setError(null);
        }

        // Téléphone
        String phone = etPhoneNumber.getText().toString().trim();
        Pattern phonePattern = Pattern.compile("[0-9]{10,15}");
        if (phone.isEmpty() || !phonePattern.matcher(phone).matches()) {
            tilPhoneNumber.setError("Veuillez entrer un numéro de téléphone valide");
            isValid = false;
        } else {
            tilPhoneNumber.setError(null);
        }

        // Ville
        String city = etCity.getText().toString().trim();
        Pattern cityPattern = Pattern.compile("^[a-zA-Z\\séèêëàâäîïôöùûüç-]{2,50}$");
        if (city.isEmpty() || !cityPattern.matcher(city).matches()) {
            tilCity.setError("Veuillez entrer un nom de ville valide");
            isValid = false;
        } else {
            tilCity.setError(null);
        }

        // Dates
        String startDateStr = etStartDate.getText().toString().trim();
        String endDateStr = etEndDate.getText().toString().trim();
        if (startDateStr.isEmpty()) {
            tilStartDate.setError("Veuillez sélectionner une date de début");
            isValid = false;
        } else {
            tilStartDate.setError(null);
        }
        if (endDateStr.isEmpty()) {
            tilEndDate.setError("Veuillez sélectionner une date de fin");
            isValid = false;
        } else {
            tilEndDate.setError(null);
        }

        try {
            if (!startDateStr.isEmpty() && !endDateStr.isEmpty()) {
                Calendar startCal = Calendar.getInstance();
                Calendar endCal = Calendar.getInstance();
                startCal.setTime(dateFormatter.parse(startDateStr));
                endCal.setTime(dateFormatter.parse(endDateStr));

                if (endCal.before(startCal)) {
                    tilEndDate.setError("La date de fin doit être après la date de début");
                    isValid = false;
                } else {
                    tilEndDate.setError(null);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            isValid = false;
        }

        // Heure
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
        intent.putExtra("START_DATE", etStartDate.getText().toString().trim());
        intent.putExtra("END_DATE", etEndDate.getText().toString().trim());
        intent.putExtra("TIME", etBookingTime.getText().toString().trim());
        intent.putExtra("CITY", etCity.getText().toString().trim());
        intent.putExtra("ADDITIONAL_REQUESTS", etAdditionalRequests.getText().toString().trim());
        startActivity(intent);
    }
}