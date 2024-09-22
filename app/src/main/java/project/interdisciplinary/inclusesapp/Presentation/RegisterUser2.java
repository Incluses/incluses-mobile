package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

        setupAutoComplete();

        //button back
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


        //input born date
        binding.dateBornEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterUser2.this, R.style.CustomDatePickerDialog,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                String selectedDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year);
                                binding.dateBornEditText.setText(selectedDate);

                                Calendar dob = Calendar.getInstance();
                                dob.set(year, month, dayOfMonth);
                                int age = calculateAge(dob);

                                if (age < 18) {
                                    binding.dateBornInputLayout.setError(getString(R.string.error_underage));
                                } else {
                                    binding.dateBornInputLayout.setError(null);
                                }
                            }
                        },
                        year, month, dayOfMonth
                );

                dialog.show();
            }
        });

        //continue button
        binding.continueButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterUser2.this, JobsOfInterest.class);
                    startActivity(intent);
                }
        );

        //enter button
        binding.enterTextView.setOnClickListener(
                v -> {
                    Intent intent = new Intent(RegisterUser2.this, LoginUser.class);
                    startActivity(intent);
                    finish();
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

    private void setupAutoComplete() {
        // Array of states
        String[] states = getResources().getStringArray(R.array.states_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, states);
        binding.laborSituationAutoCompleteTextView.setAdapter(adapter);
        binding.laborSituationAutoCompleteTextView.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}