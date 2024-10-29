package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.data.dbApi.PerfilApi;
import project.interdisciplinary.inclusesapp.data.dbApi.PerfilCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioApi;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioCallback;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.data.models.Usuario;
import project.interdisciplinary.inclusesapp.databinding.ActivityEditAccountBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditAccount extends AppCompatActivity {

    private String token;
    private Usuario userObj;
    private Perfil perfilObj;
    private Retrofit retrofit;
    private ActivityEditAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityEditAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Carregar token e dados do usuário do SharedPreferences
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        String usuarioJson = preferences.getString("usuario", "");
        String perfilJson = preferences.getString("perfil", "");

        // Verificar se o JSON de usuário e perfil não estão vazios
        if (!usuarioJson.isEmpty() && !perfilJson.isEmpty()) {
            Gson gson = new Gson();
            userObj = gson.fromJson(usuarioJson, Usuario.class);
            perfilObj = gson.fromJson(perfilJson, Perfil.class);
        }

        // Preencher os campos da tela com os dados do usuário, verificando null
        if (userObj != null && perfilObj != null) {
            if (userObj.getPerfil() != null) {
                binding.nameEditAccountEditText.setText(perfilObj.getNome() != null ? perfilObj.getNome() : "");
                binding.biographyEditAccountEditText.setText(perfilObj.getBiografia() != null ? perfilObj.getBiografia() : "");
            }
            binding.pronounEditAccountEditText.setText(userObj.getPronomes() != null ? userObj.getPronomes() : "");
            binding.nameSocialEditAccountEditText.setText(userObj.getNomeSocial() != null ? userObj.getNomeSocial() : "");

            // Verificar se o usuário tem uma foto de perfil
            if (perfilObj.getFotoPerfil() != null) {
                binding.textAddPhotoEditAccount.setVisibility(View.GONE);
                binding.photoImageEditAccount.setVisibility(View.VISIBLE);
            }
        }

        // Configurar clique no botão de voltar
        binding.imageViewEditAccountBackButton.setOnClickListener(v -> atualizarConta());

        // Configurar clique no texto de voltar
        binding.textViewEditAccountBack.setOnClickListener(v -> atualizarConta());
    }

    private void atualizarConta() {
        // Criar objetos atualizados com os novos valores
        Map<String, Object> requestUsuario = new HashMap<>();
        Map<String, Object> requestPerfil = new HashMap<>();

        // Verificar se os valores de userObj não são null antes de comparar
        if (userObj != null) {
            if (userObj.getPronomes() != null && !userObj.getPronomes().equals(binding.pronounEditAccountEditText.getText().toString())) {
                requestUsuario.put("pronomes", binding.pronounEditAccountEditText.getText().toString());
            }
            if (userObj.getNomeSocial() != null && !userObj.getNomeSocial().equals(binding.nameSocialEditAccountEditText.getText().toString())) {
                requestUsuario.put("nomeSocial", binding.nameSocialEditAccountEditText.getText().toString());
            }
        }

        // Verificar se os valores de perfilObj não são null antes de comparar
        if (perfilObj != null) {
            if (perfilObj.getNome() != null && !perfilObj.getNome().equals(binding.nameEditAccountEditText.getText().toString())) {
                requestPerfil.put("nome", binding.nameEditAccountEditText.getText().toString());
            }
            if (perfilObj.getBiografia() != null && !perfilObj.getBiografia().equals(binding.biographyEditAccountEditText.getText().toString())) {
                requestPerfil.put("biografia", binding.biographyEditAccountEditText.getText().toString());
            }
        }

        // Verificar se houve mudanças
        if (requestUsuario.isEmpty() && requestPerfil.isEmpty()) {
            Toast.makeText(this, "Nenhuma alteração detectada.", Toast.LENGTH_SHORT).show();
            return;
        }

        final int[] successCount = {0};

        // Atualizar o usuário se houve mudanças
        if (!requestUsuario.isEmpty()) {
            callApiRetrofitUpdateUser(userObj.getId(), requestUsuario, new UsuarioCallback() {
                @Override
                public void onSuccess(JsonObject responseBody) {
                    successCount[0]++;
                    checkForCompletion(successCount);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.e("Erro", throwable.getMessage());
                    Toast.makeText(EditAccount.this, "Erro ao atualizar usuário!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            successCount[0]++;
        }

        // Atualizar o perfil se houve mudanças
        if (!requestPerfil.isEmpty()) {
            callApiRetrofitUpdatePerfil(perfilObj.getId(), requestPerfil, new PerfilCallback() {
                @Override
                public void onSuccess(Perfil perfil) {
                    successCount[0]++;
                    checkForCompletion(successCount);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    Log.e("ErroPerfil", throwable.getMessage());
                    Toast.makeText(EditAccount.this, "Erro ao atualizar perfil!" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            successCount[0]++;
        }
    }

    private void checkForCompletion(int[] successCount) {
        if (successCount[0] == 2) {
            // Atualizar SharedPreferences com os novos valores de usuário e perfil
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            Gson gson = new Gson();
            if (userObj != null) {
                String updatedUserJson = gson.toJson(userObj);  // Converter o objeto atualizado em JSON
                editor.putString("usuario", updatedUserJson);
            }

            if (perfilObj != null) {
                String updatedPerfilJson = gson.toJson(perfilObj);  // Converter o objeto atualizado em JSON
                editor.putString("perfil", updatedPerfilJson);
            }

            editor.apply();  // Salvar as alterações

            Toast.makeText(this, "Conta atualizada com sucesso!", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void callApiRetrofitUpdateUser(UUID userId, Map<String, Object> request, UsuarioCallback callback) {
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

        UsuarioApi api = retrofit.create(UsuarioApi.class);
        Call<JsonObject> call = api.updateUser(token, userId, request);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(new Exception("Erro de requisição: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }

    private void callApiRetrofitUpdatePerfil(UUID perfilId, Map<String, Object> request, PerfilCallback callback) {
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
        Call<JsonObject> call = api.updatePerfil(token, perfilId, request);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(new Gson().fromJson(response.body(), Perfil.class));
                } else {
                    callback.onFailure(new Exception("Erro de requisição: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                callback.onFailure(throwable);
            }
        });
    }
}
