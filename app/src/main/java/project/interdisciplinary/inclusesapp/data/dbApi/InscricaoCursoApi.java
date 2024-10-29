package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.UUID;

import project.interdisciplinary.inclusesapp.data.models.CreateInscricaoCursoRequest;
import project.interdisciplinary.inclusesapp.data.models.InscricaoCurso;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InscricaoCursoApi {
    @POST("inscricao-curso/inserir")
    Call<JsonObject> insertInscricaoCurso(@Header("Authorization") String token, @Body CreateInscricaoCursoRequest inscricaoCursoRequest);

    @GET("inscricao-curso/selecionar-fk-usuario/{fkUsuario}")
    Call<List<InscricaoCurso>> findInscricaoCurso(@Header("Authorization") String token, @Path("fkUsuario")UUID fkUsuario);

    @GET("inscricao-curso/selecionar-acessos/{fkCurso}")
    Call<JsonObject> findAcesses(@Header("Authorization") String token, @Path("fkCurso")UUID fkCurso);
}
