package project.interdisciplinary.inclusesapp.data.dbApi;

import java.util.List;
import java.util.UUID;

import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface MaterialCursoApi {
    @GET("material-curso/selecionar-fk-curso/{fkUsuario}")
    Call<List<MaterialCurso>> findMaterialCursoByCurso(@Header("Authorization") String token, @Path("fkUsuario") UUID fkUsuario);
}
