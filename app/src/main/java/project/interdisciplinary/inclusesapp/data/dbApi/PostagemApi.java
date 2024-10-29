package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import java.util.List;

import project.interdisciplinary.inclusesapp.data.models.Postagem;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PostagemApi {
    @GET("postagem/listar")
    Call<List<JsonObject>> getPostagens();

    @POST("postagem/inserir")
    Call<JsonObject> insertPostagem(@Body Postagem postagem);
}
