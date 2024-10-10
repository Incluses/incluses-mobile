package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import project.interdisciplinary.inclusesapp.data.models.LoginRequest;
import project.interdisciplinary.inclusesapp.data.models.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginApi {
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest body);
}
