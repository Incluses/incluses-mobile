package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.adapters.CoursesAdapter;
import project.interdisciplinary.inclusesapp.data.ConvertersToObjects;
import project.interdisciplinary.inclusesapp.data.dbApi.CursoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.CursoCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.InscricaoCursoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.InscricaoCursoCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioApi;
import project.interdisciplinary.inclusesapp.data.dbApi.UsuarioCallback;
import project.interdisciplinary.inclusesapp.data.models.Curso;
import project.interdisciplinary.inclusesapp.data.models.InscricaoCurso;
import project.interdisciplinary.inclusesapp.data.models.Usuario;
import project.interdisciplinary.inclusesapp.databinding.ActivityCoursesViewedBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoursesViewed extends AppCompatActivity {

    private ActivityCoursesViewedBinding binding;

    private String token;
    private Retrofit retrofit;

    private String idPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        //forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityCoursesViewedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences preferences = getApplication().getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        String perfil = preferences.getString("perfil", "");
        idPerfil = ConvertersToObjects.convertStringToPerfil(perfil).getId().toString();
        binding.imageViewScreenViewedBackButton.setOnClickListener(v -> {
            finish();
        });

        binding.textViewScreenViewedBack.setOnClickListener(v -> {
            finish();
        });
        binding.coursesViewedRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        callApiRetrofitFindUser(UUID.fromString(idPerfil), new UsuarioCallback() {
            @Override
            public void onSuccess(JsonObject jsonObject) {
                Gson gson = new Gson();
                Usuario apiResponse = gson.fromJson(jsonObject, Usuario.class);
                final UUID idUser = apiResponse.getId();
                setUpAdapter(idUser,new InscricaoCursoCallback() {
                    @Override
                    public void onSuccessFind(List<InscricaoCurso> list) {
                        List<Curso> listCurso = new ArrayList<>();
                        for (int i = 0; i < list.size(); i++){
                            listCurso.add(list.get(i).getCurso());
                        }
                        binding.coursesViewedRecyclerView.setAdapter(new CoursesAdapter(listCurso));
                    }

                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(JsonObject jsonObject) {

                    }
                });
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

    }
    private void setUpAdapter(UUID userID, InscricaoCursoCallback callback) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        String urlApi = "https://incluses-api.onrender.com/";

        retrofit = new Retrofit.Builder()
                .baseUrl(urlApi)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        InscricaoCursoApi api = retrofit.create(InscricaoCursoApi.class);
        Call<List<InscricaoCurso>> call = api.findInscricaoCurso(token, userID);
        call.enqueue(new Callback<List<InscricaoCurso>>() {
            @Override
            public void onResponse(Call<List<InscricaoCurso>> call, Response<List<InscricaoCurso>> response) {
                Log.e("retorno", String.valueOf(response.code()));
                if (response.isSuccessful() && response.body() != null) {
                    List<InscricaoCurso> responseBody = response.body();
                    callback.onSuccessFind(responseBody); // Sucesso
                } else if (response.code() == 401) {
                }
                else {
                }
            }

            @Override
            public void onFailure(Call<List<InscricaoCurso>> call, Throwable throwable) {
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
    private void callApiRetrofitFindUser(UUID userId , UsuarioCallback callback) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();

        String urlApi = "https://incluses-api.onrender.com/";

        retrofit = new Retrofit.Builder()
                .baseUrl(urlApi)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UsuarioApi api = retrofit.create(UsuarioApi.class);
        Call<JsonObject> call = api.findByFkPerfil(token,userId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                Log.e("retorno", String.valueOf(response.code()));
                if (response.isSuccessful() && response.body() != null) {
                    JsonObject responseBody = response.body();
                    callback.onSuccess(responseBody); // Sucesso
                } else if (response.code() == 401) {
                }
                else {
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable throwable) {
                Log.e("ERRO", throwable.getMessage());
                callback.onFailure(throwable); // Falha por erro de requisição
            }
        });
    }
}