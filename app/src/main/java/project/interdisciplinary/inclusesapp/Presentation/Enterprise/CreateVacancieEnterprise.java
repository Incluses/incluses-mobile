package project.interdisciplinary.inclusesapp.Presentation.Enterprise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityCreateVacancieEnterpriseBinding;

public class CreateVacancieEnterprise extends AppCompatActivity {

    private ActivityCreateVacancieEnterpriseBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        binding = ActivityCreateVacancieEnterpriseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupAutoComplete();

        binding.cancelCreateVacancieButton.setOnClickListener(v -> finish());

    }

    private void setupAutoComplete() {
        String[] states = getResources().getStringArray(R.array.type_vacancie_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, states);
        binding.typeAutoCompleteTextView.setAdapter(adapter);
        binding.typeAutoCompleteTextView.setDropDownHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
    }
}