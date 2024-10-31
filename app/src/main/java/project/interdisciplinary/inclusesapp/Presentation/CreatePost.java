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
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.EditAccountEnterpriseActivity;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.archive.FileChoose;
import project.interdisciplinary.inclusesapp.data.ConvertersToObjects;
import project.interdisciplinary.inclusesapp.data.dbApi.ArquivoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.ArquivoCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.MaterialCursoCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.PostagemApi;
import project.interdisciplinary.inclusesapp.data.dbApi.PostagemCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.TipoArquivoApi;
import project.interdisciplinary.inclusesapp.data.firebase.DatabaseFirebase;
import project.interdisciplinary.inclusesapp.data.models.Arquivo;
import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.data.models.Postagem;
import project.interdisciplinary.inclusesapp.data.models.TipoArquivo;
import project.interdisciplinary.inclusesapp.databinding.ActivityCreatePostBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreatePost extends AppCompatActivity {

    private Retrofit retrofit;

    private String token;

    private Perfil perfilObj;

    private ActivityCreatePostBinding binding;
    private static final int PICK_FILE_REQUEST = 1;
    private FileChoose fileChoose = new FileChoose();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityCreatePostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.cancelAddMaterialCourseButton.setOnClickListener(v -> {
            finish();
        });

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        String perfilJson = preferences.getString("perfil", "");

        // Verifique se o JSON não é nulo ou vazio antes de tentar convertê-lo
        if (!perfilJson.isEmpty()) {
            Gson gson = new Gson();
            perfilObj = gson.fromJson(perfilJson, Perfil.class);  // Converte o JSON de volta para o objeto Usuario
            Log.e("Perfil", "ID: " + perfilObj.getId());
        } else {
            Log.e("Perfil", "Nenhuma empresa encontrada no SharedPreferences.");
        }
        binding.inputArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fileChoose.openFileChooser(CreatePost.this);
            }
        });

        binding.createPostButton.setOnClickListener(v -> {

                    if (binding.legendCreatePostEditText.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Preencha pelo menos o campo de legenda!", Toast.LENGTH_LONG).show();
                        binding.legendCreatePostEditText.setError("Preencha pelo menos o campo de legenda!");

                    } else {
                        SharedPreferences preferences2 = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                        boolean archiveFilled = preferences2.getBoolean("archiveFilled", false);
                        Postagem postagem = new Postagem();
                        if (archiveFilled){
                            Arquivo arquivo = ConvertersToObjects.convertStringToArquivo(preferences2.getString("archive",""));
                            postagem.setArquivoId(arquivo.getId());
                        }

                        postagem.setLegenda(binding.legendCreatePostEditText.getText().toString());
                        postagem.setPerfilId(perfilObj.getId());

                        inserirPost(postagem, new PostagemCallback() {
                                    @Override
                                    public void onSuccess(List<JsonObject> listJsonObject) {
                                    }

                                    @Override
                                    public void onSuccessVerifyLike(Boolean booleanResponse) {

                                    }

                                    @Override
                                    public void onSuccessInsert(JsonObject jsonObject) {
                                        Toast.makeText(CreatePost.this, "Post criado com sucesso!", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        Log.e("Erro", throwable.getMessage());
                                    }

                                    @Override
                                    public void onSucessFind(List<Postagem> list) {

                                    }
                                }
                        );
                    }


                }
        );
    }

    private void inserirPost(Postagem postagem, PostagemCallback callback) {

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
        Call<JsonObject> call = api.insertPostagem(postagem);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();
                    callback.onSuccessInsert(responseBody);
                } else {
                    // Outro erro
                    callback.onFailure(new Exception("Erro ao inserir postagem: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "erro", Toast.LENGTH_LONG).show();
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
                                binding.inputArchiveText.setText("Arquivo Inserido");
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
                Toast.makeText(CreatePost.this, "A foto deve ser um jpg/jpeg/png", Toast.LENGTH_LONG).show();
            }
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