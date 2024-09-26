package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.os.Bundle;
import project.interdisciplinary.inclusesapp.databinding.ActivityLoginUserBinding;

public class LoginUser extends AppCompatActivity {

    private ActivityLoginUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Força o tema para o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityLoginUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Botão de voltar
        binding.imageViewLoginUserBackButton.setOnClickListener(v -> {
            startActivity(new Intent(LoginUser.this, Login.class));
            finish();
        });

        binding.textViewLoginUserBack.setOnClickListener(v -> {
            startActivity(new Intent(LoginUser.this, Login.class));
            finish();
        });

        // Ação para registrar novo usuário
        binding.registerTextView.setOnClickListener(v -> {
            startActivity(new Intent(LoginUser.this, RegisterUserActivity.class));
            finish();
        });

        // Botão para continuar
        binding.continueButton.setOnClickListener(v -> {
            startActivity(new Intent(LoginUser.this, Home.class));
            finish();
        });
    }
}
