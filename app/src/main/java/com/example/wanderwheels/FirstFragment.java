package com.example.wanderwheels;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class FirstFragment extends Fragment {

    private View loginLayout, signUpLayout;
    private EditText loginEmail, loginPassword;
    private EditText signUpFirstName, signUpLastName, signUpEmail, signUpPassword, signUpBirthDate;
    private Button loginButton, switchToSignUpButton;
    private Button signUpButton, switchToLoginButton;
    private TextView forgotPasswordTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_first, container, false);

        loginLayout = root.findViewById(R.id.loginLayout);
        loginEmail = root.findViewById(R.id.loginEmail);
        loginPassword = root.findViewById(R.id.loginPassword);
        loginButton = root.findViewById(R.id.loginButton);
        switchToSignUpButton = root.findViewById(R.id.switchToSignUpButton);
        forgotPasswordTextView = root.findViewById(R.id.forgotPasswordTextView);

        signUpLayout = root.findViewById(R.id.signUpLayout);
        signUpFirstName = root.findViewById(R.id.signUpFirstName);
        signUpLastName = root.findViewById(R.id.signUpLastName);
        signUpEmail = root.findViewById(R.id.signUpEmail);
        signUpPassword = root.findViewById(R.id.signUpPassword);
        signUpBirthDate = root.findViewById(R.id.signUpBirthDate);
        signUpButton = root.findViewById(R.id.signUpButton);
        switchToLoginButton = root.findViewById(R.id.switchToLoginButton);

        showLoginForm();

        loginButton.setOnClickListener(v -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(getContext(), "Connexion réussie", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            getActivity().finish();
        });

        forgotPasswordTextView.setOnClickListener(v -> showForgotPasswordDialog());

        signUpButton.setOnClickListener(v -> {
            String firstName = signUpFirstName.getText().toString().trim();
            String lastName = signUpLastName.getText().toString().trim();
            String email = signUpEmail.getText().toString().trim();
            String password = signUpPassword.getText().toString().trim();
            String birthDate = signUpBirthDate.getText().toString().trim();

            if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(lastName) ||
                    TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||
                    TextUtils.isEmpty(birthDate)) {
                Toast.makeText(getContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
                return;
            }

            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("firstName", firstName);
                jsonBody.put("lastName", lastName);
                jsonBody.put("email", email);
                jsonBody.put("password", password);
                jsonBody.put("birthDate", birthDate);
            } catch (JSONException e) {
                Toast.makeText(getContext(), "Erreur JSON", Toast.LENGTH_SHORT).show();
                return;
            }

            String url = ApiClient.BASE_URL + "/signup";

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                    response -> {
                        Toast.makeText(getContext(), "Inscription réussie", Toast.LENGTH_SHORT).show();
                        showLoginForm();
                    },
                    error -> Toast.makeText(getContext(), "Erreur serveur", Toast.LENGTH_SHORT).show()
            );

            Volley.newRequestQueue(requireContext()).add(request);
        });

        switchToSignUpButton.setOnClickListener(v -> showSignUpForm());
        switchToLoginButton.setOnClickListener(v -> showLoginForm());

        return root;
    }

    private void showLoginForm() {
        loginLayout.setVisibility(View.VISIBLE);
        signUpLayout.setVisibility(View.GONE);
    }

    private void showSignUpForm() {
        loginLayout.setVisibility(View.GONE);
        signUpLayout.setVisibility(View.VISIBLE);
    }

    private void showForgotPasswordDialog() {
        if (getContext() == null) return;

        EditText emailInput = new EditText(getContext());
        emailInput.setHint("Entrez votre email");
        emailInput.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        new AlertDialog.Builder(getContext())
                .setTitle("Mot de passe oublié")
                .setMessage("Veuillez entrer votre email pour recevoir un lien de réinitialisation.")
                .setView(emailInput)
                .setPositiveButton("Envoyer", (dialog, which) -> {
                    String enteredEmail = emailInput.getText().toString().trim();
                    if (TextUtils.isEmpty(enteredEmail)) {
                        Toast.makeText(getContext(), "Email requis", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Lien envoyé à : " + enteredEmail, Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}
