package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import project.interdisciplinary.inclusesapp.data.models.CreateUserRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UsuarioApi {
    @POST("usuario/public/inserir")
    Call<JsonObject> register(@Body CreateUserRequest createUserRequest);
}
