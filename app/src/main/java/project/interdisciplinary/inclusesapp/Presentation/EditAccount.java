package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.EditAccountEnterpriseActivity;
import project.interdisciplinary.inclusesapp.archive.FileChoose;
import project.interdisciplinary.inclusesapp.data.ConvertersToObjects;
import project.interdisciplinary.inclusesapp.data.dbApi.ArquivoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.ArquivoCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.MaterialCursoCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.PerfilApi;
import project.interdisciplinary.inclusesapp.data.dbApi.PerfilCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.TipoArquivoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioApi;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioCallback;
import project.interdisciplinary.inclusesapp.data.firebase.DatabaseFirebase;
import project.interdisciplinary.inclusesapp.data.models.Arquivo;
import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.data.models.TipoArquivo;
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
    private DatabaseFirebase firebase;
    private static final int PICK_FILE_REQUEST = 1;

    private FileChoose fileChoose = new FileChoose();

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

        binding.inputPhoto.setOnClickListener(v -> {
            fileChoose.openFileChooser(EditAccount.this);
        });
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
                binding.textAddPhotoEditAccount.setText("Mudar Foto Atual");
            }
        }

        // Configurar clique no botão de voltar
        binding.imageViewEditAccountBackButton.setOnClickListener(v -> {
            Map<String, Object> requestPerfil = new HashMap<>();
            Map<String, Object> requestUser = new HashMap<>();
            SharedPreferences preferences2 = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            boolean nameFilled, pronounFilled, socialNameFilled, biographyFilled;
            boolean archiveFilled = preferences2.getBoolean("archiveFilled", false);
            String inputName =binding.nameEditAccountEditText.getText().toString();
            String inputPronoun = binding.pronounEditAccountEditText.getText().toString();
            String inputSocialName = binding.nameSocialEditAccountEditText.getText().toString();
            String inputBiography = binding.biographyEditAccountEditText.getText().toString();
            SharedPreferences.Editor editor = preferences.edit();
            if (archiveFilled){
                Arquivo arquivo2 = ConvertersToObjects.convertStringToArquivo(preferences2.getString("archive",""));
                requestPerfil.put("fkFtPerfilId", arquivo2.getId());
                editor.putBoolean("archiveFilled", false);
            }
            if (!inputName.equals(userObj.getPerfil().getNome())){
                nameFilled = true;
                requestPerfil.put("nome", inputName);
            }
            else {
                nameFilled = false;
            }
            if (!inputBiography.equals(userObj.getPerfil().getBiografia())){
                biographyFilled = true;
                requestPerfil.put("biografia", inputBiography);
            }
            else {
                biographyFilled = false;
            }
            if (!inputPronoun.equals(userObj.getPronomes())){
                requestUser.put("pronomes", inputPronoun);
                pronounFilled = true;
            }
            else {
                pronounFilled = false;
            }
            if (!inputSocialName.equals(userObj.getNomeSocial())){
                requestUser.put("nomeSocial", inputPronoun);
                socialNameFilled = true;
            }
            else {
                socialNameFilled = false;
            }
            if (nameFilled || biographyFilled || archiveFilled){
                callApiRetrofitUpdatePerfil(userObj.getFkPerfilId(), requestPerfil, new PerfilCallback() {
                    @Override
                    public void onSucesssObject(JsonObject jsonObject) {
                        editor.putString("perfil", jsonObject.toString());
                        editor.apply();
                    }

                    @Override
                    public void onSuccess(Perfil perfil) {

                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
            }
            if(pronounFilled || socialNameFilled){
                callApiRetrofitUpdateUser(userObj.getId(), requestUser, new UsuarioCallback() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        editor.putString("usuario", jsonObject.toString());
                        editor.apply();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
            }
            finish();
        });

        // Configurar clique no texto de voltar
        binding.textViewEditAccountBack.setOnClickListener(v -> {
            Map<String, Object> requestPerfil = new HashMap<>();
            Map<String, Object> requestUser = new HashMap<>();
            SharedPreferences preferences2 = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
            boolean nameFilled, pronounFilled, socialNameFilled, biographyFilled;
            boolean archiveFilled = preferences2.getBoolean("archiveFilled", false);
            String inputName =binding.nameEditAccountEditText.getText().toString();
            String inputPronoun = binding.pronounEditAccountEditText.getText().toString();
            String inputSocialName = binding.nameSocialEditAccountEditText.getText().toString();
            String inputBiography = binding.biographyEditAccountEditText.getText().toString();
            SharedPreferences.Editor editor = preferences2.edit();
            if (archiveFilled){
                Arquivo arquivo2 = ConvertersToObjects.convertStringToArquivo(preferences2.getString("archive",""));
                requestPerfil.put("fkFtPerfilId", arquivo2.getId());
                editor.putBoolean("archiveFilled", false);
                editor.apply();
            }
            if (!inputName.equals(userObj.getPerfil().getNome())){
                nameFilled = true;
                requestPerfil.put("nome", inputName);
            }
            else {
                nameFilled = false;
            }
            if (!inputBiography.equals(userObj.getPerfil().getBiografia())){
                biographyFilled = true;
                requestPerfil.put("biografia", inputBiography);
            }
            else {
                biographyFilled = false;
            }
            if (!inputPronoun.equals(userObj.getPronomes())){
                requestUser.put("pronomes", inputPronoun);
                pronounFilled = true;
            }
            else {
                pronounFilled = false;
            }
            if (!inputSocialName.equals(userObj.getNomeSocial())){
                requestUser.put("nomeSocial", inputSocialName);
                socialNameFilled = true;
            }
            else {
                socialNameFilled = false;
            }
            if (nameFilled || biographyFilled || archiveFilled){
                callApiRetrofitUpdatePerfil(userObj.getFkPerfilId(), requestPerfil, new PerfilCallback() {
                    @Override
                    public void onSucesssObject(JsonObject jsonObject) {
                        editor.putString("perfil", jsonObject.toString());
                        editor.apply();
                    }

                    @Override
                    public void onSuccess(Perfil perfil) {

                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }
                });
            }
            if(pronounFilled || socialNameFilled){
                callApiRetrofitUpdateUser(userObj.getId(), requestUser, new UsuarioCallback() {
                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        editor.putString("usuario", jsonObject.toString());
                        editor.apply();
                    }@Override
                    public void onFailure(Throwable throwable) {

                    }
                });
            }
            finish();
        });
    }
    private void findTipo(String nome, MaterialCursoCallback callback) {
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

        TipoArquivoApi api = retrofit.create(TipoArquivoApi.class);
        Call<JsonObject> call = api.findByNome(token,nome);
        call.enqueue(new Callback<JsonObject>() {


            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("retorno", String.valueOf(response.code()));
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();
                    callback.onSuccess(responseBody); // Sucesso
                } else if (response.code() == 401) {
                }
                else {
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
    private void insertArquivo(Arquivo arquivo, ArquivoCallback callback) {
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

        ArquivoApi api = retrofit.create(ArquivoApi.class);
        Call<JsonObject> call = api.insertArquivo(token,arquivo);
        call.enqueue(new Callback<JsonObject>() {


            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("retorno inserir", String.valueOf(response.code()));
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();
                    callback.onSuccess(responseBody); // Sucesso
                } else if (response.code() == 401) {
                }
                else {
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DatabaseFirebase databaseFirebase = new DatabaseFirebase();
        List<String> fileDetails = new ArrayList<>();
        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            final Uri fileUri = data.getData();
            fileDetails = fileChoose.getFileDetails(fileUri, this);
            final List<String> fileDetailsFinal = fileDetails;

            if (fileDetailsFinal.get(2).equals("jpg") || fileDetailsFinal.get(2).equals("png") || fileDetailsFinal.get(2).equals("jpeg")){
                findTipo(fileDetails.get(2), new MaterialCursoCallback() {
                    @Override
                    public void onSuccessFind(List<MaterialCurso> list) {

                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(JsonObject jsonObject) {
                        Gson gson = new Gson();
                        TipoArquivo apiResponse = gson.fromJson(jsonObject, TipoArquivo.class);
                        Arquivo arquivo = new Arquivo(fileDetailsFinal.get(1),fileDetailsFinal.get(0),
                                apiResponse.getId());
                        Log.e("arquivo", arquivo.toString());
                        insertArquivo(arquivo, new ArquivoCallback() {
                            @Override
                            public void onSuccessFind(List<Arquivo> list) {

                            }

                            @Override
                            public void onFailure(Throwable throwable) {

                            }

                            @Override
                            public void onSuccess(JsonObject jsonObject) {
                                Gson gson = new Gson();
                                Arquivo apiResponse2 = gson.fromJson(jsonObject, Arquivo.class);
                                binding.textAddPhotoEditAccount.setText("Mudar Foto Atual");
                                databaseFirebase.uploadFileToFirebase(fileUri, apiResponse2);
                                SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putBoolean("archiveFilled", true);
                                editor.putString("archive", apiResponse2.toString());
                                editor.apply();
                            }
                        });
                    }
                });
            }
            else {
                Toast.makeText(EditAccount.this, "A foto deve ser um jpg/jpeg/png", Toast.LENGTH_LONG).show();
            }
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
                Log.e("retorno update user", String.valueOf(response.code()));

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
                Log.e("retorno update perfil", String.valueOf(response.code()));
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSucesssObject(response.body());
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
