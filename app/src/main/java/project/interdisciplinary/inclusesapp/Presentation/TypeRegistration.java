package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityLoginUserBinding;
import project.interdisciplinary.inclusesapp.databinding.ActivityTypeRegistrationBinding;

public class TypeRegistration extends AppCompatActivity {

    private ActivityTypeRegistrationBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_registration);

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityTypeRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.voltarlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentVoltar = new Intent(TypeRegistration.this, LoginUser.class);
                startActivity(intentVoltar);
            }
        });


    }
}