package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import project.interdisciplinary.inclusesapp.Presentation.Enterprise.RegisterEnterprise;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityRegisterUserBinding;

public class RegisterUserActivity extends AppCompatActivity {

    private ActivityRegisterUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityRegisterUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Botão de voltar
        binding.imageViewLoginUserBackButton.setOnClickListener(v -> finish());
        binding.textViewLoginUserBack.setOnClickListener(v -> finish());

        // Botão Continuar - Verificação CPF ou CNPJ e Telefone
        binding.continueButtonRegister.setOnClickListener(v -> {
            boolean phoneFilled, numRegisterFilled, nameFilled, emailFilled, passwordFilled;
            // Verificar se o nome foi digitado
            String nameInput = binding.nameEditText.getText().toString();
            if (!nameInput.isEmpty()){
                nameFilled = true;
                binding.phoneNumberInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM); // Restaura o ícone de telefone
                binding.phoneNumberInputLayout.setErrorIconDrawable(null); // Remove o ícone de erro
            }
            else {
                nameFilled = false;
                binding.nameEditText.setError("Digite um nome");
            }
            // Verificar se o email foi digitado
            String emailInput = binding.emailEditText.getText().toString();
            if (!emailInput.isEmpty()){
                if (emailInput.contains("@")){
                    emailFilled = true;
                    binding.phoneNumberInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM); // Restaura o ícone de telefone
                    binding.phoneNumberInputLayout.setErrorIconDrawable(null); // Remove o ícone de erro
                }
                else {
                    emailFilled = false;
                    binding.emailEditText.setError("Um email deve conter arroba",getResources().getDrawable(R.drawable.ic_email));

                }
            }
            else {
                emailFilled = false;
                binding.emailEditText.setError("Preencha o email",getResources().getDrawable(R.drawable.ic_email));
            }
            // senha
            String passwordInput = binding.passwordEditText.getText().toString();
            String passwordConfirmedInput = binding.passwordConfirmedEditText.getText().toString();
            if (!passwordInput.isEmpty() || !passwordConfirmedInput.isEmpty()){
                if (passwordInput.equals(passwordConfirmedInput)){
                    passwordFilled = true;
                    binding.phoneNumberInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM); // Restaura o ícone de telefone
                    binding.phoneNumberInputLayout.setErrorIconDrawable(null); // Remove o ícone de erro
                }
                else {
                    passwordFilled = false;
                    binding.passwordEditText.setError("A senha confirmada e a senha devem ser iguais", getResources().getDrawable(R.drawable.eye_block));
                    binding.passwordConfirmedEditText.setError("A senha confirmada e a senha devem ser iguais", getResources().getDrawable(R.drawable.eye_block));
                }
            }
            else{
                passwordFilled = false;
                if(passwordInput.isEmpty()){
                    binding.passwordEditText.setError("A senha não pode ser vazia", getResources().getDrawable(R.drawable.eye_block));
                }
                else if(passwordConfirmedInput.isEmpty()){
                    binding.passwordConfirmedEditText.setError("A senha confirmada não pode ser vazia",getResources().getDrawable(R.drawable.eye_block) );
                }
            }
            // Verificar se o telefone foi digitado
            String phoneInput = binding.phoneEditText.getText().toString();
            String cleanPhoneInput = phoneInput.replaceAll("[^\\d]", ""); // Remove caracteres não numéricos

            if (cleanPhoneInput.isEmpty() || cleanPhoneInput.length() < 10) {
                // Exibe uma mensagem de erro se o telefone for inválido
                binding.phoneEditText.setError("Número de telefone inválido", getResources().getDrawable(R.drawable.ic_phone));
                phoneFilled = false;
                return;
            } else {
                binding.phoneNumberInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM); // Restaura o ícone de telefone
                binding.phoneNumberInputLayout.setErrorIconDrawable(null); // Remove o ícone de erro
                phoneFilled = true;
            }

            // Verificar CPF/CNPJ
            String inputNumRegister = binding.numRegisterEditText.getText().toString();
            String cleanInputNumRegister = inputNumRegister.replaceAll("[^\\d]", ""); // Remove caracteres não numéricos

            if (!inputNumRegister.isEmpty()) {
                numRegisterFilled = true;
                binding.phoneNumberInputLayout.setEndIconMode(TextInputLayout.END_ICON_CUSTOM); // Restaura o ícone de telefone
                binding.phoneNumberInputLayout.setErrorIconDrawable(null); // Remove o ícone de erro
            }
            else {
                // Exibe uma mensagem de erro se o documento for inválido
                numRegisterFilled = false;
                binding.numRegisterEditText.setError("CPF ou CNPJ inválido",getResources().getDrawable(R.drawable.ic_doc));
            }

            if(nameFilled && emailFilled && passwordFilled && phoneFilled && numRegisterFilled){
                Bundle infosUser = new Bundle();
                infosUser.putString("name", nameInput);
                infosUser.putString("email", emailInput);
                infosUser.putString("password", passwordInput);
                infosUser.putString("phone_number", cleanPhoneInput);
                if(cleanInputNumRegister.length() == 11){
                    Intent intent = new Intent(RegisterUserActivity.this, RegisterUser2.class);//todo: Mudar para o endereco certo dps de realizar o teste
                    infosUser.putString("user_type", "user");
                    infosUser.putString("cpf", cleanInputNumRegister);
                    intent.putExtras(infosUser);
                    startActivity(intent);
                }
                else if (cleanInputNumRegister.length() == 14){
                    Intent intent = new Intent(RegisterUserActivity.this, RegisterEnterprise.class);//todo: Mudar para o endereco certo dps de realizar o teste
                    infosUser.putString("user_type", "enterprise");
                    infosUser.putString("cnpj", cleanInputNumRegister);
                    intent.putExtras(infosUser);
                    startActivity(intent);
                }
                else {
                    binding.numRegisterEditText.setError("CPF ou CNPJ inválido", getResources().getDrawable(R.drawable.ic_doc));
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
