package project.interdisciplinary.inclusesapp.data.dbApi;

import java.util.List;
import java.util.UUID;

import project.interdisciplinary.inclusesapp.data.models.AvaliacaoCurso;
import project.interdisciplinary.inclusesapp.data.models.Curso;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface CursoApi {
    @GET("curso/selecionar")
    Call<List<Curso>> findCursos(@Header("Authorization") String token);
}
