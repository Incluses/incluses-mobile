package project.interdisciplinary.inclusesapp.data.api;

import com.google.gson.JsonObject;

import project.interdisciplinary.inclusesapp.data.models.Cep;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CepApi {

    @GET("ws/{cep}/json/")
    Call<JsonObject> getCep(@Path("cep") String cep);
}
