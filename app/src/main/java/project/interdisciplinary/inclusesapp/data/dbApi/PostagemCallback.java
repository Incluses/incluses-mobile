package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import java.util.List;

import project.interdisciplinary.inclusesapp.data.models.Postagem;

public interface PostagemCallback {
    void onSuccess(List<JsonObject> listJsonObject);

    void onSucessFind(List<Postagem> list);
    void onSuccessInsert(JsonObject jsonObject);
    void onFailure(Throwable throwable);
}
