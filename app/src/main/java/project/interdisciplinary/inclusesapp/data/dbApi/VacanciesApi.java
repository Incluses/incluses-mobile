package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.UUID;

import project.interdisciplinary.inclusesapp.data.models.CriarVagaDTO;
import project.interdisciplinary.inclusesapp.data.models.Vaga;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface VacanciesApi {
    @GET("vaga/selecionar")
    Call<List<Vaga>> getVacancies(@Header("Authorization") String token);
    @GET("vaga/selecionar-nome/{nome}")
    Call<List<Vaga>> getVacanciesByName(@Header("Authorization") String token, @Path("nome") String nome);
    @GET("vaga/selecionar-nome-tipo/{nome}")
    Call<List<Vaga>> getVacanciesByNameType(@Header("Authorization") String token, @Path("nome") String nome);

    @GET("vaga/selecionar-fk-empresa/{fkEmpresa}")
    Call<List<Vaga>> getVacanciesByEnterprise(@Header("Authorization") String token, @Path("fkEmpresa") UUID fkEmpresa);

    @POST("vaga/inserir")
    Call<JsonObject> insertVacancie(@Header("Authorization") String token, @Body CriarVagaDTO vaga);
}
