package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import project.interdisciplinary.inclusesapp.data.models.Arquivo;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ArquivoApi {
    @POST("arquivo/inserir")
    Call<JsonObject> insertArquivo(@Header("Authorization") String token, @Body Arquivo arquivo);
}
