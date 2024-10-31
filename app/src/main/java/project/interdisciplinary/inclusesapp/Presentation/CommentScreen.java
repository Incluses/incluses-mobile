package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.adapters.CommentsAdapter;
import project.interdisciplinary.inclusesapp.data.dbApi.PostagemApi;
import project.interdisciplinary.inclusesapp.data.dbApi.PostagemCallback;
import project.interdisciplinary.inclusesapp.data.firebase.DatabaseFirebase;
import project.interdisciplinary.inclusesapp.data.models.Error;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.data.models.Postagem.Comentario;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.models.Postagem;
import project.interdisciplinary.inclusesapp.databinding.ActivityCommentScreenBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentScreen extends AppCompatActivity {

    private ActivityCommentScreenBinding binding;
    private Retrofit retrofit;
    private DatabaseFirebase firebase = new DatabaseFirebase();
    private String token;
    private String postId;
    private Perfil perfilObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //force light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityCommentScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.comentsRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        binding.textViewCommentScreenBack.setOnClickListener(v -> {
            finish();
        });
        binding.imageViewCommentScreenBackButton.setOnClickListener(v -> {
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

        if (getIntent().hasExtra("comments")) {
            String jsonComments = getIntent().getStringExtra("comments");
            postId = getIntent().getStringExtra("Idpost");

            // Converte o JSON string para List<Comentario>
            List<Comentario> comentariosList = jsonToComentarioList(jsonComments);
            // Aqui você pode setar no adapter (assumindo que você tenha um adapter para os comentários)
            CommentsAdapter adapter = new CommentsAdapter(comentariosList, this);
            binding.comentsRecyclerView.setAdapter(adapter);
        }

        binding.sendCommentButton.setOnClickListener(v -> {
            String comment = binding.fieldMessageEditText.getText().toString();
            if (!comment.isEmpty()) {
                comment(perfilObj.getId(), UUID.fromString(postId), comment, new PostagemCallback() {
                    @Override
                    public void onSuccess(List<JsonObject> listJsonObject) {
                        Toast.makeText(CommentScreen.this, "Comentário enviado!", Toast.LENGTH_SHORT).show();

                        // Limpa o campo de entrada
                        binding.fieldMessageEditText.setText("");

                        // Remove o foco do campo de entrada
                        binding.fieldMessageEditText.clearFocus();

                        // Oculta o teclado
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            imm.hideSoftInputFromWindow(binding.fieldMessageEditText.getWindowToken(), 0);
                        }

                        Toast.makeText(CommentScreen.this, "Enviado! Atualize o feed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccessInsert(JsonObject jsonObject) {}

                    @Override
                    public void onSuccessVerifyLike(Boolean booleanResponse) {}

                    @Override
                    public void onFailure(Throwable throwable) {
                        firebase.saveError(new Error("Erro ao comentar: " + throwable.getMessage()));
                        Log.e("Erro", throwable.getMessage());
                    }
                });
            } else {
                Toast.makeText(CommentScreen.this, "Preencha algo para comentar!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public List<Postagem.Comentario> jsonToComentarioList(String jsonArray) {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Comentario>>() {}.getType();
        return gson.fromJson(jsonArray, listType);
    }

    private void comment(UUID idCurrentUser, UUID idPostagem, String comment, PostagemCallback callback) {

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
        Call<JsonObject> call = api.addComment(idCurrentUser, idPostagem, comment);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Processar a resposta para extrair a lista de postagens, se for o caso
                    List<JsonObject> listResponseBody = new ArrayList<>();
                    JsonObject responseBody = response.body();
                    listResponseBody.add(responseBody); // Adiciona o item na lista
                    // Chama o callback com a lista
                    callback.onSuccess(listResponseBody);
                } else {
                    // Outro erro
                    callback.onFailure(new Exception("Erro ao comentar: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                firebase.saveError(new Error("Erro ao comentar: " + throwable.getMessage()));
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
}