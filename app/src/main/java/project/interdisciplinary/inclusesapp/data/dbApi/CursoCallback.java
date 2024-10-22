package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import java.util.List;

import project.interdisciplinary.inclusesapp.data.models.Curso;

public interface CursoCallback {
    void onSuccessFind( List<Curso> list);
    void onFailure(Throwable throwable);
    void onSuccess( JsonObject jsonObject);

}
