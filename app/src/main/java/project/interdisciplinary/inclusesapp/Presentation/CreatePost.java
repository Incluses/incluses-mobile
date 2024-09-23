package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityCreatePostBinding;

public class CreatePost extends AppCompatActivity {

    private ActivityCreatePostBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cancelEditMaterialCourseButton.setOnClickListener(v -> { finish(); });

    }
}