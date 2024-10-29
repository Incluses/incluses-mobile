package project.interdisciplinary.inclusesapp.Presentation.Enterprise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioApi;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioCallback;
import project.interdisciplinary.inclusesapp.data.models.Empresa;
import project.interdisciplinary.inclusesapp.data.models.Usuario;
import project.interdisciplinary.inclusesapp.databinding.ActivityEditAccountEnterpriseBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditAccountEnterpriseActivity extends AppCompatActivity {

    private ActivityEditAccountEnterpriseBinding binding;

    private String token;

    private Retrofit retrofit;

    private Empresa empresaObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityEditAccountEnterpriseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        String empresaJson = preferences.getString("empresa", "");

        // Verifique se o JSON não é nulo ou vazio antes de tentar convertê-lo
        if (!empresaJson.isEmpty()) {
            Gson gson = new Gson();
            empresaObj = gson.fromJson(empresaJson, Empresa.class);  // Converte o JSON de volta para o objeto Usuario

            Log.e("Empresa", "ID: " + empresaObj.getId());
        } else {
            Log.e("Empresa", "Nenhuma empresa encontrada no SharedPreferences.");
        }
        //inflando os dados do usuário
        binding.nameEnterpriseEditAccountEditText.setText(empresaObj.getPerfil().getNome());
        binding.websiteEnterpriseEditAccountEditText.setText(empresaObj.getWebsite());
        binding.reasonSocialEnterpriseEditAccountEditText.setText(empresaObj.getRazaoSocial());
        binding.biographyEnterpriseEditAccountEditText.setText(empresaObj.getPerfil().getBiografia());
        binding.cepEnterpriseEditAccountEditText.setText(empresaObj.getPerfil().getBiografia());
        binding.numLocEnterpriseEditAccountEditText.setText(empresaObj.getPerfil().getBiografia());
        binding.passwordEnterpriseEditAccountInputLayout.setVisibility(View.GONE);

        //setando a imagem
        if (empresaObj.getPerfil().getFotoPerfil() != null) {
            //todo: implementar lógica de carregamento da imagem
            binding.textAddPhotoEditAccountEnterprise.setVisibility(View.GONE);
            binding.photoImageEditAccountEnterprise.setVisibility(View.VISIBLE);
        }else {
            //todo: implementar lógica de carregamento da imagem
        }

        //button back
        binding.imageViewEnterpriseEditAccountBackButton.setOnClickListener(v -> {
            finish();
        });

        binding.textViewEnterpriseEditAccountBack.setOnClickListener(v -> {
            finish();
        });
    }
}