package project.interdisciplinary.inclusesapp.data.dbApi;

import java.util.List;

import project.interdisciplinary.inclusesapp.data.models.InscricaoVaga;

public interface InscricaoVacancieCallback {
    void onSuccess(InscricaoVaga inscricaoVaga);
    void onSuccessFind(List<InscricaoVaga> inscricaoVaga);
    void onFailure(Throwable throwable);
}
