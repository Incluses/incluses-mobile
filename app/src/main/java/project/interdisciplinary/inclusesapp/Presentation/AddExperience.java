package project.interdisciplinary.inclusesapp.Presentation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityAddExperienceBinding;

public class AddExperience extends AppCompatActivity {
    private ActivityAddExperienceBinding binding;
    private Uri uriImageExperience;
    private ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Uri selectedFileUri = result.getData().getData();
                }
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri fileUri = data.getData();
                        String mimeType = getContentResolver().getType(fileUri);
                        if (mimeType != null && mimeType.startsWith("image/")) {
                            uriImageExperience = fileUri;
                            binding.textViewButtonAddImageExperience.setText("+ Adicionar nova Imagem");
                        } else {
                            Toast.makeText(this, "Por favor, selecione uma imagem.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_experience);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding = ActivityAddExperienceBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.switchConfirmFinsh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.switchConfirmFinsh.isChecked()) {
                    binding.dateFinishExperienceInputLayout.setVisibility(View.VISIBLE);
                } else {
                    binding.dateFinishExperienceInputLayout.setVisibility(View.GONE);
                }
            }
        });

        binding.dateStartExperienceEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        AddExperience.this, R.style.CustomDatePickerDialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                                binding.dateStartExperienceEditText.setText(selectedDate);
                                // Calcular a idade do usuário
                                Calendar dob = Calendar.getInstance();
                                dob.set(year, month, dayOfMonth);
                            }
                        },
                        year, month, dayOfMonth
                );
                dialog.show();
            }
        });
        binding.dateFinishExperienceEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        AddExperience.this, R.style.CustomDatePickerDialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                                binding.dateFinishExperienceEditText.setText(selectedDate);
                                // Calcular a idade do usuário
                                Calendar dob = Calendar.getInstance();
                                dob.set(year, month, dayOfMonth);
                            }
                        },
                        year, month, dayOfMonth
                );
                dialog.show();
            }
        });
        binding.textViewButtonAddImageExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                intent2.setType("*/*");
                intent2.addCategory(Intent.CATEGORY_OPENABLE);
                filePickerLauncher.launch(intent2);
            }
        });

    }

}