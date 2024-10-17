package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import project.interdisciplinary.inclusesapp.data.models.Perfil;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PerfilApi {

    @GET("perfil/selecionar-email/{email}")
    Call<Perfil> getPerfil(@Header ("Authorization") String token, @Path("email") String email);
}
