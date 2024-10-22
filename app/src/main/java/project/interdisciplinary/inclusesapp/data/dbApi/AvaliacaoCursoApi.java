package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import project.interdisciplinary.inclusesapp.data.models.AvaliacaoCurso;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AvaliacaoCursoApi {
    @GET("avaliacao-curso/selecionar-fk-usuario/{id}")
    Call<List<AvaliacaoCurso>> findByFkUser(@Header ("Authorization") String token ,@Path("id") UUID fkUser);

    @PATCH("avaliacao-curso/atualizarParcial/{id}")
    Call<JsonObject> updateAvaliacao(@Header("Authorization") String token, @Path("id") UUID fkAvaliacao, @Body Map<String,Double> body);

    @POST("avaliacao-curso/inserir")
    Call<JsonObject> insertAvaliacao(@Header("Authorization") String token, @Body AvaliacaoCurso avaliacaoCurso);
}
