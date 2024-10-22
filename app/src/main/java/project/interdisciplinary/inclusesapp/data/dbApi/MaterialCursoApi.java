package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import project.interdisciplinary.inclusesapp.data.models.CreateMaterialCursoRequest;
import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MaterialCursoApi {

    @POST("material-curso/inserir")
    Call<JsonObject> insertMaterialCurso(@Header("Authorization") String token, @Body CreateMaterialCursoRequest createMaterialCursoRequest);
    @GET("material-curso/selecionar-fk-curso/{fkUsuario}")
    Call<List<MaterialCurso>> findMaterialCursoByCurso(@Header("Authorization") String token, @Path("fkUsuario") UUID fkUsuario);

    @PATCH("material-curso/atualizarParcial/{id}")
    Call<JsonObject> updateMaterialCurso(@Header("Authorization") String token, @Path("id") UUID fkAvaliacao, @Body Map<String,Object> body);
}
