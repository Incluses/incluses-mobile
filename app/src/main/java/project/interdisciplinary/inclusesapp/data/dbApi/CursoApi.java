package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.UUID;

import project.interdisciplinary.inclusesapp.data.models.CreateCursoRequest;
import project.interdisciplinary.inclusesapp.data.models.Curso;
import project.interdisciplinary.inclusesapp.data.models.Vaga;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CursoApi {
    @GET("curso/selecionar")
    Call<List<Curso>> findCursos(@Header("Authorization") String token);
    @GET("curso/selecionar-nome/{nome}")
    Call<List<Curso>> findCursosByName(@Header("Authorization") String token, @Path("nome") String nome);
    @POST("curso/inserir")
    Call<JsonObject> insertCurso(@Header("Authorization") String token, @Body CreateCursoRequest createCursoRequest);
    @GET("curso/selecionar-fk-perfil/{fkPerfil}")
    Call<List<Curso>> findCursosByFkPerfil(@Header("Authorization") String token, @Path("fkPerfil") UUID fkPerfil);

    @GET("curso/selecionar-perfil-nome/{fkPerfil}/{nome}")
    Call<List<Curso>> findCursosByFkPerfilByName(@Header("Authorization") String token, @Path("fkPerfil") UUID fkPerfil, @Path("nome") String nome);

}
