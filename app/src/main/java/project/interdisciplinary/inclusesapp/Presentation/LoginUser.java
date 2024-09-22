package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import project.interdisciplinary.inclusesapp.databinding.ActivityLoginUserBinding;

public class LoginUser extends AppCompatActivity {

    private ActivityLoginUserBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityLoginUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //button back
        binding.imageViewLoginUserBackButton.setOnClickListener(
                v -> {
                    Intent intent = new Intent(LoginUser.this, Login.class);
                    startActivity(intent);
                    finish();
                }
        );

        binding.textViewLoginUserBack.setOnClickListener(
                v -> {
                    Intent intent = new Intent(LoginUser.this, Login.class);
                    startActivity(intent);
                    finish();
                }
        );

        binding.registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginUser.this, RegisterUserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        binding.continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginUser.this, Home.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
