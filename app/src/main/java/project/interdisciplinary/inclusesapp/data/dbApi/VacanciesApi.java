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

public interface VacanciesApi {
    @GET("vaga/selecionar")
    Call<List<Vaga>> getVacancies(@Header("Authorization") String token);
    @GET("vaga/selecionar")
    Call<List<Vaga>> getYourVacancies(@Header("Authorization") String token, UUID idEmpresa);
    @POST("vaga/inserir")
    Call<JsonObject> insertVacancie(@Header("Authorization") String token, @Body CriarVagaDTO vaga);
}
