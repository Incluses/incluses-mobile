package project.interdisciplinary.inclusesapp.data.dbApi;

import project.interdisciplinary.inclusesapp.data.models.LoginResponse;

public interface LoginCallback {
    void onSuccess(LoginResponse loginResponse);
    void onFailure(Throwable throwable);
}
