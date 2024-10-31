package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.dbApi.PostagemApi;
import project.interdisciplinary.inclusesapp.data.dbApi.PostagemCallback;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.data.models.Postagem;
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

    private boolean haveImage = false;

    private ActivityCreatePostBinding binding;

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

        binding.createPostButton.setOnClickListener(v -> {

                    if (binding.legendCreatePostEditText.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Preencha pelo menos o campo de legenda!", Toast.LENGTH_LONG).show();
                        binding.legendCreatePostEditText.setError("Preencha pelo menos o campo de legenda!");

                    } else {
                        Postagem postagem = new Postagem();
                        if (haveImage) {
                            postagem.setArquivoId(UUID.randomUUID()); //Mudar Aqui quando os arquivos estiverem implementados
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
}