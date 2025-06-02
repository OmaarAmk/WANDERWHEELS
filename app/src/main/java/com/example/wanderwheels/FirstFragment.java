package com.example.wanderwheels;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FirstFragment extends Fragment {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_first, container, false);

        emailEditText = root.findViewById(R.id.emailEditText);
        passwordEditText = root.findViewById(R.id.passwordEditText);
        loginButton = root.findViewById(R.id.loginButton);
        forgotPasswordTextView = root.findViewById(R.id.forgotPasswordTextView);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ajoute ici la logique de connexion (Firebase ou autre)
            Toast.makeText(getContext(), "Tentative de connexion avec\nEmail: " + email, Toast.LENGTH_SHORT).show();
        });

        forgotPasswordTextView.setOnClickListener(v -> {
            // Logique mot de passe oublié
            Toast.makeText(getContext(), "Fonction mot de passe oublié déclenchée", Toast.LENGTH_SHORT).show();
        });

        return root;
    }
}