package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import project.interdisciplinary.inclusesapp.data.models.CreateEnterpriseRequest;
import project.interdisciplinary.inclusesapp.data.models.CreateUserRequest;
import project.interdisciplinary.inclusesapp.data.models.Empresa;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface EmpresaApi {
    @POST("empresa/public/inserir")
    Call<JsonObject> register(@Body CreateEnterpriseRequest createEnterpriseRequest);
    @GET("empresa/selecionar-fk-perfil/{fkPerfil}")
    Call<JsonObject> getEnterpriseByProfileFk(@Header ("Authorization") String token, @Path("fkPerfil") String fkPerfil);
}
