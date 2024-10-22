package project.interdisciplinary.inclusesapp.data.dbApi;

import com.google.gson.JsonObject;

import project.interdisciplinary.inclusesapp.data.models.LoginRequest;
import project.interdisciplinary.inclusesapp.data.models.LoginResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LoginApi {
    @POST("api/auth/login")
    Call<LoginResponse> login(@Body LoginRequest body);
    @GET("admin-account/find/{email}")
    Call<JsonObject> findAdmin(@Path("email") String email);
}
