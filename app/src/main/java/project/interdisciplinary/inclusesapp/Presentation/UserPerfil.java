package project.interdisciplinary.inclusesapp.Presentation;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.EditAccountEnterpriseActivity;
import project.interdisciplinary.inclusesapp.Presentation.Enterprise.EnterpriseProfileActivity;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.adapters.PostagensAdapter;
import project.interdisciplinary.inclusesapp.archive.FileChoose;
import project.interdisciplinary.inclusesapp.data.ConvertersToObjects;
import project.interdisciplinary.inclusesapp.data.dbApi.ArquivoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.ArquivoCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.MaterialCursoCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.PostagemApi;
import project.interdisciplinary.inclusesapp.data.dbApi.PostagemCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.TipoArquivoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioApi;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioCallback;
import project.interdisciplinary.inclusesapp.data.firebase.DatabaseFirebase;
import project.interdisciplinary.inclusesapp.data.models.Arquivo;
import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.data.models.Postagem;
import project.interdisciplinary.inclusesapp.data.models.TipoArquivo;
import project.interdisciplinary.inclusesapp.data.models.Usuario;
import project.interdisciplinary.inclusesapp.databinding.ActivityUserPerfilBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserPerfil extends AppCompatActivity {

    private ActivityUserPerfilBinding binding;
    private String token;
    private Retrofit retrofit;
    private Perfil perfilObj;
    private Usuario userObj;
    private DatabaseFirebase firebase;
    private static final int PICK_FILE_REQUEST = 1;

    private FileChoose fileChoose = new FileChoose();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Force Theme to Light Mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        firebase = new DatabaseFirebase(); // Or retrieve the instance if it’s a singleton


        binding = ActivityUserPerfilBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //button back
        binding.imageViewLoginUserBackButton.setOnClickListener(v -> {
            finish(); //finish() to go back to the previous screen
        });

        binding.textViewLoginUserBack.setOnClickListener(v -> {
            finish(); //finish() to go back to the previous screen
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        String usuarioJson = preferences.getString("usuario", "");
        String perfilJson = preferences.getString("perfil", "");

        // Verifique se o JSON não é nulo ou vazio antes de tentar convertê-lo
        if (!usuarioJson.isEmpty() && !perfilJson.isEmpty()) {
            Gson gson = new Gson();
            perfilObj = gson.fromJson(perfilJson, Perfil.class);
            userObj = gson.fromJson(usuarioJson, Usuario.class);  // Converte o JSON de volta para o objeto Usuario

            Log.e("Usuario", "ID: " + userObj.getId());
        } else {
            Log.e("Usuario", "Nenhuma empresa encontrada no SharedPreferences.");
        }

        //inflando os dados do usuário
        if (userObj.getNomeSocial() != null) {
            binding.namePerfiltextView.setText(userObj.getNomeSocial());
        }else {
            binding.namePerfiltextView.setText(perfilObj.getNome());
        }
        binding.pronounUserProfile.setText(userObj.getPronomes());
        if (perfilObj.getBiografia()!= null) {
            binding.addBiographyProfile.setText(perfilObj.getBiografia());
            binding.addBiographyProfile.setVisibility(View.VISIBLE);
        }
        else {
            binding.addBiographyProfile.setVisibility(View.GONE);
        }
        if (userObj.getFkCurriculoId() != null) {
            binding.curriculamAddText.setText("Currículo Atual");
            binding.inputCurriculum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    SharedPreferences preferences2 = getApplicationContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences2.edit();

                    boolean updated = preferences2.getBoolean("archiveFilled", false);
                    if (updated){
                        Arquivo arquivo2 = ConvertersToObjects.convertStringToArquivo(preferences2.getString("archive",null));
                        firebase.getFileUriFromFirebase(arquivo2.getId().toString(),
                                uri -> {
                                    intent.setDataAndType(uri, "*/*");
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                    try {
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException e) {
                                        Toast.makeText(UserPerfil.this, "Não há aplicativo disponível para abrir este arquivo.", Toast.LENGTH_SHORT).show();
                                    }
                                },
                                e -> {
                                    Log.e("Firebase", "Erro ao obter URL de download", e);
                                }
                        );
                        editor.putBoolean("archiveFilled", false);

                    }


                }
            });
            binding.inputCurriculum.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    fileChoose.openFileChooser(UserPerfil.this);
                    return false;
                }
            });
        }
        else {
            binding.inputCurriculum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fileChoose.openFileChooser(UserPerfil.this);
                }
            });
        }
        if (perfilObj.getFkFtPerfilId() != null){
            firebase.getFileUriFromFirebase(perfilObj.getFkFtPerfilId().toString(),
                    uri -> {
                        Glide.with(this)
                                .load(uri.toString())  // Convertendo a URI em String se necessário
                                .apply(RequestOptions.circleCropTransform())  // Aplica a transformação circular
                                .into(binding.ImageViewPerfilUser);
                    },
                    e -> {
                        Log.e("Firebase", "Erro ao obter URL de download", e);
                    }
            );
        }

        binding.icConfigImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserPerfil.this, ScreenConfigurations.class);
                intent.putExtra("user_type", "user");
                startActivity(intent);
            }
        });
        binding.myPostsProfileRecyclerView.setLayoutManager(new LinearLayoutManager(UserPerfil.this));
        setupAdapter(perfilObj.getId(), new PostagemCallback() {
            @Override
            public void onSuccess(List<JsonObject> listJsonObject) {
                binding.myPostsProfileRecyclerView.setAdapter(new PostagensAdapter(listJsonObject, getApplicationContext()));
            }

            @Override
            public void onSucessFind(List<Postagem> list) {
            }

            @Override
            public void onSuccessVerifyLike(Boolean booleanResponse) {

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
                Toast.makeText(UserPerfil.this, "erro", Toast.LENGTH_LONG).show();
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
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
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            Gson gson = new Gson();
                            Arquivo apiResponse2 = gson.fromJson(jsonObject, Arquivo.class);
                            binding.curriculamAddText.setText("Currículo Atual");
                            databaseFirebase.uploadFileToFirebase(fileUri, apiResponse2);
                            SharedPreferences preferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("archiveFilled", true);
                            editor.putString("archive", apiResponse2.toString());
                            editor.apply();
                            if (userObj.getCurriculo().getId() != null){
                                Log.e("arquivo update", apiResponse2.getId().toString());
                                editor.apply();
                                updateCurriculum(userObj.getId(), apiResponse2.getId(), new UsuarioCallback() {
                                    @Override
                                    public void onSuccess(JsonObject jsonObject) {
                                        Toast.makeText(UserPerfil.this,"Currículo atualizado",Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {

                                    }
                                });
                            }
                            Log.e("id curriculo", userObj.getCurriculo().getId().toString());
                        }
                    });
                }
            });
            }
        }
    private void updateCurriculum(UUID idUser,UUID idArquivo, UsuarioCallback callback) {
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

        Map<String,Object> request = new HashMap<>();
        request.put("fkCurriculoId", idArquivo);
        UsuarioApi api = retrofit.create(UsuarioApi.class);
        Call<JsonObject> call = api.updateUser(token,idUser,request);
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
    }