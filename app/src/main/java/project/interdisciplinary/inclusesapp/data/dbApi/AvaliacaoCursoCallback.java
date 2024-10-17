package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import java.util.List;

import project.interdisciplinary.inclusesapp.data.models.AvaliacaoCurso;

public interface AvaliacaoCursoCallback {
    void onSuccessFind( List<AvaliacaoCurso> list);
    void onFailure(Throwable throwable);
    void onSuccess( JsonObject jsonObject);


}
