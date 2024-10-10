package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

public interface EmpresaCallback {
    void onSuccess( JsonObject jsonObject);
    void onFailure(Throwable throwable);
}
