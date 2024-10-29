package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import project.interdisciplinary.inclusesapp.data.models.TipoArquivo;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface TipoArquivoApi {
    @GET("tipo-arquivo/selecionar-nome/{nome}")
    Call<JsonObject> findByNome(@Header("Authorization") String token, @Path("nome") String nome);
}
