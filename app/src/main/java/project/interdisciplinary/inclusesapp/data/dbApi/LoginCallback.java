package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import project.interdisciplinary.inclusesapp.data.models.LoginResponse;

public interface LoginCallback {
    void onSuccess(LoginResponse loginResponse);
    void onSuccessAdmin(JsonObject loginResponse);
    void onFailure(Throwable throwable);
}
