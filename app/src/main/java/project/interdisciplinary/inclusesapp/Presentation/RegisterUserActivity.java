package project.interdisciplinary.inclusesapp.Presentation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityLoginUserBinding;
import project.interdisciplinary.inclusesapp.databinding.ActivityRegisterUserBinding;

public class RegisterUserActivity extends AppCompatActivity {

    private ActivityRegisterUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //button back
        binding.imageViewLoginUserBackButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterUserActivity.this, Login.class);
                    startActivity(intent);
                }
        );

        binding.textViewLoginUserBack.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterUserActivity.this, Login.class);
                    startActivity(intent);
                }
        );

        binding.continueButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterUserActivity.this, RegisterUser2.class);
                startActivity(intent);
            }
        });

        // Format phone number
        binding.phoneEditText.addTextChangedListener(new TextWatcher() {
            private static final String PHONE_FORMAT = "(##) #####-####";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                String cleanInput = input.replaceAll("[^\\d.]", ""); // Remove todos os caracteres que não são números

                StringBuilder formattedPhone = new StringBuilder();

                for (int i = 0, j = 0; i < cleanInput.length() && j < PHONE_FORMAT.length(); i++) {
                    char c = cleanInput.charAt(i);
                    char f = PHONE_FORMAT.charAt(j);
                    if (f == '#') {
                        formattedPhone.append(c);
                        j++;
                    } else {
                        formattedPhone.append(f);
                        j++;
                        i--; // Não incrementar índice de entrada quando adicionar o formato
                    }
                }

                // Remover o listener para evitar loop de chamadas recursivas
                binding.phoneEditText.removeTextChangedListener(this);
                binding.phoneEditText.setText(formattedPhone.toString());
                binding.phoneEditText.setSelection(formattedPhone.length());  // Posiciona o cursor no final do texto
                binding.phoneEditText.addTextChangedListener(this);
            }
        });

        //enter button
        binding.enterTextView.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterUserActivity.this, LoginUser.class);
                    startActivity(intent);
                }
        );
    }
}