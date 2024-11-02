package project.interdisciplinary.inclusesapp.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.Firebase;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.dbApi.PerfilApi;
import project.interdisciplinary.inclusesapp.data.dbApi.PerfilCallback;
import project.interdisciplinary.inclusesapp.data.firebase.DatabaseFirebase;
import project.interdisciplinary.inclusesapp.data.models.Error;
import project.interdisciplinary.inclusesapp.data.models.Perfil;
import project.interdisciplinary.inclusesapp.data.models.Postagem.Comentario;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ComentarioViewHolder> {

    private List<Comentario> listComments;
    private Retrofit retrofit;
    private String token;
    private DatabaseFirebase firebase = new DatabaseFirebase();

    private Perfil perfilObj;

    public CommentsAdapter(List<Comentario> listaComentarios, Context context) {
        this.listComments = listaComentarios;

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
    public ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new ComentarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioViewHolder holder, int position) {
        Comentario comentario = listComments.get(position);

        findPerfil(String.valueOf(comentario.getPerfilId()), new PerfilCallback() {
            @Override
            public void onSuccess(Perfil perfil) {
                holder.textViewPerfilName.setText(perfil.getNome());
                if (perfil.getFkFtPerfilId()!=null){
                    firebase.getFileUriFromFirebase(perfil.getFkFtPerfilId().toString(),
                            uri -> {
                                Glide.with(holder.itemView)
                                        .load(uri.toString())  // Convertendo a URI em String se necessário
                                        .apply(RequestOptions.circleCropTransform())  // Aplica a transformação circular
                                        .into(holder.imageViewPerfil);
                            },
                            e -> {
                                Log.e("Firebase", "Erro ao obter URL de download", e);
                            }
                    );
                }
            }

            @Override
            public void onSucesssObject(JsonObject jsonObject) {

            }

            @Override
            public void onFailure(Throwable throwable) {
                firebase.saveError(new Error("Erro ao carregar o perfil: " + throwable.getMessage()));
                Log.e("Erro", throwable.getMessage());
            }
        });
        holder.textViewComentario.setText(comentario.getComentario());
    }

    @Override
    public int getItemCount() {
        return listComments.size();
    }

    public class ComentarioViewHolder extends RecyclerView.ViewHolder {
        TextView textViewComentario;
        TextView textViewPerfilName;

        ImageView imageViewPerfil;

        public ComentarioViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPerfil = itemView.findViewById(R.id.perfilImageView);
            textViewComentario = itemView.findViewById(R.id.commentTextView);
            textViewPerfilName = itemView.findViewById(R.id.namePerfilCommentTextView);
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
}