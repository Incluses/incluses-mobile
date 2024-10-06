package project.interdisciplinary.inclusesapp.Presentation.Enterprise;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import project.interdisciplinary.inclusesapp.Presentation.ScreenConfigurations;
import project.interdisciplinary.inclusesapp.Presentation.UserPerfil;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.databinding.ActivityEnterpriseProfileBinding;
import project.interdisciplinary.inclusesapp.databinding.ActivityHomeBinding;
import project.interdisciplinary.inclusesapp.databinding.ActivityHomeEnterpriseBinding;
import project.interdisciplinary.inclusesapp.databinding.ActivityUserPerfilBinding;

public class EnterpriseProfileActivity extends AppCompatActivity {

    private ActivityEnterpriseProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityEnterpriseProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //button back
        binding.imageViewEnterpriseProfileBackButton.setOnClickListener(v -> {
            finish(); //finish() to go back to the previous screen
        });

        binding.textViewEnterpriseProfileBack.setOnClickListener(v -> {
            finish(); //finish() to go back to the previous screen
        });

//        Glide.with(this).load(R.drawable.fotorenato).circleCrop().into(binding.ImageViewPerfilUser);

        binding.icConfigEnterpriseProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EnterpriseProfileActivity.this, ScreenConfigurations.class);
                intent.putExtra("user_type", "enterprise");
                startActivity(intent);
            }
        });
    }
}