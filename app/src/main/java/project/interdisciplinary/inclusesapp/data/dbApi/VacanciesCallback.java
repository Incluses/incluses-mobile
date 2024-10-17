package project.interdisciplinary.inclusesapp.data.dbApi;

import java.util.List;

import project.interdisciplinary.inclusesapp.data.models.Vaga;

public interface VacanciesCallback {

    void onSuccess(List<Vaga> vacanciesResponse);
    void onFailure(Throwable throwable);
}
