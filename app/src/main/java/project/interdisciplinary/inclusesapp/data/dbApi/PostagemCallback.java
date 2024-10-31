package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import java.util.List;

public interface PostagemCallback {
    void onSuccess(List<JsonObject> listJsonObject);
    void onSuccessVerifyLike(Boolean booleanResponse);
    void onSuccessInsert(JsonObject jsonObject);
    void onFailure(Throwable throwable);
}
