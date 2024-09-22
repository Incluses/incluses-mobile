package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityScreenConfigurationsBinding;

public class ScreenConfigurations extends AppCompatActivity {

    private ActivityScreenConfigurationsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityScreenConfigurationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.textViewConfigBack.setOnClickListener(v -> finish());
        binding.icBackConfig.setOnClickListener(v -> finish());

        binding.accountAndPasswordConfig.setOnClickListener(v -> {
            startActivity(new android.content.Intent(ScreenConfigurations.this, EditAccount.class));
        });
    }
}