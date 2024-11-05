
package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import java.util.UUID;

import project.interdisciplinary.inclusesapp.data.models.Arquivo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ArquivoApi {
    @POST("arquivo/inserir")
    Call<JsonObject> insertArquivo(@Header("Authorization") String token, @Body Arquivo arquivo);

    @GET("arquivo/selecionar-id/{id}")
    Call<JsonObject> findArquivo(@Header("Authorization") String token, @Path("id")UUID idArquivo);
}
