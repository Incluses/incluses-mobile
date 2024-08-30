package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.os.Bundle;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityHomeBinding;
import project.interdisciplinary.inclusesapp.databinding.ActivityLoginBinding;

public class Home extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}