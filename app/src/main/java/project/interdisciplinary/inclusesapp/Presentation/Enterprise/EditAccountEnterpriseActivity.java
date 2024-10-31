package project.interdisciplinary.inclusesapp.Presentation.Enterprise;

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
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.archive.FileChoose;
import project.interdisciplinary.inclusesapp.data.ConvertersToObjects;
import project.interdisciplinary.inclusesapp.data.dbApi.ArquivoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.ArquivoCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.EmpresaApi;
import project.interdisciplinary.inclusesapp.data.dbApi.EmpresaCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.MaterialCursoCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.PerfilApi;
import project.interdisciplinary.inclusesapp.data.dbApi.PerfilCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.TipoArquivoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioApi;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioCallback;
import project.interdisciplinary.inclusesapp.data.firebase.DatabaseFirebase;
import project.interdisciplinary.inclusesapp.data.models.Arquivo;
import project.interdisciplinary.inclusesapp.data.models.Empresa;
import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.data.models.TipoArquivo;
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
    private static final int PICK_FILE_REQUEST = 1;

    private FileChoose fileChoose = new FileChoose();
    boolean hasPerfil = false, hasEmpresa = false;

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
        binding.passwordEnterpriseEditAccountInputLayout.setVisibility(View.GONE);

        //setando a imagem
        if (empresaObj.getPerfil().getFkFtPerfilId() != null) {
            //todo: implementar lógica de carregamento da imagem
            binding.textAddPhotoEditAccountEnterprise.setText("Mudar Foto Atual");
        }else {
            //todo: implementar lógica de carregamento da imagem
        }
        binding.inputEnterprisePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChoose.openFileChooser(EditAccountEnterpriseActivity.this);
            }
        });
        binding.imageViewEnterpriseEditAccountBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> requestPerfil = new HashMap<>();
                Map<String, Object> requestEnterprise = new HashMap<>();
                boolean nameFilled, websiteFilled, socialReasonFilled, biographyFilled;
                SharedPreferences preferences2 = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                String inputName = binding.nameEnterpriseEditAccountEditText.getText().toString();
                String inputWebsite = binding.websiteEnterpriseEditAccountEditText.getText().toString();
                String inputSocialReason = binding.reasonSocialEnterpriseEditAccountEditText.getText().toString();
                String inputBiography = binding.biographyEnterpriseEditAccountEditText.getText().toString();
                boolean archiveFilled = preferences2.getBoolean("archiveFilled", false);
                SharedPreferences.Editor editor = preferences2.edit();

                if (!inputName.equals(empresaObj.getPerfil().getNome())){
                    nameFilled = true;
                    requestPerfil.put("nome", inputName);
                    hasPerfil = true;
                }
                else {
                    nameFilled = false;
                }
                if (!inputWebsite.equals(empresaObj.getWebsite())){
                    websiteFilled = true;
                    requestEnterprise.put("website", inputWebsite);
                    hasEmpresa = true;

                }
                else {
                    websiteFilled = false;
                }
                if (!inputSocialReason.equals(empresaObj.getRazaoSocial())){
                    socialReasonFilled = true;
                    requestEnterprise.put("razaoSocial", inputSocialReason);
                    hasEmpresa = true;

                }
                else {
                    socialReasonFilled = false;
                }
                if (!inputBiography.equals(empresaObj.getPerfil().getBiografia())){
                    biographyFilled = true;
                    requestPerfil.put("biografia", inputBiography);
                    hasPerfil = true;
                }
                else {
                    biographyFilled = false;
                }
                if (archiveFilled){
                    Arquivo arquivo2 = ConvertersToObjects.convertStringToArquivo(preferences2.getString("archive",""));
                    requestPerfil.put("fkFtPerfilId", arquivo2.getId());
                    editor.putBoolean("archiveFilled", false);
                    hasPerfil = true;

                }

                if (nameFilled || biographyFilled || archiveFilled){
                    updatePerfil(empresaObj.getFkPerfilId(), requestPerfil, new PerfilCallback() {
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
                else if (websiteFilled || socialReasonFilled){
                    updateEnterprise(empresaObj.getId(), requestEnterprise, new EmpresaCallback() {
                        @Override
                        public void onSuccess(JsonObject jsonObject) {
                            editor.putString("empresa", jsonObject.toString());
                            editor.apply();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {

                        }
                    });
                }
                finish();
            }
        });
        binding.textViewEnterpriseEditAccountBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> requestPerfil = new HashMap<>();
                Map<String, Object> requestEnterprise = new HashMap<>();
                boolean nameFilled, websiteFilled, socialReasonFilled, biographyFilled;
                SharedPreferences preferences2 = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                String inputName = binding.nameEnterpriseEditAccountEditText.getText().toString();
                String inputWebsite = binding.websiteEnterpriseEditAccountEditText.getText().toString();
                String inputSocialReason = binding.reasonSocialEnterpriseEditAccountEditText.getText().toString();
                String inputBiography = binding.biographyEnterpriseEditAccountEditText.getText().toString();
                boolean archiveFilled = preferences2.getBoolean("archiveFilled", false);
                SharedPreferences.Editor editor = preferences2.edit();

                if (!inputName.equals(empresaObj.getPerfil().getNome())){
                    nameFilled = true;
                    requestPerfil.put("nome", inputName);
                    hasPerfil = true;
                }
                else {
                    nameFilled = false;
                }
                if (!inputWebsite.equals(empresaObj.getWebsite())){
                    websiteFilled = true;
                    requestEnterprise.put("website", inputWebsite);
                    hasEmpresa = true;

                }
                else {
                    websiteFilled = false;
                }
                if (!inputSocialReason.equals(empresaObj.getRazaoSocial())){
                    socialReasonFilled = true;
                    requestEnterprise.put("razaoSocial", inputSocialReason);
                    hasEmpresa = true;

                }
                else {
                    socialReasonFilled = false;
                }
                if (!inputBiography.equals(empresaObj.getPerfil().getBiografia())){
                    biographyFilled = true;
                    requestPerfil.put("biografia", inputBiography);
                    hasPerfil = true;
                }
                else {
                    biographyFilled = false;
                }
                if (archiveFilled){
                    Arquivo arquivo2 = ConvertersToObjects.convertStringToArquivo(preferences2.getString("archive",""));
                    requestPerfil.put("fkFtPerfilId", arquivo2.getId());
                    editor.putBoolean("archiveFilled", false);
                    hasPerfil = true;

                }

                if (nameFilled || biographyFilled || archiveFilled){
                    updatePerfil(empresaObj.getFkPerfilId(), requestPerfil, new PerfilCallback() {
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
                if (websiteFilled || socialReasonFilled){
                    updateEnterprise(empresaObj.getId(), requestEnterprise, new EmpresaCallback() {
                        @Override
                        public void onSuccess(JsonObject jsonObject) {
                            editor.putString("empresa", jsonObject.toString());
                            editor.apply();
                        }

                        @Override
                        public void onFailure(Throwable throwable) {

                        }
                    });
                }
                finish();
            }
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
                                binding.textAddPhotoEditAccountEnterprise.setText("Mudar Foto Atual");
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
                Toast.makeText(EditAccountEnterpriseActivity.this, "A foto deve ser um jpg/jpeg/png", Toast.LENGTH_LONG).show();
            }
        }
    }
    private void updateEnterprise(UUID idEnterprise,Map<String, Object> request, EmpresaCallback callback) {
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

        EmpresaApi api = retrofit.create(EmpresaApi.class);
        Call<JsonObject> call = api.updateEmpresa(token,idEnterprise,request);
        call.enqueue(new Callback<JsonObject>() {


            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("retorno update", String.valueOf(response.code()));
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
    private void updatePerfil(UUID idPerfil,Map<String, Object> request, PerfilCallback callback) {
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
        Call<JsonObject> call = api.updatePerfil(token,idPerfil,request);
        call.enqueue(new Callback<JsonObject>() {


            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("retorno update", String.valueOf(response.code()));
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();
                    callback.onSucesssObject(responseBody); // Sucesso
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
}