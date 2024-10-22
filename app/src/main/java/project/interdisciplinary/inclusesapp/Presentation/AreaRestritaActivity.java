package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityAreaRestritaBinding;

public class AreaRestritaActivity extends AppCompatActivity {

    private ActivityAreaRestritaBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityAreaRestritaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageViewAreaRestritaBackButton.setOnClickListener(v -> {
            finish();
        });

        binding.textViewAreaRestritaBack.setOnClickListener(v -> {
            finish();
        });
    }
}