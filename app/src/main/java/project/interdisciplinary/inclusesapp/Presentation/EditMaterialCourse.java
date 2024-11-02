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
import project.interdisciplinary.inclusesapp.data.dbApi.MaterialCursoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.MaterialCursoCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.TipoArquivoApi;
import project.interdisciplinary.inclusesapp.data.firebase.DatabaseFirebase;
import project.interdisciplinary.inclusesapp.data.models.Arquivo;
import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;
import project.interdisciplinary.inclusesapp.data.models.TipoArquivo;
import project.interdisciplinary.inclusesapp.databinding.ActivityEditMaterialCourseBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditMaterialCourse extends AppCompatActivity {

    private Retrofit retrofit;

    private FileChoose fileChoose = new FileChoose();

    private String token;
    private ActivityEditMaterialCourseBinding binding;

    private static final int PICK_FILE_REQUEST = 1;
    private boolean titleFilled, descriptionFilled, archiveFilled;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityEditMaterialCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intentExtras = getIntent();
        Bundle extras = intentExtras.getExtras();
        MaterialCurso materialCurso = ConvertersToObjects.convertStringToMaterialCurso(extras.getString("materialCurso"));

        SharedPreferences preferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        binding.descriptionEditTextEditMaterial.setText(materialCurso.getDescricao());
        binding.titleEditTextEditMaterial.setText(materialCurso.getNome());
        binding.textArchiveEditMaterialCourse.setText(materialCurso.getArquivo().getNome());

        binding.cancelButtonEditMaterial.setOnClickListener(v -> {
            finish();
        });

        binding.archiveEditMaterialCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChoose.openFileChooser(EditMaterialCourse.this);
            }
        });
        binding.submitButtonEditMaterial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences2 = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences2.edit();
                archiveFilled = preferences2.getBoolean("archiveEdited", false);
                String inputTitle = binding.titleEditTextEditMaterial.getText().toString();
                String inputDescription = binding.descriptionEditTextEditMaterial.getText().toString();
                Map<String, Object> request = new HashMap<>();

                if(!inputTitle.equals(materialCurso.getNome())){
                    request.put("nome", inputTitle);
                    titleFilled = true;
                }
                else if (!inputDescription.equals(materialCurso.getDescricao())) {
                    request.put("descricao", inputDescription);
                    descriptionFilled = true;
                } else if (archiveFilled) {
                    Arquivo arquivo = ConvertersToObjects.convertStringToArquivo(preferences2.getString("archiveEdit",""));
                    request.put("fkArquivoId",arquivo.getId());
                }
                if (titleFilled || descriptionFilled || archiveFilled){
                    callApiRetrofitAlterAvaliacao(materialCurso.getId(), request, new MaterialCursoCallback() {
                        @Override
                        public void onSuccessFind(List<MaterialCurso> list) {

                        }

                        @Override
                        public void onFailure(Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(JsonObject jsonObject) {
                            finish();
                        }
                    });
                    editor.putBoolean("archiveFilled", false);
                }
            }
        });
    }
    private void callApiRetrofitAlterAvaliacao(UUID materialId, Map<String, Object> request, MaterialCursoCallback callback) {
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
        MaterialCursoApi api = retrofit.create(MaterialCursoApi.class);
        Log.e("body", request.toString());
        Call<JsonObject> call = api.updateMaterialCurso(token,materialId, request);
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
                    Log.e("retorno 400", response.toString());
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
                            binding.textArchiveEditMaterialCourse.setText(apiResponse2.getNome());
                            databaseFirebase.uploadFileToFirebase(fileUri, apiResponse2);
                            SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("archiveEdited", true);
                            editor.putString("archiveEdit", apiResponse2.toString());
                            editor.apply();
                        }
                    });
                }
            });
        }
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
}