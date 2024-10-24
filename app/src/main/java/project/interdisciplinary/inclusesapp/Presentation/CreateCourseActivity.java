package project.interdisciplinary.inclusesapp.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import project.interdisciplinary.inclusesapp.R;
import project.interdisciplinary.inclusesapp.data.ConvertersToObjects;
import project.interdisciplinary.inclusesapp.data.dbApi.CursoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.CursoCallback;
import project.interdisciplinary.inclusesapp.data.dbApi.MaterialCursoApi;
import project.interdisciplinary.inclusesapp.data.dbApi.MaterialCursoCallback;
import project.interdisciplinary.inclusesapp.data.models.CreateCursoRequest;
import project.interdisciplinary.inclusesapp.data.models.CreateMaterialCursoRequest;
import project.interdisciplinary.inclusesapp.data.models.Curso;
import project.interdisciplinary.inclusesapp.databinding.ActivityCreateCourseBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateCourseActivity extends AppCompatActivity {

    private ActivityCreateCourseBinding binding;
    private Retrofit retrofit;
    private String token;
    private UUID idPerfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Forçar o modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        binding = ActivityCreateCourseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences preferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        String perfil = preferences.getString("perfil", "");
        idPerfil = ConvertersToObjects.convertStringToPerfil(perfil).getId();

        binding.nextCreateMyCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean nameFilled = false, descriptionFilled = false;
                String inputName = binding.nameMyCourseEditText.getText().toString();
                String inputDescription = binding.descripitionEditText.getText().toString();
                if(!inputName.isEmpty()){
                    nameFilled = true;
                }
                else {
                    nameFilled = false;
                }
                if(!inputDescription.isEmpty()){
                    descriptionFilled = true;
                }
                else {
                    descriptionFilled = false;

                }
                if (nameFilled && descriptionFilled){
                    CreateCursoRequest createCursoRequest = new CreateCursoRequest(inputDescription,inputName,idPerfil);
                    callApiRetrofitInsertCurso(createCursoRequest, new CursoCallback() {
                        @Override
                        public void onSuccessFind(List<Curso> list) {

                        }

                        @Override
                        public void onFailure(Throwable throwable) {

                        }

                        @Override
                        public void onSuccess(JsonObject jsonObject) {
                            String idCurso = jsonObject.get("id").getAsString();
                            Bundle extra = new Bundle();
                            extra.putString("idCurso", idCurso);
                            Intent intent = new Intent(CreateCourseActivity.this, CreateCourseStep2.class);
                            intent.putExtras(extra);
                            startActivity(intent);
                            finish();
                        }
                    });
                }

            }
        });

        binding.cancelCreateMyCourseButton.setOnClickListener(v -> finish());
    }
    private void callApiRetrofitInsertCurso(CreateCursoRequest curso , CursoCallback callback) {
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
        CursoApi api = retrofit.create(CursoApi.class);
        Call<JsonObject> call = api.insertCurso(token,curso);
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