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
import android.app.AlertDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FirstFragment extends Fragment {

    private EditText emailEditText, passwordEditText;
    private Button loginButton, backButton;
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
        backButton = root.findViewById(R.id.backButton);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(getContext(), "Tentative de connexion avec\nEmail: " + email, Toast.LENGTH_SHORT).show();
        });

        forgotPasswordTextView.setOnClickListener(v -> {
            showForgotPasswordDialog();
        });

        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
        });

        return root;
    }

    private void showForgotPasswordDialog() {
        if (getContext() == null) return;

        EditText emailInput = new EditText(getContext());
        emailInput.setHint("Entrez votre email");
        emailInput.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        new AlertDialog.Builder(getContext())
                .setTitle("Réinitialisation du mot de passe")
                .setMessage("Veuillez entrer votre email pour recevoir un lien de réinitialisation.")
                .setView(emailInput)
                .setPositiveButton("Envoyer", (dialog, which) -> {
                    String enteredEmail = emailInput.getText().toString().trim();
                    if (TextUtils.isEmpty(enteredEmail)) {
                        Toast.makeText(getContext(), "Email requis", Toast.LENGTH_SHORT).show();
                    } else {
                        // Simulated success message — replace this with actual backend logic
                        Toast.makeText(getContext(), "Lien envoyé à : " + enteredEmail, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}
