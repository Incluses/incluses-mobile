package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import java.util.List;

import project.interdisciplinary.inclusesapp.data.models.Arquivo;
import project.interdisciplinary.inclusesapp.data.models.TipoArquivo;

public interface TipoArquivoCallback {
    void onSuccessFind( List<TipoArquivo> list);
    void onFailure(Throwable throwable);
    void onSuccess( JsonObject jsonObject);
}
