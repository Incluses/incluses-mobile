package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import com.bumptech.glide.Glide;

import project.interdisciplinary.inclusesapp.Presentation.Enterprise.HomeEnterprise;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivitySplashScreenBinding;

public class SplashScreen extends AppCompatActivity {

    private ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);

        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Glide.with(this)
                .asDrawable()
                .load(R.drawable.loading_incluses)
                .into(binding.imageViewloading);

        Glide.with(this)
                .asDrawable()
                .load(R.drawable.splash_screen)
                .into(binding.imageViewSplashScreen);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(preferences.getBoolean("isLogged", false)){
                    if(preferences.getBoolean("isEnterprise", false)){
                        Intent intent = new Intent(SplashScreen.this, HomeEnterprise.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(SplashScreen.this, Home.class);
                        startActivity(intent);
                        finish();
                    }
                }
                else {
                    Intent intent = new Intent(SplashScreen.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 5000);
    }
}
