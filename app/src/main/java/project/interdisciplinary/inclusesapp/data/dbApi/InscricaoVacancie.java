package project.interdisciplinary.inclusesapp.data.dbApi;

import project.interdisciplinary.inclusesapp.data.models.InscricaoVaga;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface InscricaoVacancie {
    @POST("/inscricao-vaga/inserir")
    Call<InscricaoVaga> insertInscricaoVaga(@Header("Authorization") String token, @Body InscricaoVaga request);
}
