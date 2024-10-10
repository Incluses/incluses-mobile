package project.interdisciplinary.inclusesapp.data.dbApi;


import com.google.gson.JsonObject;

public interface UsuarioCallback {
    void onSuccess( JsonObject jsonObject);
    void onFailure(Throwable throwable);
}
