package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import java.util.Map;
import java.util.UUID;

import project.interdisciplinary.inclusesapp.data.models.CreateUserRequest;
import project.interdisciplinary.inclusesapp.data.models.Usuario;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UsuarioApi {
    @POST("usuario/public/inserir")
    Call<JsonObject> register(@Body CreateUserRequest createUserRequest);

    @GET("usuario/selecionar-fk-perfil/{fkPerfil}")
    Call<JsonObject> getUserByProfileFk(@Header("Authorization") String token, @Path("fkPerfil") UUID fkPerfil);

    @PATCH("usuario/atualizarParcial/{id}")
    Call<JsonObject> updateUser(@Header("Authorization") String token, @Path("id") UUID id, @Body Map<String,Object> body);
}