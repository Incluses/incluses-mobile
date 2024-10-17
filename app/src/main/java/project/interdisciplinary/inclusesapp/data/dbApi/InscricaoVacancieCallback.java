package project.interdisciplinary.inclusesapp.data.dbApi;

import project.interdisciplinary.inclusesapp.data.models.InscricaoVaga;

public interface InscricaoVacancieCallback {

    void onSuccess(InscricaoVaga inscricaoVaga);
    void onFailure(Throwable throwable);
}
