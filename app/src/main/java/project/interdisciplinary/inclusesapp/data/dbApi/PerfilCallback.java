package project.interdisciplinary.inclusesapp.data.dbApi;

import project.interdisciplinary.inclusesapp.data.models.Perfil;

public interface PerfilCallback {
    void onSuccess( Perfil perfil);
    void onFailure(Throwable throwable);
}
