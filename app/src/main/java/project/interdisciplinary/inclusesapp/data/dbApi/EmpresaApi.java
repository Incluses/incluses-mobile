package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import project.interdisciplinary.inclusesapp.data.models.CreateEnterpriseRequest;
import project.interdisciplinary.inclusesapp.data.models.CreateUserRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface EmpresaApi {
    @POST("empresa/public/inserir")
    Call<JsonObject> register(@Body CreateEnterpriseRequest createEnterpriseRequest);
}
