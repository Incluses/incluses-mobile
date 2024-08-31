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

public class LoginUser extends AppCompatActivity {

    private ActivityLoginUserBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityLoginUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //button back
        binding.imageViewLoginUserBackButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(LoginUser.this, Login.class);
                    startActivity(intent);
                }
        );

        binding.textViewLoginUserBack.setOnClickListener(
                v -> {
                    Intent intent = new Intent(LoginUser.this, Login.class);
                    startActivity(intent);
                }
        );

        binding.registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginUser.this, RegisterUserActivity.class);
                startActivity(intent);
            }
        });

        // Format CPF number
        binding.cpfEditText.addTextChangedListener(new TextWatcher() {
            private static final String CPF_FORMAT = "###.###.###-##";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                String cleanInput = input.replaceAll("[^\\d]", ""); // Remove todos os caracteres que não são números

                StringBuilder formattedCpf = new StringBuilder();

                for (int i = 0, j = 0; i < cleanInput.length() && j < CPF_FORMAT.length(); i++) {
                    char c = cleanInput.charAt(i);
                    char f = CPF_FORMAT.charAt(j);
                    if (f == '#') {
                        formattedCpf.append(c);
                        j++;
                    } else {
                        formattedCpf.append(f);
                        j++;
                        i--; // Não incrementar índice de entrada quando adicionar o formato
                    }
                }

                // Remover o listener para evitar loop de chamadas recursivas
                binding.cpfEditText.removeTextChangedListener(this);
                binding.cpfEditText.setText(formattedCpf.toString());
                binding.cpfEditText.setSelection(formattedCpf.length());  // Posiciona o cursor no final do texto
                binding.cpfEditText.addTextChangedListener(this);
            }
        });

        binding.continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginUser.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
