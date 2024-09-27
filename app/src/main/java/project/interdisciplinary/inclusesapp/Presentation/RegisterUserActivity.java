package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import project.interdisciplinary.inclusesapp.Presentation.Enterprise.RegisterEnterprise;
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

        // Botão de voltar
        binding.imageViewLoginUserBackButton.setOnClickListener(
                v -> {
                    finish();
                }
        );

        binding.textViewLoginUserBack.setOnClickListener(
                v -> {
                    finish();
                }
        );

        // Botão Continuar - Verificação CPF ou CNPJ e Telefone
        binding.continueButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verificar se o telefone foi digitado
                String phoneInput = binding.phoneEditText.getText().toString();
                String cleanPhoneInput = phoneInput.replaceAll("[^\\d]", ""); // Remove caracteres não numéricos

                if (cleanPhoneInput.isEmpty() || cleanPhoneInput.length() < 10) {
                    // Exibe uma mensagem de erro se o telefone for inválido
                    binding.phoneEditText.setError("Número de telefone inválido");
                    return;
                }

                // Verificar CPF/CNPJ
                String input = binding.numRegisterEditText.getText().toString();
                String cleanInput = input.replaceAll("[^\\d]", ""); // Remove caracteres não numéricos

                if (cleanInput.length() <= 11) {
                    // CPF - Direciona para tela de Registro de Usuário (User2)
                    Intent intent = new Intent(RegisterUserActivity.this, IntroVerifyNumber.class);
                    intent.putExtra("phone_number", cleanPhoneInput); // Passa o número de telefone
                    intent.putExtra("user_type", "user");
                    startActivity(intent);
                } else if (cleanInput.length() == 14) {
                    // CNPJ - Direciona para tela de Registro de Empresa (RegisterEnterprise)
                    Intent intent = new Intent(RegisterUserActivity.this, IntroVerifyNumber.class);
                    intent.putExtra("phone_number", cleanPhoneInput); // Passa o número de telefone
                    intent.putExtra("user_type", "enterprise");
                    startActivity(intent);
                } else {
                    // Exibe uma mensagem de erro se o documento for inválido
                    binding.numRegisterEditText.setError("CPF ou CNPJ inválido");
                }
            }
        });

        // Formatar número de telefone
        binding.phoneEditText.addTextChangedListener(new TextWatcher() {
            private static final String PHONE_FORMAT = "(##) #####-####";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                String cleanInput = input.replaceAll("[^\\d]", "");

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
                        i--;
                    }
                }

                binding.phoneEditText.removeTextChangedListener(this);
                binding.phoneEditText.setText(formattedPhone.toString());
                binding.phoneEditText.setSelection(formattedPhone.length());
                binding.phoneEditText.addTextChangedListener(this);
            }
        });

        // Botão Entrar
        binding.enterTextView.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterUserActivity.this, LoginUser.class);
                    startActivity(intent);
                    finish();
                }
        );

        // Formatar CPF/CNPJ
        binding.numRegisterEditText.addTextChangedListener(new TextWatcher() {
            private static final String CPF_FORMAT = "###.###.###-##";
            private static final String CNPJ_FORMAT = "##.###.###/####-##";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString();
                String cleanInput = input.replaceAll("[^\\d]", "");

                // Decide se é CPF ou CNPJ com base no número de dígitos
                String format = cleanInput.length() <= 11 ? CPF_FORMAT : CNPJ_FORMAT;

                StringBuilder formattedDocument = new StringBuilder();

                for (int i = 0, j = 0; i < cleanInput.length() && j < format.length(); i++) {
                    char c = cleanInput.charAt(i);
                    char f = format.charAt(j);
                    if (f == '#') {
                        formattedDocument.append(c);
                        j++;
                    } else {
                        formattedDocument.append(f);
                        j++;
                        i--;
                    }
                }

                binding.numRegisterEditText.removeTextChangedListener(this);
                binding.numRegisterEditText.setText(formattedDocument.toString());
                binding.numRegisterEditText.setSelection(formattedDocument.length());
                binding.numRegisterEditText.addTextChangedListener(this);
            }
        });
    }
}
