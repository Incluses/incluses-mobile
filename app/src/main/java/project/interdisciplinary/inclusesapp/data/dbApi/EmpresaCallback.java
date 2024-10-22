package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import project.interdisciplinary.inclusesapp.data.models.Empresa;

public interface EmpresaCallback {
    void onSuccess( JsonObject jsonObject);
    void onFailure(Throwable throwable);
}
