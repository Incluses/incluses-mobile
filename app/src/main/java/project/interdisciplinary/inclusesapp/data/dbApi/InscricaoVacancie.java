package project.interdisciplinary.inclusesapp.data.dbApi;

import java.util.List;
import java.util.UUID;

import project.interdisciplinary.inclusesapp.data.models.CriarInscricaoVagaDTO;
import project.interdisciplinary.inclusesapp.data.models.InscricaoVaga;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface InscricaoVacancie {
    @POST("/inscricao-vaga/inserir")
    Call<InscricaoVaga> insertInscricaoVaga(@Header("Authorization") String token, @Body CriarInscricaoVagaDTO request);

    @GET("/inscricao-vaga/selecionar-fk-usuario/{fkUsuario}")
    Call<List<InscricaoVaga>> selectInscricaoVagaByFkUsuario(@Header("Authorization") String token, @Path("fkUsuario") UUID fkUsuario);
}
