package project.interdisciplinary.inclusesapp.Presentation.Enterprise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityEditAccountEnterpriseBinding;

public class EditAccountEnterpriseActivity extends AppCompatActivity {

    private ActivityEditAccountEnterpriseBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityEditAccountEnterpriseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //button back
        binding.imageViewEnterpriseEditAccountBackButton.setOnClickListener(v -> {
            finish();
        });

        binding.textViewEnterpriseEditAccountBack.setOnClickListener(v -> {
            finish();
        });
    }
}