package project.interdisciplinary.inclusesapp.Presentation.Enterprise;

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

import project.interdisciplinary.inclusesapp.Presentation.ScreenConfigurations;
import project.interdisciplinary.inclusesapp.Presentation.UserPerfil;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.models.Empresa;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.databinding.ActivityEnterpriseProfileBinding;
import project.interdisciplinary.inclusesapp.databinding.ActivityHomeBinding;
import project.interdisciplinary.inclusesapp.databinding.ActivityHomeEnterpriseBinding;
import project.interdisciplinary.inclusesapp.databinding.ActivityUserPerfilBinding;
import retrofit2.Retrofit;

public class EnterpriseProfileActivity extends AppCompatActivity {

    private ActivityEnterpriseProfileBinding binding;
    private String token;
    private Retrofit retrofit;
    private Empresa empresaObj;

    private Perfil perfilObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityEnterpriseProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        String empresaJson = preferences.getString("empresa", "");
        String perfilJson = preferences.getString("perfil", "");

        // Verifique se o JSON não é nulo ou vazio antes de tentar convertê-lo
        if (!empresaJson.isEmpty()) {
            Gson gson = new Gson();
            empresaObj = gson.fromJson(empresaJson, Empresa.class);  // Converte o JSON de volta para o objeto Usuario

            Log.e("Empresa", "ID: " + empresaObj.getId());
        } else {
            Log.e("Empresa", "Nenhuma empresa encontrada no SharedPreferences.");
        }

        //inflando os dados do usuário
        //binding.ImageViewEnterpriseProfile.setImageURI();

        binding.nameEnterpriseProfiletextView.setText(perfilObj.getNome());
        binding.decriptionEnterpriseProfile.setVisibility(View.GONE);
        if(perfilObj.getBiografia()!=null){
            binding.decriptionEnterpriseProfile.setText(perfilObj.getBiografia());
            binding.addBiographyEnterpriseProfile.setVisibility(View.GONE);
        }


//            binding.imageViewCurriculo.setImageURI(Uri.parse(userObj.getCurriculo()));
//            binding.imageButtonRemoveImageCurriculum.setVisibility(View.VISIBLE);

    //button back
        binding.imageViewEnterpriseProfileBackButton.setOnClickListener(v ->

    {
        finish(); //finish() to go back to the previous screen
    });

        binding.textViewEnterpriseProfileBack.setOnClickListener(v ->

    {
        finish(); //finish() to go back to the previous screen
    });

//        Glide.with(this).load(R.drawable.fotorenato).circleCrop().into(binding.ImageViewPerfilUser);

        binding.icConfigEnterpriseProfileImageView.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        Intent intent = new Intent(EnterpriseProfileActivity.this, ScreenConfigurations.class);
        intent.putExtra("user_type", "enterprise");
        startActivity(intent);
    }
    });
}
}