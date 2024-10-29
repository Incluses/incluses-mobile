package project.interdisciplinary.inclusesapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.dbApi.PerfilApi;
import project.interdisciplinary.inclusesapp.data.dbApi.PerfilCallback;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostagensAdapter extends RecyclerView.Adapter<PostagensAdapter.ItemPostagensViewHolder> {
    private List<JsonObject> listaPostagens = new ArrayList<>();
    private String token;
    private Perfil perfilObj;
    private Retrofit retrofit;

    public PostagensAdapter(List<JsonObject> listaPostagens, Context context) {
        this.listaPostagens = listaPostagens;

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

        findPerfil(jsonObject.get("perfilId").getAsString(), new PerfilCallback() {
            @Override
            public void onSuccess(Perfil perfil) {
                holder.namePerfil.setText(perfil.getNome());
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.e("Erro", throwable.getMessage());
            }
        });


        // Exemplo de como setar a imagem, se você tiver a URL ou o recurso da imagem
        // holder.imgPost.setImageURI(); // Configurar imagem aqui
        holder.legendPost.setText(jsonObject.get("legenda").getAsString());

        holder.imgPost.setVisibility(View.GONE);


        // Controle do estado do like
        holder.likePost.setOnClickListener(v -> {
            if (holder.isLiked) {
                holder.likePost.setBackgroundTintList(null);
                holder.isLiked = false;
                //removeLike();
            } else {
                holder.likePost.setBackgroundTintList(ContextCompat.getColorStateList(holder.itemView.getContext(), R.color.light_blue));
                holder.isLiked = true;
                //addLike();
            }
        });

        // Ação de comentário
        holder.commentPost.setOnClickListener(v -> {
            // Implementar a lógica de adicionar comentário
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
        private ImageView imgPost;
        private TextView legendPost;
        private ImageButton likePost;
        private ImageButton commentPost;
        private ImageButton sendPost;
        public boolean isLiked = false; // Adiciona o controle de estado para o like

        public ItemPostagensViewHolder(@NonNull View itemView) {
            super(itemView);
            namePerfil = itemView.findViewById(R.id.namePerfilPostTextView);
            descPerfil = itemView.findViewById(R.id.descriptionPerfilPostTextView);
            imgPost = itemView.findViewById(R.id.imagePostImageView);
            legendPost = itemView.findViewById(R.id.legendPostTextView);
            likePost = itemView.findViewById(R.id.likePostImageButton);
            commentPost = itemView.findViewById(R.id.comentPostImageButton);
            sendPost = itemView.findViewById(R.id.sendPostImageButton);
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
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });

    }
}