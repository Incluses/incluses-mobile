package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import java.util.List;

import project.interdisciplinary.inclusesapp.data.models.Arquivo;
import project.interdisciplinary.inclusesapp.data.models.AvaliacaoCurso;

public interface ArquivoCallback {
    void onSuccessFind( List<Arquivo> list);
    void onFailure(Throwable throwable);
    void onSuccess( JsonObject jsonObject);
}
