package project.interdisciplinary.inclusesapp.Presentation;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import project.interdisciplinary.inclusesapp.Presentation.Enterprise.EnterpriseProfileActivity;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.data.models.Usuario;
import project.interdisciplinary.inclusesapp.databinding.ActivityUserPerfilBinding;
import retrofit2.Retrofit;

public class UserPerfil extends AppCompatActivity {

    private ActivityUserPerfilBinding binding;
    private String token;
    private Retrofit retrofit;
    private Perfil perfilObj;
    private Usuario userObj;

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

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        String usuarioJson = preferences.getString("usuario", "");
        String perfilJson = preferences.getString("perfil", "");

        // Verifique se o JSON não é nulo ou vazio antes de tentar convertê-lo
        if (!usuarioJson.isEmpty() && !perfilJson.isEmpty()) {
            Gson gson = new Gson();
            perfilObj = gson.fromJson(perfilJson, Perfil.class);
            userObj = gson.fromJson(usuarioJson, Usuario.class);  // Converte o JSON de volta para o objeto Usuario

            Log.e("Usuario", "ID: " + userObj.getId());
        } else {
            Log.e("Usuario", "Nenhuma empresa encontrada no SharedPreferences.");
        }

        //inflando os dados do usuário
        //binding.ImageViewPerfilUser.setImageURI();
        if (userObj.getNomeSocial() != null) {
            binding.namePerfiltextView.setText(userObj.getNomeSocial());
        }else {
            binding.namePerfiltextView.setText(perfilObj.getNome());
        }
        binding.pronounUserProfile.setText(userObj.getNomeSocial());
        if (perfilObj.getBiografia()!= null) {
            binding.addBiographyProfile.setBackgroundColor(getResources().getColor(R.color.gray));
            binding.addBiographyProfile.setText(perfilObj.getBiografia());
        }
        if (userObj.getCurriculo() != null) {
//            binding.imageViewCurriculo.setImageURI(Uri.parse(userObj.getCurriculo()));
//            binding.imageButtonRemoveImageCurriculum.setVisibility(View.VISIBLE);
        }


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
                Intent intent = new Intent(UserPerfil.this, ScreenConfigurations.class);
                intent.putExtra("user_type", "user");
                startActivity(intent);
            }
        });
    }
}