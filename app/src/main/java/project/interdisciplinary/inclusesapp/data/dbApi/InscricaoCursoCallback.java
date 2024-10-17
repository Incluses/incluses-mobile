package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import java.util.List;

import project.interdisciplinary.inclusesapp.data.models.AvaliacaoCurso;
import project.interdisciplinary.inclusesapp.data.models.InscricaoCurso;

public interface InscricaoCursoCallback {
    void onSuccess( JsonObject jsonObject);
    void onFailure(Throwable throwable);

    void onSuccessFind( List<InscricaoCurso> list);

}
