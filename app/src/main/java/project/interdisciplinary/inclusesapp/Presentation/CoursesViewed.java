package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityCoursesViewedBinding;

public class CoursesViewed extends AppCompatActivity {

    private ActivityCoursesViewedBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityCoursesViewedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imageViewScreenViewedBackButton.setOnClickListener(v -> {
            finish();
        });

        binding.textViewScreenViewedBack.setOnClickListener(v -> {
            finish();
        });
    }
}