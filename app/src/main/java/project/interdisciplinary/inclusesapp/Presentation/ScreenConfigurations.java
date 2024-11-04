package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;

import project.interdisciplinary.inclusesapp.Presentation.Enterprise.EditAccountEnterpriseActivity;
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

        String userType = getIntent().getStringExtra("user_type");
        SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        binding.textViewConfigBack.setOnClickListener(v -> finish());
        binding.icBackConfig.setOnClickListener(v -> finish());

        binding.exitAccountConfig.setOnClickListener(v -> {

            PopupCardView popup = new PopupCardView(v.getContext());
            popup.showPopup();

            popup.setOnOptionSelectedListener(new PopupCardView.OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(boolean optionSelected) {
                    if (optionSelected) {
                        Intent intent = new Intent(ScreenConfigurations.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        editor.putBoolean("isLogged", false);
                        editor.putBoolean("isEnterprise", false);
                        editor.apply();
                        startActivity(intent);
                    }
                }
            });
        });

        binding.addAccountConfig.setOnClickListener(v -> {

            PopupCardView popup = new PopupCardView(v.getContext());
            popup.showPopup();

            popup.setOnOptionSelectedListener(new PopupCardView.OnOptionSelectedListener() {
                @Override
                public void onOptionSelected(boolean optionSelected) {
                    if (optionSelected) {
                        Intent intent = new Intent(ScreenConfigurations.this, Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        editor.putBoolean("isLogged", false);
                        editor.putBoolean("isEnterprise", false);
                        editor.apply();
                        startActivity(intent);
                    }
                }
            });

        });

        binding.accountAndPasswordConfig.setOnClickListener(v -> {
            if (userType.equals("user")) { //user
                Intent intent = new Intent(ScreenConfigurations.this, EditAccount.class);
                intent.putExtra("user_type", userType);
                startActivity(intent);
            }else { //enterprise
                Intent intent = new Intent(ScreenConfigurations.this, EditAccountEnterpriseActivity.class);
                intent.putExtra("user_type", userType);
                startActivity(intent);
            }
        });
    }
}