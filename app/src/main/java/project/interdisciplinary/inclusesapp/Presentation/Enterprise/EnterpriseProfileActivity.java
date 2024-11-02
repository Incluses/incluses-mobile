package project.interdisciplinary.inclusesapp.Presentation.Enterprise;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.AddMaterialCourse;
import project.interdisciplinary.inclusesapp.Presentation.CourseClass;
import project.interdisciplinary.inclusesapp.Presentation.ScreenConfigurations;
import project.interdisciplinary.inclusesapp.Presentation.UserPerfil;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.adapters.PostagensAdapter;
import project.interdisciplinary.inclusesapp.archive.FileChoose;
import project.interdisciplinary.inclusesapp.data.ConvertersToObjects;
import project.interdisciplinary.inclusesapp.data.dbApi.PostagemApi;
import project.interdisciplinary.inclusesapp.data.dbApi.PostagemCallback;
import project.interdisciplinary.inclusesapp.data.firebase.DatabaseFirebase;
import project.interdisciplinary.inclusesapp.data.models.Empresa;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.data.models.Postagem;
import project.interdisciplinary.inclusesapp.databinding.ActivityEnterpriseProfileBinding;
import project.interdisciplinary.inclusesapp.databinding.ActivityHomeBinding;
import project.interdisciplinary.inclusesapp.databinding.ActivityHomeEnterpriseBinding;
import project.interdisciplinary.inclusesapp.databinding.ActivityUserPerfilBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EnterpriseProfileActivity extends AppCompatActivity {

    private ActivityEnterpriseProfileBinding binding;
    private String token;
    private Retrofit retrofit;
    private Empresa empresaObj;

    private Perfil perfilObj;

    private DatabaseFirebase firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = new DatabaseFirebase();


        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityEnterpriseProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.ImageViewEnterpriseProfile.setVisibility(View.VISIBLE);
    //button back
        binding.imageViewEnterpriseProfileBackButton.setOnClickListener(v ->
        {
            finish();
        });

        binding.textViewEnterpriseProfileBack.setOnClickListener(v ->

    {
        finish(); //finish() to go back to the previous screen
    });
        binding.icConfigEnterpriseProfileImageView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick (View v){
        Intent intent = new Intent(EnterpriseProfileActivity.this, ScreenConfigurations.class);
        intent.putExtra("user_type", "enterprise");
        startActivity(intent);
    }
    });

}

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        String empresaJson = preferences.getString("empresa", "");
        String perfilJson = preferences.getString("perfil", "");
        perfilObj = ConvertersToObjects.convertStringToPerfil(perfilJson);



        //inflando os dados do usuário
        binding.nameEnterpriseProfiletextView.setText(perfilObj.getNome());
        binding.decriptionEnterpriseProfile.setVisibility(View.GONE);
        if(perfilObj.getBiografia()!=null){
            binding.decriptionEnterpriseProfile.setText(perfilObj.getBiografia());
            binding.decriptionEnterpriseProfile.setVisibility(View.VISIBLE);
            binding.addBiographyEnterpriseProfile.setVisibility(View.GONE);
        }
        if (perfilObj.getFkFtPerfilId()!=null){
            firebase.getFileUriFromFirebase(perfilObj.getFkFtPerfilId().toString(),
                    uri -> {
                        Glide.with(this)
                                .load(uri.toString())  // Convertendo a URI em String se necessário
                                .apply(RequestOptions.circleCropTransform())  // Aplica a transformação circular
                                .into(binding.ImageViewEnterpriseProfile);
                    },
                    e -> {
                        Log.e("Firebase", "Erro ao obter URL de download", e);
                    }
            );
        }
        binding.myPostsEnterpriseProfileRecyclerView.setLayoutManager(new LinearLayoutManager(EnterpriseProfileActivity.this));
        setupAdapter(perfilObj.getId(), new PostagemCallback() {
            @Override
            public void onSuccess(List<JsonObject> listJsonObject) {
                binding.myPostsEnterpriseProfileRecyclerView.setAdapter(new PostagensAdapter(listJsonObject, getApplicationContext()));
            }

            @Override
            public void onSuccessVerifyLike(Boolean booleanResponse) {

            }

            @Override
            public void onSucessFind(List<Postagem> list) {
            }

            @Override
            public void onSuccessInsert(JsonObject jsonObject) {

            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    private void setupAdapter(UUID idPerfil, PostagemCallback callback) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        String urlApi = "https://api-mongo-incluses.onrender.com/";

        retrofit = new Retrofit.Builder()
                .baseUrl(urlApi)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostagemApi api = retrofit.create(PostagemApi.class);
        Call<List<JsonObject>> call = api.findPostagemByIdUser(idPerfil.toString());
        call.enqueue(new Callback<List<JsonObject>>() {
            @Override
            public void onResponse(Call<List<JsonObject>> call, Response<List<JsonObject>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<JsonObject> responseBody = response.body();
                    callback.onSuccess(responseBody);
                } else {
                    // Outro erro
                    callback.onFailure(new Exception("Erro ao buscar postagens: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<List<JsonObject>> call, Throwable throwable) {
                Toast.makeText(EnterpriseProfileActivity.this, "erro", Toast.LENGTH_LONG).show();
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
}