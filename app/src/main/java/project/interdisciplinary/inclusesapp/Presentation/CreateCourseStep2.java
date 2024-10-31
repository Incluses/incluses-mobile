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
import java.util.List;
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
import project.interdisciplinary.inclusesapp.data.models.CreateMaterialCursoRequest;
import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;
import project.interdisciplinary.inclusesapp.data.models.TipoArquivo;
import project.interdisciplinary.inclusesapp.databinding.ActivityCreateCourseStep2Binding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateCourseStep2 extends AppCompatActivity {

    private ActivityCreateCourseStep2Binding binding;

    private UUID idCurso;

    private Retrofit retrofit;
    private String token;
    private static final int PICK_FILE_REQUEST = 1;

    private FileChoose fileChoose = new FileChoose();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);


        binding = ActivityCreateCourseStep2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent extrasIntent = getIntent();
        Bundle bundle = extrasIntent.getExtras();
        idCurso = UUID.fromString(bundle.getString("idCurso"));
        SharedPreferences preferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");

        binding.archiveCreateCourseStep2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChoose.openFileChooser(CreateCourseStep2.this);
            }
        });
        binding.nextCreateMyCourse2Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences2 = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                boolean archiveFilled = preferences2.getBoolean("archiveFilled", false);
                boolean nameFilled = false, descriptionFilled =  false;
                String nameInput = binding.titleMyCourseEditText.getText().toString();
                String descriptionInput = binding.descriptionEditTextMyCourse.getText().toString();
                if (!nameInput.isEmpty()){
                    nameFilled = true;
                }
                else {
                    nameFilled = false;
                }
                if (!descriptionInput.isEmpty()){
                    descriptionFilled = true;
                }
                else {
                    descriptionFilled = false;
                }
                if (nameFilled && descriptionFilled && archiveFilled){
                    Arquivo arquivo = ConvertersToObjects.convertStringToArquivo(preferences2.getString("archive", ""));
                    CreateMaterialCursoRequest createMaterialCursoRequest = new CreateMaterialCursoRequest(
                            descriptionInput,nameInput,idCurso,arquivo.getId());
                    callApiRetrofitInsertMaterial(createMaterialCursoRequest, new MaterialCursoCallback() {
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
                    SharedPreferences.Editor editor = preferences2.edit();
                    editor.putBoolean("archiveFilled", false);

                }
            }
        });



        binding.cancelCreateMyCourse2Button.setOnClickListener(v -> finish());

    }

    private void callApiRetrofitInsertMaterial(CreateMaterialCursoRequest materialCurso , MaterialCursoCallback callback) {
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
        Call<JsonObject> call = api.insertMaterialCurso(token,materialCurso);
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
                            binding.textArchiveCreateCourseStep2.setText(apiResponse2.getNome());
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