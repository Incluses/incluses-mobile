package project.interdisciplinary.inclusesapp.Presentation;

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

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityRegisterUser2Binding;

public class RegisterUser2 extends AppCompatActivity {

    private ActivityRegisterUser2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityRegisterUser2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //button back
        binding.imageViewLoginUserBackButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterUser2.this, RegisterUserActivity.class);
                    startActivity(intent);
                }
        );

        binding.textViewLoginUserBack.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterUser2.this, RegisterUserActivity.class);
                    startActivity(intent);
                }
        );


        //input born date
        binding.dateBornEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                // Define o listener diretamente no construtor do DatePickerDialog
                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterUser2.this, R.style.CustomDatePickerDialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                                binding.dateBornEditText.setText(selectedDate);

                                // Calcular a idade do usuário
                                Calendar dob = Calendar.getInstance();
                                dob.set(year, month, dayOfMonth);
                                int age = calculateAge(dob);

                                // Verificar se é menor de 18 anos
                                if (age < 18) {
                                    binding.dateBornInputLayout.setError(getString(R.string.error_underage)); // Adicione esta string ao seu strings.xml
                                } else {
                                    binding.dateBornInputLayout.setError(null); // Limpa o erro
                                }
                            }
                        },
                        year, month, dayOfMonth
                );

                dialog.show();
            }
        });

        // Format CPF number
        binding.numRegisterEditText.addTextChangedListener(new TextWatcher() {
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
                binding.numRegisterEditText.removeTextChangedListener(this);
                binding.numRegisterEditText.setText(formattedCpf.toString());
                binding.numRegisterEditText.setSelection(formattedCpf.length());  // Posiciona o cursor no final do texto
                binding.numRegisterEditText.addTextChangedListener(this);
            }
        });

        //enter button
        binding.enterTextView.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterUser2.this, LoginUser.class);
                    startActivity(intent);
                }
        );
    }
    private int calculateAge(Calendar dob) {
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }
}