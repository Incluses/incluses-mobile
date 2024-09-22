package project.interdisciplinary.inclusesapp.Presentation;

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

import project.interdisciplinary.inclusesapp.databinding.ActivityUserPerfilBinding;

public class UserPerfil extends AppCompatActivity {

    private ActivityUserPerfilBinding binding;

    private ActivityResultLauncher<Intent> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Uri selectedFileUri = result.getData().getData();
                }
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri fileUri = data.getData();
                        String mimeType = getContentResolver().getType(fileUri);
                        if (mimeType != null && mimeType.startsWith("image/")) {
                            binding.imageViewCurriculo.setImageURI(fileUri);
                            binding.imageButtonRemoveImageCurriculum.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(this, "Por favor, selecione uma imagem.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityUserPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //button back
        binding.imageViewLoginUserBackButton.setOnClickListener(v -> {
            finish(); //finish() to go back to the previous screen
        });

        binding.textViewLoginUserBack.setOnClickListener(v -> {
            finish(); //finish() to go back to the previous screen
        });

//        Glide.with(this).load(R.drawable.fotorenato).circleCrop().into(binding.ImageViewPerfilUser);


        binding.imageButtonRemoveImageCurriculum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imageViewCurriculo.setImageURI(null);
            }
        });

        binding.icConfigImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserPerfil.this, ScreenConfigurations.class));
            }
        });
    }
}