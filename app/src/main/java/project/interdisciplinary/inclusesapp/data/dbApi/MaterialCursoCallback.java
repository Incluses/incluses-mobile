package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import java.util.List;

import project.interdisciplinary.inclusesapp.data.models.AvaliacaoCurso;
import project.interdisciplinary.inclusesapp.data.models.MaterialCurso;

public interface MaterialCursoCallback {
    void onSuccessFind( List<MaterialCurso> list);
    void onFailure(Throwable throwable);
    void onSuccess( JsonObject jsonObject);


}
