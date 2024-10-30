package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.UUID;

import project.interdisciplinary.inclusesapp.data.models.Postagem;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PostagemApi {
    @GET("postagem/listar")
    Call<List<JsonObject>> getPostagens();

    @POST("postagem/inserir")
    Call<JsonObject> insertPostagem(@Body Postagem postagem);

    @POST("/postagem/comentar")
    Call<JsonObject> addComment(
            @Query("id_user") UUID idUser,
            @Query("id_postagem") UUID idPostagem,
            @Query("comentario") String comentario
    );
    @POST("postagem/like")
    Call<JsonObject> like(@Query("id_user") String idUser, @Query("postagem_id") String postagemId);

    @POST("postagem/removerlike")
    Call<JsonObject> removeLike(@Query("id_user") String idUser, @Query("postagem_id") String postagemId);
}
