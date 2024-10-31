package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import project.interdisciplinary.inclusesapp.data.models.Perfil;

public interface PerfilCallback {
    void onSucesssObject(JsonObject jsonObject);
    void onSuccess( Perfil perfil);
    void onFailure(Throwable throwable);
}
