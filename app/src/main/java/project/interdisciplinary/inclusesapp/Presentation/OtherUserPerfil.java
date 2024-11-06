package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.EnterpriseProfileActivity;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.adapters.PostagensAdapter;
import project.interdisciplinary.inclusesapp.data.ConvertersToObjects;
import project.interdisciplinary.inclusesapp.data.dbApi.PerfilApi;
import project.interdisciplinary.inclusesapp.data.dbApi.PerfilCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.PostagemApi;
import project.interdisciplinary.inclusesapp.data.dbApi.PostagemCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioApi;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioCallback;
import project.interdisciplinary.inclusesapp.data.firebase.DatabaseFirebase;
import project.interdisciplinary.inclusesapp.data.models.Empresa;
import project.interdisciplinary.inclusesapp.data.models.Error;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.data.models.Postagem;
import project.interdisciplinary.inclusesapp.databinding.ActivityEnterpriseProfileBinding;
import project.interdisciplinary.inclusesapp.databinding.ActivityOtherUserPerfilBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class OtherUserPerfil extends AppCompatActivity {

    private ActivityOtherUserPerfilBinding binding;
    private String token;
    private String perfilId;

    private Retrofit retrofit;

    private DatabaseFirebase firebase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebase = new DatabaseFirebase();


        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityOtherUserPerfilBinding.inflate(getLayoutInflater());
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
        perfilId = getIntent().getStringExtra("idPerfil");

        binding.myPostsEnterpriseProfileRecyclerView.setLayoutManager(new LinearLayoutManager(OtherUserPerfil.this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        // Obtenha o ID do perfil da Intent

        // Verifique se perfilId não é nulo ou vazio
        if (perfilId != null && !perfilId.isEmpty()) {
            findPerfil(perfilId, new PerfilCallback() {
                @Override
                public void onSucesssObject(JsonObject jsonObject) {
                    // Lógica de sucesso
                }

                @Override
                public void onSuccess(Perfil perfilObj) {
                    // Inflando os dados do usuário
                    binding.nameEnterpriseProfiletextView.setText(perfilObj.getNome());
                    binding.decriptionEnterpriseProfile.setVisibility(View.GONE);

                    if(perfilObj.getBiografia() != null) {
                        binding.decriptionEnterpriseProfile.setText(perfilObj.getBiografia());
                        binding.decriptionEnterpriseProfile.setVisibility(View.VISIBLE);
                        binding.addBiographyEnterpriseProfile.setVisibility(View.GONE);
                    }

                    if (perfilObj.getFkFtPerfilId() != null) {
                        firebase.getFileUriFromFirebase(perfilObj.getFkFtPerfilId().toString(),
                                uri -> {
                                    Glide.with(OtherUserPerfil.this)
                                            .load(uri.toString())
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(binding.ImageViewEnterpriseProfile);
                                },
                                e -> {
                                    Log.e("Firebase", "Erro ao obter URL de download", e);
                                });
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    // Lógica de falha
                }
            });

            // Configurando o adapter
            setupAdapter(UUID.fromString(perfilId), new PostagemCallback() {
                @Override
                public void onSuccess(List<JsonObject> listJsonObject) {
                    binding.myPostsEnterpriseProfileRecyclerView.setAdapter(new PostagensAdapter(listJsonObject, getApplicationContext()));
                }

                @Override
                public void onSuccessVerifyLike(Boolean booleanResponse) {}

                @Override
                public void onSucessFind(List<Postagem> list) {}

                @Override
                public void onSuccessInsert(JsonObject jsonObject) {}

                @Override
                public void onFailure(Throwable throwable) {
                    firebase.saveError(new Error("Erro ao carregar as postagens: " + throwable.getMessage()));
                    Log.e("ERRO", throwable.getMessage());
                }
            });
        } else {
            Log.e("OtherUserPerfil", "perfilId is null or empty");
            Toast.makeText(this, "Perfil não encontrado.", Toast.LENGTH_SHORT).show();
            finish(); // Finaliza a atividade se o perfilId não for válido
        }
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
                Toast.makeText(OtherUserPerfil.this, "erro", Toast.LENGTH_LONG).show();
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
    private void findPerfil(String idPerfil, PerfilCallback callback) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        String urlApi = "https://incluses-api.onrender.com/";

        retrofit = new Retrofit.Builder()
                .baseUrl(urlApi)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PerfilApi api = retrofit.create(PerfilApi.class);
        Call<JsonObject> call = api.getPerfilById(token, UUID.fromString(idPerfil));
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();
                    Perfil perfil = new Gson().fromJson(responseBody, Perfil.class);
                    callback.onSuccess(perfil);
                } else if (response.code() == 401) {
                    // Token inválido ou expirado
                    callback.onFailure(new Exception("Token expirado ou inválido!"));
                } else {
                    // Outro erro
                    callback.onFailure(new Exception("Erro ao buscar usuario: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "erro", Toast.LENGTH_LONG).show();
                firebase.saveError(new Error("Erro ao buscar usuario: " + throwable.getMessage()));
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
}