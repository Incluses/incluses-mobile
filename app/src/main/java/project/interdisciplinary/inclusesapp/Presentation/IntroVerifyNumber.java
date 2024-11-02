package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityIntroVerifyNumberBinding;

public class IntroVerifyNumber extends AppCompatActivity {

    private ActivityIntroVerifyNumberBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityIntroVerifyNumberBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configurar o botão de voltar
        binding.imageViewIntroVerifyNumberBackButton.setOnClickListener(v -> finish());
        binding.textViewIntroVerifyNumberBack.setOnClickListener(v -> finish());

        // Continuar para a próxima tela
        binding.continueButtonIntroVerifyNumber.setOnClickListener(v -> {
            Intent intent = new Intent(IntroVerifyNumber.this, VerifyNumber.class);
            Bundle extras = getIntent().getExtras();
            Log.e("IntroVerifyNumber", "onCreate: " + extras);
            intent.putExtras(extras);
            startActivity(intent);
            finish();
        });
    }

}
