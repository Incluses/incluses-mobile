package project.interdisciplinary.inclusesapp.Presentation.Enterprise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;

import project.interdisciplinary.inclusesapp.Presentation.JobsOfInterest;
import project.interdisciplinary.inclusesapp.Presentation.LoginUser;
import project.interdisciplinary.inclusesapp.Presentation.RegisterUser2;
import project.interdisciplinary.inclusesapp.Presentation.RegisterUserActivity;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityRegisterEnterpriseBinding;

public class RegisterEnterprise extends AppCompatActivity {

    private ActivityRegisterEnterpriseBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        binding = ActivityRegisterEnterpriseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //button back
        binding.imageViewLoginEnterpriseBackButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterEnterprise.this, RegisterUserActivity.class);
                    startActivity(intent);
                    finish();
                }
        );

        binding.textViewLoginEnterpriseBack.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterEnterprise.this, RegisterUserActivity.class);
                    startActivity(intent);
                    finish();
                }
        );

        //continue button
        binding.continueButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterEnterprise.this, HomeEnterprise.class);
                    startActivity(intent);
                    finish();
                }
        );

        //enter button
        binding.enterTextView.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterEnterprise.this, LoginUser.class);
                    startActivity(intent);
                    finish();
                }
        );

        // Formatar o número de CEP
        binding.cepEditText.addTextChangedListener(new TextWatcher() {
            private static final String CEP_FORMAT = "#####-###";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                String cleanInput = input.replaceAll("[^\\d]", ""); // Remove caracteres não numéricos

                StringBuilder formattedCEP = new StringBuilder();

                for (int i = 0, j = 0; i < cleanInput.length() && j < CEP_FORMAT.length(); i++) {
                    char c = cleanInput.charAt(i);
                    char f = CEP_FORMAT.charAt(j);
                    if (f == '#') {
                        formattedCEP.append(c);
                        j++;
                    } else {
                        formattedCEP.append(f);
                        j++;
                        i--;
                    }
                }

                binding.cepEditText.removeTextChangedListener(this);
                binding.cepEditText.setText(formattedCEP.toString());
                binding.cepEditText.setSelection(formattedCEP.length());
                binding.cepEditText.addTextChangedListener(this);
            }
        });



    }
}