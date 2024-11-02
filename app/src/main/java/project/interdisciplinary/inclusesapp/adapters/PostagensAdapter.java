package project.interdisciplinary.inclusesapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.Presentation.CommentScreen;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.dbApi.PerfilApi;
import project.interdisciplinary.inclusesapp.data.dbApi.PerfilCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.PostagemApi;
import project.interdisciplinary.inclusesapp.data.dbApi.PostagemCallback;
import project.interdisciplinary.inclusesapp.data.firebase.DatabaseFirebase;
import project.interdisciplinary.inclusesapp.data.models.Error;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.data.models.Postagem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostagensAdapter extends RecyclerView.Adapter<PostagensAdapter.ItemPostagensViewHolder> {
    private List<JsonObject> listaPostagens = new ArrayList<>();
    private String token;
    private DatabaseFirebase firebase = new DatabaseFirebase();
    private Perfil perfilObj;
    private Retrofit retrofit;


    public PostagensAdapter(List<JsonObject> listaPostagens, Context context) {
        this.listaPostagens = listaPostagens;
        firebase = new DatabaseFirebase(); // Or retrieve the instance if it’s a singleton


        SharedPreferences preferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        String perfilJson = preferences.getString("perfil", "");

        // Verifique se o JSON não é nulo ou vazio antes de tentar convertê-lo
        if (!perfilJson.isEmpty()) {
            Gson gson = new Gson();
            perfilObj = gson.fromJson(perfilJson, Perfil.class);  // Converte o JSON de volta para o objeto Perfil
            Log.e("Perfil", "ID: " + perfilObj.getId());
        } else {
            Log.e("Perfil", "Nenhum perfil encontrado no SharedPreferences.");
        }
    }

    @NonNull
    @Override
    public PostagensAdapter.ItemPostagensViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View viewItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feed, parent, false);
        return new ItemPostagensViewHolder(viewItem);
    }

    @Override
    public void onBindViewHolder(@NonNull PostagensAdapter.ItemPostagensViewHolder holder, int position) {
        JsonObject jsonObject = listaPostagens.get(position);
        Postagem postagem = new Gson().fromJson(jsonObject, Postagem.class);

        findPerfil(jsonObject.get("perfilId").getAsString(), new PerfilCallback() {
            @Override
            public void onSucesssObject(JsonObject jsonObject) {

            }

            @Override
            public void onSuccess(Perfil perfil) {
                holder.namePerfil.setText(perfil.getNome());
                if (perfil.getFkFtPerfilId() != null){
                    firebase.getFileUriFromFirebase(perfil.getFkFtPerfilId().toString(),
                            uri -> {
                                Glide.with(holder.itemView.getContext())
                                        .load(uri.toString())  // Convertendo a URI em String se necessário
                                        .apply(RequestOptions.circleCropTransform())  // Aplica a transformação circular
                                        .into(holder.perfilImg);
                            },
                            e -> {
                                Log.e("Firebase", "Erro ao obter URL de download", e);
                            }
                    );
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("Erro", throwable.getMessage());
            }
        });


        // Exemplo de como setar a imagem, se você tiver a URL ou o recurso da imagem
        // holder.imgPost.setImageURI(); // Configurar imagem aqui
        // Configure a legenda
        holder.legendPost.setText(jsonObject.get("legenda").getAsString());

        // Inicialmente, esconda ambos os views
        holder.imgPost.setVisibility(View.GONE);
        holder.videoPostImageView.setVisibility(View.GONE);

        if (!jsonObject.get("arquivoId").isJsonNull()) {
            String arquivoId = jsonObject.get("arquivoId").getAsString();

            firebase.getFileUriFromFirebase(arquivoId,
                    uri -> {
                        String fileExtension = getFileExtension(uri.toString());
                        if (fileExtension.equals("mp4")) {
                            holder.videoPostImageView.setVisibility(View.VISIBLE);
                            holder.imgPost.setVisibility(View.GONE);

                            // Carregue o vídeo no VideoView
                            MediaController mediaController = new MediaController(holder.itemView.getContext());
                            mediaController.setMediaPlayer(holder.videoPostImageView);
                            holder.videoPostImageView.setMediaController(mediaController);
                            Log.e("video", uri.toString());
                            holder.videoPostImageView.setVideoURI(uri);
                            holder.videoPostImageView.start();
                        } else {
                            holder.imgPost.setVisibility(View.VISIBLE);
                            holder.videoPostImageView.setVisibility(View.GONE);

                            // Carregue a imagem
                            Glide.with(holder.itemView.getContext())
                                    .load(uri.toString())
                                    .into(holder.imgPost);
                        }
                    },
                    e -> {
                        Log.e("Firebase", "Erro ao obter URL de download", e);
                    }
            );
        }


        // Controle do like

        verifyLike(UUID.fromString(jsonObject.get("id").getAsString()), perfilObj.getId(), new PostagemCallback() {
            @Override
            public void onSuccessVerifyLike(Boolean booleanResponse) {
                if (!booleanResponse) {
                    holder.likePost.setBackground(ContextCompat.getDrawable(holder.likePost.getContext(), R.drawable.like));
                    holder.isLiked = false;
                } else {
                    holder.likePost.setBackground(ContextCompat.getDrawable(holder.likePost.getContext(), R.drawable.ic_liked));
                    holder.isLiked = true;
                }
            }

            @Override
            public void onSuccess(List<JsonObject> listJsonObject) {

            }

            @Override
            public void onSucessFind(List<Postagem> list) {

            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("Erro", throwable.getMessage());
            }

            @Override
            public void onSuccessInsert(JsonObject jsonObject) {

            }
        });

        // Controle do estado do like
         holder.likePost.setOnClickListener(v -> {
            if (holder.isLiked) {
                holder.likePost.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.ic_liked));
                holder.isLiked = false;

                addLike(UUID.fromString(jsonObject.get("id").getAsString()), perfilObj.getId(), new PostagemCallback() {
                    @Override
                    public void onSuccess(List<JsonObject> postagem) {
                        Toast.makeText(v.getContext(), "Foi!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccessVerifyLike(Boolean booleanResponse) {

                    }

                    @Override
                    public void onSucessFind(List<Postagem> list) {

                    }

                    @Override
                    public void onSuccessInsert(JsonObject jsonObject) {

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        firebase.saveError(new Error("Erro ao descurtir: " + throwable.getMessage()));
                        Log.e("Erro", throwable.getMessage());
                    }
                });

            } else {
                holder.likePost.setBackground(ContextCompat.getDrawable(v.getContext(), R.drawable.like));
                holder.isLiked = true;
                removeLike(UUID.fromString(jsonObject.get("id").getAsString()), perfilObj.getId(), new PostagemCallback() {
                    @Override
                    public void onSuccess(List<JsonObject> postagem) {
                        Toast.makeText(v.getContext(), "Foi!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccessVerifyLike(Boolean booleanResponse) {

                    }

                    @Override
                    public void onSucessFind(List<Postagem> list) {

                    }

                    @Override
                    public void onSuccessInsert(JsonObject jsonObject) {

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        firebase.saveError(new Error("Erro ao curtir: " + throwable.getMessage()));
                        Log.e("Erro", throwable.getMessage());
                    }
                });
            }
        });

        // Ação de comentário
        holder.commentPost.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), CommentScreen.class);
            intent.putExtra("comments", jsonObject.get("comentarios").getAsJsonArray().toString());
            intent.putExtra("Idpost", jsonObject.get("id").getAsString());
            holder.itemView.getContext().startActivity(intent);
        });

        // Ação de compartilhar/enviar
        holder.sendPost.setOnClickListener(v -> {
            Toast.makeText(holder.itemView.getContext(), "Esta função será implementada na próxima versão!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return listaPostagens.size();
    }

    public class ItemPostagensViewHolder extends RecyclerView.ViewHolder {
        private TextView namePerfil;
        private TextView descPerfil;
        private VideoView videoPostImageView;
        private ImageView imgPost;
        private TextView legendPost;
        private ImageButton likePost;
        private ImageButton commentPost;
        private ImageButton sendPost;
        private ImageView perfilImg;
        public boolean isLiked = false; // Adiciona o controle de estado para o like

        public ItemPostagensViewHolder(@NonNull View itemView) {
            super(itemView);
            namePerfil = itemView.findViewById(R.id.namePerfilPostTextView);
            descPerfil = itemView.findViewById(R.id.descriptionPerfilPostTextView);
            imgPost = itemView.findViewById(R.id.imagePostImageView);
            videoPostImageView = itemView.findViewById(R.id.videoPostImageView);
            legendPost = itemView.findViewById(R.id.legendPostTextView);
            likePost = itemView.findViewById(R.id.likePostImageButton);
            commentPost = itemView.findViewById(R.id.comentPostImageButton);
            sendPost = itemView.findViewById(R.id.sendPostImageButton);
            perfilImg = itemView.findViewById(R.id.perfilImageView);
        }
    }

    public void findPerfil(String idPerfil, PerfilCallback callback) {

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

        UUID uuid = UUID.fromString(idPerfil);
        PerfilApi api = retrofit.create(PerfilApi.class);
        Call<JsonObject> call = api.getPerfilById(token, uuid);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();
                    Gson gson = new Gson();
                    Perfil perfil = gson.fromJson(responseBody, Perfil.class);
                    callback.onSuccess(perfil);
                } else if (response.code() == 401) {
                    // Token inválido ou expirado
                    callback.onFailure(new Exception("Token expirado ou inválido!"));
                } else {
                    // Outro erro
                    callback.onFailure(new Exception("Erro ao buscar perfil: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                firebase.saveError(new Error("Erro ao buscar perfil: " + throwable.getMessage()));
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });

    }

    private void verifyLike( UUID idCurrentUser, UUID idPostagem, PostagemCallback callback) {

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
        Call<Boolean> call = api.verifyLike(idPostagem, idCurrentUser);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Chama o callback com a lista
                    callback.onSuccessVerifyLike(response.body());
                } else {
                    // Outro erro
                    callback.onFailure(new Exception("Erro ao adicionar like: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable throwable) {
                firebase.saveError(new Error("Erro ao adicionar like: " + throwable.getMessage()));
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }

    private void addLike(UUID idPostagem, UUID idCurrentUser, PostagemCallback callback) {

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
        Call<JsonObject> call = api.like(idCurrentUser.toString(), idPostagem.toString());
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
                    callback.onFailure(new Exception("Erro ao adicionar like: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                firebase.saveError(new Error("Erro ao adicionar like: " + throwable.getMessage()));
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
    private void removeLike(UUID idPostagem, UUID idCurrentUser, PostagemCallback callback) {

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
        Call<JsonObject> call = api.removeLike(idCurrentUser.toString(), idPostagem.toString());
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
                    callback.onFailure(new Exception("Erro ao remover like: " + response.code()));
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                firebase.saveError(new Error("Erro ao remover like: " + throwable.getMessage()));
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }

    // Método para obter a extensão do arquivo
    private String getFileExtension(String uri) {
        String[] parts = uri.split("\\.");
        return parts.length > 1 ? parts[parts.length - 1].toLowerCase() : "";
    }
}